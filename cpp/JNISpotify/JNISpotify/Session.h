#include "api.h"
#include "JNI.h"

sp_session* getSession();
jobject getSessionListener();
void cb_logged_in(sp_session *session, sp_error error);
void cb_logged_out(sp_session *session);
void cb_metadata_updated(sp_session *session);
void cb_connection_error(sp_session *session, sp_error error);
void cb_message_to_user(sp_session *session, const char *message);
void cb_play_token_lost(sp_session *session);
void cb_log_message(sp_session *session, const char *data);
void cb_end_of_track(sp_session *session);
void cb_streaming_error(sp_session *session, sp_error error);
void cb_userinfo_updated(sp_session *session);
void cb_start_playback(sp_session *session);
void cb_stop_playback(sp_session *session);
void cb_get_audio_buffer_stats(sp_session *session, sp_audio_buffer_stats *stats);
void cb_offline_status_updated(sp_session *session);

jobject readAlbum(sp_album *, bool);
jobject readArtist(sp_artist *, bool);
jobject readTrack(sp_track *, bool);