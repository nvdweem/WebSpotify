package com.vdweem.webspotify.action;

import jahspotify.services.MediaPlayer;

import com.opensymphony.xwork2.Result;
import com.vdweem.webspotify.result.JsonResult;

/**
 * Goes to the next song.
 * @author Niels
 */
public class NextAction {

	public Result execute() {
		MediaPlayer.getInstance().skip();
		return JsonResult.SUCCESS;
	}

}
