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
    private int page = 0;
    private boolean suggest;
    private String term;

    public Result execute() {
		if (Util.isEmpty(query) && Util.isEmpty(term)) {
			return JsonResult.onAjax("You are required to enter a query.", true);
		}
		if (Util.isEmpty(query)) query = term;

		jahspotify.Search search = new jahspotify.Search(Query.token(query));
		if (page == 0) {
			search.setNumAlbums(albums);
			search.setNumArtists(artists);
		} else {
			search.setNumAlbums(0);
			search.setNumArtists(0);
		}
		search.setNumTracks(tracks);
		search.setTrackOffset(tracks * page);
		search.setSuggest(isSuggest());
		SearchResult result = searchEngine.search(search);

		try {
			String resultStr = Gsonner.getGson(null).toJson(result);
			if (search.isSuggest()) // The jQuery.autocomplete widget requires an array result.
				resultStr = "[" + resultStr + "]";
			return JsonResult.onAjax(resultStr);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return JsonResult.onAjax("Error", true);
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

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public boolean isSuggest() {
		return suggest;
	}

	public void setSuggest(boolean suggest) {
		this.suggest = suggest;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}
}
