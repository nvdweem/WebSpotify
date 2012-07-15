package com.vdweem.webspotify.interceptor;

import jahspotify.services.JahSpotifyService;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.vdweem.webspotify.Util;
import com.vdweem.webspotify.result.LoginResult;

/**
 * Makes sure that a user is logged into Spotify before serving the pages.
 * If no user is logged it, it serves a login page.
 * @author Niels
 */
public class LoginInterceptor implements Interceptor {

	@Override
	public void destroy() {
	}

	@Override
	public void init() {
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
		ServletActionContext.getResponse().addHeader("Cache-Control", "no-cache");
		ServletActionContext.getResponse().addHeader("Pragma", "no-cache");
		ServletActionContext.getResponse().addHeader("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");

		attemptLogin();

		int i = 0;
		while (JahSpotifyService.getInstance().getJahSpotify().isLoggingIn() && i++ < 6) {
			Thread.sleep(250);
		}

		if (JahSpotifyService.getInstance().getJahSpotify().isLoggedIn())
			return invocation.invoke();

		LoginResult login = new LoginResult();
		login.execute(invocation);
		return null;
	}

	private void attemptLogin() {
		String username = ServletActionContext.getRequest().getParameter("username");
		String password = ServletActionContext.getRequest().getParameter("password");
		boolean save = "true".equals(ServletActionContext.getRequest().getParameter("savePassword"));

		if (Util.isEmpty(username) || Util.isEmpty(password)) {
			return;
		}
		JahSpotifyService.getInstance().getJahSpotify().login(username, password, save);
	}

}
