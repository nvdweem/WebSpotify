package com.vdweem.webspotify.action;

import jahspotify.services.MediaPlayer;
import jahspotify.services.MediaStreamer;
import jahspotify.util.ByteArrayInOutputStream;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.AudioFormat;

import org.apache.struts2.ServletActionContext;
import org.xiph.libogg.ogg_packet;
import org.xiph.libogg.ogg_page;
import org.xiph.libogg.ogg_stream_state;
import org.xiph.libvorbis.vorbis_block;
import org.xiph.libvorbis.vorbis_comment;
import org.xiph.libvorbis.vorbis_dsp_state;
import org.xiph.libvorbis.vorbis_info;
import org.xiph.libvorbis.vorbisenc;

import com.opensymphony.xwork2.Result;

/**
 * Action for streaming audio.
 * @author Niels
 */
public class StreamAction implements MediaStreamer {
	private HttpServletResponse response;
	private ByteArrayInOutputStream bios;

	/**
	 * Setup the response and start streaming.
	 * @return
	 */
	public Result execute() {
		response = ServletActionContext.getResponse();
		response.setContentType("audio/ogg");

		try {
			bios = new ByteArrayInOutputStream();
			MediaPlayer.getInstance().addStreamer(this);

			Thread.sleep(1000); // Buffer for 5 seconds before starting.
			streamOgg();
		} catch (Exception e) {
			// An exception will be thrown when the stream is closed.
		} finally {
			MediaPlayer.getInstance().removeStreamer(this);
		}
		return null;
	}
	/**
	 * The MediaPlayer wants to add to the buffer.
	 */
	@Override
	public void addToBuffer(byte[] buff, int len) {
		bios.write(buff, 0, len);
	}

	/**
	 * Don't use the audio format. Assume 44100 stereo.
	 */
	@Override
	public void setAudioFormat(AudioFormat format) {

	}


	/*
	 * From here on ripped from the vorbis example. Changed it a small bit so that it doesn't use files.
	 */
	static vorbisenc 			encoder;
	static ogg_stream_state 	os;	// take physical pages, weld into a logical stream of packets
	static ogg_page				og;	// one Ogg bitstream page.  Vorbis packets are inside
	static ogg_packet			op;	// one raw packet of data for decode
	static vorbis_info			vi;	// struct that stores all the static vorbis bitstream settings
	static vorbis_comment		vc;	// struct that stores all the user comments
	static vorbis_dsp_state		vd;	// central working state for the packet->PCM decoder
	static vorbis_block			vb;	// local working space for packet->PCM decode

	static int READ = 1024;
	static byte[] readbuffer = new byte[READ*4+44];

	static int page_count = 0;
	static int block_count = 0;


	/**
	 * VorbisEncoder.java
	 *
	 * Usage:
	 * java -cp VorbisEncoder <Input File[.wav]> <Output File[.ogg]>
	 * @throws IOException
	 *
	 */
	public void streamOgg() throws IOException {
		boolean eos = false;

		vi = new vorbis_info();
		encoder = new vorbisenc();

		if ( !encoder.vorbis_encode_init_vbr( vi, 2, 44100, .3f ) ) {
			return;
		}

		vc = new vorbis_comment();
		vc.vorbis_comment_add_tag( "ENCODER", "WebSpotify" );

		vd = new vorbis_dsp_state();

		if ( !vd.vorbis_analysis_init( vi ) ) {
			return;
		}

		vb = new vorbis_block( vd );

		java.util.Random generator = new java.util.Random();  // need to randomize seed
		os = new ogg_stream_state( generator.nextInt(256) );

		ogg_packet header = new ogg_packet();
		ogg_packet header_comm = new ogg_packet();
		ogg_packet header_code = new ogg_packet();

		vd.vorbis_analysis_headerout( vc, header, header_comm, header_code );

		os.ogg_stream_packetin( header); // automatically placed in its own page
		os.ogg_stream_packetin( header_comm );
		os.ogg_stream_packetin( header_code );

		og = new ogg_page();
		op = new ogg_packet();

		OutputStream stream = response.getOutputStream();

		while( !eos ) {

			if ( !os.ogg_stream_flush( og ) )
				break;

			stream.write( og.header, 0, og.header_len );
			stream.write( og.body, 0, og.body_len );
		}

		while ( !eos ) {

			int i;
			stream.flush();
			int bytes = bios.read( readbuffer, 0, READ*4, true ); // stereo hardwired here
			if (bios.size() < 50000) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if ( bytes==0 ) {

				// end of file.  this can be done implicitly in the mainline,
				// but it's easier to see here in non-clever fashion.
				// Tell the library we're at end of stream so that it can handle
				// the last frame and mark end of stream in the output properly

				vd.vorbis_analysis_wrote( 0 );

			} else {

				// data to encode

				// expose the buffer to submit data
				float[][] buffer = vd.vorbis_analysis_buffer( READ );

				// uninterleave samples
				for ( i=0; i < bytes/4; i++ ) {
					buffer[0][vd.pcm_current + i] = ( readbuffer[i*4+1]<<8 | 0x00ff&readbuffer[i*4] ) / 32768.f;
					buffer[1][vd.pcm_current + i] = ( readbuffer[i*4+3]<<8 | 0x00ff&readbuffer[i*4+2] ) / 32768.f;
				}

				// tell the library how much we actually submitted
				vd.vorbis_analysis_wrote( i );
			}

			// vorbis does some data preanalysis, then divvies up blocks for more involved
			// (potentially parallel) processing.  Get a single block for encoding now

			while ( vb.vorbis_analysis_blockout( vd ) ) {

				// analysis, assume we want to use bitrate management

				vb.vorbis_analysis( null );
				vb.vorbis_bitrate_addblock();

				while ( vd.vorbis_bitrate_flushpacket( op ) ) {

					// weld the packet into the bitstream
					os.ogg_stream_packetin( op );

					// write out pages (if any)
					while ( !eos ) {

						if ( !os.ogg_stream_pageout( og ) ) {
							break;
						}

						stream.write( og.header, 0, og.header_len );
						stream.write( og.body, 0, og.body_len );

						// this could be set above, but for illustrative purposes, I do
						// it here (to show that vorbis does know where the stream ends)
						if ( og.ogg_page_eos() > 0 )
							eos = true;
					}
				}
			}
		}

		stream.close();
	}
}
