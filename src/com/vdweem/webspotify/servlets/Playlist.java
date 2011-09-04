package com.vdweem.webspotify.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spotify.PlaylistContainer;
import spotify.Session;

import com.vdweem.webspotify.Util;

/**
 * Serves playlist data.
 * @author Niels
 */
public class Playlist extends SpotifyServlet {
	private static final long serialVersionUID = 4148447949067864519L;

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse response)
			throws ServletException, IOException {
		String playlist = getParam("index");
		
		PlaylistContainer container = Session.getInstance().getPlaylistContainer();
		container.waitFor(50);
		
		// Show all playlists.
		if (Util.isEmpty(playlist)) {
			printLn(container.toString());
			return;
		}
		
		int index = Util.parseInt(playlist, 0);
		if (index < 0 || index > container.getPlaylistCount()) {
			printError("Specified index is incorrect.");
			return;
		}
		
		spotify.Playlist playlistObject = container.getPlaylist(index);
		if (getParam("image") != null) {
			byte[] bytes = playlistObject.getImage();
			response.getOutputStream().write(bytes, 0, bytes.length);
			return;
		}
		
		printLn(playlistObject.toString());
	}
	
	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}
	
}
