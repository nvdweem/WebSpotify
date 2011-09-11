var TopList = function() {
	$(document).ready(init);
	
	function init() {
		var toplist = $('<ol class="toplist"></ol>');
		toplist.append($('<li>Top Tracks</li>').click(showTracks));
		toplist.append($('<li>Top Albums</li>').click(showAlbums));
		
		$('#queue').after(toplist);
	}
	
	function showTracks() {
		Ajax.call("TopList", {}, decorateTracks);
	}
	
	function showAlbums() {
		Ajax.call("TopList", {"type": 0}, decorateAlbums);
	}
	
	function decorateTracks(data) {
		var result = $('<div class="toplist"></div>');
		
		var table = $('<table></table>');
		for (var i = 0; i < data.tracks.length; i++) {
			var track = data.tracks[i];
			var row = $('<tr class="track"></tr>').data('id', track.id);
			row.append($('<td class="position"></td>').text(i+1 + ". "));
			
			row.addClass(i % 2 == 0 ? 'even' : 'odd');
			if (i < 3) {
				var cell = $('<td colspan="2"></td>');
				cell.append('<img src="Image?id='+ track.album.id +'&amp;'+new Date()+'" class="cover" />');
				cell.append(track.name);
				cell.append('<br/>');
				var by = $('<span class="by"></span');
				by.append(' by ');
				by.append(Media.artistLink(track.artist));
				cell.append(by);
				cell.append('<br/>');
				cell.append(Media.decoratePopularity(track.popularity));
				row.append(cell);
			}
			else {
				var cell = $('<td></td>');
				cell.append(track.name);
				var by = $('<span class="by"></span');
				by.append(' by ');
				by.append(Media.artistLink(track.artist));
				cell.append(by);
				row.append(cell);
				row.append($('<td class="popularity"></td>').append(Media.decoratePopularity(track.popularity)));
			}
				
			table.append(row);
		}
		
		return result.append(table);
	}
	
	function decorateAlbums(data) {
		var result = $('<div class="toplist"></div>');
		
		var table = $('<table></table>');
		for (var i = 0; i < data.albums.length; i++) {
			var album = data.albums[i];
			var row = $('<tr></tr>');
			row.append($('<td class="position"></td>').text(i+1 + ". "));
			
			row.addClass(i % 2 == 0 ? 'even' : 'odd');
			if (i < 3) {
				var cell = $('<td colspan="2"></td>');
				cell.append('<img src="Image?id='+ album.id +'&amp;'+new Date()+'" class="cover" />');
				cell.append(Media.albumLink(album));
				cell.append('<br/>');
				var by = $('<span class="by"></span');
				by.append(' by ');
				by.append(Media.artistLink(album.artist));
				cell.append(by);
				row.append(cell);
			}
			else {
				var cell = $('<td></td>');
				cell.append(Media.albumLink(album));
				var by = $('<span class="by"></span');
				by.append(' by ');
				by.append(Media.artistLink(album.artist));
				cell.append(by);
				row.append(cell);
			}
				
			table.append(row);
		}
		
		return result.append(table);
	}
}();