package com.vdweem.webspotify.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spotify.Session;

/**
 * Goes to the next song.
 * @author Niels
 */
public class Next extends SpotifyServlet {
	private static final long serialVersionUID = 1663753237297957525L;

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		Session.getInstance().getPlayer().skip();
		printSuccess();
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
