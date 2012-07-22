package com.vdweem.webspotify.action;

import jahspotify.media.Album;
import jahspotify.media.Link;
import jahspotify.services.JahSpotifyService;
import jahspotify.services.MediaHelper;

import com.opensymphony.xwork2.Result;
import com.vdweem.webspotify.Gsonner;
import com.vdweem.webspotify.Util;
import com.vdweem.webspotify.result.JsonResult;

/**
 * Browse for albums.
 * @author Niels
 */
public class AlbumBrowseAction {
	private String id;

	public Result execute() {
		if (Util.isEmpty(id)) return new JsonResult("No id given.", true);

		Album album = JahSpotifyService.getInstance().getJahSpotify().readAlbum(Link.create(id), true);
		MediaHelper.waitFor(album, 5);
		return JsonResult.onAjax(Gsonner.getGson(Album.class).toJson(album));
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
