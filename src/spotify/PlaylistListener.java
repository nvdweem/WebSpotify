package spotify;

/**
 * Interface for playlist events from the Spotify library.
 * @author Niels
 */
public interface PlaylistListener {
	
	void cb_playlist_added(Object list, int position);
	void cb_playlist_removed(int position);
	void cb_playlist_moved(int from, int to);
	void cb_container_loaded();
	PlaylistContainer getContainer();
	void incrementRevision();
}
