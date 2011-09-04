package com.vdweem.webspotify.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spotify.Session;

import com.vdweem.webspotify.Util;

/**
 * Perform a search.
 * @author Niels
 */
public class Search extends SpotifyServlet {
	private static final long serialVersionUID = 4386468939868538644L;

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		String query = getParam("query");
		int trackCount = Util.parseInt(getParam("tracks"), 100);
		int artistCount = Util.parseInt(getParam("artists"), 15);
		int albumCount = Util.parseInt(getParam("albums"), 15);
		
		if (Util.isEmpty(query)) {
			printError("Query is required");
			return;
		}
		if (!Session.getInstance().isLoggedIn()) {
			printError("There is no connection to spotify.");
			return;
		}

		Session session = Session.getInstance();
		spotify.Search search = session.search(query, trackCount, albumCount, artistCount);
		if (!search.waitFor(100)) {
			printError("Search did not complete in time");
			return;
		}
		
		printLn(search.toString());
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
