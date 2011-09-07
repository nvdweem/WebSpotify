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
		if (com.vdweem.webspotify.Util.isEmpty(list)) return null;
		JSONArray result = new JSONArray();
		for (Media media : list) {
			result.put(media.toJSON(includeChildren));
		}
		return result;
	}
}
