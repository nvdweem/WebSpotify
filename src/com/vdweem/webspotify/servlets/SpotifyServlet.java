package com.vdweem.webspotify.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SpotifyServlet extends HttpServlet {
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		request = arg0;
		response = arg1;
		super.service(arg0, arg1);
	}
	
	public String getParam(String key) {
		return request.getParameter(key);
	}
	public String[] getParams(String key) {
		return request.getParameterValues(key);
	}

	public void printLn(String line) 
	{
		try {
			response.getWriter().println(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void printError(String error) {
		printLn("{\"error\": \""+error+"\"}");
	}
}
