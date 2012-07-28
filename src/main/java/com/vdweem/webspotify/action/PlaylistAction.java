package com.vdweem.webspotify.action;

import jahspotify.media.Link;
import jahspotify.media.Playlist;
import jahspotify.media.PlaylistContainer;
import jahspotify.services.JahSpotifyService;
import jahspotify.services.MediaHelper;

import com.opensymphony.xwork2.Result;
import com.vdweem.webspotify.Gsonner;
import com.vdweem.webspotify.Util;
import com.vdweem.webspotify.result.JsonResult;

/**
 * Serves playlist data.
 * @author Niels
 */
public class PlaylistAction {
	private int index = -1;
	private String id;

	public Result execute() {

		if (!Util.isEmpty(id)) {
			Playlist playlist = JahSpotifyService.getInstance().getJahSpotify().readPlaylist(Link.create(id), 0, 0);
			MediaHelper.waitFor(playlist, 2);
			return JsonResult.onAjax(Gsonner.getGson(Playlist.class).toJson(playlist));
		}

		if (index == -2) {
			Playlist pl = JahSpotifyService.getInstance().getJahSpotify().readPlaylist(null, 0, 0);
			return JsonResult.onAjax(Gsonner.getGson(Playlist.class).toJson(pl));
		}

		if (index != -1) {
			Playlist pl = PlaylistContainer.getPlaylist(index);
			return JsonResult.onAjax(Gsonner.getGson(Playlist.class).toJson(pl));
		}

		return JsonResult.onAjax(Gsonner.getGson(PlaylistContainer.class).toJson(new PlaylistContainer()));
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
