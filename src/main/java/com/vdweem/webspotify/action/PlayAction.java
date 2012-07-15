package com.vdweem.webspotify.action;

import jahspotify.media.Link;
import jahspotify.services.MediaPlayer;

import com.opensymphony.xwork2.Result;
import com.vdweem.webspotify.QueueHandler;
import com.vdweem.webspotify.Util;
import com.vdweem.webspotify.result.JsonResult;

/**
 * Play a track.
 * @author Niels
 */
public class PlayAction {

	private String id;
	private String delete;

	public Result execute() {
		boolean delete = "true".equals(this.delete);
		boolean deleteAll = "all".equals(this.delete);

		if (deleteAll) {
			QueueHandler.clear();
		}
		if (Util.isEmpty(id)) {
			return new JsonResult("No id specified", true);
		}

		Link link = Link.create(id);
		if (delete) {
			QueueHandler.remove(link);
			return JsonResult.SUCCESS;
		}
		QueueHandler.addToQueue(link);
		MediaPlayer player = MediaPlayer.getInstance();
		if (!player.isPlaying())
			player.play();
		return JsonResult.SUCCESS;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getDelete() {
		return delete;
	}
	public void setDelete(String delete) {
		this.delete = delete;
	}

}
