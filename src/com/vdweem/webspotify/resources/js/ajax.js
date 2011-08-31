var Ajax = function() {
	
	var calling;
	
	function call(page, params, decorator) {
		$('#center').html('<div class="loader"></div>');
		calling = $.getJSON(page, params, function(data) {
			$('#center').html("").append(decorator(data));
		});
	}
	
	function get(link, params, whenDone) {
		$.get(link, params, whenDone);
	}
	return {
		call: call,
		get: get,
	};
}();