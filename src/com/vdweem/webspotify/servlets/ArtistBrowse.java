package com.vdweem.webspotify.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spotify.Artist;
import spotify.Session;

import com.vdweem.webspotify.Util;

/**
 * Browse for artists.
 * @author Niels
 */
public class ArtistBrowse extends SpotifyServlet {
	private static final long serialVersionUID = -1546402760249122315L;

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		String id = getParam("id");
		if (Util.isEmpty(id)) return;
		
		Artist artist = Session.getInstance().browseArtist(id);
		artist.waitFor(100);
		printLn(artist.toString());
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
