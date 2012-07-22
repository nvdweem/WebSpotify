/**
 * Provides functions for playing tracks.
 */
var Player = function() {
	$(document).ready(init);
	$("#playpause").live('click', playPause);
	$("#skip").live('click', next);
	$("#back").live('click', prev);
	$("#shuffle").live('click', toggleShuffle);
	
	/**
	 * Initialize the player.
	 */
	function init() {
		// Play tracks when the row is double clicked.
		$(document).on('dblclick', 'tr.track', function() {
			Ajax.get("Play", {"id": $(this).data("id")});
			queued($(this));
			blurText();
		});

		// Make the progress bar clickable to seek a position.
		$("#progress").unbind('click').click(function(e){
			if (!e.pageX)
				e.pageX = $(this).find('#position').position().left + $(this).position().left;
		    var position = e.pageX - $(this).position().left;
		    if (!position) return;
		    var width = $(this).width();
		    var seek = Math.round(position / width * $('#controls .duration.progressTime').data('value'));
		    Ajax.get("Seek", {position: seek});
		}); 
	}
	
	function queued(_this) {
		_this
		    .animate({"background-color": "black"})
		    .animate({"background-color": '#A9D9FE'}, function() {
		    	$(this).css('background-color', '');
		    });
	}
	
	function playPause() {
		$.get('PlayPause');
	}
	
	function next() {
		$.getJSON('Next');
	}
	
	function prev() {
		$.getJSON('Prev');
	}
	
	/**
	 * Update the position of the progress bar.
	 */
	function updatePosition(pause, positionObj) {
		if (pause) $("#playpause").addClass("pause");
		else	   $("#playpause").removeClass("pause");
		
		var position = positionObj.position;
		var duration = positionObj.duration;
		
		var left = duration == 0 ? 0 : (position / duration) * $("#progress").width();
		if (!$('#position').is('.dragging'))
			$('#position').css("left", left - 4);
		$('#play').css("left", left);
		
		$('#controls .progressTime').detach();
		$('#controls')
			.append(Media.decorateDuration(position * 1000).removeClass("duration").addClass("played progressTime"))
			.append(Media.decorateDuration(duration * 1000).addClass("progressTime"));
	}
	
	function addSelectedToPlaylist() {
		var ids = [];
		$('.selected').each(function() {
			ids.push($(this).data('id'));
		});
		if (ids.length > 0)
			Ajax.post("Play", {"ids": ids, "playlist": true});
	}
	
	function toggleShuffle() {
		$.get("Shuffle");
	}
	
	function updateShuffling(shuffling) {
		if (shuffling) $("#shuffle").addClass("on");
		else		   $("#shuffle").removeClass("on");
	}
	
	return {
		"updatePosition": updatePosition,
		"updateShuffling": updateShuffling,
		"addSelectedToPlaylist": addSelectedToPlaylist
	};
}();