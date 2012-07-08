package com.vdweem.webspotify.action;

import jahspotify.SearchResult;
import jahspotify.media.TopListType;
import jahspotify.services.MediaHelper;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vdweem.webspotify.Gsonner;
import com.vdweem.webspotify.Util;

public class TopListAction extends SpotifyServlet {
	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		int typeInt = Util.parseInt(getParam("type"), 2);
		if (typeInt < 0 || typeInt > TopListType.values().length) {
			printError("Type must be 0 <= type <= 2");
			return;
		}

		TopListType type = TopListType.values()[typeInt];
		SearchResult result = spotify.getJahSpotify().getTopList(type);
		MediaHelper.waitFor(result, 10);
		printLn(Gsonner.getGson(SearchResult.class).toJson(result));
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
