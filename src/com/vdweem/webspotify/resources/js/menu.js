var Menu = function() {
	var isLoggedIn = undefined;
	var spotifyConnection = undefined;
	
	$("#menu li").live('mouseenter', function() {$(this).addClass("hover")})
				 .live('mouseleave', function() {$(this).removeClass("hover")});
	
	function update(menu) {
		if (isLoggedIn !== menu.loggedIn || spotifyConnection !== menu.spotify) {
			spotifyConnection = menu.spotify;
			setMenu(menu.loggedIn);
		}
		isLoggedIn = menu.loggedIn;
		spotifyConnection = menu.spotify;
	}
	
	function setMenu(isLoggedIn) {
		var result;
		if (!isLoggedIn)
			result = loginMenu();
		else
			result = adminMenu();
		
		$("#menu").html("").append(result);
	}
	
	function loginMenu() {
		return $('<ol><li>Login</li></ol>').find('li').click(showLogin).end();
	}
	
	function showLogin() {
		var dialog = $('<div title="Login">'
		 +'<table><tr><td>Username: </td><td> <input type="text" class="username" /> </td></tr>'
		 +'       <tr><td>Password: </td><td> <input type="password" class="password" /> </td></tr></table>'
		 +'</div>');
		dialog.find('.password').keypress(function(e) {
			if (e.which == 13)
				$(this).parents('div.ui-dialog').find('button:first').click()
		});
		dialog.dialog({"buttons": {"Ok": doLogin, "Cancel": closeDialog}});
	}
	
	function doLogin() {
		$.getJSON('Login', {"username": $(this).find(".username").val(), "password": $(this).find(".password").val()}, loginResult);
		close(this);
	}
	function loginResult(data) {
		if (data.result != 'success')
			alert('Incorrect username or password.');
	}
	
	function closeDialog() {
		close(this);
	}
	
	function closeDialog() {
		close(this);
	}
	
	function close(dlg) {
		$(dlg).dialog("close")
	}
	
	/**
	 * Admin functions
	 */
	function adminMenu() {
		var result = $('<ol></ol>');
		
		if (spotifyConnection)
			result.append($('<li>Spotify logout</li>').click(spotifyLogout));
		else
			result.append($('<li>Spotify login</li>').click(spotifyLogin));
		return result;
	}
	
	function spotifyLogin() {
		$('<div title="Login">'
		 +'<table><tr><td>Username: </td><td> <input type="text" class="username" /> </td></tr>'
		 +'       <tr><td>Password: </td><td> <input type="password" class="password" /> </td></tr></table>'
		 +'</div>').dialog({"buttons": {"Ok": doSpotifyLogin, "Cancel": closeDialog}});
	}
	
	function doSpotifyLogin() {
		$.getJSON('SpotifyLogin', {"username": $(this).find(".username").val(), "password": $(this).find(".password").val()});
		close(this);
	}
	
	function spotifyLogout() {
		$.get('SpotifyLogout');
	}
	
	
	return {
		"update": update
	};
}();
var xxx;