package com.vdweem.webspotify.action;

import jahspotify.services.MediaPlayer;

import com.opensymphony.xwork2.Result;
import com.vdweem.webspotify.result.JsonResult;

/**
 * Toggles the play state for the player.
 * @author Niels
 */
public class PlayPauseAction {

	public Result execute() {
		MediaPlayer.getInstance().pause();
		return JsonResult.SUCCESS;
	}

}
