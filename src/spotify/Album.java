package spotify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONObject;

/**
 * Holds Album data.
 * @author Niels
 */
public class Album extends Media {
	private static final long serialVersionUID = 6142589606176813944L;
	
	private int type;
	private int year;
	private String review;
	private Artist artist;
	private List<Track> tracks;
	private boolean notAvailable;
	
	public static final int SP_ALBUMTYPE_ALBUM = 0;
	public static final int SP_ALBUMTYPE_SINGLE = 1;
	public static final int SP_ALBUMTYPE_COMPILATION = 2;
	public static final int SP_ALBUMTYPE_UNKNOWN = 3;
	public static final int SP_ALBUMTYPE_APPEARSON = 4;
	
	public static final Comparator<Album> typeYearComparator = new Comparator<Album>(){
		public int compare(Album o1, Album o2) {
			// Album, Single, Compilation, Unknown
			int typeCompare = Integer.valueOf(o1.getType()).compareTo(o2.getType());
			if (typeCompare != 0) return typeCompare;
			
			// Year desc
			return Integer.valueOf(o2.getYear()).compareTo(o1.getYear());
		}
	};
	
	public Album(String id) {
		super(id);
		tracks = new ArrayList<Track>();
	}
	
	public void unComplete() {
		if (tracks == null || tracks.size() == 0)
			setComplete(false);
	}
	
	public void addTrack(Object track) {
		if (!(track instanceof Track)) return;
		Track t = (Track) track;
		tracks.add(t);
		if (!getArtist().equals(t.getArtist()))
			type = SP_ALBUMTYPE_APPEARSON;
	}
	
	@Override
	public JSONObject toJSON(boolean includeChildren) {
		JSONObject json = super.toJSON(includeChildren);
		json.put("type", type);
		json.put("year", year);
		json.put("review", review);
		json.put("artist", artist == null ? null : artist.toJSON(false));
		
		if (!includeChildren) return json;
		
		if (!com.vdweem.webspotify.Util.isEmpty(tracks)) {
			Collections.sort(tracks, Track.sequenceComparator);
			json.put("tracks", Util.listToArray(tracks, true));
		}
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

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public boolean isNotAvailable() {
		return notAvailable;
	}

	public void setNotAvailable(boolean notAvailable) {
		this.notAvailable = notAvailable;
	}

}
