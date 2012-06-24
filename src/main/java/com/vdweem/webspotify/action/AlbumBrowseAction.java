package com.vdweem.webspotify.action;

import jahspotify.media.Album;
import jahspotify.media.Link;
import jahspotify.services.MediaHelper;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vdweem.webspotify.Gsonner;
import com.vdweem.webspotify.Util;

/**
 * Browse for albums.
 * @author Niels
 */
public class AlbumBrowseAction extends SpotifyServlet {
	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		String id = getParam("id");
		if (Util.isEmpty(id)) return;

		Album album = spotify.getJahSpotify().readAlbum(Link.create(id), true);
		MediaHelper.waitFor(album, 5);
		printLn(Gsonner.getGson(Album.class).toJson(album));
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
