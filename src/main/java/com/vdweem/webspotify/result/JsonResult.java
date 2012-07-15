package com.vdweem.webspotify.result;

import org.apache.struts2.ServletActionContext;

import com.google.gson.JsonObject;
import com.opensymphony.xwork2.ActionInvocation;

public class JsonResult extends WebSpotifyResult {
	public static final JsonResult SUCCESS = new JsonResult("{\"message\": \"success\"}");
	private final String json;

	public JsonResult(String json) {
		this(json, false);
	}
	public JsonResult(String json, boolean isMessage) {
		if (isMessage) {
			JsonObject jso = new JsonObject();
			jso.addProperty("message", json);
			json = jso.toString();
		}
		this.json = json;
	}

	@Override
	public void execute(ActionInvocation invocation) throws Exception {
		super.execute(invocation);
		ServletActionContext.getResponse().setContentType("application/json");
		ServletActionContext.getResponse().getWriter().print(json);
	}

}
