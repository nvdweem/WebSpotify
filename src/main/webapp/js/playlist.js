/**
 * Handles playlist events.
 */
var Playlist = function() {
	$(document).ready(init);
	
	function init() {
		$("li.playlist").live('click', function() {
			Ajax.call("Playlist", {"index": $(this).data("index")}, decoratePlaylist);
		});
	}
	
	function decoratePlaylists(data) {
		var ps = data.playlists;
		var result = $('<div class="playlists"></div>');
		var list = $('<ol></ol>');
		result.append(list);
		for (var i = 0; i < ps.length; i++)
			list.append($('<li class="playlist"></li>').text(ps[i].name).data("index", i));
		
		$("#lists").find('.playlists').detach().end().append(result);
	}
	
	function decoratePlaylist(data) {
		var result = $('<div class="playlist"></div>').data("index", data.index);
		
		var title = $('<div class="title"></div>');
		var duration = $('<span class="duration"></span>');
		title.append(duration);
		title.append($('<img src="images/playlistIcon.png" />').click(queuePlaylist));
		title.append(data.name);
		
		var table = $(
			'<table class="playlist">' +
			'  <tr class="searchHead">' +
			'    <th class="track">Track</th>' +
			'    <th class="artist">Artist</th>' +
			'    <th class="time">Time</th>' +
			'    <th class="popularity">Popularity</th>' +
			'    <th class="album">Album</th>' +
			'  </tr>' +
			'</table>');
		
		if (!data.tracks) {
			table.append($('<tr><td colspan="5">Playlist not (yet) loaded</td></tr>'));
			return table;
		}
		var totalduration = 0;
		for (var i = 0; i < data.tracks.length; i++) {
			var track = data.tracks[i];
			totalduration += track.duration;
			table.append(Media.decorateTrack(track).addClass(i % 2 == 0 ? "even" : "odd"));
		}
		
		decorateDuration(duration, data.tracks ? data.tracks.length : -1, totalduration);
		
		return result.append(title).append(table);
	}
	
	function queuePlaylist() {
		$.get("PlaylistQueue", {index: $('div.playlist').data("index")});
	}
	
	function decorateDuration(duration, tracks, total) {
		var seconds = total / 1000;
		var minutes = seconds / 60;
		var hours = minutes / 60;
		var days = hours / 24;
		
		var result;
		var resultNr;
		if (days >= 1) 			{resultNr = Math.floor(days);	 result = resultNr + ' day'; }
		else if (hours >= 1) 	{resultNr = Math.floor(hours);	 result = resultNr + ' hour'; }
		else if (minutes >= 1)  {resultNr = Math.floor(minutes); result = resultNr + ' minute'; }
		else 					{resultNr = Math.floor(seconds); result = resultNr + ' second'; }
		if (resultNr > 1) result += 's';
		
		return duration.text(tracks + ' tracks, ' + result);
	}
	
	var lastRevision = -1;
	function update(revision) {
		if (revision != lastRevision) {
			$.getJSON("Playlist", {}, decoratePlaylists);
			lastRevision = revision;
		}
	}
	
	return {
		"update": update,
	};
}();