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
	
	private int rate = 0, channels = 0;
	int positionOffset = 0;
	private SourceDataLine audio;
	private Track playing;
	
	public Player(Session session) {
		this.session = session;
		session.RegisterPlayer(this);
	}
	
	public void queue(Track track) {
		queue.add(track);
	}
	
	public void changeSong() {
		audio = null;
	}
	
	public void play(Track track) {
		playing = track;
		session.Play(track);
		positionOffset = 0;
	}
	
	public int getDuration() {
		if (playing == null) return 0;
		return playing.getDuration() / 1000;
	}
	
	public int getPosition() {
		if (audio == null) return 0;
		return positionOffset + (audio.getFramePosition() / this.rate);
	}
	
	public void pause() {
		
	}
	
	public void skip() {
		
	}
	
	public void seek(int position) {
		session.Seek(position * 1000);
	}
	public void seekCallback(int position) {
		audio = null;
		positionOffset = position / 1000;
	}
	
	public int addToBuffer(byte[] buffer) {
		if (audio == null) return 0;
		int available = audio.available();
		if (available == 0) return 0;
		int bufferSize = buffer.length;
		int toWrite = Math.min(available, bufferSize);
		return audio.write(buffer, 0, toWrite) / 4;
	}
	
	public void setAudioFormat(int rate, int sampleSize, int channels) {
		if (audio != null && rate == this.rate && channels == this.channels) return;
		this.rate = rate;
		this.channels = channels;
		
		try {
			AudioFormat format = new AudioFormat(rate, 8*channels, channels, true, false);
			format = new AudioFormat(format.getEncoding(), format.getSampleRate(), format.getSampleSizeInBits(), format.getChannels(), format.getFrameSize(), format.getFrameRate(), false);
			
			audio = AudioSystem.getSourceDataLine(format);
			audio.open(format);
			audio.start();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public Track getPlaying() {
		return playing;
	}
	
}
