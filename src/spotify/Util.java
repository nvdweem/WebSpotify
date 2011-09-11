package spotify;

import java.util.List;

import org.json.JSONArray;

/**
 * Provides helper functions.
 * @author Niels
 */
public class Util {
	
	/**
	 * Transforms a list of Media types to a JSON array.
	 * @param list
	 * @return
	 */
	public static JSONArray listToArray(List<? extends Media> list, boolean includeChildren) {
		return listToArray(list, includeChildren, list == null ? 0 : list.size());
	}
	
	/**
	 * Transforms a list of Media types to a JSON array.
	 * @param list
	 * @return
	 */
	public static JSONArray listToArray(List<? extends Media> list, boolean includeChildren, int max) {
		if (com.vdweem.webspotify.Util.isEmpty(list)) return null;
		JSONArray result = new JSONArray();
		for (int i = 0; i < max && i < list.size(); i++)
			result.put(list.get(i).toJSON(includeChildren));
		return result;
	}
}
