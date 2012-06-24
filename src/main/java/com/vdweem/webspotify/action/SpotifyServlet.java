package com.vdweem.webspotify.action;

import jahspotify.services.JahSpotifyService;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

/**
 * Super class for all servlets. Provides some helper functions.
 * @author Niels
 */
public abstract class SpotifyServlet {
	private HttpServletRequest request;
	private HttpServletResponse response;

	protected JahSpotifyService spotify = JahSpotifyService.getInstance();

	public enum ResultType {
		plain, json, image
	}

	public void execute() {
		try {
			service(ServletActionContext.getRequest(), ServletActionContext.getResponse());
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {

	}

	protected void service(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		request = arg0;
		response = arg1;

		if ("post".equalsIgnoreCase(request.getMethod()))
			doPost(request, response);
		else
			doGet(request, response);

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
	 * Prints a default success message.
	 */
	public void printSuccess() {
		printLn("{\"result\": \"success\"}");
	}

	/**
	 * Returns the Session object for the current session.
	 * @return
	 */
	protected Session getSession() {
		return Session.getSession(request.getSession().getId());
	}
}
