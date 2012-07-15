package com.vdweem.webspotify.result;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;

public class ImageResult extends WebSpotifyResult {
	private final byte[] image;
	private final String redirect;

	public ImageResult(byte[] image) {
		this.image = image;
		this.redirect = null;
	}
	public ImageResult(String redirect) {
		this.image = null;
		this.redirect = redirect;
	}

	@Override
	public void execute(ActionInvocation invocation) throws Exception {
		super.execute(invocation);
		ServletActionContext.getResponse().setContentType("image/jpg");

		if (redirect != null) {
			ServletActionContext.getResponse().sendRedirect("images/" + redirect);
			return;
		}
		ServletActionContext.getResponse().getOutputStream().write(image, 0, image.length);
	}
}
