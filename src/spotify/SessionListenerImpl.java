package spotify;

/**
 * Implementation for Spotify events and actions.
 * @author Niels
 */
public class SessionListenerImpl implements SessionListener {

	private boolean initialized = false;
	private boolean loggedIn = false;
	private EventProcessor processor;
	
	public SessionListenerImpl() {
		processor = new EventProcessor();
		processor.start();
	}
	
	public void initialize() {
		initialized = true;
	}
	
	public void debug(String text) {
		System.out.println(text);
	}
	
	@Override
	public void cb_notify_main_thread() {
		processor.processEventsNow();
	}

	
	public void checkLogin() throws RuntimeException {
		if (!initialized) throw new RuntimeException("Please initialize first.");
		if (!loggedIn) throw new RuntimeException("Please login first.");
	}
	
	public void cb_logged_in(int error){
		System.out.println("Logged in: " + error);
		loggedIn = error == SP_ERROR_OK;
	}
	
	public void cb_logged_out(){
		System.out.println("Logged out");
		loggedIn = false;
	}
	
	public void cb_metadata_updated(){
		System.out.println("cb_metadata_updated");
	}
	public void cb_connection_error(int error){
		System.out.println("cb_connection_error: " + error);
	}
	public void cb_message_to_user(String message){
		System.out.println("cb_message_to_user: " + message);
	}
	public void cb_play_token_lost(){
		System.out.println("cb_play_token_lost");
	}
	public void cb_log_message(String data){
		System.out.println("cb_log_message: " + data);
	}
	public void cb_end_of_track(){
		System.out.println("cb_end_of_track");
		System.exit(0);
	}
	public void cb_streaming_error(int error){
		System.out.println("cb_streaming_error: " + error);
	}
	public void cb_userinfo_updated(){
		System.out.println("cb_userinfo_updated");
	}
	public void cb_start_playback(){
		System.out.println("cb_start_playback");
	}
	public void cb_stop_playback(){
		System.out.println("cb_stop_playback");
	}
	public void cb_get_audio_buffer_stats(/*sp_audio_buffer_stats *stats*/){
//		System.out.println("cb_get_audio_buffer_stats");
	}
	public void cb_offline_status_updated(){
		System.out.println("cb_offline_status_updated");
	}

	// Callbacks from C++
	public Object createTrack(String id) {
		return new Track(id);
	}
	public Object createAlbum(String id) {
		return new Album(id);
	}
	public Object createArtist(String id) {
		return new Artist(id);
	}
}
