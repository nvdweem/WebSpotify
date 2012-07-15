package com.vdweem.webspotify.action;

import jahspotify.services.MediaPlayer;

import com.opensymphony.xwork2.Result;
import com.vdweem.webspotify.result.JsonResult;

/**
 * Goes to the previous song.
 * @author Niels
 */
public class PrevAction {

	public Result execute() {
		MediaPlayer.getInstance().prev();
		return JsonResult.SUCCESS;
	}

}
