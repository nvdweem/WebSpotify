package com.vdweem.webspotify.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spotify.Session;

import com.vdweem.webspotify.Util;

/**
 * Show an image. If no image is found then show the corresponding placeholder.
 * @author Niels
 */
public class Image extends SpotifyServlet {
	private static final long serialVersionUID = -2475432570212048165L;

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse response)
			throws ServletException, IOException {
		String id = Util.nonNullString(getParam("id"));
		
		spotify.Image image = null;
		if (id.startsWith("spotify:artist"))
			image = Session.getInstance().readArtistImage(id);
		else if (id.startsWith("spotify:album"))
			image = Session.getInstance().readAlbumImage(id);
		
		if (image == null)
			return;
		
		image.waitFor(50);
		byte[] bytes = image.getBytes();
		response.getOutputStream().write(bytes, 0, bytes.length);
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.image;
	}
	
}
