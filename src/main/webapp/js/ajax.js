/**
 * Allows a few interfaces for jQuery ajax calls.
 */
var Ajax = function() {
	$.ajaxSetup({ "traditional": true });
	
    History.Adapter.bind(window,'statechange',function(e){
        var State = History.getState();
        State.data.noHistory = true;
        call(State.data);
    });
    solveHashUrl();
	
	/**
	 * Calls the page with specified parameters. Then decorates the result and places it in the center div.
	 */
	function call(page, params, decorator) {
		if (!page || (typeof(page) == 'string' && !page) || (typeof(page) == 'object' && !page.page))
			return $('#center').html('');
		
		var def = {"clear": true, "params": '', "method": "get"};
		if (typeof(page) != 'object') {
			page = {
				page: page,
				params: params,
				decorator: decorator
			};
		}
		var args = $.extend(def, page);
		
		if (!args.noHistory) {
			var paramStr = "";
			for (key in args.params) {
				paramStr += paramStr.length == 0 ? '?' : '&';
				paramStr += key + "=" + escape(args.params[key]);
			}
			History.pushState(args, null, args.page + paramStr);
			return;
		}

		if (args.clear)
			$('#center').html('<div class="loader"></div>');
		
		var fnc = /get/i.test(args.methos) ? $.getJSON : $.post;
		fnc(args.page, args.params, function(data) {
			var center = $('#center');
			if (args.clear) {
				center.html("");
			}
			
			if (data.error)
				$('<div title="Error"></div>').text(data.error).dialog({"buttons": {"Ok": function() {$(this).dialog("close");}}});
			else {
				var html;
				if (!args.decorator) {
					html = window[args.page].decorate(data);
				} else {
					html = args.decorator(data);
				}
				center.append(html).img();
			}
		});
	}
	function fromPage(response) {
		$('#center').append(response).img();
	}
	
	function get(link, params, whenDone) {
		$.getJSON(link, params, whenDone);
	}
	function post(link, params, whenDone) {
		$.post(link, params, whenDone);
	}
	
	function solveHashUrl() {
		/#(\.\/)?(.*)(\?.*)/.test(location.href);
		var page = RegExp.$2;
		var params = RegExp.$3.substring(1);
		call({"page": page, "params": params, "noHistory": true});
	}
	return {
		"call": call,
		"get": get,
		"post": post,
		"fromPage": fromPage
	};
}();