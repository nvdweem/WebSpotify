package com.vdweem.webspotify.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import spotify.Player;
import spotify.Session;
import spotify.Track;

/**
 * Performs a status update.
 * @author Niels
 */
public class Status extends SpotifyServlet {
	private static final long serialVersionUID = 3616323075290964160L;

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		
		Player player = Session.getInstance().getPlayer();
		
		JSONObject result = new JSONObject();
		
		JSONObject position = new JSONObject();
		position.put("duration", player.getDuration());
		position.put("position", player.getPosition());
		result.put("position", position);
		
		Track playing = player.getPlaying();
		if (playing != null)
			result.put("playing", playing.toJSON());
		
		printLn(result.toString());
	}
	
}
