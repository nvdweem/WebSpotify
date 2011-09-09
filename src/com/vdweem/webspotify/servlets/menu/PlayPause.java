package com.vdweem.webspotify.servlets.menu;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spotify.Session;

import com.vdweem.webspotify.servlets.SpotifyServlet;

public class PlayPause extends SpotifyServlet {
	private static final long serialVersionUID = 7943504750398693190L;

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {

		Session.getInstance().getPlayer().pause();
		printSuccess();
	}



	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
