package com.vdweem.webspotify.action;

import jahspotify.media.Artist;
import jahspotify.media.Image;
import jahspotify.media.Link;
import jahspotify.services.MediaHelper;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vdweem.webspotify.Util;

/**
 * Show an image. If no image is found then show the corresponding placeholder.
 * @author Niels
 */
public class ImageAction extends SpotifyServlet {

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse response)
			throws ServletException, IOException {
		setHeaders(response);
		String id = Util.nonNullString(getParam("id"));

		Link link = Link.create(id);
		String img = null;

		switch (link.getType()) {
		case ALBUM:
			img = "noAlbum.png";
			link = spotify.getJahSpotify().readAlbum(link).getCover();
			break;
		case ARTIST:
			img = "noArtist.png";
			Artist artist = spotify.getJahSpotify().readArtist(link, true);
			if (!MediaHelper.waitFor(artist, 2)) break;
			List<Link> links = artist.getPortraits();
			if (links.size() > 0) link = links.get(0);
			else link = null;
			break;
		case TRACK:
			link = Link.create(spotify.getJahSpotify().readTrack(link).getCover());
			break;
		}

		if (link != null) {
			Image image = spotify.getJahSpotify().readImage(link);
			if (image == null) return;
			if (MediaHelper.waitFor(image, 2)) {
				byte[] bytes = image.getBytes();
				response.getOutputStream().write(bytes, 0, bytes.length);
				return;
			}
		}
		response.sendRedirect("images/" + img);
	}
	private void setHeaders(HttpServletResponse response) {
		response.addHeader("Expires", "Mon, 26 Jul 1990 05:00:00 GMT");
		response.addHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.addHeader("Pragma", "no-cache");
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.image;
	}

}
