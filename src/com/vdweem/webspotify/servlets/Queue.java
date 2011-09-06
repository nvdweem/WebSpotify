package com.vdweem.webspotify.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spotify.Session;

public class Queue extends SpotifyServlet {
	private static final long serialVersionUID = 957052397960529372L;

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		
		printLn(Session.getInstance().getPlayer().toJSON().toString());
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
