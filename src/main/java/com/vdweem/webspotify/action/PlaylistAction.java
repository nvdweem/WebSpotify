package com.vdweem.webspotify.action;

import jahspotify.media.Playlist;
import jahspotify.media.PlaylistContainer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vdweem.webspotify.Gsonner;

/**
 * Serves playlist data.
 * @author Niels
 */
public class PlaylistAction extends SpotifyServlet {
	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse response)
			throws ServletException, IOException {
		int playlist = Integer.parseInt(getParam("index") == null ? "-1" : getParam("index"));
		PlaylistContainer container = spotify.getJahSpotify().getPlaylistContainer();

		if (playlist != -1) {
			Playlist pl = container.getPlaylist(playlist);
			printLn(Gsonner.getGson(Playlist.class).toJson(pl));
			return;
		}

		printLn(Gsonner.getGson(PlaylistContainer.class).toJson(container));
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
