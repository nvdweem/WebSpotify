package com.vdweem.webspotify.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vdweem.webspotify.Util;

public class TopListAction extends SpotifyServlet {
	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		int type = Util.parseInt(getParam("type"), 2);

//		spotify.Search search = Session.getInstance().topList(type);
//		search.waitFor(50);
//		printLn(search.toString());
		printSuccess();
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
