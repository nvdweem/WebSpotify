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
		var image = $('<div class="playlistImage"/>');
		title.append(duration);
		title.append($('<img src="images/playlistIcon.png" />').click(queuePlaylist));
		title.append(image);
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
		var albums = {};
		var albumCount = 0;
		for (var i = 0; i < data.tracks.length; i++) {
			var track = data.tracks[i];
			totalduration += track.duration;
			table.append(Media.decorateTrack(track).addClass(i % 2 == 0 ? "even" : "odd"));
			if (albumCount < 4 && !albums[track.album.id]) {
				albums[track.album.id] = true;
				albumCount++;
			}
		}
		decorateImage(image, albums);
		
		decorateDuration(duration, data.tracks ? data.tracks.length : -1, totalduration);
		
		return result.append(title).append(table);
	}
	
	function decorateImage(imageTarget, albums) {
		var covers = [];
		for (cover in albums)
			covers.push(cover);
		
		if (covers.length < 4) {
		    return $('<img />').attr('src', 'Image?id=' + covers[0]).appendTo(imageTarget);
		}
		var target = $('<div/>').addClass('smallimages');
		for (var i = 0; i < covers.length; i++) {
		    $('<img />').attr('src', 'Image?id=' + covers[i]).appendTo(target);
		    if (i == 1)
		        $('<br/>').appendTo(target);
		}
		return target.appendTo(imageTarget);
	}
	
	function queuePlaylist() {
		$.get("PlaylistQueue", {index: $('div.playlist').data("index")});
	}
	
	function decorateDuration(duration, tracks, total) {
		var seconds = total / 1000;
		var minutes = seconds / 60;	seconds = seconds % 60;
		var hours = minutes / 60;	minutes = minutes % 60;
		var days = hours / 24; 		hours = hours % 24;
		
		
		var result = "";
		if (days >= 1) 		{ result += Math.floor(days) + ' day'; 		if (Math.floor(days)) result += 's'; result += ' '; }
		if (hours >= 1) 	{ result += Math.floor(hours) + ' hour'; 	if (Math.floor(hours)) result += 's'; result += ' ';}
		if (minutes >= 1)	{ result += Math.floor(minutes) + ' minute';if (Math.floor(minutes)) result += 's'; }
		else 				{ result += Math.floor(seconds) + ' second';if (Math.floor(seconds)) result += 's'; }
		
		return duration.text(tracks + ' tracks, ' + result);
	}
	
	function deleteSelected() {
		$('.playingTable .selected').each(function() {
			$.get('Play', {'delete': true, 'id': $(this).data('id')});
		});
	}
	
	function clear() {
		$.get('Play', {'delete': 'all'});
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
		"deleteSelected": deleteSelected,
		"clear": clear,
		"decorateDuration": decorateDuration,
		"decorate": decoratePlaylist
	};
}();
