var Queue = function(){
	var currentRevision = -1;
	var selected = {};
	var selectedIndex = 0;
	
	$("#lists #queue li").live('click', showQueue);
	
	function showQueue() {
		Ajax.call('Queue', {}, decorateQueue);
	}
	
	function update(revision) {
		if (revision !== currentRevision && $(".playingTable:visible").length != 0) {
			// Store the current selection
			selected = {};
			$('.selected').each(function() {
			    selected[$(this).data('id')] = true;;
			});
			selectedIndex = $('.selected:first').index();
			
			showQueue();
		}
		
		currentRevision = revision;
	}
	
	function decorateQueue(q) {
		var queue = $('<div></div>');
		var playingTable = $(
				'<table class="playingTable">' +
				'  <tr class="playingHead head">' +
				'    <th class="track">Track</th>' +
				'    <th class="artist">Artist</th>' +
				'    <th class="time">Time</th>' +
				'    <th class="popularity">Popularity</th>' +
				'    <th class="album">Album</th>' +
				'  </tr>' +
				'</table>');
		
		var evenOddOffset = 0;
		if (q.queue) {
			for (var i = 0; i < q.queue.length; i++) {
				var track = q.queue[i];
				evenOddOffset = (i+1) % 2;
				playingTable.append(Media.decorateTrack(track).addClass(i % 2 == 0 ? "even" : "odd").addClass("queue"));
			}
		}
		if (q.playlist) {
			for (var i = 0; i < q.playlist.length; i++) {
				var track = q.playlist[i];
				playingTable.append(Media.decorateTrack(track).addClass((i + evenOddOffset) % 2 == 0 ? "even" : "odd"));
			}
		}
		
		queue.append(playingTable);
		
		//// Restore selection
		// Select all selected id's
		var tracks = playingTable.find('tr.track').each(function() {
		    if (selected[$(this).data('id')])
		        $(this).addClass('selected');
		});
		// None selected, probably deleted, select the correct index.
		if (selectedIndex > 0) {
			if (playingTable.find('.selected').length == 0) {
				$(tracks.get(selectedIndex-1)).addClass('selected');
			}
		}
		
		return queue;
	}
	
	return {
		"update": update
	};
}();