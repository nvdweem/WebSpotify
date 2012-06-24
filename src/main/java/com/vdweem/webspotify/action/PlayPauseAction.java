package com.vdweem.webspotify.action;

import jahspotify.services.MediaPlayer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PlayPauseAction extends SpotifyServlet {

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {

		MediaPlayer.getInstance().pause();
		printSuccess();
	}



	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
