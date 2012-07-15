package com.vdweem.webspotify.action;

import jahspotify.services.MediaPlayer;

import com.opensymphony.xwork2.Result;
import com.vdweem.webspotify.result.JsonResult;

public class VolumeAction {
	private int volume;

	public Result execute() {
		volume = Math.min(100, Math.max(0, volume)); // Between 0 and 100.
		MediaPlayer.getInstance().setVolume(volume);
		return JsonResult.SUCCESS;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

}
