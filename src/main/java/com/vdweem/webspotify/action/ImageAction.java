package com.vdweem.webspotify.action;

import jahspotify.media.Image;
import jahspotify.media.Link;
import jahspotify.services.JahSpotifyService;
import jahspotify.services.MediaHelper;

import com.opensymphony.xwork2.Result;
import com.vdweem.webspotify.result.ImageResult;

/**
 * Show an image. If no image is found then show the corresponding placeholder.
 * @author Niels
 */
public class ImageAction {
	private String id;

	public Result execute() {
		Link link = Link.create(id);
		String img = null;

		Image image = JahSpotifyService.getInstance().getJahSpotify().readImage(link);
		if (image != null) {
			if (MediaHelper.waitFor(image, 2)) {
				return new ImageResult(image.getBytes());
			}
		}

		switch (link.getType()) {
			case ARTIST:
				img = "noArtist.png";
				break;
			default:
				img = "noAlbum.png";
		}
		return new ImageResult(img);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
