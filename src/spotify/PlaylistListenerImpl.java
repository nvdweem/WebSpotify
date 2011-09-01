package spotify;

/**
 * Listens to Spotify playlist events.
 * @author Niels
 */
public class PlaylistListenerImpl implements PlaylistListener {

	@Override
	public void cb_playlist_added() {
		System.out.println("cb_playlist_added");
	}

	@Override
	public void cb_playlist_removed() {
		System.out.println("cb_playlist_removed");
	}

	@Override
	public void cb_playlist_moved() {
		System.out.println("cb_playlist_moved");
	}

	@Override
	public void cb_container_loaded() {
		System.out.println("cb_container_loaded");
	}
}
