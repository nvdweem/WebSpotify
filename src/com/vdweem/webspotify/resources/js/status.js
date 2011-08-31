var Status = function() {
	$().ready(init);
	
	function init() {
		update();
	}
	
	function update() {
		$.getJSON("Status", {}, updateStatus);
		setTimeout(update, 1000);
	}
	
	function updateStatus(data) {
		Player.updatePosition(data.position);
		updatePlaying(data.playing);
	}
	
	var playing;
	function updatePlaying(t) {
		if (!t) {
			$('#playingArt, #playingText').html("");
			return;
		}
		else if (t.id == playing) 
			return;
		
		playing = t.id;
		$('#playingArt, #playingText').html("");
		$('#playingArt').append($('<img src="Image?id='+t.album.id+'"/>'));
		$('#playingText').append(
			$('<div class="playing"></div>')
				.append($('<div class="artist"></div>').append(Media.artistLink(t.artist)))
				.append($('<div class="song"></div>').text(t.name))
		);
	}
	
}();
