package spotify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.vdweem.webspotify.Util;

public class Artist extends Media {
	private String bio;
	private List<Artist> relatedArtists;
	private List<Track> topTracks;
	private List<Album> albums;
	
	public Artist(String id) {
		super(id);
		relatedArtists = new ArrayList<Artist>();
		topTracks = new ArrayList<Track>();
		albums = new ArrayList<Album>();
	}
	
	public JSONObject toJSON() {
		JSONObject object = super.toJSON();
		
		if (!Util.isEmpty(bio)) object.put("bio", bio);
		if (!Util.isEmpty(relatedArtists)) {
			JSONArray artists = new JSONArray();
			for (Artist a : relatedArtists) artists.put(a.toJSON());
			object.put("relatedArtists", artists);
		}
		if (!Util.isEmpty(topTracks)) {
			keepOnlyTop5Tracks();
			JSONArray tracks = new JSONArray();
			for (Track t : topTracks) tracks.put(t.toJSON());
			object.put("topTracks", tracks);
		}
		if (!Util.isEmpty(albums)) {
			JSONArray albums = new JSONArray();
			for (Album t : this.albums) albums.put(t.toJSON());
			object.put("albums", albums);
		}
		
		return object;
	}
	
	private void keepOnlyTop5Tracks() {
		for (int i = topTracks.size() - 1; i >= 0; i--)
			if (!this.equals(topTracks.get(i).artist))
				topTracks.remove(i);
		
		Collections.sort(topTracks);
		
		Set<String> tracks = new HashSet<String>();
		List<Track> topTracks = new ArrayList<Track>();
		for (Track t : this.topTracks) {
			if (!tracks.add(t.name)) continue;
			topTracks.add(t);
			if (topTracks.size() == 5) break;
		}
		this.topTracks = topTracks;
	}
	
	public void addRelatedArtist(Object artist) {
		if (!(artist instanceof Artist)) return;
		relatedArtists.add((Artist) artist);
	}
	
	public void addTopTrack(Object track) {
		if (!(track instanceof Track)) return;
		topTracks.add((Track) track);
	}
	
	public void addAlbum(Object album) {
		if (!(album instanceof Album)) return;
		albums.add((Album) album);
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	
}
