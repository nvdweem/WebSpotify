package com.vdweem.webspotify.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spotify.Artist;
import spotify.Session;

import com.vdweem.webspotify.Util;

public class ArtistBrowse extends SpotifyServlet {

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		String id = getParam("id");
		if (Util.isEmpty(id)) return;
		
		Artist artist = Session.getInstance().browseArtist(id);
		artist.waitFor(100);
		printLn(artist.toString());
	}

}
