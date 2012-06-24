package com.vdweem.webspotify;

import java.util.Collection;

/**
 * Some helper functions.
 * @author Niels
 */
public class Util {
	
	/**
	 * Check if an object is empty. Works for null values, empty strings and empty collections.
	 * @param o
	 * @return
	 */
	public static boolean isEmpty(Object o) {
		if (o == null) return true;
		if (o instanceof Collection) return ((Collection<?>) o).size() == 0;
		if ("".equals(o.toString())) return true;
		return false;
	}
	
	/**
	 * Checks to see if object is empty, if so it returns the def parameter.
	 * @param object
	 * @param def
	 * @return
	 */
	public static <T> T getDefault(T object, T def) {
		if (isEmpty(object)) return def;
		return object;
	}
	
	/**
	 * 
	 * @param o
	 * @return
	 */
	public static String nonNullString(Object o) {
		if (o == null) return "";
		return o.toString();
	}
	
	/**
	 * Parse an integer and return the default on error.
	 * @param o
	 * @param def
	 * @return
	 */
	public static int parseInt(Object o, int def) {
		try {
			return getDefault(Integer.parseInt(Util.nonNullString(o)), def);
		} catch (Exception e) {
			return def;
		}
	}
}
