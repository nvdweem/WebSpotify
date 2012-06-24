package com.vdweem.webspotify.action;

import jahspotify.media.Track;
import jahspotify.services.MediaPlayer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.vdweem.webspotify.Gsonner;

/**
 * Performs a status update.
 * @author Niels
 */
public class StatusAction extends SpotifyServlet {

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {

		JsonObject result = new JsonObject();
		Track playing = MediaPlayer.getInstance().getCurrentTrack();
		MediaPlayer mp = MediaPlayer.getInstance();
		JsonObject position = new JsonObject();
		position.addProperty("duration", mp.getDuration());
		position.addProperty("position", mp.getPosition());
		result.add("position", position);

		if (playing != null) {
			result.add("playing", Gsonner.getGson(Track.class).toJsonTree(mp.getCurrentTrack()));
		}
		result.addProperty("pause", !mp.isPlaying());
		result.addProperty("shuffling", false);
		result.addProperty("volume", mp.getVolume());

		result.addProperty("playlistRevision", 0);
		result.addProperty("queuerevision", 0);

		printLn(result.toString());
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
