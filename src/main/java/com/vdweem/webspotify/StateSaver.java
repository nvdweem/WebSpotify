package com.vdweem.webspotify;

import jahspotify.impl.JahSpotifyImpl;
import jahspotify.services.MediaPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Saves and loads relevant parts of the application.
 * @author Niels
 */
public class StateSaver extends Thread {

	private boolean running = true;

	private static StateSaver instance;
	public static synchronized StateSaver getInstance() {
		if (instance == null) {
			instance = new StateSaver();
			instance.start();
		}
		return instance;
	}

	private StateSaver() {super("State saver");}

	/**
	 * Save every 5 minutes.
	 */
	@Override
	public void run() {

		// Wait for JahSpotify to be started.
		while (!JahSpotifyImpl.getInstance().isStarted()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}

		// Once it has started, give it some time to initialize.
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		// Load the previous state.
		loadState();
		while (running) {
			try {
				synchronized(this) {
					wait(1000 * 60 * 5); // 5 minutes
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			try {
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(SpotifyInitializer.getAppFolder(), "jahspotify.dat")));

				QueueHandler.save(oos);
				oos.writeInt(MediaPlayer.getInstance().getVolume());
				oos.writeBoolean(MediaPlayer.getInstance().isPlaying());

				oos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * Stop waiting for the 5 minute marker, save now.
	 */
	public synchronized void saveNow() {
		notify();
	}

	/**
	 * Load the saved state.
	 */
	private void loadState() {
		try {
			File file = new File(SpotifyInitializer.getAppFolder(), "jahspotify.dat");
			if (!file.exists()) return;
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

			QueueHandler.load(ois);
			MediaPlayer.getInstance().setVolume(ois.readInt());
			if (ois.readBoolean())
				MediaPlayer.getInstance().play();

			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Save and terminate the thread.
	 */
	public synchronized void terminate() {
		running = false;
		notify();
	}

}
