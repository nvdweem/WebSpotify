package spotify;

import java.util.Comparator;

import org.json.JSONObject;

/**
 * Holds data for Tracks.
 * @author Niels
 */
public class Track extends Media { 
	private static final long serialVersionUID = -429087740793002622L;
	
	protected int duration;
	protected Album album;
	protected Artist artist;
	protected int popularity;
	protected int disc;
	protected int index;
	
	public static final Comparator<Track> top5Comparator = new Comparator<Track>(){
		@Override
		public int compare(Track o1, Track o2) {
			return Integer.valueOf(o2.getPopularity()).compareTo(o1.getPopularity());
		}
	};
	public static final Comparator<Track> sequenceComparator = new Comparator<Track>(){
		@Override
		public int compare(Track o1, Track o2) {
			int discCompare = Integer.valueOf(o1.getDisc()).compareTo(o2.getDisc());
			if (discCompare != 0) return discCompare;
			
			return Integer.valueOf(o1.getIndex()).compareTo(o2.getIndex());
		}
	};
	
	public Track(String id) {
		super(id);
	}
	
	public void unComplete() {}
	
	@Override
	public JSONObject toJSON(boolean includeChildren) {
		JSONObject json = super.toJSON(includeChildren);
		json.put("duration", duration);
		json.put("popularity", popularity);
		json.put("disc", disc);
		json.put("index", index);
		json.put("album", album == null ? null : album.toJSON(false));
		json.put("artist", artist == null ? null : artist.toJSON(false));
		
		return json;
	}

	public String toString() {
		return name;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}
	public void setAlbum(Object album) {
		if (album instanceof Album)
			this.album = (Album) album;
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

	public int getPopularity() {
		return popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}

	public int getDisc() {
		return disc;
	}

	public void setDisc(int disc) {
		this.disc = disc;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}