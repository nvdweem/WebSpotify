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
	private List<Track> playlistOrdered = new ArrayList<Track>();
	private List<Track> queue = new ArrayList<Track>();
	public boolean shuffling = false;
	
	private transient List<Track> played = new ArrayList<Track>();
	private transient final Session session;
	
	private transient int rate = 0, channels = 0;
	private transient int positionOffset = 0;
	private transient SourceDataLine audio;
	private transient Track currentTrack;
	private transient boolean playing = false;
	private transient int queueRevision = 0;
	private transient boolean editing = false;
	
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
			if (player.playlistOrdered != null)
				this.playlistOrdered = player.playlistOrdered;
			this.shuffling = player.shuffling;
		} catch (Exception e) {
			System.out.println("Could not load playlist.");
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
		if (isEditing()) return;
		savePlaylist();
		queueRevision++;
		
		for (int i = 0; i < 100 && i < playlist.size(); i++)
			complete(playlist.get(i));
	}
	
	public JSONObject toJSON() {
		JSONObject result = new JSONObject();
		
		result.put("playlist", Util.listToArray(playlist, true, 100));
		result.put("queue", Util.listToArray(queue, true, 100));
		result.put("played", Util.listToArray(played, true));
		
		return result;
	}
	
	public String toString() {
		return toJSON().toString();
	}
	
	/**
	 * Toggles shuffling
	 */
	public void shuffle() {
		if (shuffling == true) {
			playlist.clear();
			playlist.addAll(playlistOrdered);
		}
		else {
			playlist.clear();
			for (int i = 0; i < playlistOrdered.size(); i++)
				playlist.add((int) Math.round(Math.random() * playlist.size()), playlistOrdered.get(i));
		}
		shuffling = !shuffling;
		triggerChange();
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
		playlistOrdered.remove(track);
		complete(track);
		queue.add(track);
		start();
		triggerChange();
	}
	
	public void addToPlaylist(Track track) {
		if (playlist.contains(track)) return;
		if (queue.contains(track)) return;
		complete(track);
		playlistOrdered.add(track);
		
		if (shuffling)
			playlist.add((int) Math.round(Math.random() * playlist.size()), track);
		else
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
		if (track == null && playlist.size() != 0) {
			track = playlist.remove(0);
			playlistOrdered.remove(track);
		}
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

	public boolean isEditing() {
		return editing;
	}

	public void setEditing(boolean editing) {
		this.editing = editing;
		triggerChange();
	}

	public boolean isShuffling() {
		return shuffling;
	}
	
}
