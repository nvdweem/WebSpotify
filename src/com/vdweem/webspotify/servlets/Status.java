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
		
		Track playing = player.getCurrentTrack();
		if (playing != null)
			result.put("playing", playing.toJSON());
		result.put("pause", !player.isPlaying());
		
		result.put("playlistRevision", Session.getInstance().getPlaylistContainer().getRevision());
		
		JSONObject menu = new JSONObject();
		menu.put("loggedIn", getSession().isAdmin());
		if (getSession().isAdmin()) {
			menu.put("loginError", Session.getInstance().isLoginError());
			menu.put("spotify", Session.getInstance().isLoggedIn());
		}
		result.put("menu", menu);
		
		printLn(result.toString());
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}
	
}
