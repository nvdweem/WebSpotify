package com.vdweem.webspotify.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spotify.Session;
import spotify.Track;

import com.vdweem.webspotify.Util;

/**
 * Play a track.
 * @author Niels
 */
public class Play extends SpotifyServlet {
	private static final long serialVersionUID = 4369945020882244504L;

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		String id = getParam("id");
		if (Util.isEmpty(id)) {
			printError("No id specified");
			return;
		}
		
		Session session = Session.getInstance();
		session.getPlayer().play(new Track(id));
		printLn("{\"result\": \"success\"}");
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}
	
}
