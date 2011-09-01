/**
 * Provides functions for playing tracks.
 */
var Player = function() {
	$(document).ready(init);
	
	/**
	 * Initialize the player.
	 */
	function init() {
		// Play tracks when the row is double clicked.
		$("tr.track").live('dblclick', function() {
			Ajax.get("Play", {"id": $(this).data("id")});
		})

		// Make the progress bar clickable to seek a position.
		$("#progress").unbind('click').click(function(e){
		    var position = e.pageX - $(this).position().left;
		    var width = $(this).width();
		    var seek = Math.round(position / width * $('#controls .duration.progressTime').data('value'));
		    Ajax.get("Seek", {position: seek});
		}); 
	}
	
	/**
	 * Update the position of the progress bar.
	 */
	function updatePosition(positionObj) {
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
	
	return {
		"updatePosition": updatePosition,
	};
}();