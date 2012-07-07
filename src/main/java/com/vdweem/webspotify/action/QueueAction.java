package com.vdweem.webspotify.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vdweem.webspotify.QueueHandler;

public class QueueAction extends SpotifyServlet {
	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		printLn(QueueHandler.toJson().toString());
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
