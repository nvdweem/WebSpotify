#include "playlist.h"

jobject playlistListener;

/**
 * Called when a new playlist has been added to the playlist container.
 *
 * @param[in]  pc         Playlist container
 * @param[in]  playlist   Playlist object.
 * @param[in]  position   Position in list
 * @param[in]  userdata   Userdata as set in sp_playlistcontainer_add_callbacks()
 */
void cb_playlist_added(sp_playlistcontainer *pc, sp_playlist *playlist, int position, void *userdata) {
	callVoidMethod(playlistListener, "cb_playlist_added");
}

/**
 * Called when a new playlist has been removed from playlist container
 *
 * @param[in]  pc         Playlist container
 * @param[in]  playlist   Playlist object.
 * @param[in]  position   Position in list
 * @param[in]  userdata   Userdata as set in sp_playlistcontainer_add_callbacks()
 */
void cb_playlist_removed(sp_playlistcontainer *pc, sp_playlist *playlist, int position, void *userdata) {
	callVoidMethod(playlistListener, "cb_playlist_removed");
}


/**
 * Called when a playlist has been moved in the playlist container
 *
 * @param[in]  pc         Playlist container
 * @param[in]  playlist   Playlist object.
 * @param[in]  position   Previous position in playlist container list
 * @param[in]  new_position   New position in playlist container list
 * @param[in]  userdata   Userdata as set in sp_playlistcontainer_add_callbacks()
 */
void cb_playlist_moved(sp_playlistcontainer *pc, sp_playlist *playlist, int position, int new_position, void *userdata) {
	callVoidMethod(playlistListener, "cb_playlist_moved");
}

/**
 * Called when the playlist container is loaded
 *
 * @param[in]  pc         Playlist container
 * @param[in]  userdata   Userdata as set in sp_playlistcontainer_add_callbacks()
 */
void cb_container_loaded(sp_playlistcontainer *pc, void *userdata) {
	callVoidMethod(playlistListener, "cb_container_loaded");
	for ( int i = 0; i < sp_playlistcontainer_num_playlists ( pc ); ++i ) {
        switch ( sp_playlistcontainer_playlist_type ( pc,i ) ) {
			case SP_PLAYLIST_TYPE_PLAYLIST:
				break;
			case SP_PLAYLIST_TYPE_START_FOLDER:
				break;
			case SP_PLAYLIST_TYPE_END_FOLDER:
				break;
			case SP_PLAYLIST_TYPE_PLACEHOLDER:
				break;
        }
    }
}

static sp_playlistcontainer_callbacks playlistContainerCallback = {
    (void (__stdcall *)(sp_playlistcontainer *pc, sp_playlist *playlist, int position, void *userdata)) &cb_playlist_added,
    (void (__stdcall *)(sp_playlistcontainer *pc, sp_playlist *playlist, int position, void *userdata)) &cb_playlist_removed,
    (void (__stdcall *)(sp_playlistcontainer *pc, sp_playlist *playlist, int position, int new_position, void *userdata)) &cb_playlist_moved,
    (void (__stdcall *)(sp_playlistcontainer *pc, void *userdata)) &cb_container_loaded
};

void initPlaylist(sp_session* session, jobject _playlistListener) {
	sp_playlistcontainer_add_callbacks ( sp_session_playlistcontainer ( session ), &playlistContainerCallback, NULL);
	playlistListener = _playlistListener;
}