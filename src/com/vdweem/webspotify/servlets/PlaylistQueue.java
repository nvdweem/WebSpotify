package com.vdweem.webspotify.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spotify.Session;
import spotify.Track;

import com.vdweem.webspotify.Util;

public class PlaylistQueue extends SpotifyServlet {
	private static final long serialVersionUID = -1569502590977144891L;

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		
		int position = Util.parseInt(getParam("index"), -1);
		if (position == -1) {
			printError("Unknown playlist index.");
			return;
		}
		for (Track track : Session.getInstance().getPlaylistContainer().getPlaylist(position).getTracks()) {
			Session.getInstance().getPlayer().addToPlaylist(track);
		}
		
		printSuccess();
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
