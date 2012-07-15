package com.vdweem.webspotify.action;

import jahspotify.Query;
import jahspotify.SearchResult;
import jahspotify.services.SearchEngine;

import com.opensymphony.xwork2.Result;
import com.vdweem.webspotify.Gsonner;
import com.vdweem.webspotify.Util;
import com.vdweem.webspotify.result.JsonResult;

/**
 * Perform a search.
 * @author Niels
 */
public class SearchAction {
    private SearchEngine searchEngine = SearchEngine.getInstance();

    private String query;
    private int tracks = 50;
    private int artists = 15;
    private int albums = 15;

    public Result execute() {
		if (Util.isEmpty(query)) {
			return new JsonResult("You are required to enter a query.", true);
		}

		jahspotify.Search search = new jahspotify.Search(Query.token(query));
		search.setNumTracks(tracks);
		search.setNumAlbums(albums);
		search.setNumArtists(artists);
		SearchResult result = searchEngine.search(search);

		try {
			return new JsonResult(Gsonner.getGson(null).toJson(result));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return new JsonResult("Error", true);
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public int getTracks() {
		return tracks;
	}

	public void setTracks(int tracks) {
		this.tracks = tracks;
	}

	public int getArtists() {
		return artists;
	}

	public void setArtists(int artists) {
		this.artists = artists;
	}

	public int getAlbums() {
		return albums;
	}

	public void setAlbums(int albums) {
		this.albums = albums;
	}
}
