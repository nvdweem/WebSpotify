package com.vdweem.webspotify.action;

import com.opensymphony.xwork2.Result;
import com.vdweem.webspotify.StateSaver;
import com.vdweem.webspotify.result.JsonResult;

/**
 * Saves the current state.
 * @author Niels
 */
public class SaveAction {

	public Result execute() {
		StateSaver.getInstance().saveNow();
		return JsonResult.SUCCESS;
	}

}
