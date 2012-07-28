package com.vdweem.webspotify.action;

import jahspotify.media.Playlist;
import jahspotify.media.PlaylistContainer;
import jahspotify.media.Track;
import jahspotify.services.MediaPlayer;

import com.google.gson.JsonObject;
import com.opensymphony.xwork2.Result;
import com.vdweem.webspotify.Gsonner;
import com.vdweem.webspotify.QueueHandler;
import com.vdweem.webspotify.result.JsonResult;

/**
 * Performs a status update.
 * @author Niels
 */
public class StatusAction {

	public Result execute() {
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
		result.addProperty("shuffling", QueueHandler.isShuffle());
		result.addProperty("volume", mp.getVolume());

		result.addProperty("queuerevision", QueueHandler.getRevision());

		StringBuffer hashBuff = new StringBuffer();
		for (Playlist key : PlaylistContainer.getPlaylists())
			hashBuff.append(key.getId());
		result.addProperty("playlistRevision", hashBuff.toString().hashCode());

		return new JsonResult(result.toString());
	}

}
