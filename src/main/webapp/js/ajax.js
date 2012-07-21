/**
 * Allows a few interfaces for jQuery ajax calls.
 */
var Ajax = function() {
	/**
	 * Calls the page with specified parameters. Then decorates the result and places it in the center div.
	 */
	function call(page, params, decorator) {
		var def = {clear: true, params: '', 'decorator': window.console ? console.log : alert};
		if (typeof(page) != 'object') {
			page = {
				page: page,
				params: params,
				decorator: decorator
			};
		}
		var args = $.extend(def, page);

		if (args.clear)
			$('#center').html('<div class="loader"></div>');
		
		$.getJSON(args.page, args.params, function(data) {
			var center = $('#center');
			if (args.clear)
				center.html("");
			
			if (data.error)
				$('<div title="Error"></div>').text(data.error).dialog({"buttons": {"Ok": function() {$(this).dialog("close");}}});
			else
				center.append(args.decorator(data)).img();
		});
	}
	
	function get(link, params, whenDone) {
		$.getJSON(link, params, whenDone);
	}
	return {
		call: call,
		get: get
	};
}();