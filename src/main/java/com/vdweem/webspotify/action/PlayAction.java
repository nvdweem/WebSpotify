package com.vdweem.webspotify.action;

import jahspotify.media.Link;
import jahspotify.services.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

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
	private List<String> ids;
	private Boolean playlist;
	private String delete;

	public Result execute() {
		boolean delete = "true".equals(this.delete);
		boolean deleteAll = "all".equals(this.delete);

		if (deleteAll) {
			QueueHandler.clear();
		}

		List<String> ids = null;
		if (!Util.isEmpty(this.ids)) {
			ids = this.ids;
		}
		if (!Util.isEmpty(id)) {
			ids = new ArrayList<String>();
			ids.add(id);
		}
		if (Util.isEmpty(ids))
			return new JsonResult("No ids given");

		for (int i = 0; i < ids.size(); i++) {
			String id = ids.get(Boolean.TRUE.equals(playlist) ? ids.size()-1-i : i);
			Link link = Link.create(id);
			if (delete)
				QueueHandler.remove(link);
			else if (Boolean.TRUE.equals(playlist))
				QueueHandler.addToList(link);
			else
				QueueHandler.addToQueue(link);
		}

		if (delete)
			return JsonResult.SUCCESS;

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

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public Boolean getPlaylist() {
		return playlist;
	}

	public void setPlaylist(Boolean playlist) {
		this.playlist = playlist;
	}

}
