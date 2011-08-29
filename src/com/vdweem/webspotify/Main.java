package com.vdweem.webspotify;

import spotify.PlaylistListenerImpl;
import spotify.Session;
import spotify.SessionListenerImpl;

import com.vdweem.webspotify.servlets.Image;
import com.vdweem.webspotify.servlets.Search;

public class Main {

	public static void main(String... args) {
		Session.getInstance().initialize(new SessionListenerImpl(), new PlaylistListenerImpl());
		Session.getInstance().login(Setup.username, Setup.password);
		
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
		aliases.put("/", new java.io.File(Main.class.getResource(".").getFile() + "/resources/"));
		srv.setMappingTable(aliases);
		
		// setting properties for the server, and exchangeable Acceptors
		java.util.Properties properties = new java.util.Properties();
		properties.put("port", 80);
		properties.setProperty(Acme.Serve.Serve.ARG_NOHUP, "nohup");
		srv.arguments = properties;
		srv.addDefaultServlets(null); // optional file servlet

		srv.addServlet("/Search", new Search());
		srv.addServlet("/Image", new Image());
		 
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				srv.notifyStop();
				srv.destroyAllServlets();
			}
		}));
		srv.serve();
	}

}
