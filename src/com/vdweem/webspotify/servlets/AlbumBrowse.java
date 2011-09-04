package com.vdweem.webspotify.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spotify.Album;
import spotify.Session;

import com.vdweem.webspotify.Util;

/**
 * Browse for albums.
 * @author Niels
 */
public class AlbumBrowse extends SpotifyServlet {
	private static final long serialVersionUID = -5892036524310521996L;

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		String id = getParam("id");
		if (Util.isEmpty(id)) return;
		
		Album album = Session.getInstance().browseAlbum(id);
		album.waitFor(100);
		printLn(album.toString());
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}
	
}
