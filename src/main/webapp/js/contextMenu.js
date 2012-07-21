
$(function(){
	var addToQueue = {"name": "Add to queue", "icon":"queue", "callback": function() {
		$('.selected').dblclick();
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
	
	$.contextMenu({
        selector: '.playingTable .track', 
        items: {
        	"mtt": moveToTop,
        	"rfq": removeFromQueue,
        }
    });
	$.contextMenu({
        selector: '.toplist .track, .playlist .track, .browse .track, .searchResults .track', 
        items: {
        	"atq": addToQueue,
        	"atp": addToPlaylist,
        }
    });
    
});