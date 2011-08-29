package spotify;

public interface PlaylistListener {
	void cb_playlist_added(/*sp_playlistcontainer *pc, sp_playlist *playlist, int position, void *userdata*/);
	void cb_playlist_removed(/*sp_playlistcontainer *pc, sp_playlist *playlist, int position, void *userdata*/);
	void cb_playlist_moved(/*sp_playlistcontainer *pc, sp_playlist *playlist, int position, int new_position, void *userdata*/);
	void cb_container_loaded(/*sp_playlistcontainer *pc, void *userdata*/);
}
