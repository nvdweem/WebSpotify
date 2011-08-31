//Ajax.call('ArtistBrowse', {"id": "spotify:artist:05fG473iIaoy82BF1aGhL8"}, ArtistBrowse.decorateArtistBrowse);
var ArtistBrowse = function() {
	$("a.artist").live('click', artistBrowseClick);
	
	function artistBrowseClick(event) {
		event.preventDefault();
		browseArtist(this);
	}
	
	function browseArtist(elem) {
		var artistId = $(elem).data("id");
		Ajax.call('ArtistBrowse', {"id": artistId}, decorateArtistBrowse);
	}
	
	function decorateArtistBrowse(data) {
		var result = $('<div class="browse"></div>');
		xxx = data;
		var topTable = $('<table class="browseTop"></table>');
		var topRow = $('<tr></tr>');
		topRow.append($('<td></td>').append(decorateBio(data)));
		topRow.append($('<td></td>')
				.append($('<div class="relatedArtists">Related Artists</div>'))
				.append(Search.decorateList("", data.relatedArtists, Media.artistLink).addClass('Artists')));
		topTable.append(topRow);
		result.append(topTable);
		
		result.append(decorateTopTracks(data.topTracks));
		return result;
	}
	
	function decorateBio(a) {
		var result = $('<div class="bio"></div>');
		result.append($('<img src="Image?id='+a.id+'">'));
		result.append($('<div class="artistname"></div>').text(a.name));
		result.append($('<div class="text"></span>').html(a.bio || "").find('a[href^=spotify]').each(function(i, elem) {
			$(elem).addClass(elem.href.split(':')[1]).data("id", elem.href);
		}).end());
		return result;
	}
	
	function decorateTopTracks(ts) {
		var result = $('<div class="topTracks"></div>');
		result.append(decorateLabel('Top hits'));
		
		var targetTable = $('<table></table>');
		for (var i = 0; i < ts.length; i++) {
			targetTable.append(Media.decorateTrack(ts[i], i+1).addClass(i % 2 == 0 ? "even" : "odd"));
		}
		return result.append(targetTable);
	}
	
	function decorateLabel(label) {
		return $('<div class="label"></div>').text(label);
	}
	
	return {
		"decorateArtistBrowse": decorateArtistBrowse,
	}
}();
var xxx;