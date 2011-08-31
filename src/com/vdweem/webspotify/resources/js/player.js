var Player = function() {
	$(document).ready(init);
	
	function init() {
		$("tr.track").live('dblclick', function() {
			Ajax.get("Play", {"id": $(this).data("id")});
		})
		

		$("#progress").unbind('click').click(function(e){
		    var position = e.pageX - $(this).position().left;
		    var width = $(this).width();
		    var seek = Math.round(position / width * $('#controls .duration.progressTime').data('value'));
		    Ajax.get("Seek", {position: seek});
		}); 
	}
	
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