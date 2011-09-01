package com.vdweem.webspotify;

import spotify.PlaylistListenerImpl;
import spotify.Session;
import spotify.SessionListenerImpl;

import com.vdweem.webspotify.servlets.AlbumBrowse;
import com.vdweem.webspotify.servlets.ArtistBrowse;
import com.vdweem.webspotify.servlets.Image;
import com.vdweem.webspotify.servlets.Play;
import com.vdweem.webspotify.servlets.Search;
import com.vdweem.webspotify.servlets.Seek;
import com.vdweem.webspotify.servlets.Status;

/**
 * Start the webserver and initialize the Spotify library.
 * @author Niels
 */
public class Main {

	public static void main(String... args) {
		// Setup the Spotify library
		Session.getInstance().initialize(new SessionListenerImpl(), new PlaylistListenerImpl());
		Session.getInstance().login(Setup.username, Setup.password); // TODO: Make username and password configurable.
		
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
		srv.addServlet("/Play", new Play());
		srv.addServlet("/Status", new Status());
		srv.addServlet("/Seek", new Seek());
		srv.addServlet("/ArtistBrowse", new ArtistBrowse());
		srv.addServlet("/AlbumBrowse", new AlbumBrowse());
		
		 
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				srv.notifyStop();
				srv.destroyAllServlets();
			}
		}));
		srv.serve();
	}

}
