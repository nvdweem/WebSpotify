package com.vdweem.webspotify.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vdweem.webspotify.Util;

public class Volume extends SpotifyServlet {
	private static final long serialVersionUID = 463099436656577475L;
	
	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		int volume = Util.parseInt(getParam("volume"), 100);
		volume = Math.min(100, Math.max(0, volume)); // Between 0 and 100.
		spotify.Session.getInstance().getPlayer().setVolume(volume);
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
