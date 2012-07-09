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
		boolean delete = "true".equals(getParam("delete"));
		boolean deleteAll = "all".equals(getParam("delete"));

		if (deleteAll) {
			QueueHandler.clear();
		}
		if (Util.isEmpty(id)) {
			printError("No id specified");
			return;
		}

		Link link = Link.create(id);
		if (delete) {
			QueueHandler.remove(link);
			return;
		}
		QueueHandler.addToQueue(link);
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
