var Queue = function(){
	var currentRevision = -1;
	var selected = {};
	var selectedIndex = 0;
	
	$("#lists #queue li").live('click', showQueue);
	$(document).on('click', '.tabheader', showTab);
	
	function showQueue(force) {
		var args = {"page": 'Queue', params: {}, decorator: decorateQueue};
		if (force === true)
			args.noHistory = true;
		Ajax.call(args);
	}
	
	function showTab() {
		$this = $(this);
		$('.tab').hide();
		$('.tabheader.shown').removeClass('shown');
		$('.tab.' + $(this).attr('data-tab')).show();
		$this.addClass('shown');
	}
	
	function update(revision) {
		if (revision !== currentRevision && $(".playingTable:visible").length != 0) {
			// Store the current selection
			selected = {};
			$('.selected').each(function() {
			    selected[$(this).data('id')] = true;;
			});
			selectedIndex = $('.selected:first').index();
			
			showQueue(true);
		}
		
		currentRevision = revision;
	}
	
	function decorateQueue(q) {
		var container = $('<div class="queue"></div>');
		$('<div class="tabs" />')
			.append('<span data-tab="queue" class="tabheader shown">Queue</span>')
			.append('<span data-tab="history" class="tabheader">History</span>')
			.appendTo(container);
		
		var queue = $('<div class="tab queue"></div>');
		var playingTable = $(
				'<table class="playingTable">' +
				'  <tr class="playingHead head">' +
				'    <th class="track">Track</th>' +
				'    <th class="artist">Artist</th>' +
				'    <th class="time">Time</th>' +
				'    <th class="popularity">Popularity</th>' +
				'    <th class="album">Album</th>' +
				'  </tr>' +
				'</table>');
		var historyTable = playingTable.clone();
		
		var evenOddOffset = 0;
		if (q.queue) {
			for (var i = 0; i < q.queue.length; i++) {
				var track = q.queue[i];
				evenOddOffset = (i+1) % 2;
				playingTable.append(Media.decorateTrack(track).addClass(i % 2 == 0 ? "even" : "odd").addClass("queue"));
			}
		}
		if (q.playlist) {
			for (var i = 0; i < q.playlist.length; i++) {
				var track = q.playlist[i];
				playingTable.append(Media.decorateTrack(track).addClass((i + evenOddOffset) % 2 == 0 ? "even" : "odd"));
			}
		}
		if (q.history) {
			for (var i = 0; i < q.history.length; i++) {
				var track = q.history[i];
				historyTable.append(Media.decorateTrack(track).addClass((i + evenOddOffset) % 2 == 0 ? "even" : "odd"));
			}
		}
		
		var duration = $('<span class="duration"></span>');
		Playlist.decorateDuration(duration, q.size ? q.size : 0, q.duration);
		queue.append($('<div class="playlist" />').append($('<span class="title"/>').append(duration)));
		
		queue.append(playingTable);
		
		//// Restore selection
		// Select all selected id's
		var tracks = playingTable.find('tr.track').each(function() {
		    if (selected[$(this).data('id')])
		        $(this).addClass('selected');
		});
		// None selected, probably deleted, select the correct index.
		if (selectedIndex > 0) {
			if (playingTable.find('.selected').length == 0) {
				$(tracks.get(selectedIndex-1)).addClass('selected');
			}
			selectedIndex = -1;
		}
		
		container.append(queue);
		container.append($('<div class="tab history" style="display:none;"/>').append(historyTable));
		return container;
	}
	
	return {
		"update": update,
		"decorate": decorateQueue,
		"showQueue": showQueue
	};
}();