/**
 * Makes the application keyboard accessible.
 */
(function($) {
	
	/**
	 * Determines if the current track is still in view.
	 */
	function isInView(elem, up)
	{
		if (!elem && elem.length == 0) return false;
		var margin = 60;
		var top = 56 + margin;
		var bottom = $('#center').height() + top - margin*2;
		
		if (up)
			return elem.offset().top > top;
		else
			return (elem.offset().top + elem.height()) < bottom;
	}
	/**
	 * Scrolls the center panel to the track.
	 */
	function scrollTo(elem) {
		$('#center').scrollTop(elem.position().top);
		return elem;
	}
	
	/**
	 * Select tracks. Multiple tracks can be selected by shift clicking.
	 */
	$(document).on('click', '.track', function(e) {
		if (e.shiftKey) {
			var a = $('.selected');
			var b = $(this);
			if (a.position().top > b.position().top) {
			    var c = a;
			    a = b;
			    b = c;
			}
			a.nextUntil(b).add(a).add(b).addClass('selected');
			blurText();
		} else {
			$('.selected').removeClass('selected');
			$(this).addClass('selected');
		}
	});
	
	/**
	 * Navigating through the tracks.
	 */
	key('up', function() {
		var dontScroll = false;
	    var selected = $('.selected').removeClass('selected').filter(':last');
	    var prev = selected.prev('.track');
	    if (prev.length == 0) {
	    	prev = scrollTo($('tr.track:last'));
	    	dontScroll = true;
	    }
	    prev.addClass('selected');
	    return !dontScroll && !isInView(selected, true);
	});
	key('down', function() {
		var dontScroll = false;
	    var selected = $('.selected').removeClass('selected').filter(':last');
	    var prev = selected.next('.track');
	    if (prev.length == 0) {
	    	prev = scrollTo($('tr.track:first'));
	    	dontScroll = true;
	    }
	    prev.addClass('selected');
	    return !dontScroll && !isInView(selected, false);
	});
	/**
	 * Add a track to the queue.
	 */
	key('enter', function() {
		$('.selected').dblclick();
	});
	/**
	 * Focus and select the text in the search popup.
	 */
	key('ctrl+s', function() {
		$('input.search').focus().get(0).select();
		return false;
	});
	/**
	 * Remove a track from the queue.
	 */
	key('delete, backspace', function() {
		$('.playingTable .selected').each(function() {
			$.get('Play', {'delete': true, 'id': $(this).data('id')});
		});
		return false;
	});
	
	/**
	 * Function for blurring text (after shift clicking).
	 */
	window.blurText = function() {
		if (document.selection) {
            document.selection.empty();
            obj.blur();
        } else {
            window.getSelection().removeAllRanges();
        }
	};
}(jQuery));