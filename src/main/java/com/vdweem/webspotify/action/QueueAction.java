package com.vdweem.webspotify.action;

import com.opensymphony.xwork2.Result;
import com.vdweem.webspotify.QueueHandler;
import com.vdweem.webspotify.result.JsonResult;

/**
 * Shows the current queue.
 * @author Niels
 */
public class QueueAction {

	public Result execute() {
		return JsonResult.onAjax(QueueHandler.toJson().toString());
	}

}
