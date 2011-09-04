package com.vdweem.webspotify.servlets.menu;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spotify.Session;

import com.vdweem.webspotify.Setup;
import com.vdweem.webspotify.servlets.SpotifyServlet;

public class SpotifyLogout extends SpotifyServlet {
	private static final long serialVersionUID = 3398279217996980615L;

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		Setup.getInstance().deleteSettings();
		Session.getInstance().logout();
		Session.getInstance().getPlaylistContainer().clear();
		printLn("{\"result\": \"success\"}");
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
