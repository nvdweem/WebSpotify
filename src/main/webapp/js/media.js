/**
 * Provides functions to decorate media types.
 */
var Media = function() {
	
	/**
	 * Helper to return a cell with the specified class.
	 */
	function cell(clazz) {
		return $('<td></td>').addClass(clazz);
	}
	
	/**
	 * Adds zero's until the required length is achieved.
	 */
	function lz(value, len) {
		while ((value + "").length < len)
			value = "0" + value;
		return value;
	}
	
	/**
	 * Format a duration integer to mm:ss span.
	 */
	function decorateDuration(duration) {
		duration /= 1000;
		var minutes = Math.floor(duration / 60);
		var seconds = lz(duration % 60, 2);
		return $('<span class="duration">'+minutes+':'+seconds+'</span>').data("value", duration);
	}
	
	/**
	 * Decorate a popularity integer to an image.
	 */
	function decoratePopularity(popularity) {
		var rating = Math.round(popularity * 0.12);
		if (rating > 0) 
			return $('<img src="images/popularity/'+rating+'.png" />');
		return $('<span> &nbsp; </span>');
	}
	/**
	 * Decorate a track row. Some options are available by providing an options object.
	 */
	function decorateTrack(t, options) {
		var printArtist = options ? options.printArtist !== false : true;
		var printAlbum = options ? options.printAlbum !== false : true;
		var index = options ? options.index : false;
		
		var row = $('<tr class="track"></tr>').data("id", t.id).data("album", t.album ? t.album.id : '');
		if (index) row.append(cell('index').text(index));
		row.append(cell('name').text(t.name));
		if (printArtist)
			row.append(cell('artist').append(artistLink(t.artist)));
		row.append(cell('duration').append(decorateDuration(t.duration)));
		row.append(cell('popularity').append(decoratePopularity(t.popularity)));
		if (printAlbum)
			row.append(cell('album').append(albumLink(t.album)));
		return row;
	}
	
	/**
	 * Decorate an artist to a link.
	 */
	function artistLink(a) {
		if (!a) return $('<a href="#">Unknown</a>');
		return $('<a href="#" class="artist"></a>').data("id", a.id || "").text(a.name || "");
	}
	
	/**
	 * Decorate an album to a link.
	 */
	function albumLink(a) {
		if (a)
			return $('<a href="#" class="album"></a>').data("id", a.id).text(a.name);
		return $('<span>Unknown</span>');
	}
	
	/**
	 * Queue all tracks on the screen.
	 */
	function queueAll() {
		$(".track").dblclick();
	}
	
	return {
		"decorateTrack" : decorateTrack,
		"artistLink": artistLink,
		"albumLink": albumLink,
		"decorateDuration": decorateDuration,
		"decoratePopularity": decoratePopularity,
		"queueAll": queueAll,
	};
}();
(function($) {
    $.fn.bio = function() {
    	return this.find('a[href^=spotify]').each(function(i, elem) {
			$(elem).addClass(elem.href.split(':')[1]).data("id", elem.href);
		}).end();
    };
})(jQuery);