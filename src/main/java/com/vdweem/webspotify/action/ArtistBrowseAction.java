package com.vdweem.webspotify.action;

import jahspotify.media.Album;
import jahspotify.media.Artist;
import jahspotify.media.Link;
import jahspotify.services.JahSpotifyService;
import jahspotify.services.MediaHelper;

import java.util.HashMap;
import java.util.Map;

import com.opensymphony.xwork2.Result;
import com.vdweem.webspotify.Gsonner;
import com.vdweem.webspotify.Util;
import com.vdweem.webspotify.result.JsonResult;

/**
 * Browse for artists.
 * @author Niels
 */
public class ArtistBrowseAction {
	private String id;

	public Result execute() {
		if (Util.isEmpty(id)) return new JsonResult("No id given.", true);

		Artist artist = JahSpotifyService.getInstance().getJahSpotify().readArtist(Link.create(id), true);
		MediaHelper.waitFor(artist, 10);

		Map<Link, Album> albums = new HashMap<Link, Album>();
		for (Link al : artist.getAlbums()) {
			albums.put(al, JahSpotifyService.getInstance().getJahSpotify().readAlbum(al, true));
		}
		MediaHelper.waitFor(albums.values(), 10);

		return JsonResult.onAjax(Gsonner.getGson(Artist.class, albums).toJson(artist));
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
