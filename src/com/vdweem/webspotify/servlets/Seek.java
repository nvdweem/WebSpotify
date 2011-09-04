package com.vdweem.webspotify.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spotify.Session;

import com.vdweem.webspotify.Util;

/**
 * Seeks a position for a song.
 * @author Niels
 */
public class Seek extends SpotifyServlet {
	private static final long serialVersionUID = 1663753237297957525L;

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		if (Util.isEmpty(getParam("position"))) return;
		int position = Util.parseInt(getParam("position"), 0);
		Session.getInstance().getPlayer().seek(position);
		
		printLn("{\"result\": \"success\"}");
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
