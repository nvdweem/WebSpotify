package com.vdweem.webspotify.action;

import java.util.HashMap;
import java.util.Map;

public class Session {
	private static Map<String, Session> sessions = new HashMap<String, Session>();
	
	private boolean admin;
	
	public static Session getSession(String id) {
		if (!sessions.containsKey(id)) 
			sessions.put(id, new Session());
		return sessions.get(id);
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
}
