package com.vdweem.webspotify.interceptor;

import jahspotify.ConnectionListener;
import jahspotify.services.JahSpotifyService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.vdweem.webspotify.SpotifyInitializer;
import com.vdweem.webspotify.Util;
import com.vdweem.webspotify.result.LoginResult;

/**
 * Makes sure that a user is logged into Spotify before serving the pages.
 * If no user is logged it, it serves a login page.
 * @author Niels
 */
public class LoginInterceptor implements Interceptor {
	private boolean rememberMe = false;
	private boolean attemptedLoginThroughCredentials = false;

	@Override
	public void destroy() {
	}

	@Override
	public void init() {
		JahSpotifyService.getInstance().getJahSpotify().addConnectionListener(new ConnectionListener() {
			@Override
			public void loggedOut() {}
			@Override
			public void disconnected() {}
			@Override
			public void connected() {}
			@Override
			public void loggedIn() {}
			@Override
			public void blobUpdated(String blob) {
				if (rememberMe) {
					saveCredentials(blob);
				}
			}
		});
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
		ServletActionContext.getResponse().addHeader("Cache-Control", "no-cache");
		ServletActionContext.getResponse().addHeader("Pragma", "no-cache");
		ServletActionContext.getResponse().addHeader("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");

		loadCredentials();
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
		if (Util.isEmpty(username) || Util.isEmpty(password))
			return;

		rememberMe = "true".equals(ServletActionContext.getRequest().getParameter("savePassword"));

		if (Util.isEmpty(username) || Util.isEmpty(password)) {
			return;
		}
		JahSpotifyService.getInstance().getJahSpotify().login(username, password, null, rememberMe);
	}

	private void loadCredentials() {
		if (attemptedLoginThroughCredentials) return;
		attemptedLoginThroughCredentials = true;
		File cred = new File(SpotifyInitializer.getAppFolder(), "user.dat");
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(cred));
			String username = in.readLine();
			String blob = in.readLine();

			JahSpotifyService.getInstance().getJahSpotify().login(username, null, blob, false);
		} catch (Exception e) {
			// Unable to save.
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Save the encrypted credentials.
	 * @param blob
	 */
	private void saveCredentials(String blob) {
		File cred = new File(SpotifyInitializer.getAppFolder(), "user.dat");

		if (!isRememberMe()) {
			cred.delete();
			return;
		}

		PrintWriter out = null;
		try {
			String username = JahSpotifyService.getInstance().getJahSpotify().getUser().getUserName();
			out = new PrintWriter(cred);
			out.println(username);
			out.println(blob);
		} catch (Exception e) {
			// Unable to save.
		} finally {
			if (out != null)
				out.close();
		}

	}

	public boolean isRememberMe() {
		return rememberMe;
	}
}
