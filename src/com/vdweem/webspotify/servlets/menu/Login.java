package com.vdweem.webspotify.servlets.menu;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vdweem.webspotify.servlets.SpotifyServlet;

public class Login extends SpotifyServlet {
	private static final long serialVersionUID = 3398279217996980615L;

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		
		String username = getParam("username");
		String password = getParam("password");
		
		if (true) {
			getSession().setAdmin(true);
			printLn("{\"result\": \"success\"}");
		}
		else
			printLn("{\"result\": \"fail\"}");
	}

	@Override
	protected ResultType getResultType() {
		return ResultType.json;
	}

}
