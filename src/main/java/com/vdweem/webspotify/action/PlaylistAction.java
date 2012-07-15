package com.vdweem.webspotify.action;

import jahspotify.media.Playlist;
import jahspotify.media.PlaylistContainer;
import jahspotify.services.JahSpotifyService;

import com.opensymphony.xwork2.Result;
import com.vdweem.webspotify.Gsonner;
import com.vdweem.webspotify.result.JsonResult;

/**
 * Serves playlist data.
 * @author Niels
 */
public class PlaylistAction {
	private int index = -1;

	public Result execute() {
		PlaylistContainer container = JahSpotifyService.getInstance().getJahSpotify().getPlaylistContainer();

		if (index == -2) {
			Playlist pl = JahSpotifyService.getInstance().getJahSpotify().readPlaylist(null, 0, 0);
			return new JsonResult(Gsonner.getGson(Playlist.class).toJson(pl));
		}

		if (index != -1) {
			Playlist pl = container.getPlaylist(index);
			return new JsonResult(Gsonner.getGson(Playlist.class).toJson(pl));
		}

		return new JsonResult(Gsonner.getGson(PlaylistContainer.class).toJson(container));
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
