/**
 * Provides functions to search for songs.
 */
var Search = function() {
	$(document).ready(init);
	$("a.search").live('click', searchFromLink);
	
	/**
	 * Initializer.
	 */
	function init() {
		var search = function(event) {
			if (event) event.preventDefault();
			var query = $('input.search').val();
			if (query == '') return;
			Ajax.call("Search", {query: query}, Search.decorateSearch);
		};
		
		// Make the search bar usable.
		$('.searchRight').click(search);
		$('input.search').keypress(function(e) {
			if(e.which == 13){
				search(e);
				return false;
			}
		});
	}
	
	/**
	 * A search link is clicked.
	 */
	function searchFromLink(event) {
		event.preventDefault();
		
		var id = $(this).data('id');
		/[^:]+:search:(.*)/.test(id);
		$('input.search').val(unescape(RegExp.$1));
		$('div.searchRight').click();
	}
	
	/**
	 * Decorates a list of artists or albums.
	 */
	function decorateList(title, listItems, listDecorator) {
		var list = $('<ol></ol>');
		var images = $('<div class="images"></div>');
		
		if (title)
			list.append($('<li class="title">'+title+': </li>'));
		
		if (!listItems) {
			list.append($('<li>Error: timout retrieving data.</li>'));
			return $('<div class="'+title+'"></div>').append(images).append(list);
		}
		for (var i = 0; i < listItems.length; i++) {
			if (i != 0)
				list.append($('<li> - </li>'));
			list.append($('<li></li>').append(listDecorator(listItems[i])));
			
			if (i < 15) {
				images.append(
					listDecorator(listItems[i], true).html("").append(
							$('<img />').attr("src", "Image?id=" + listItems[i].id)	
					)
				);
			}
		}
		return $('<div class="'+title+'"></div>').append(images).append(list);
	}
	
	/**
	 * Decorator to combine albums and artists.
	 */
	function decorateAlbumAndArtist(album, forImage) {
		if (forImage === true)
			return Media.albumLink(album);
		var target = $('<span class="albumandartist"></span>');
		target.append(Media.albumLink(album));
		target.append($('<span class="artist"> by </span>').append(Media.artistLink(album.artist)));
		return target;
	}

	/**
	 * Decorate the search result.
	 */
	function decorateSearch(search) {
		var result = $('<div></div>');
		
		var head;
		if ((search.artists && search.artists.length != 0) || (search.albums && search.albums.length != 0)) {
			head = $('<table class="searchTop"></table>');
			var row = $('<tr></tr>');
			head.append(row);
			
			if (search.artists && search.artists.length != 0) row.append($('<td></td>').append(decorateList('Artists', search.artists, Media.artistLink)));
			if (search.albums && search.albums.length != 0) row.append($('<td></td>').append(decorateList('Albums', search.albums, decorateAlbumAndArtist)));
		}
		else
			head = $('<div></div>');
		
		var table = $(
			'<table class="searchResults">' +
			'  <tr class="searchHead">' +
			'    <th class="track">Track</th>' +
			'    <th class="artist">Artist</th>' +
			'    <th class="time">Time</th>' +
			'    <th class="popularity">Popularity</th>' +
			'    <th class="album">Album</th>' +
			'  </tr>' +
			'</table>');
		if (search.tracks) {
			for (var i = 0; i < search.tracks.length; i++) {
				var track = search.tracks[i];
				table.append(Media.decorateTrack(track).addClass(i % 2 == 0 ? "even" : "odd"));
			}
		}
		else {
			table.append($('<tr><td colspan="5">No search results.</td></tr>'));
		}
		
		return result
				  .append(head)
				  .append(table);
	}
	
	return {
		"decorateSearch": decorateSearch,
		"decorateList": decorateList,
		"decorateAlbumAndArtist": decorateAlbumAndArtist,
	};
}();