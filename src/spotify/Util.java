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
	public static JSONArray listToArray(List<? extends Media> list) {
		JSONArray result = new JSONArray();
		for (Media media : list) {
			result.put(media.toJSON());
		}
		return result;
	}
}
