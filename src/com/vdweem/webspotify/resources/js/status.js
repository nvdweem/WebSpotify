/**
 * Updates the current track and position of the progressbar.
 */
var Status = function() {
	$().ready(init);
	var windowTitle;
	function init() {
		windowTitle = $("title").text();
		update();
	}
	
	/**
	 * Request an update from the server.
	 */
	function update() {
		$.getJSON("Status", {}, updateStatus);
		setTimeout(update, 1000); // Repeat after 1 second.
	}
	
	/**
	 * Delegates the required updates.
	 */
	function updateStatus(data) {
		Player.updatePosition(data.pause, data.position);
		updatePlaying(data.playing);
		Playlist.update(data.playlistRevision);
		Menu.update(data.menu);
		Queue.update(data.queuerevision);
	}
	
	var playing;
	/**
	 * Update the currently playing div.
	 */
	function updatePlaying(t) {
		if (!t) {
			$('#playingArt, #playingText').html("");
			return;
		}
		else if (t.id == playing) 
			return;
		
		playing = t.id;
		$('#playingArt, #playingText').html("");
		$('#playingArt').append($('<img src="Image?id='+t.album.id+'&amp;'+new Date()+'"/>'));
		$('#playingText').append(
			$('<div class="playing"></div>')
				.append($('<div class="artist"></div>').append(Media.artistLink(t.artist)))
				.append($('<div class="song"></div>').text(t.name))
		);
		
		$("title").text(t.artist.name + " - " + t.name + " -- " + windowTitle);
	}
	
}();
