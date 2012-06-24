package com.vdweem.webspotify.action;

import jahspotify.Query;
import jahspotify.SearchResult;
import jahspotify.services.SearchEngine;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vdweem.webspotify.Gsonner;
import com.vdweem.webspotify.Util;

/**
 * Perform a search.
 * @author Niels
 */
public class SearchAction extends SpotifyServlet {
    private SearchEngine searchEngine = SearchEngine.getInstance();

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		String query = getParam("query");
		int trackCount = Util.parseInt(getParam("tracks"), 50);
		int artistCount = Util.parseInt(getParam("artists"), 15);
		int albumCount = Util.parseInt(getParam("albums"), 15);

		if (Util.isEmpty(query)) {
			printError("Query is required");
			return;
		}

		jahspotify.Search search = new jahspotify.Search(Query.token(query));
		search.setNumAlbums(albumCount);
		search.setNumArtists(artistCount);
		search.setNumTracks(trackCount);
		SearchResult result = searchEngine.search(search);

		try {
			printLn(Gsonner.getGson(null).toJson(result));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
