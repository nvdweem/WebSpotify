package com.vdweem.webspotify.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spotify.Session;

import com.vdweem.webspotify.Util;

public class TopList extends SpotifyServlet {
	private static final long serialVersionUID = 8234547657723010308L;
	
	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		int type = Util.parseInt(getParam("type"), 2);
		
		spotify.Search search = Session.getInstance().topList(type);
		search.waitFor(50);
		
		printLn(search.toString());
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
