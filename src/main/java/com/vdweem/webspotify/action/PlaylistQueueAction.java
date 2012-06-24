package com.vdweem.webspotify.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vdweem.webspotify.Util;

public class PlaylistQueueAction extends SpotifyServlet {
	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {

		int position = Util.parseInt(getParam("index"), -1);
		if (position == -1) {
			printError("Unknown playlist index.");
			return;
		}

//		Player player = Session.getInstance().getPlayer();
//		player.setEditing(true);
//		for (Track track : Session.getInstance().getPlaylistContainer().getPlaylist(position).getTracks())
//			player.addToPlaylist(track);
//		player.setEditing(false);

		printSuccess();
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
