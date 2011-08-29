var Ajax = function() {
	
	var calling;
	
	function call(page, params, decorator) {
		$('#center').html('<div class="loader"></div>');
		calling = $.getJSON(page, params, function(data) {
			$('#center').html("").append(decorator(data));
			
		});
	}
	return {
		call: call,
	};
}();