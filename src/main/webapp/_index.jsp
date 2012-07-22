<!DOCTYPE html>
<html>
  <head>
  	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="shortcut icon" type="image/ico" href="favicon.ico"/>
    <title> WebSpotify </title>
    
    <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
    <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jqueryui/1.8.18/jquery-ui.min.js"></script>
    <script type="text/javascript" src="js/jquery.lazyload.mini.js"></script>
    <script type="text/javascript" src="js/jquery.contextMenu.js"></script>
    <script type="text/javascript" src="js/jquery.history.js"></script>
    <script type="text/javascript" src="js/keymaster.js"></script>
    
    <link rel="stylesheet" type="text/css" href="css/jquery.contextMenu.css" />
    <link rel="stylesheet" type="text/css" href="css/style.css" />
    <link rel="stylesheet" type="text/css" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/themes/base/jquery-ui.css" />
    
    <script type="text/javascript" src="js/actions.js"></script>
    <script type="text/javascript" src="js/contextMenu.js"></script>
    <script type="text/javascript" src="js/ajax.js"></script>
    <script type="text/javascript" src="js/media.js"></script>
    <script type="text/javascript" src="js/queue.js"></script>
    <script type="text/javascript" src="js/toplist.js"></script>
    <script type="text/javascript" src="js/playlist.js"></script>
    <script type="text/javascript" src="js/artistbrowse.js"></script>
    <script type="text/javascript" src="js/albumbrowse.js"></script>
    <script type="text/javascript" src="js/search.js"></script>
    <script type="text/javascript" src="js/player.js"></script>
    <script type="text/javascript" src="js/volume.js"></script>
    <script type="text/javascript" src="js/status.js"></script>
    <script type="text/javascript">
      $(function() {
    	  ${invocation}
      });
    </script>
  </head>
  <body>
    <div id="topbar">
      <div id="applicationTitle"> WebSpotify </div>
      <div id="menu"></div>
      <div id="search">
        <div class="searchLeft"></div><div class="searchRight"></div><input type="text" class="search" />
      </div>
    </div>
    <div id="leftbar">
      <div id="lists">
        <ol id="queue">
          <li>Play queue</li>
        </ol>
      </div>
      <div id="playing">
        <div id="playingArt">
        </div>
        <div id="playingText">
      	</div>
      </div>
    </div>
    <div id="center">
    </div>
    <div id="controls">
      <div id="buttons">
        <div id="back"></div>
        <div id="playpause"></div>
        <div id="skip"></div>
      </div>
      
      <div id="volume">
      	<div class="left"></div>
    	<div class="position"></div>
    	<div class="right"></div>
      </div>
      <div id="volumeImg"></div>
      
      <div id="progress">
    	<div id="played"></div>
    	<div id="position"></div>
    	<div id="play"></div>
      </div>
      <div id="shuffle"></div>
    </div>
  </body>
</html>