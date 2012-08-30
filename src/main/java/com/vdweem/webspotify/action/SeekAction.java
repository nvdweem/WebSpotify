package com.vdweem.webspotify.action;

import jahspotify.services.MediaPlayer;

import com.opensymphony.xwork2.Result;
import com.vdweem.webspotify.result.JsonResult;

/**
 * Seeks a position for a song.
 * @author Niels
 */
public class SeekAction {
	private int position = 0;

	public Result execute() {
		MediaPlayer.getInstance().seek(position * 1000);
		return JsonResult.SUCCESS;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
