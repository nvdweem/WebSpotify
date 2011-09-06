package com.vdweem.webspotify;

import spotify.PlaylistListenerImpl;
import spotify.Session;
import spotify.SessionListenerImpl;

import com.vdweem.webspotify.servlets.AlbumBrowse;
import com.vdweem.webspotify.servlets.ArtistBrowse;
import com.vdweem.webspotify.servlets.Image;
import com.vdweem.webspotify.servlets.Next;
import com.vdweem.webspotify.servlets.Play;
import com.vdweem.webspotify.servlets.Playlist;
import com.vdweem.webspotify.servlets.Prev;
import com.vdweem.webspotify.servlets.Queue;
import com.vdweem.webspotify.servlets.Search;
import com.vdweem.webspotify.servlets.Seek;
import com.vdweem.webspotify.servlets.Status;
import com.vdweem.webspotify.servlets.menu.Login;
import com.vdweem.webspotify.servlets.menu.PlayPause;
import com.vdweem.webspotify.servlets.menu.SpotifyLogin;
import com.vdweem.webspotify.servlets.menu.SpotifyLogout;

/**
 * Start the webserver and initialize the Spotify library.
 * @author Niels
 */
public class Main {

	public static void main(String... args) {
		Session.getInstance().initialize(new SessionListenerImpl(), new PlaylistListenerImpl());
		Setup.getInstance().initSession();
		
		class MyServer extends Acme.Serve.Serve {
			private static final long serialVersionUID = -2268615199362928333L;

			// Overriding method for public access
			public void setMappingTable(PathTreeDictionary mappingtable) {
				super.setMappingTable(mappingtable);
			}

			// add the method below when .war deployment is needed
			public void addWarDeployer(String deployerFactory, String throttles) {
				super.addWarDeployer(deployerFactory, throttles);
			}
		};

		final MyServer srv = new MyServer();
		// setting aliases, for an optional file servlet
		Acme.Serve.Serve.PathTreeDictionary aliases = new Acme.Serve.Serve.PathTreeDictionary();
		aliases.put("/", new java.io.File(Main.class.getResource(".").getFile() + "/resources/")); // Point the root to the resources folder.
		srv.setMappingTable(aliases);
		
		// setting properties for the server, and exchangeable Acceptors
		java.util.Properties properties = new java.util.Properties();
		properties.put("port", 80); // TODO: Make the port configurable.
		properties.setProperty(Acme.Serve.Serve.ARG_NOHUP, "nohup");
		srv.arguments = properties;
		srv.addDefaultServlets(null);

		// Add the servlets to the webserver.
		srv.addServlet("/Search", new Search());
		srv.addServlet("/Image", new Image());
		srv.addServlet("/Status", new Status());
		
		srv.addServlet("/Queue", new Queue());
		srv.addServlet("/Play", new Play());
		srv.addServlet("/Seek", new Seek());
		srv.addServlet("/PlayPause", new PlayPause());
		srv.addServlet("/Next", new Next());
		srv.addServlet("/Prev", new Prev());
		
		srv.addServlet("/ArtistBrowse", new ArtistBrowse());
		srv.addServlet("/AlbumBrowse", new AlbumBrowse());
		srv.addServlet("/Playlist", new Playlist());
		
		srv.addServlet("/Login", new Login());
		srv.addServlet("/SpotifyLogin", new SpotifyLogin());
		srv.addServlet("/SpotifyLogout", new SpotifyLogout());
		
		 
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				srv.notifyStop();
				srv.destroyAllServlets();
			}
		}));
		srv.serve();
	}

}
