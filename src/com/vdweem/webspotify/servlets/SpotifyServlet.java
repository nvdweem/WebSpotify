package com.vdweem.webspotify.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Super class for all servlets. Provides some helper functions.
 * @author Niels
 */
public abstract class SpotifyServlet extends HttpServlet {
	private static final long serialVersionUID = -3271532191176837879L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public enum ResultType {
		plain, json, image
	}
	
	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		request = arg0;
		response = arg1;
		super.service(arg0, arg1);
		
		switch (getResultType()) {
			case image: response.setContentType("image/jpg"); break;
			case json: response.setContentType("application/json"); break;
			case plain: response.setContentType("text/plain"); break;
		}
	}
	
	/**
	 * Determines the result headers.
	 * @return
	 */
	protected abstract ResultType getResultType();
	
	/**
	 * Get a parameter from the request.
	 * @param key
	 * @return
	 */
	public String getParam(String key) {
		return request.getParameter(key);
	}
	/**
	 * Get parameters from the request.
	 * @param key
	 * @return
	 */
	public String[] getParams(String key) {
		return request.getParameterValues(key);
	}

	/**
	 * Print a line to the output.
	 * @param line
	 */
	public void printLn(String line) 
	{
		try {
			response.getWriter().println(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Prints an error. This should be the only thing printed to
	 * the result stream.
	 * @param error
	 */
	public void printError(String error) {
		printLn("{\"error\": \""+error+"\"}");
	}
	
	/**
	 * Returns the Session object for the current session.
	 * @return
	 */
	protected Session getSession() {
		return Session.getSession(request.getSession().getId());
	}
}
