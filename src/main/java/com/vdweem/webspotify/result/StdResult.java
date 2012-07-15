package com.vdweem.webspotify.result;

import org.apache.struts2.dispatcher.ServletDispatcherResult;

public class StdResult extends ServletDispatcherResult {

	public StdResult() {
		super();
	}

	public StdResult(String location) {
		super(location);
	}

}
