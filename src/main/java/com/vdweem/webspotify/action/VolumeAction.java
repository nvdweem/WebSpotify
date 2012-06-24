package com.vdweem.webspotify.action;

import jahspotify.services.MediaPlayer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vdweem.webspotify.Util;

public class VolumeAction extends SpotifyServlet {
	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		int volume = Util.parseInt(getParam("volume"), 100);
		volume = Math.min(100, Math.max(0, volume)); // Between 0 and 100.

		MediaPlayer.getInstance().setVolume(volume);
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
