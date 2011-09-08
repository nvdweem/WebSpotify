/**
 * Functions to allow albumbrowsing.
 */
var AlbumBrowse = function() {
	$("a.album").live('click', albumBrowseClick); // Add onclick to all album links.

	/**
	 * Handle album browse link click.
	 */
	function albumBrowseClick(event) {
		event.preventDefault();
		browseAlbum(this);
	}
	
	/**
	 * Perform the browse ajax call.
	 */
	function browseAlbum(elem) {
		var artistId = $(elem).data("id");
		Ajax.call('AlbumBrowse', {"id": artistId}, decorateAlbumBrowse);
	}

	/**
	 * Decorate the albumbrowse result.
	 */
	function decorateAlbumBrowse(album) {
		var result = $('<div class="browse"></div>');
		result.append(decorateAlbum(album));
		
		if (album.review) {
			result.append($('<div class="review"></div>')
					.append('<div class="reviewTitle">Review</div>')
					.append('<span class="review"></span>').html(album.review).bio()
			);
		}
		return result;
	}
	
	/**
	 * Decorate a single album.
	 */
	function decorateAlbum(a) {
		var result = $('<div class="album"></div>');
		
		result.append($('<img src="Image?id='+a.id+'" class="left cover"/>'));
		result.append($('<div class="albumTitle"></div>').text(a.name).append($('<span class="year"></span>').text('(' + a.year + ')')));
		result.append(decorateTracks(a.type > 2, a.tracks)); // No artist for album, compilation and singles.
		result.append($('<div class="spacer"></div>'));
		return result;
	}
	
	/**
	 * Decorate all tracks for an album.
	 */
	function decorateTracks(showArtist, ts) {
		var targetTable = $('<table></table>');
		
		if (!ts) {
			targetTable.append($('<tr><td>Error: Timeout while retrieving tracks </td></tr>'));
			return targetTable;
		}
		for (var i = 0; i < ts.length; i++) {
			targetTable.append(Media.decorateTrack(ts[i], {index: i+1, printArtist: showArtist, printAlbum: false}).addClass(i % 2 == 1 ? "even" : "odd"));
		}
		return targetTable;
	}
	
	return {
		"decorateAlbum": decorateAlbum,
	}
}();