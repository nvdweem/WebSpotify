package spotify;

/**
 * Listens to Spotify playlist events.
 * @author Niels
 */
public class PlaylistListenerImpl implements PlaylistListener {
	private PlaylistContainer container;
	
	public PlaylistListenerImpl() {
		container = new PlaylistContainer();
	}
	
	public Object createPlaylist(String id) {
		return new Playlist(id);
	}
	
	@Override
	public void addPlaylist(Object playlist) {
		if (!(playlist instanceof Playlist)) return;
		container.addPlaylist((Playlist) playlist);
	}
	
	@Override
	public void cb_playlist_added(Object playlist, int position) {
//		if (!(playlist instanceof Playlist)) return;
//		container.putPlaylist((Playlist) playlist, position);
//		System.err.println("cb_playlist_added");
	}

	@Override
	public void cb_playlist_removed(int position) {
//		container.remove(position);
	}

	@Override
	public void cb_playlist_moved(int from, int to) {
//		container.move(from, to);
	}

	@Override
	public void cb_container_reload() {
		container.reset();
	}

	@Override
	public void cb_container_loaded() {
		container.setComplete();
		System.err.println("cb_container_loaded");
	}

	@Override
	public PlaylistContainer getContainer() {
		return container;
	}
}
