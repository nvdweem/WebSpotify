package com.vdweem.webspotify;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import spotify.Session;

/**
 * TODO: Use the libspotify 9 'remember me' functions.
 * @author Niels
 */
public class Setup {
	public static final String fileName = "spotify.properties";
	
	private static Setup setup;
	private Properties properties;
	
	public static Setup getInstance() {
		if (setup == null) setup = new Setup();
		return setup;
	}
	
	private Setup() {
		properties = new Properties();
	}
	
	public void deleteSettings() {
		if (!new File(getFileName()).delete())
			new File(getFileName()).deleteOnExit();
	}
	
	public void initSession() {
		try {
			properties.load(Setup.class.getResourceAsStream(fileName));
		} catch (Exception e) {
			return;
		}
		Session.getInstance().login(properties.getProperty("username"), properties.getProperty("password"));
	}
	
	public void saveSettings(String username, String password) {
		properties.put("username", username);
		properties.put("password", password);
		try {
			properties.store(new FileOutputStream(getFileName()), "Spotify username/password");
		} catch (Exception e) {
		}
	}
	
	private String getFileName() {
		String name;
		
		URL url = Setup.class.getResource(fileName);
		if (url == null)
			name = Setup.class.getResource(".").getFile() + fileName;
		else
			name = url.getFile();
		return name;
	}
}
