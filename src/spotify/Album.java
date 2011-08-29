package spotify;

import org.json.JSONObject;

public class Album extends Media {

	private int type;
	private int year;
	private Artist artist;
	
	public Album(String id) {
		super(id);
	}
	
	@Override
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", type);
		json.put("year", year);
		json.put("artist", artist == null ? null : artist.toJSON());
		return json;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}
	public void setArtist(Object artist) {
		if (artist instanceof Artist)
			this.artist = (Artist) artist;
	}

	
}
