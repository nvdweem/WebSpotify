package com.vdweem.webspotify.action;

import jahspotify.SearchResult;
import jahspotify.media.TopListType;
import jahspotify.services.JahSpotifyService;
import jahspotify.services.MediaHelper;

import com.opensymphony.xwork2.Result;
import com.vdweem.webspotify.Gsonner;
import com.vdweem.webspotify.result.JsonResult;

/**
 * Shows a toplist.
 * @author Niels
 */
public class TopListAction {
	private int type = 2;

	public Result execute() {
		if (type < 0 || type > TopListType.values().length) {
			return new JsonResult("Type must be 0 <= type <= 2", true);
		}

		TopListType typeObj = TopListType.values()[type];
		SearchResult result = JahSpotifyService.getInstance().getJahSpotify().getTopList(typeObj, JahSpotifyService.getInstance().getJahSpotify().getUser().getCountry());
		MediaHelper.waitFor(result, 10);
		return JsonResult.onAjax(Gsonner.getGson(SearchResult.class).toJson(result));
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
