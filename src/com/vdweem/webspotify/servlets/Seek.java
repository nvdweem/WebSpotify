package com.vdweem.webspotify.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vdweem.webspotify.Util;

import spotify.Session;

public class Seek extends SpotifyServlet {

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		if (Util.isEmpty(getParam("position"))) return;
		int position = Util.parseInt(getParam("position"), 0);
		Session.getInstance().getPlayer().seek(position);
	}

}
