package com.vdweem.webspotify.action;

import jahspotify.media.Album;
import jahspotify.media.Artist;
import jahspotify.media.Link;
import jahspotify.services.MediaHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vdweem.webspotify.Gsonner;
import com.vdweem.webspotify.Util;

/**
 * Browse for artists.
 * @author Niels
 */
public class ArtistBrowseAction extends SpotifyServlet {

	@Override
	public void execute() {
		super.execute();
	}
	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		String id = getParam("id");
		if (Util.isEmpty(id)) return;

		Artist artist = spotify.getJahSpotify().readArtist(Link.create(id), true);

		MediaHelper.waitFor(artist, 10);

		Map<Link, Album> albums = new HashMap<Link, Album>();
		for (Link al : artist.getAlbums()) {
			albums.put(al, spotify.getJahSpotify().readAlbum(al, true));
		}
		MediaHelper.waitFor(albums.values(), 10);

		printLn(Gsonner.getGson(Artist.class, albums).toJson(artist));
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
