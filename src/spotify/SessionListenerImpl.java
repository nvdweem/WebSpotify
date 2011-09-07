package spotify;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation for Spotify events and actions.
 * @author Niels
 */
public class SessionListenerImpl implements SessionListener {

	private boolean initialized = false;
	private boolean loggedIn = false;
	private EventProcessor processor;
	private boolean loginError;
	
	public SessionListenerImpl() {
		processor = new EventProcessor();
		processor.start();
	}
	
	public boolean isLoggedIn() {
		return loggedIn;
	}
	public boolean isLoginError() {
		boolean result = loginError;
		loginError = false;
		return result;
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
		loggedIn = error == SP_ERROR_OK;
		loginError = !loggedIn;
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
		Session.getInstance().getPlayer().endOfTrack();
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
	private Map<String, WeakReference<Track>> tracks = new HashMap<String, WeakReference<Track>>();
	private Map<String, WeakReference<Album>> albums = new HashMap<String, WeakReference<Album>>();
	private Map<String, WeakReference<Artist>> artists = new HashMap<String, WeakReference<Artist>>();
	public Object createTrack(String id) {
		if (!tracks.containsKey(id) || tracks.get(id).get() == null)
			tracks.put(id, new WeakReference<Track>(new Track(id)));
		Track track = tracks.get(id).get();
		track.unComplete();
		return track;
	}
	public Object createAlbum(String id) {
		if (!albums.containsKey(id) || albums.get(id).get() == null)
			albums.put(id, new WeakReference<Album>(new Album(id)));
		Album album = albums.get(id).get();
		album.unComplete();
		return album;
	}
	public Object createArtist(String id) {
		if (!artists.containsKey(id) || artists.get(id).get() == null)
			artists.put(id, new WeakReference<Artist>(new Artist(id)));
		Artist artist = artists.get(id).get();
		artist.unComplete();
		return artist;
	}
}
