package com.vdweem.webspotify.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spotify.Session;

public class Shuffle extends SpotifyServlet {
	private static final long serialVersionUID = -5018002231574876394L;

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		
		Session.getInstance().getPlayer().shuffle();
		printSuccess();
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
