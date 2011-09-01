package spotify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

/**
 * Holds artist data.
 * @author Niels
 */
public class Artist extends Media {
	private static final long serialVersionUID = -6068013696650606562L;
	
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
	
	@Override
	public boolean isComplete() {
		return super.isComplete() && allAlbumsComplete();
	}
	
	private boolean allAlbumsComplete() {
		for (Album a : albums) if (!a.isComplete()) return false;
		return true;
	}
	
	public JSONObject toJSON() {
		JSONObject object = super.toJSON();
		
		if (!com.vdweem.webspotify.Util.isEmpty(bio)) object.put("bio", bio);
		if (!com.vdweem.webspotify.Util.isEmpty(relatedArtists))
			object.put("relatedArtists", Util.listToArray(relatedArtists));

		if (!com.vdweem.webspotify.Util.isEmpty(topTracks)) {
			keepOnlyTop5Tracks();
			object.put("topTracks", Util.listToArray(topTracks));
		}
		if (!com.vdweem.webspotify.Util.isEmpty(albums)) {
			Collections.sort(albums, Album.typeYearComparator);
			object.put("albums", Util.listToArray(albums));
		}
		
		return object;
	}
	
	/**
	 * Keeps only the best 5 tracks. Removes duplicates.
	 */
	private void keepOnlyTop5Tracks() {
		// Remove all tracks that are not from this artist.
		for (int i = topTracks.size() - 1; i >= 0; i--)
			if (!this.equals(topTracks.get(i).artist))
				topTracks.remove(i);
		
		// Sort by popularity.
		Collections.sort(topTracks, Track.top5Comparator);
		
		// Keep the first 5 unique results.
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
