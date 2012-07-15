package com.vdweem.webspotify.action;

import com.opensymphony.xwork2.Result;
import com.vdweem.webspotify.QueueHandler;
import com.vdweem.webspotify.result.JsonResult;

/**
 * Shuffles the queue.
 * @author Niels
 */
public class ShuffleAction {
	public Result execute() {
		QueueHandler.setShuffle(!QueueHandler.isShuffle());
		return JsonResult.SUCCESS;
	}

}
