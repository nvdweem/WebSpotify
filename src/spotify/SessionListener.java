package spotify;

public interface SessionListener {
	public void initialize();
	public void checkLogin() throws RuntimeException;
	
	public void cb_logged_in(int error);
	public void cb_logged_out();
	public void cb_metadata_updated();
	public void cb_connection_error(int error);
	public void cb_message_to_user(String message);
	public void cb_play_token_lost();
	public void cb_log_message(String data);
	public void cb_end_of_track();
	public void cb_streaming_error(int error);
	public void cb_userinfo_updated();
	public void cb_start_playback();
	public void cb_stop_playback();
	public void cb_get_audio_buffer_stats(/*sp_audio_buffer_stats *stats*/);
	public void cb_offline_status_updated();
	
	// Callbacks from C++
	public Object createTrack(String id);
	public Object createAlbum(String id);
	public Object createArtist(String id);
	
	
	static final int SP_ERROR_OK                        = 0;  ///< No errors encountered
	static final int SP_ERROR_BAD_API_VERSION           = 1;  ///< The library version targeted does not match the one you claim you support
	static final int SP_ERROR_API_INITIALIZATION_FAILED = 2;  ///< Initialization of library failed - are cache locations etc. valid?
	static final int SP_ERROR_TRACK_NOT_PLAYABLE        = 3;  ///< The track specified for playing cannot be played
	static final int SP_ERROR_RESOURCE_NOT_LOADED       = 4;  ///< One or several of the supplied resources is not yet loaded
	static final int SP_ERROR_BAD_APPLICATION_KEY       = 5;  ///< The application key is invalid
	static final int SP_ERROR_BAD_USERNAME_OR_PASSWORD  = 6;  ///< Login failed because of bad username and/or password
	static final int SP_ERROR_USER_BANNED               = 7;  ///< The specified username is banned
	static final int SP_ERROR_UNABLE_TO_CONTACT_SERVER  = 8;  ///< Cannot connect to the Spotify backend system
	static final int SP_ERROR_CLIENT_TOO_OLD            = 9;  ///< Client is too old; library will need to be updated
	static final int SP_ERROR_OTHER_PERMANENT           = 10; ///< Some other error occured; and it is permanent (e.g. trying to relogin will not help)
	static final int SP_ERROR_BAD_USER_AGENT            = 11; ///< The user agent string is invalid or too long
	static final int SP_ERROR_MISSING_CALLBACK          = 12; ///< No valid callback registered to handle events
	static final int SP_ERROR_INVALID_INDATA            = 13; ///< Input data was either missing or invalid
	static final int SP_ERROR_INDEX_OUT_OF_RANGE        = 14; ///< Index out of range
	static final int SP_ERROR_USER_NEEDS_PREMIUM        = 15; ///< The specified user needs a premium account
	static final int SP_ERROR_OTHER_TRANSIENT           = 16; ///< A transient error occured.
	static final int SP_ERROR_IS_LOADING                = 17; ///< The resource is currently loading
	static final int SP_ERROR_NO_STREAM_AVAILABLE       = 18; ///< Could not find any suitable stream to play
	static final int SP_ERROR_PERMISSION_DENIED         = 19; ///< Requested operation is not allowed
	static final int SP_ERROR_INBOX_IS_FULL             = 20; ///< Target inbox is full
	static final int SP_ERROR_NO_CACHE                  = 21; ///< Cache is not enabled
	static final int SP_ERROR_NO_SUCH_USER              = 22; ///< Requested user does not exist
}
