package spotify;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

public class Player {
	private List<Track> playlist = new ArrayList<Track>();
	private List<Track> queue = new ArrayList<Track>();
	private final Session session;
	
	private SourceDataLine audio;
	
	public Player(Session session) {
		this.session = session;
		session.RegisterPlayer(this);
	}
	
	public void queue(Track track) {
		queue.add(track);
	}
	
	public void play(Track track) {
		playlist.add(track);
		session.Play(track.getId());
	}
	
	public void pause() {
		
	}
	
	public void skip() {
		
	}
	
	public int addToBuffer(byte[] buffer) {
		int available = audio.available();
		if (available == 0) return 0;
		int bufferSize = buffer.length;
		int toWrite = Math.min(available, bufferSize);
		return audio.write(buffer, 0, toWrite) / 4;
	}
	
	int rate = 0, channels = 0;
	public void setAudioFormat(int rate, int sampleSize, int channels) {
		if (rate == this.rate && channels == this.channels) return;
		this.rate = rate;
		this.channels = channels;
		
		try {
			AudioFormat format = new AudioFormat(44100, 16, 2, true, false);
			format = new AudioFormat(format.getEncoding(), format.getSampleRate(), format.getSampleSizeInBits(), format.getChannels(), format.getFrameSize(), format.getFrameRate(), false);
			
			audio = AudioSystem.getSourceDataLine(format);
			audio.open(format);
			audio.start();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
