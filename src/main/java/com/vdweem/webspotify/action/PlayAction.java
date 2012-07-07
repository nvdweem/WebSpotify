package com.vdweem.webspotify.action;

import jahspotify.media.Link;
import jahspotify.services.MediaPlayer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vdweem.webspotify.QueueHandler;
import com.vdweem.webspotify.Util;

/**
 * Play a track.
 * @author Niels
 */
public class PlayAction extends SpotifyServlet {

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		String id = getParam("id");
		if (Util.isEmpty(id)) {
			printError("No id specified");
			return;
		}

		QueueHandler.addToQueue(Link.create(id));
		MediaPlayer player = MediaPlayer.getInstance();
		if (!player.isPlaying())
			player.play();
		printSuccess();
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
