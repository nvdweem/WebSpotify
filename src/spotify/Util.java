package spotify;

import java.util.List;

import org.json.JSONArray;

public class Util {
	public static JSONArray listToArray(List<? extends Media> list) {
		JSONArray result = new JSONArray();
		for (Media media : list) {
			result.put(media.toJSON());
		}
		return result;
	}
}
