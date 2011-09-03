#include "playlist.h"
#include "media.h"
#include "Session.h"

jobject playlistListener;

void playlist_metadata_updated(sp_playlist *pl, void *userdata) {
	// Check if all tracks are available.
	for (int i = 0; i < sp_playlist_num_tracks(pl); i++) {
		sp_track *track = sp_playlist_track(pl, i);
		if (!sp_track_is_loaded(track)) return;
	}

	// All tracks are available, read playlist.
	jobject playlist = (jobject) userdata;
	callVoidMethod(playlist, "clear");
	for (int i = 0; i < sp_playlist_num_tracks(pl); i++) {
		sp_track *track = sp_playlist_track(pl, i);
		if (!sp_track_is_loaded(track)) continue;
		callVoidMethod(playlist, "addTrack", readTrack(track, false));
	}
	callVoidMethod(playlistListener, "incrementRevision");
}
void tracks_added(sp_playlist *pl, sp_track * const *tracks, int num_tracks, int position, void *userdata) {
	jobject playlist = (jobject) userdata;
	for (int i = 0; i < num_tracks; i++) {
		if (!sp_track_is_loaded(tracks[i])) { 
			position--;
			continue;
		}
		callVoidMethod(playlist, "putTrack", readTrack(tracks[i], false), position + i);
	}
}
void tracks_removed(sp_playlist *pl, const int *tracks, int num_tracks, void *userdata) {
	playlist_metadata_updated(pl, userdata); // Just being lazy
}
void tracks_moved(sp_playlist *pl, const int *tracks, int num_tracks, int new_position, void *userdata) {
	playlist_metadata_updated(pl, userdata); // Just being lazy
}

void playlist_renamed(sp_playlist *pl, void *userdata) {
	fflush(stderr);
	jobject playlist = (jobject) userdata;
	callVoidMethod(playlist, "setName", sp_playlist_name(pl));
}

void description_changed(sp_playlist *pl, const char *desc, void *userdata) {
	jobject playlist = (jobject) userdata;
	callVoidMethod(playlist, "setDescription", desc);
}


sp_playlist_callbacks cb_playlist = {
	&tracks_added, // void (SP_CALLCONV *tracks_added)(sp_playlist *pl, sp_track * const *tracks, int num_tracks, int position, void *userdata);
	&tracks_removed, // void (SP_CALLCONV *tracks_removed)(sp_playlist *pl, const int *tracks, int num_tracks, void *userdata);
	&tracks_moved, // void (SP_CALLCONV *tracks_moved)(sp_playlist *pl, const int *tracks, int num_tracks, int new_position, void *userdata);
	&playlist_renamed, // void (SP_CALLCONV *playlist_renamed)(sp_playlist *pl, void *userdata);
	NULL, // void (SP_CALLCONV *playlist_state_changed)(sp_playlist *pl, void *userdata);
	NULL, // void (SP_CALLCONV *playlist_update_in_progress)(sp_playlist *pl, bool done, void *userdata);
	&playlist_metadata_updated, // void (SP_CALLCONV *playlist_metadata_updated)(sp_playlist *pl, void *userdata);
	NULL, // void (SP_CALLCONV *track_created_changed)(sp_playlist *pl, int position, sp_user *user, int when, void *userdata);
	NULL, // void (SP_CALLCONV *track_seen_changed)(sp_playlist *pl, int position, bool seen, void *userdata);
	&description_changed, // void (SP_CALLCONV *description_changed)(sp_playlist *pl, const char *desc, void *userdata);
	NULL, // void (SP_CALLCONV *image_changed)(sp_playlist *pl, const byte *image, void *userdata);
	NULL, // void (SP_CALLCONV *track_message_changed)(sp_playlist *pl, int position, const char *message, void *userdata);
	NULL // void (SP_CALLCONV *subscribers_changed)(sp_playlist *pl, void *userdata);
};


jobject readPlaylist(sp_playlist *pl) {
	jobject playlist = callObjectMethod(playlistListener, "createPlaylist", "", true);
	callVoidMethod(playlist, "setName", sp_playlist_name(pl));
	callVoidMethod(playlist, "setDescription", sp_playlist_get_description(pl));

	for (int i = 0; i < sp_playlist_num_tracks(pl); i++) {
		sp_track *track = sp_playlist_track(pl, i);
		if (!sp_track_is_loaded(track)) continue;
		callVoidMethod(playlist, "addTrack", readTrack(track, false));
	}

	sp_playlist_add_callbacks(pl, &cb_playlist, playlist);

	callVoidMethod(playlist, "setComplete");
	return playlist;
}

/**
 * Called when a new playlist has been added to the playlist container.
 *
 * @param[in]  pc         Playlist container
 * @param[in]  playlist   Playlist object.
 * @param[in]  position   Position in list
 * @param[in]  userdata   Userdata as set in sp_playlistcontainer_add_callbacks()
 */
void cb_playlist_added(sp_playlistcontainer *pc, sp_playlist *playlist, int position, void *userdata) {
	jobject list = readPlaylist(playlist);
	callVoidMethod(playlistListener, "cb_playlist_added", list, position);
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
	callVoidMethod(playlistListener, "cb_playlist_removed", position);
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
	callVoidMethod(playlistListener, "cb_playlist_moved", position, new_position);
}

/**
 * Called when the playlist container is loaded
 *
 * @param[in]  pc         Playlist container
 * @param[in]  userdata   Userdata as set in sp_playlistcontainer_add_callbacks()
 */
void cb_container_loaded(sp_playlistcontainer *pc, void *userdata) {
	/*
	callVoidMethod(playlistListener, "clear");
	for ( int i = 0; i < sp_playlistcontainer_num_playlists ( pc ); ++i ) {
        switch ( sp_playlistcontainer_playlist_type ( pc,i ) ) {
			case SP_PLAYLIST_TYPE_PLAYLIST:
				callVoidMethod(playlistListener, "addPlaylist", readPlaylist(sp_playlistcontainer_playlist(pc, i)));
				break;
			case SP_PLAYLIST_TYPE_START_FOLDER:
				break;
			case SP_PLAYLIST_TYPE_END_FOLDER:
				break;
			case SP_PLAYLIST_TYPE_PLACEHOLDER:
				break;
        }
    }
	*/
	callVoidMethod(playlistListener, "cb_container_loaded");
}

static sp_playlistcontainer_callbacks playlistContainerCallback = {
    &cb_playlist_added,
    &cb_playlist_removed,
    &cb_playlist_moved,
    &cb_container_loaded
};

void setListener(jobject _playlistListener) {
	playlistListener = _playlistListener;
}

void initPlaylistListener() {
	sp_playlistcontainer_add_callbacks ( sp_session_playlistcontainer ( getSession() ), &playlistContainerCallback, NULL);
}