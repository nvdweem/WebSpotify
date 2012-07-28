package com.vdweem.webspotify.action;

import jahspotify.media.Link;
import jahspotify.media.Playlist;
import jahspotify.media.PlaylistContainer;
import jahspotify.services.MediaPlayer;

import com.opensymphony.xwork2.Result;
import com.vdweem.webspotify.QueueHandler;
import com.vdweem.webspotify.result.JsonResult;

/**
 * Queues a playlist.
 * @author Niels
 */
public class PlaylistQueueAction {
	private int index = -1;

	public Result execute() {
		if (index == -1) {
			return new JsonResult("Unknown playlist index.", true);
		}

		Playlist pl = PlaylistContainer.getPlaylist(index);
		for (Link track : pl.getTracks()) {
			QueueHandler.addToList(track);
		}

		MediaPlayer player = MediaPlayer.getInstance();
		if (!player.isPlaying())
			player.play();
		return JsonResult.SUCCESS;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
