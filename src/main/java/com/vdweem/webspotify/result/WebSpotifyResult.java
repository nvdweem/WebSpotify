package com.vdweem.webspotify.result;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;

public abstract class WebSpotifyResult implements Result {

	@Override
	public void execute(ActionInvocation invocation) throws Exception {
		ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
		ServletActionContext.getResponse().addHeader("Cache-Control", "no-cache");
		ServletActionContext.getResponse().addHeader("Pragma", "no-cache");
		ServletActionContext.getResponse().addHeader("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");
	}

}
