package spotify;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Contains all playlists.
 * @author Niels
 *
 */
public class PlaylistContainer extends Completable {
	private int revision = 0; // Keeps track of changes.
	public List<Playlist> playlists;
	
	public PlaylistContainer() {
		playlists = new ArrayList<Playlist>();
	}
	
	public void unComplete() {
		setComplete(false);
	}
	
	public void clear() {
		revision++;
		playlists.clear();
	}
	
	public void putPlaylist(Playlist playlist, int position) {
		playlists.add(position, playlist);
	}
	
	public void remove(int position) {
		playlists.remove(position);
	}
	
	public void move(int from, int to) {
		Playlist src = playlists.remove(from);
		if (from < to) to--;
		playlists.add(to, src);
	}
	
	public void reset() {
		playlists.clear();
		unComplete();
	}
	
	public int getPlaylistCount() {
		return playlists.size();
	}
	
	public Playlist getPlaylist(int position) {
		return playlists.get(position);
	}
	
	public JSONObject toJSON() {
		JSONObject result = new JSONObject();
		JSONArray arr = new JSONArray();
		for (int i = 0; i < playlists.size(); i++)
			arr.put(playlists.get(i).getName());
		
		result.put("playlists", arr);
		return result;
	}
	
	public String toString() {
		return toJSON().toString();
	}
	
	public void addPlaylist(Playlist playlist) {
		playlists.add(playlist);
	}
	
	public void setComplete() {
		for (int i = 0; i < playlists.size(); i++)
			playlists.get(i).setIndex(i);
		revision++;
		super.setComplete();
	}

	@Override
	public boolean isComplete() {
		for (Playlist playlist : playlists)
			if (!playlist.isComplete()) return false;
		return super.isComplete();
	}

	public int getRevision() {
		return revision;
	}
	
}
