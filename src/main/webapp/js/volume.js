var Volume = function() {
	$(document).ready(init);
	
	function init() {
		// Make the progress bar clickable to seek a position.
		$("#volume").unbind('click').click(function(e){
		    var position = e.pageX - $(this).position().left;
		    var width = $(this).width();
		    var volume = Math.round((position / width) * 100);
		    Ajax.get("Volume", {volume: volume});
		});
	}
	
	function update(volume) {
		var left = (volume / 100) * $("#volume").width();
		$('#volume .position').css("left", left - 4);
		$('#volume .right').css("left", left);
	}
	
	return {
		"update": update,
	};
}();