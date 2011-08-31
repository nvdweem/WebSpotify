var Media = function() {
	
	function cell() {
		return $('<td></td>');
	}
	
	function lz(value, len) {
		while ((value + "").length < len)
			value = "0" + value;
		return value;
	}
	
	function decorateDuration(duration) {
		duration /= 1000;
		var minutes = Math.floor(duration / 60);
		var seconds = lz(duration % 60, 2);
		return $('<span class="duration">'+minutes+':'+seconds+'</span>').data("value", duration);
	}
	
	function decoratePopularity(popularity) {
		var rating = Math.round(popularity * 0.12);
		if (rating > 0) 
			return $('<img src="images/popularity/'+rating+'.png" />');
		return $('<span> &nbsp; </span>');
	}
	/**
	 * { id, name, duration, popularity, disc, album, artist }
	 */
	function decorateTrack(t) {
		var row = $('<tr class="track"></tr>').data("id", t.id).data("album", t.album.id);
		row.append(cell().text(t.name));
		row.append(cell().append(artistLink(t.artist)));
		row.append(cell().append(decorateDuration(t.duration)));
		row.append(cell().append(decoratePopularity(t.popularity)));
		row.append(cell().append(albumLink(t.album)));
		return row;
	}
	
	function artistLink(a) {
		return $('<a href="#" class="artist"></a>').data("id", a.id).text(a.name);
	}
	
	function albumLink(a) {
		return $('<a href="#" class="album"></a>').data("id", a.id).text(a.name);
	}
	
	return {
		"decorateTrack" : decorateTrack,
		"artistLink": artistLink,
		"albumLink": albumLink,
		"decorateDuration": decorateDuration,
	}
}();
/*
$.getJSON('Search', {query: "Test"});
*/