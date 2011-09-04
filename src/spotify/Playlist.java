package spotify;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

/**
 * Holds playlist data.
 * @author Niels
 */
public class Playlist extends Media {
	private static final long serialVersionUID = 2581723963648031759L;
	
	private int index;
	private String description;
	private List<Track> tracks;
	private byte[] image;
	private int totalDuration = 0;
	
	public Playlist(String id) {
		super(id);
		tracks = new ArrayList<Track>();
		setComplete();
	}
	
	@Override
	public JSONObject toJSON() {
		JSONObject result = super.toJSON();
		result.put("tracks", Util.listToArray(tracks));
		result.put("description", description);
		result.put("index", index);
		result.put("totalduration", totalDuration);
		return result;
	}

	public void clear() {
		totalDuration = 0;
		tracks.clear();
	}
	
	public void putTrack(Object track, int position) {
		if (!(track instanceof Track)) return;
		tracks.add(position, (Track) track);
	}
	
	public void removeTrack(int position) {
		tracks.remove(position);
	}
	
	public void addTrack(Object track) {
		if (!(track instanceof Track)) return;
		totalDuration += ((Track) track).getDuration();
		tracks.add((Track) track);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getTotalDuration() {
		return totalDuration;
	}

}
