package spotify;

import org.json.JSONObject;

public class Track extends Media implements Comparable<Track> {

	protected int duration;
	protected Album album;
	protected Artist artist;
	protected int popularity;
	protected int disc;
	
	public Track(String id) {
		super(id);
	}
	
	@Override
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("duration", duration);
		json.put("popularity", popularity);
		json.put("disc", disc);
		json.put("album", album == null ? null : album.toJSON());
		json.put("artist", artist == null ? null : artist.toJSON());
		
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

	@Override
	public int compareTo(Track o) {
		return Integer.valueOf(o.getPopularity()).compareTo(getPopularity());
	}
	
}