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
		File userDir = new File(System.getProperty("user.home"));
		File config = new File(userDir, ".libjahspotify");
		config.mkdirs();

		JahSpotifyService.initialize(config);
		QueueHandler.initialize();
		appFolder = config;

		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(250);
				JahSpotifyService.getInstance().getJahSpotify().login(System.getProperty("jahspotify.spotify.username"), System.getProperty("jahspotify.spotify.password"), null, false);
				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		StateSaver.getInstance();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		StateSaver.getInstance().terminate();
		JahSpotifyService.getInstance().getJahSpotify().destroy();
	}

	public static File getAppFolder() {
		return appFolder;
	}
}
