package com.vdweem.webspotify.action;

import jahspotify.services.JahSpotifyService;

import com.opensymphony.xwork2.Result;
import com.vdweem.webspotify.result.JsonResult;

/**
 * @author Niels
 */
public class LogoutAction {

	public Result execute() {
		JahSpotifyService.getInstance().getJahSpotify().logout();
		JahSpotifyService.getInstance().getJahSpotify().forgetMe();
		return new JsonResult("success", true);
	}

}
