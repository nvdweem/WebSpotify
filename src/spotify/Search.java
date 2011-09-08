package spotify;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

/**
 * Holds data for Search results.
 * @author Niels
 */
public class Search extends Completable {
	private List<Track> tracks;
	private List<Album> albums;
	private List<Artist> artists;
	
	public Search() {
		this.tracks = new ArrayList<Track>();
		this.albums = new ArrayList<Album>();
		this.artists = new ArrayList<Artist>();
	}
	
	public void unComplete() {}
	
	public void addTrack(Object track) {
		if ((track instanceof Track))
			this.tracks.add((Track)track);
	}
	
	public void addAlbum(Object album) {
		if ((album instanceof Album)) {
			Album _album = (Album) album;
			if (_album.isNotAvailable()) return;
			this.albums.add((Album) album);
		}
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
		target.put("tracks", Util.listToArray(tracks, true));
		target.put("albums", Util.listToArray(albums, false));
		target.put("artists", Util.listToArray(artists, false));
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