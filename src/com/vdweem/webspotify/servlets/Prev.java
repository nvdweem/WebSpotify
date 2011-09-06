package com.vdweem.webspotify.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spotify.Session;

/**
 * Goes to the previous song.
 * @author Niels
 */
public class Prev extends SpotifyServlet {
	private static final long serialVersionUID = 1663753237297957525L;

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		Session.getInstance().getPlayer().prev();
		printLn("{\"result\": \"success\"}");
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
