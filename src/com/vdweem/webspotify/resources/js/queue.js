var Queue = function(){
	$("#lists #queue li").live('click', showQueue);
	
	var currentRevision;
	function showQueue() {
		Ajax.call('Queue', {}, decorateQueue);
	}
	
	function update(revision) {
		if (revision !== currentRevision && $(".playingTable:visible").length != 0)
			showQueue();
		
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
		
		return queue.append(playingTable);
	}
	
	return {
		"update": update
	}
}();