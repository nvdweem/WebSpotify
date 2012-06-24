package com.vdweem.webspotify.action;

import jahspotify.media.LibraryEntry;
import jahspotify.media.Link;
import jahspotify.media.Playlist;

import java.io.IOException;
import java.util.ArrayList;

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
		LibraryEntry entry = spotify.getJahSpotify().readFolder(Link.create("jahspotify:folder:ROOT"), 0);

		int playlist = Integer.parseInt(getParam("index") == null ? "-1" : getParam("index"));

		if (playlist != -1) {
			Playlist pl = spotify.getJahSpotify().readPlaylist(Link.create(new ArrayList<LibraryEntry>(entry.getSubEntries()).get(playlist).getId()), 0, 0);
			printLn(Gsonner.getGson(Playlist.class).toJson(pl));
			return;
		}

		printLn(Gsonner.getGson(LibraryEntry.class).toJson(entry));
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
