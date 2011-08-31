var Search = function() {
	$(document).ready(init);
	
	function init() {
		var search = function(event) {
			if (event) event.preventDefault();
			var query = $('input.search').val();
			if (query == '') return;
			Ajax.call("Search", {query: query}, Search.decorateSearch);
		};
		
		$('.searchRight').click(search);
		$('input.search').keypress(function(e) {
			if(e.which == 13){
				search(e);
				return false;
			}
		});
	}
	
	function decorateList(title, listItems, listDecorator) {
		var list = $('<ol></ol>');
		var images = $('<div class="images"></div>');
		
		if (title)
			list.append($('<li class="title">'+title+': </li>'));
		for (var i = 0; i < listItems.length; i++) {
			if (i != 0)
				list.append($('<li> - </li>'));
			list.append($('<li></li>').append(listDecorator(listItems[i])));
			
			images.append(
				listDecorator(listItems[i]).html("").append(
						$('<img />').attr("src", "Image?id=" + listItems[i].id)	
				)
			);
		}
		return $('<div class="'+title+'"></div>').append(images).append(list);
	}
	
	function decorateAlbumAndArtist(album) {
		var target = $('<span class="albumandartist"></span>');
		target.append(Media.albumLink(album));
		target.append($('<span class="artist"> by </span>').append(Media.artistLink(album.artist)));
		return target;
	}

	function decorateSearch(search) {
		var result = $('<div></div>');
		
		var head;
		if (search.artists.length != 0 || search.albums.length != 0) {
			head = $('<table class="searchTop"></table>');
			var row = $('<tr></tr>');
			head.append(row);
			
			if (search.artists.length != 0) row.append($('<td></td>').append(decorateList('Artists', search.artists, Media.artistLink)));
			if (search.albums.length != 0) row.append($('<td></td>').append(decorateList('Albums', search.albums, decorateAlbumAndArtist)));
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
		for (var i = 0; i < search.tracks.length; i++) {
			var track = search.tracks[i];
			table.append(Media.decorateTrack(track).addClass(i % 2 == 0 ? "even" : "odd"));
		}
		
		return result
				  .append(head)
				  .append(table);
	}
	
	return {
		"decorateSearch": decorateSearch,
		"decorateList": decorateList,
		"decorateAlbumAndArtist": decorateAlbumAndArtist,
		"init": init,
	};
}();