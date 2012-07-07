package com.vdweem.webspotify.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vdweem.webspotify.StateSaver;

/**
 * Saves the current state.
 * @author Niels
 */
public class SaveAction extends SpotifyServlet {

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		StateSaver.getInstance().saveNow();
		printSuccess();
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}
}
