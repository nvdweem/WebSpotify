package spotify;

/**
 * Session God class. Handles all interactions between the Java code and the Spotify library.
 * @author Niels
 */
public class Session {
	private SessionListener sessionListener;
	private PlaylistListener playlistListener;
	private Player player;
	
	private static final Session instance = new Session();
	static {
		System.loadLibrary("JNISpotify");
	}
	
	public static Session getInstance() {
		return instance;
	}
	private Session() {}
	
	public void initialize(final SessionListener listener, final PlaylistListener playlist)
	{
		Init(listener, playlist);
		listener.initialize();
		this.sessionListener = listener;
		this.playlistListener = playlist;
		this.player = new Player(this);
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void login(String username, String password) {
		if (sessionListener != null)
			Login(username, password);
	}
	
	public Search search(String query, int trackCount, int albumCount, int artistCount) {
		sessionListener.checkLogin();
		Search target = new Search();
		Search(query, trackCount, albumCount, artistCount, target);
		return target;
	}
	
	public Image readArtistImage(String id) {
		Image result = new Image(Image.Type.artist);
		ReadArtistImage(id, result);
		return result;
	}
	
	public Image readAlbumImage(String id) {
		Image result = new Image(Image.Type.album);
		ReadAlbumImage(id, result);
		return result;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Artist browseArtist(String link) {
		return BrowseArtist(link);
	}
	
	public Album  browseAlbum(String link) {
		return BrowseAlbum(link);
	}
	
	public PlaylistContainer getPlaylistContainer() {
		return playlistListener.getContainer();
	}
	
	private native void Init(SessionListener listener, PlaylistListener playlist);
	private native void Login(String username, String password);
	private native void Search(String query, int trackCount, int albumCount, int artistCount, Search result);
	protected native int ProcessEvents();
	
	protected native void RegisterPlayer(Player player);
	protected native boolean Play(Track track);
	protected native void Seek(int position);
	
	private native void ReadArtistImage(String id, Image target);
	private native void ReadAlbumImage(String id, Image target);
	private native Artist BrowseArtist(String link);
	private native Album BrowseAlbum(String link);
}
