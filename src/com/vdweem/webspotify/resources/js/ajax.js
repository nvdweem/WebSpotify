/**
 * Allows a few interfaces for jQuery ajax calls.
 */
var Ajax = function() {
	var calling;
	
	/**
	 * Calls the page with specified parameters. Then decorates the result and places it in the center div.
	 */
	function call(page, params, decorator) {
		$('#center').html('<div class="loader"></div>');
		calling = $.getJSON(page, params, function(data) {
			var center = $('#center').html("");
			
			if (data.error)
				$('<div title="Error"></div>').text(data.error).dialog({"buttons": {"Ok": function() {$(this).dialog("close");}}});
			else
				center.append(decorator(data));
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