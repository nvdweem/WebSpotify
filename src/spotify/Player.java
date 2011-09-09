package spotify;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

import org.json.JSONObject;

/**
 * Provides an interface for playing songs.
 * @author Niels
 */
public class Player implements Serializable {
	private static final long serialVersionUID = -4971392287201910194L;
	
	public static transient final String playlistFile = "playlist.dat";
	private List<Track> playlist = new ArrayList<Track>();
	private List<Track> queue = new ArrayList<Track>();
	private transient List<Track> played = new ArrayList<Track>();
	private transient final Session session;
	
	private transient int rate = 0, channels = 0;
	transient int positionOffset = 0;
	private transient SourceDataLine audio;
	private transient Track currentTrack;
	public transient boolean playing = false;
	private transient int queueRevision = 0;
	
	public Player(Session session) {
		this.session = session;
		session.registerPlayer(this);
		loadPlaylist();
	}
	
	public void loadPlaylist() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(playlistFile));
			Player player = (Player) ois.readObject();
			if (player.playlist != null)
				this.playlist = player.playlist;
			if (player.queue != null)
				this.queue = player.queue;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void savePlaylist() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(playlistFile));
			oos.writeObject(this);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void triggerChange() {
		savePlaylist();
		queueRevision++;
	}
	
	public JSONObject toJSON() {
		JSONObject result = new JSONObject();
		
		result.put("playlist", Util.listToArray(playlist, true));
		result.put("queue", Util.listToArray(queue, true));
		result.put("played", Util.listToArray(played, true));
		
		return result;
	}
	
	public String toString() {
		return toJSON().toString();
	}
	
	/**
	 * Suplement the track data.
	 * @param track
	 */
	private void complete(Track track) {
		for (int i = 0; i < 100; i++) {
			if (Session.getInstance().completeTrack(track)) return;
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
			}
		}
	}
	
	/**
	 * Reset counters.
	 */
	public void changeSong() {
		audio = null;
		positionOffset = 0;
	}
	
	/**
	 * Queue a track.
	 * @param track
	 */
	public void play(Track track) {
		if (queue.contains(track)) return;
		playlist.remove(track);
		complete(track);
		queue.add(track);
		start();
		triggerChange();
	}
	
	public void addToPlaylist(Track track) {
		if (playlist.contains(track)) return;
		if (queue.contains(track)) return;
		complete(track);
		playlist.add(track);
		start();
		triggerChange();
	}
	
	/**
	 * Start playing if nothing is playing.
	 */
	private void start() {
		if (currentTrack == null)
			next();
	}
	
	/**
	 * Go to the next track.
	 */
	public void endOfTrack() {
		// Add the current track to the played list.
		played.add(0, currentTrack);
		while (played.size() > 50) played.remove(50); // Max 50 played items
		
		currentTrack = null;
		while (!next())
			System.out.println("Unplayable track.");
		
		triggerChange();
	}
	
	/**
	 * Try to play the next track.
	 * @return
	 */
	private boolean next() {
		Track track = null;
		
		if (track == null && queue.size() != 0) track = queue.remove(0);
		if (track == null && playlist.size() != 0) track = playlist.remove(0);
		currentTrack = track;
		
		// Try to play and update played when it succeeds.
		if (playNow(track)) {
			triggerChange();
			return true;
		}
		
		return false;
	}
	
	/**
	 * Immediately start playing a song.
	 * @param track
	 * @return
	 */
	private boolean playNow(Track track) {
		// Try to wait 5 seconds for the track to become available.
		for (int i = 0; i < 500; i++) {
			if (session.play(track)) {
				currentTrack = track;
				changeSong();
				playing = true;
				return true;
			}

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			}
		}
		return false;
	}
	
	public void pause() {
		if (!playing && currentTrack == null) {
			next();
			return;
		}
		Session.getInstance().pause(playing);
		playing = !playing;
	}
	
	public void skip() {
		endOfTrack();
	}
	
	public void prev() {
		// Prev replays the current song if it is pressed within the first 5 seconds.
		if (getPosition() > 5 || played.size() == 0) {
			seek(0);
			return;
		}
		Track played = this.played.remove(0);
		queue.add(0, currentTrack);
		if (!playNow(played)) skip();
		triggerChange();
	}
	
	public void seek(int position) {
		if (currentTrack != null)
			session.seek(position * 1000);
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
	
	public void setAudioFormat(int rate, int channels) {
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
	
	public boolean isPlaying() {
		return playing;
	}

	public Track getCurrentTrack() {
		return currentTrack;
	}
	
	public int getDuration() {
		if (currentTrack == null) return 0;
		return currentTrack.getDuration() / 1000;
	}
	
	public int getPosition() {
		if (audio == null) return 0;
		return positionOffset + (audio.getFramePosition() / this.rate);
	}

	public int getQueueRevision() {
		return queueRevision;
	}
	
}
