package com.vdweem.webspotify.action;

import jahspotify.services.Queue;
import jahspotify.services.QueueManager;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vdweem.webspotify.Gsonner;

public class QueueAction extends SpotifyServlet {
	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		Queue queue = QueueManager.getInstance().getCurrentQueue(100);
		printLn(Gsonner.getGson(Queue.class).toJson(queue));
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
