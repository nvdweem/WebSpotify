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
	
	public boolean isLoggedIn() {
		return sessionListener.isLoggedIn();
	}
	
	public void initialize(final SessionListener listener, final PlaylistListener playlist)
	{
		synchronized(this) {
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
	}
	
	public void login(String username, String password) {
		if (sessionListener != null) {
			synchronized(this) {
				Login(username, password);
			}
		}
	}
	
	public void logout() {
		if (sessionListener != null) {
			synchronized(this) {
				Logout();
			}
		}
	}
	
	public boolean isLoginError() {
		if (sessionListener == null) return false;
		return sessionListener.isLoginError();
	}
	
	public Search search(String query, int trackCount, int albumCount, int artistCount) {
		sessionListener.checkLogin();
		Search target = new Search();
		
		synchronized(this) {
			Search(query, trackCount, albumCount, artistCount, target);
		}
		return target;
	}
	
	public Image readArtistImage(String id) {
		Image result = new Image(Image.Type.artist);
		synchronized(this) {
			ReadArtistImage(id, result);
		}
		return result;
	}
	
	public Image readAlbumImage(String id) {
		Image result = new Image(Image.Type.album);
		synchronized(this) {
			ReadAlbumImage(id, result);
		}
		return result;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Artist browseArtist(String link) {
		synchronized(this) {
			return BrowseArtist(link);
		}
	}
	
	public Album  browseAlbum(String link) {
		synchronized(this) {
			return BrowseAlbum(link);
		}
	}
	
	public PlaylistContainer getPlaylistContainer() {
		return playlistListener.getContainer();
	}
	
	public boolean completeTrack(Track track) {
		synchronized(this) {
			return CompleteTrack(track);
		}
	}
	
	public void pause(boolean pause) {
		synchronized(this) {
			Pause(pause);
		}
	}
	
	public boolean play(Track track) {
		synchronized(this) {
			return Play(track);
		}
	}
	
	public void seek(int position) {
		synchronized(this) {
			Seek(position);
		}
	}
	
	public int processEvents() {
		synchronized(this) {
			return ProcessEvents();
		}
	}
	
	public void registerPlayer(Player player) {
		synchronized(this) {
			RegisterPlayer(player);
		}
	}
	
	/**
	 * Types:
	 *   0: album
	 *   1: artist
	 *   2: track
	 * @param type
	 * @return
	 */
	public Search topList(int type) {
		Search search = new Search();
		TopList(type, search);
		return search;
	}
	
	private native void Init(SessionListener listener, PlaylistListener playlist);
	private native void Login(String username, String password);
	private native void Logout();
	private native void Search(String query, int trackCount, int albumCount, int artistCount, Search result);
	private native int ProcessEvents();
	
	private native void RegisterPlayer(Player player);
	private native boolean CompleteTrack(Track track);
	private native boolean Play(Track track);
	private native void Seek(int position);
	private native void Pause(boolean pause);
	
	private native void TopList(int type, Search search);
	private native void ReadArtistImage(String id, Image target);
	private native void ReadAlbumImage(String id, Image target);
	private native Artist BrowseArtist(String link);
	private native Album BrowseAlbum(String link);
}
