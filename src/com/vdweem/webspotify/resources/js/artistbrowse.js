//Ajax.call('ArtistBrowse', {"id": "spotify:artist:05fG473iIaoy82BF1aGhL8"}, ArtistBrowse.decorateArtistBrowse);
/**
 * Provides the artist browse link handlers.
 */
var ArtistBrowse = function() {
	$("a.artist").live('click', artistBrowseClick);
	
	/**
	 * Click event for artist links.
	 */
	function artistBrowseClick(event) {
		event.preventDefault();
		browseArtist(this);
	}
	
	/**
	 * Do the browse ajax call.
	 */
	function browseArtist(elem) {
		var artistId = $(elem).data("id");
		Ajax.call('ArtistBrowse', {"id": artistId}, decorateArtistBrowse);
	}
	
	/**
	 * Decorate the result from the ajax call.
	 */
	function decorateArtistBrowse(data) {
		var result = $('<div class="browse"></div>');
		var topTable = $('<table class="browseTop"></table>');
		var topRow = $('<tr></tr>');
		topRow.append($('<td></td>').append(decorateBio(data)));
		topRow.append($('<td></td>')
				.append($('<div class="relatedArtists">Related Artists</div>'))
				.append(Search.decorateList("", data.relatedArtists, Media.artistLink).addClass('Artists')));
		topTable.append(topRow);
		result.append(topTable);
		
		result.append(decorateTopTracks(data.topTracks));
		result.append(decorateAlbums(data.albums));
		return result;
	}
	
	/**
	 * Decorate the artist bio.
	 */
	function decorateBio(a) {
		var result = $('<div class="bio"></div>');
		result.append($('<img src="Image?id='+a.id+'">'));
		result.append($('<div class="artistname"></div>').text(a.name));
		result.append($('<div class="text"></span>').html(a.bio || "").find('a[href^=spotify]').each(function(i, elem) {
			$(elem).addClass(elem.href.split(':')[1]).data("id", elem.href);
		}).end());
		return result;
	}
	
	/**
	 * Decorate the artist Top 5.
	 */
	function decorateTopTracks(ts) {
		var result = $('<div class="topTracks"></div>');
		result.append(decorateLabel('Top Hits'));
		
		result.append($('<img src="images/123.png" class="left" />'));
		var targetTable = $('<table></table>');
		if (ts) {
			for (var i = 0; i < ts.length; i++) {
				targetTable.append(Media.decorateTrack(ts[i], {index: i+1, printArtist: false, printAlbum: false}).addClass(i % 2 == 1 ? "even" : "odd"));
			}
		}
		else {
			targetTable.append($('<tr><td>No top tracks</td></tr>'));
		}
		return result.append(targetTable);
	}
	
	/**
	 * Decorate all the albums.
	 */
	function decorateAlbums(as) {
		var result = $('<div class="albums"></div>');
		result.append($('<div class="spacer"></div>'));
		
		if (!as) {
			result.append('<div class="error"> Albums are not yet loaded </div>');
			return result;
		}
		var type = -1;
		for (var i = 0; i < as.length; i++) {
			if (as[i].type != type) {
				type = as[i].type;
				switch (type) {
					case 0: result.append(decorateLabel('Albums')); break;
					case 1: result.append(decorateLabel('Singles')); break;
					case 2: result.append(decorateLabel('Compilations')); break;
					case 3: result.append(decorateLabel('Unknown')); break;
					case 4: result.append(decorateLabel('Appears On')); break;
				}
			}
			result.append(AlbumBrowse.decorateAlbum(as[i]));
		}
		
		return result;
	}
	
	/**
	 * Decorate a label.
	 */
	function decorateLabel(label) {
		return $('<div class="label"></div>').text(label);
	}
	
	return {
		"decorateArtistBrowse": decorateArtistBrowse,
	}
}();
