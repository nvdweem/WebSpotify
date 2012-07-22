var Volume = function() {
	$(document).ready(init);
	var vol = 100;
	
	function init() {
		// Make the progress bar clickable to seek a position.
		$("#volume").unbind('click').click(function(e){
		    var position = e.pageX - $(this).position().left;
		    var width = $(this).width();
		    var volume = Math.round((position / width) * 100);
		    setVolume(volume);
		});
	}
	
	function setVolume(value) {
		vol = value;
		Ajax.get("Volume", {volume: value});
	}
	
	function getVolume() {
		return vol;
	}
	
	function update(volume) {
		vol = volume;
		var left = (volume / 100) * $("#volume").width();
		if (!$('#volume .position').is('.dragging'))
			$('#volume .position').css("left", left - 4);
		$('#volume .right').css("left", left);
	}
	
	return {
		"update": update,
		"setVolume": setVolume,
		"getVolume": getVolume
	};
}();