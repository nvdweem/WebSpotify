package com.vdweem.webspotify.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;

import com.opensymphony.xwork2.Result;
import com.vdweem.webspotify.result.StdResult;

@Actions({
	@Action("Index"), @Action("/"), @Action("index.html")
})
public class IndexAction {

	public Result execute() {
		return new StdResult("_index.jsp");
	}

}
