package com.vdweem.webspotify.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Serves playlist data.
 * @author Niels
 */
public class PlaylistAction extends SpotifyServlet {
	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse response)
			throws ServletException, IOException {
//		String playlist = getParam("index");
//		
//		PlaylistContainer container = Session.getInstance().getPlaylistContainer();
//		container.waitFor(50);
//		
//		// Show all playlists.
//		if (Util.isEmpty(playlist)) {
//			printLn(container.toString());
//			return;
//		}
//		
//		int index = Util.parseInt(playlist, 0);
//		if (index < 0 || index > container.getPlaylistCount()) {
//			printError("Specified index is incorrect.");
//			return;
//		}
//		
//		spotify.Playlist playlistObject = container.getPlaylist(index);
//		if (getParam("image") != null) {
////			byte[] bytes = playlistObject.getImage();
////			response.getOutputStream().write(bytes, 0, bytes.length);
//			return;
//		}
//		
//		printLn(playlistObject.toString());
		printSuccess();
	}
	
	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}
	
}
