package com.vdweem.webspotify.action;

import jahspotify.media.Link;
import jahspotify.media.Playlist;
import jahspotify.services.MediaPlayer;
import jahspotify.services.QueueManager;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vdweem.webspotify.Util;

public class PlaylistQueueAction extends SpotifyServlet {
	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {

		int position = Util.parseInt(getParam("index"), -1);
		if (position == -1) {
			printError("Unknown playlist index.");
			return;
		}


		Playlist pl = spotify.getJahSpotify().getPlaylistContainer().getPlaylist(position);
		for (Link track : pl.getTracks()) {
			System.out.println(track);
			QueueManager.getInstance().addToQueue(null, track);
		}

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
