package com.vdweem.webspotify;

import jahspotify.SearchResult;
import jahspotify.media.Album;
import jahspotify.media.Artist;
import jahspotify.media.Image;
import jahspotify.media.Link;
import jahspotify.media.Media;
import jahspotify.media.Playlist;
import jahspotify.media.PlaylistContainer;
import jahspotify.media.Track;
import jahspotify.services.JahSpotifyService;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Class which can constuct Gson instances to jsonify the media types.
 * @author Niels
 */
public class Gsonner {
	/**
	 * Default Gson.
	 * @param src
	 * @return
	 */
	public static Gson getGson(Class<?> src) {
		return getGson(src, null);
	}

	/**
	 * Allows replacing certain links to already known instances.
	 * @param src
	 * @param replacements
	 * @return
	 */
	public static Gson getGson(Class<?> src, final Map<Link, ? extends Media> replacements) {
		final boolean artist = src == Artist.class;
		final boolean album = src == Album.class;
		final JahSpotifyService spotify = JahSpotifyService.getInstance();

		final GsonBuilder gson = new GsonBuilder();
		gson.registerTypeAdapter(SearchResult.class, new JsonSerializer<SearchResult>() {

			@Override
			public JsonElement serialize(SearchResult sr, Type arg1, JsonSerializationContext jsc) {
				JsonObject result = new JsonObject();
				result.addProperty("didyoumean", sr.getDidYouMean());
				result.add("artists", jsc.serialize(sr.getArtistsFound()));
				result.add("albums", jsc.serialize(sr.getAlbumsFound()));
				result.add("tracks", jsc.serialize(sr.getTracksFound()));
				result.add("playlists", jsc.serialize(sr.getPlaylistsFound()));

				return result;
			}
		});
		gson.registerTypeAdapter(ArrayList.class, new JsonSerializer<List<?>>() {

			@Override
			public JsonElement serialize(List<?> list, Type type, JsonSerializationContext jsc) {

				JsonArray result = new JsonArray();
				for (Object o : list) {
					result.add(jsc.serialize(o));
				}
				return result;
			}
		});

		gson.registerTypeAdapter(Link.class, new JsonSerializer<Link>() {
			@Override
			public JsonElement serialize(Link link, Type arg1, JsonSerializationContext jsc) {
				if (replacements != null && replacements.containsKey(link))
					return jsc.serialize(replacements.get(link));
				switch (link.getType()) {
					case ARTIST: return jsc.serialize(spotify.getJahSpotify().readArtist(link));
					case ALBUM: return jsc.serialize(spotify.getJahSpotify().readAlbum(link));
					case TRACK: return jsc.serialize(spotify.getJahSpotify().readTrack(link));
					case IMAGE: return jsc.serialize(spotify.getJahSpotify().readImage(link));
					case PLAYLIST: return jsc.serialize(spotify.getJahSpotify().readPlaylist(link, 0, 0));
					default: return null;
				}
			}
		});
		gson.registerTypeAdapter(Artist.class, new JsonSerializer<Artist>() {

			@Override
			public JsonElement serialize(Artist a, Type arg1, JsonSerializationContext jsc) {
				JsonObject result = new JsonObject();

				result.addProperty("otype", "artist");
				result.addProperty("id", a.getId().getId());
				result.addProperty("name", a.getName());
				result.addProperty("bio", a.getBios());
				if (!artist)
					return result;

				result.add("topTracks", getGson(null, replacements).toJsonTree(a.getTopHitTracks()));
				result.add("albums", getGson(Album.class, replacements).toJsonTree(a.getAlbums()));
				result.add("relatedArtists", getGson(null, replacements).toJsonTree(a.getSimilarArtists()));

				JsonArray arr = new JsonArray();
				for (Link image : a.getPortraits()) {
					arr.add(new JsonPrimitive(image.getId()));
				}
				result.add("pictures", arr);
				return result;
			}
		});
		gson.registerTypeAdapter(Album.class, new JsonSerializer<Album>() {

			@Override
			public JsonElement serialize(Album a, Type arg1, JsonSerializationContext jsc) {
				JsonObject result = new JsonObject();

				result.addProperty("otype", "album");
				result.addProperty("id", a.getId().getId());
				result.addProperty("name", a.getName());
				result.addProperty("year", a.getYear());
				result.addProperty("type", a.getType().ordinal());

				if (a.getArtist() != null)
					result.add("artist", jsc.serialize(spotify.getJahSpotify().readArtist(a.getArtist())));

				if (album)
					result.add("tracks", getGson(null, replacements).toJsonTree(a.getTracks()));
				return result;
			}
		});
		gson.registerTypeAdapter(Track.class, new JsonSerializer<Track>() {

			@Override
			public JsonElement serialize(Track a, Type arg1, JsonSerializationContext jsc) {
				JsonObject result = new JsonObject();

				result.addProperty("otype", "track");
				result.addProperty("id", a.getId().getId());
				result.add("album", jsc.serialize(a.getAlbum()));
				result.addProperty("index", a.getTrackNumber());
				result.addProperty("name", a.getTitle());
				if (!a.getArtists().isEmpty())
					result.add("artist", jsc.serialize(a.getArtists().get(0)));
				else
					result.add("artist", null);
				result.addProperty("duration", a.getLength());
				result.addProperty("popularity", a.getPopularity());

				return result;
			}
		});
		gson.registerTypeAdapter(PlaylistContainer.class, new JsonSerializer<PlaylistContainer>() {

			@Override
			public JsonElement serialize(PlaylistContainer le, Type arg1, JsonSerializationContext jsc) {
				JsonObject result = new JsonObject();
				JsonArray arr = new JsonArray();

				int i = 0;
				for (Playlist pl : PlaylistContainer.getPlaylists()) {
					JsonObject playlist = new JsonObject();
					playlist.addProperty("id", i++);
					playlist.addProperty("name", pl.getName());
					arr.add(playlist);
				}

				result.add("playlists", arr);
				return result;
			}
		});
		gson.registerTypeAdapter(Playlist.class, new JsonSerializer<Playlist>() {

			@Override
			public JsonElement serialize(Playlist p, Type arg1, JsonSerializationContext jsc) {
				JsonObject result = new JsonObject();

				result.add("tracks", jsc.serialize(p.getTracks()));
				result.addProperty("name", p.getName());
				result.addProperty("index", ServletActionContext.getRequest().getParameter("index"));
				result.addProperty("id", p.getId() == null ? null : p.getId().toString());
				result.addProperty("author", p.getAuthor());
				result.addProperty("otype", "playlist");

				return result;
			}
		});


		return gson.create();
	}
}
