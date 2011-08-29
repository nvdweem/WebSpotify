package spotify;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class Search extends Completable {
	private List<Track> tracks;
	private List<Album> albums;
	private List<Artist> artists;
	
	public Search() {
		this.tracks = new ArrayList<Track>();
		this.albums = new ArrayList<Album>();
		this.artists = new ArrayList<Artist>();
	}
	
	public void addTrack(Object track) {
		if ((track instanceof Track))
			this.tracks.add((Track)track);
	}
	
	public void addAlbum(Object album) {
		if ((album instanceof Album))
			this.albums.add((Album) album);
	}
	
	public void addArtist(Object artist) {
		if (artist instanceof Artist)
			this.artists.add((Artist) artist);
	}
	
	public String toString() {
		return toJSON().toString();
	}
	
	public JSONObject toJSON() {
		JSONObject target = new JSONObject();
		target.put("tracks", Util.listToArray(tracks));
		target.put("albums", Util.listToArray(albums));
		target.put("artists", Util.listToArray(artists));
		return target;
	}

	public List<Track> getTracks() {
		return tracks;
	}

	public List<Album> getAlbums() {
		return albums;
	}

	public List<Artist> getArtists() {
		return artists;
	}
	
}