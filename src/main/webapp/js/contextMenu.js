
$(function(){
	var addToQueue = {"name": "Add to queue", "icon":"queue", "callback": function() {
		Player.addSelectedToQueue();
	}};
	var addToPlaylist = {"name": "Add to playlist", "icon":"playlist", "callback": function() {
		Player.addSelectedToPlaylist();
	}};
	
	var moveToTop = {"name": "Move to top", "icon":"queue", "callback": function() {
		$(this).dblclick();
	}};
	var removeFromQueue = {"name": "Remove", "icon":"remove", "callback": function() {
		Playlist.deleteSelected();
	}};
	
	function showMenu(e) {
		if (!e.$trigger.is('.selected'))
			$('.selected').removeClass('selected');
			
		if ($('.selected').length == 0) {
			e.$trigger.click();
			selectedOnShow = true;
		}
	}
	
	$.contextMenu({
        selector: '.playingTable .track', 
        items: {
        	"mtt": moveToTop,
        	"rfq": removeFromQueue,
        },
        events: {
        	"show": showMenu
        }
    });
	$.contextMenu({
        selector: '.toplist .track, .playlist .track, .browse .track, .searchResults .track', 
        items: {
        	"atq": addToQueue,
        	"atp": addToPlaylist,
        },
        events: {
        	"show": showMenu
        }
    });
    
});