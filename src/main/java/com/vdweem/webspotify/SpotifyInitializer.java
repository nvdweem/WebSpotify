package com.vdweem.webspotify;

import jahspotify.services.JahSpotifyService;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Initializes Jah'Spotify with a setting folder.
 * @author Niels
 *
 */
public class SpotifyInitializer implements ServletContextListener{
	private static File appFolder;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		File root = new File(sce.getServletContext().getRealPath(".")).getParentFile().getParentFile().getParentFile();
		File config = new File(root, "jahspotify");
		config.mkdirs();
		JahSpotifyService.initialize(config);
		QueueHandler.initialize();
		appFolder = config;

		StateSaver.getInstance();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		StateSaver.getInstance().terminate();
	}

	public static File getAppFolder() {
		return appFolder;
	}
}
