package com.vdweem.webspotify.action;

import jahspotify.services.MediaPlayer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vdweem.webspotify.Util;

/**
 * Seeks a position for a song.
 * @author Niels
 */
public class SeekAction extends SpotifyServlet {
	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		if (Util.isEmpty(getParam("position"))) return;
		int position = Util.parseInt(getParam("position"), 0);
		MediaPlayer.getInstance().seek(position);

		printSuccess();
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
