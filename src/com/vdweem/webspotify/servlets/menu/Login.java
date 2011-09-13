package com.vdweem.webspotify.servlets.menu;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vdweem.webspotify.Main;
import com.vdweem.webspotify.Util;
import com.vdweem.webspotify.servlets.SpotifyServlet;

public class Login extends SpotifyServlet {
	private static final long serialVersionUID = 3398279217996980615L;

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		
		String username = Util.nonNullString(getParam("username"));
		String password = Util.nonNullString(getParam("password"));
		
		if (username.equals(Main.getProperty("WebSpotify.username")) && password.equals(Main.getProperty("WebSpotify.password"))) {
			getSession().setAdmin(true);
			printSuccess();
		}
		else
			printLn("{\"result\": \"fail\"}");
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
