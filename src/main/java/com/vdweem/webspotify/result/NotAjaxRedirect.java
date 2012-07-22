package com.vdweem.webspotify.result;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

public class NotAjaxRedirect extends StdResult {

	private String json;

	public NotAjaxRedirect(String json) {
		this.json = json;
	}

	@Override
	public void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {
		invocation.getInvocationContext().getContextMap().put("invocation", "Ajax.fromPage(" + ActionContext.getContext().getName() + ".decorate("+json+"))");
		super.doExecute("_index.jsp", invocation);
	}
}
