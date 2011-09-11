
#include <string.h>
#include "Session.h"
#include "spotify_Session.h"
#include "appkey.c"
#include "playlist.h"
#include "JNI.h"
#include "music.h"
#include "media.h"

sp_session *session;
jobject sessionListener;

sp_session* getSession() {
	return session;
}

jobject getSessionListener() {
	return sessionListener;
}

void notify_main_thread(sp_session *session) {
	callVoidMethod(sessionListener, "cb_notify_main_thread");
}

/**
 * The session callbacks
 */
static sp_session_callbacks callbacks = {
	  &cb_logged_in // void (SP_CALLCONV *logged_in)(sp_session *session, sp_error error);
	, &cb_logged_out // void (SP_CALLCONV *logged_out)(sp_session *session);
	, NULL //(void (__stdcall *)(sp_session *)) &cb_metadata_updated //void (SP_CALLCONV *metadata_updated)(sp_session *session);
	, &cb_connection_error //void (SP_CALLCONV *connection_error)(sp_session *session, sp_error error);
	, &cb_message_to_user //void (SP_CALLCONV *message_to_user)(sp_session *session, const char *message);
	, &notify_main_thread //void (SP_CALLCONV *notify_main_thread)(sp_session *session);
	, &music_delivery //int (SP_CALLCONV *music_delivery)(sp_session *session, const sp_audioformat *format, const void *frames, int num_frames);
	, &cb_play_token_lost //void (SP_CALLCONV *play_token_lost)(sp_session *session);
	, &cb_log_message //void (SP_CALLCONV *log_message)(sp_session *session, const char *data);
	, &cb_end_of_track //&end_of_track //void (SP_CALLCONV *end_of_track)(sp_session *session);
	, &cb_streaming_error //void (SP_CALLCONV *streaming_error)(sp_session *session, sp_error error);
	, &cb_userinfo_updated //void (SP_CALLCONV *userinfo_updated)(sp_session *session);
	, &cb_start_playback //void (SP_CALLCONV *start_playback)(sp_session *session);
	, &cb_stop_playback //void (SP_CALLCONV *stop_playback)(sp_session *session);
	, &cb_get_audio_buffer_stats //void (SP_CALLCONV *get_audio_buffer_stats)(sp_session *session, sp_audio_buffer_stats *stats);
	, &cb_offline_status_updated //void (SP_CALLCONV *offline_status_updated)(sp_session *session);
};

JNIEXPORT void JNICALL Java_spotify_Session_Init(JNIEnv * env, jobject, jobject _sessionListener, jobject _playlistListener) {
	sp_session_config config;
	sp_error error;

    extern const uint8_t g_appkey[];
    extern const size_t g_appkey_size;
	memset(&config, 0, sizeof(config));

	config.api_version = SPOTIFY_API_VERSION;
	config.cache_location = "tmp";
	config.settings_location = "tmp";
	config.application_key = g_appkey;
	config.application_key_size = g_appkey_size;
	config.user_agent = "SpotifyDJ";
	config.callbacks = &callbacks;

	setEnv(env);
	sessionListener = env->NewGlobalRef(_sessionListener);
	
	error = sp_session_create(&config, &session);
	
	if (SP_ERROR_OK != error) {
		fprintf(stderr, "failed to create session: %s\n", sp_error_message(error));
	}

	// Playlist listener
	setListener(env->NewGlobalRef(_playlistListener));
}

JNIEXPORT void JNICALL Java_spotify_Session_Login(JNIEnv * env, jobject, jstring usernameJ, jstring passwordJ) {
	jboolean iscopy;
	const char *username = env->GetStringUTFChars(usernameJ, &iscopy);
	const char *password = env->GetStringUTFChars(passwordJ, &iscopy);
	sp_session_login(session, username, password, false); 
}

JNIEXPORT void JNICALL Java_spotify_Session_Logout(JNIEnv *, jobject) {
	sp_session_logout(getSession());
}

JNIEXPORT jint JNICALL Java_spotify_Session_ProcessEvents(JNIEnv *, jobject) {
	if (!session) return 1000;
	int next_timeout = 0;

	do {
		sp_session_process_events(session, &next_timeout);
	} while (next_timeout == 0);
	
	return next_timeout;
}

void cb_search_complete(sp_search *search, void *userdata) {
	jobject target = (jobject) userdata;
	for (int i = 0; i < sp_search_num_tracks(search); i++) {
		sp_track *track = sp_search_track(search, i);
		if (!sp_track_is_loaded(track)) continue;
		callVoidMethod(target, "addTrack", readTrack(track, false));
	}

	for (int i = 0; i < sp_search_num_albums(search); i++) {
		sp_album *album = sp_search_album(search, i);
		if (!sp_album_is_available(album)) continue;
		callVoidMethod(target, "addAlbum", readAlbum(album, false));
	}

	for (int i = 0; i < sp_search_num_artists(search); i++) {
		sp_artist *artist = sp_search_artist(search, i);
		callVoidMethod(target, "addArtist", readArtist(artist, false));
	}

	callVoidMethod(target, "setDidYouMean", sp_search_did_you_mean(search));
	callVoidMethod(target, "setComplete");
	sp_search_release(search);
	removeGlobalRef(target);
}
JNIEXPORT void JNICALL Java_spotify_Session_Search(JNIEnv *env, jobject, jstring _query, jint trackCount, jint albumCount, jint artistCount, jobject _target) {
	jboolean iscopy;
	const char *query = env->GetStringUTFChars(_query, &iscopy);

	sp_search_create(session, query, 0, trackCount, 0, albumCount, 0, artistCount, &cb_search_complete, env->NewGlobalRef(_target));
}

/**
 * Callbacks
 */
void cb_logged_in(sp_session *session, sp_error error){
	callVoidMethod(sessionListener, "cb_logged_in", error);
	initPlaylistListener();
}
void cb_logged_out(sp_session *session){
	callVoidMethod(sessionListener, "cb_logged_out");
}
void cb_metadata_updated(sp_session *session){
	callVoidMethod(sessionListener, "cb_metadata_updated");
}
void cb_connection_error(sp_session *session, sp_error error){
	callVoidMethod(sessionListener, "cb_connection_error", (int) error);
}
void cb_message_to_user(sp_session *session, const char *message){
	callVoidMethod(sessionListener, "cb_message_to_user", message);
}
void cb_play_token_lost(sp_session *session){
	callVoidMethod(sessionListener, "cb_play_token_lost");
}
void cb_log_message(sp_session *session, const char *data){
	callVoidMethod(sessionListener, "cb_log_message", data);
}
void cb_end_of_track(sp_session *session){
	callVoidMethod(sessionListener, "cb_end_of_track");
}
void cb_streaming_error(sp_session *session, sp_error error){
	callVoidMethod(sessionListener, "cb_streaming_error", (int) error);
}
void cb_userinfo_updated(sp_session *session){
	callVoidMethod(sessionListener, "cb_userinfo_updated");
}
void cb_start_playback(sp_session *session){
	callVoidMethod(sessionListener, "cb_start_playback");
}
void cb_stop_playback(sp_session *session){
	callVoidMethod(sessionListener, "cb_stop_playback");
}
void cb_get_audio_buffer_stats(sp_session *session, sp_audio_buffer_stats *stats){
	callVoidMethod(sessionListener, "cb_get_audio_buffer_stats");
}
void cb_offline_status_updated(sp_session *session){
	callVoidMethod(sessionListener, "cb_offline_status_updated");
}

JNIEXPORT void JNICALL Java_spotify_Session_RegisterPlayer(JNIEnv *env, jobject, jobject player) {
	setPlayer(env->NewGlobalRef(player));
}

JNIEXPORT jboolean JNICALL Java_spotify_Session_Play(JNIEnv *env, jobject, jobject trackObject) {
	const char* trackId = callStringMethod(trackObject, "getId");
	sp_track *track = sp_link_as_track(sp_link_create_from_string(trackId));
	if (!sp_track_is_available(getSession(), track)) {
		return false;
	}
	readTrack(trackObject, track, false);
	sp_session_player_load(session, track);
	sp_session_player_play(session, true);
	return true;
}

JNIEXPORT jboolean JNICALL Java_spotify_Session_CompleteTrack(JNIEnv *, jobject, jobject trackObject) {
	const char* trackId = callStringMethod(trackObject, "getId");
	sp_track *track = sp_link_as_track(sp_link_create_from_string(trackId));
	if (!sp_track_is_loaded(track)) {
		return false;
	}
	readTrack(trackObject, track, false);
	return true;
}

JNIEXPORT void JNICALL Java_spotify_Session_Seek(JNIEnv *env, jobject _this, jint position) {
	sp_session_player_seek(session, position);
	callVoidMethod(getPlayer(), "seekCallback", position);
}

JNIEXPORT void JNICALL Java_spotify_Session_Pause(JNIEnv *, jobject, jboolean pause) {
	sp_session_player_play(getSession(), !pause);
}


JNIEXPORT void JNICALL Java_spotify_Session_ReadArtistImage(JNIEnv *env, jobject, jstring _artistId, jobject target) {
	jboolean iscopy;
	const char *artistId = env->GetStringUTFChars(_artistId, &iscopy);
	readArtistImage(sp_link_as_artist(sp_link_create_from_string(artistId)), env->NewGlobalRef(target));
}

JNIEXPORT void JNICALL Java_spotify_Session_ReadAlbumImage(JNIEnv *env, jobject, jstring _albumId, jobject target) {
	jboolean iscopy;
	const char *albumId = env->GetStringUTFChars(_albumId, &iscopy);
	readAlbumImage(sp_link_as_album(sp_link_create_from_string(albumId)), env->NewGlobalRef(target));
}

JNIEXPORT jobject JNICALL Java_spotify_Session_BrowseArtist(JNIEnv *env, jobject, jstring _link) {
	jboolean iscopy;
	const char *link = env->GetStringUTFChars(_link, &iscopy);
	sp_artist *artist = sp_link_as_artist(sp_link_create_from_string(link));
	return readArtist(artist, true);
}

JNIEXPORT jobject JNICALL Java_spotify_Session_BrowseAlbum(JNIEnv *env, jobject, jstring _link) {
	jboolean iscopy;
	const char *link = env->GetStringUTFChars(_link, &iscopy);
	sp_album *album = sp_link_as_album(sp_link_create_from_string(link));
	return readAlbum(album, true);
}

void cb_toplistbrowse_complete(sp_toplistbrowse *result, void* userdata) {
	jobject target = (jobject) userdata;

	debug(sp_toplistbrowse_num_tracks(result) + "");
	for (int i = 0; i < sp_toplistbrowse_num_tracks(result); i++) {
		sp_track *track = sp_toplistbrowse_track(result, i);
		debug(sp_track_name(track));
		if (!sp_track_is_loaded(track)) continue;
		callVoidMethod(target, "addTrack", readTrack(track, false));
	}

	for (int i = 0; i < sp_toplistbrowse_num_albums(result); i++) {
		sp_album *album = sp_toplistbrowse_album(result, i);
		if (!sp_album_is_available(album)) continue;
		callVoidMethod(target, "addAlbum", readAlbum(album, false));
	}

	for (int i = 0; i < sp_toplistbrowse_num_artists(result); i++) {
		sp_artist *artist = sp_toplistbrowse_artist(result, i);
		callVoidMethod(target, "addArtist", readArtist(artist, false));
	}

	callVoidMethod(target, "setComplete");
	sp_toplistbrowse_release(result);
	removeGlobalRef(target);
}

JNIEXPORT void JNICALL Java_spotify_Session_TopList(JNIEnv *env, jobject, jint type, jobject target) {
	sp_toplisttype _type;
	switch (type) {
		case 0: _type = SP_TOPLIST_TYPE_ALBUMS; break;
		case 1: _type = SP_TOPLIST_TYPE_ARTISTS; break;
		case 2: _type = SP_TOPLIST_TYPE_TRACKS; break;
	}
	int region = sp_session_user_country(getSession()); 
	sp_toplistbrowse_create(getSession(), _type, (sp_toplistregion) region, NULL, &cb_toplistbrowse_complete, env->NewGlobalRef(target));
}