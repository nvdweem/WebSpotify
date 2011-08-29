package com.vdweem.webspotify;

public class Util {
	public static boolean isEmpty(Object o) {
		if (o == null) return true;
		if ("".equals(o.toString())) return true;
		return false;
	}
	
	public static <T> T getDefault(T object, T def) {
		if (isEmpty(object)) return def;
		return object;
	}
	
	public static String nonNullString(Object o) {
		if (o == null) return "";
		return o.toString();
	}
	
	public static int parseInt(Object o, int def) {
		try {
			return getDefault(Integer.parseInt(Util.nonNullString(o)), def);
		} catch (Exception e) {
			return def;
		}
	}
}
