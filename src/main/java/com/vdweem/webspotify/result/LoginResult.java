package com.vdweem.webspotify.result;

import org.apache.struts2.dispatcher.ServletDispatcherResult;

import com.opensymphony.xwork2.ActionInvocation;

public class LoginResult extends ServletDispatcherResult {

	@Override
	public void doExecute(String finalLocation, ActionInvocation invocation)
			throws Exception {

		super.doExecute("login.html", invocation);
	}

}
