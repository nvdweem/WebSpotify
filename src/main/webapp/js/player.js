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
		$("tr.track").live('dblclick', function() {
			Ajax.get("Play", {"id": $(this).data("id")});
			queued($(this));
			
	        if (document.selection)
	        {
	            document.selection.empty();
	            obj.blur();
	        }
	        else
	        {
	            window.getSelection().removeAllRanges();
	        }
		});

		// Make the progress bar clickable to seek a position.
		$("#progress").unbind('click').click(function(e){
		    var position = e.pageX - $(this).position().left;
		    var width = $(this).width();
		    var seek = Math.round(position / width * $('#controls .duration.progressTime').data('value'));
		    Ajax.get("Seek", {position: seek});
		}); 
	}
	
	function queued(_this) {
		var color = _this.is(".odd") ? "#313131" : "#373737";
		_this
		    .animate({"background-color": "black"})
		    .animate({"background-color": color});
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
		
		var left = (position / duration) * $("#progress").width();
		$('#position').css("left", left - 4);
		$('#play').css("left", left);
		
		$('#controls .progressTime').detach();
		$('#controls')
			.append(Media.decorateDuration(position * 1000).removeClass("duration").addClass("played progressTime"))
			.append(Media.decorateDuration(duration * 1000).addClass("progressTime"));
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
	};
}();