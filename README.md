# WebSpotify

## About

The goal of this project is to create a web based Spotify client which can be
used with more than one person in a room. The host can specify a playlist
which should be played, and everybody can add requests to that playlist. This
way it will be possible to have Spotify running during parties or at the office.

## How to use

WebSpotify compiles to a Java web application. This means it can be deployed to,
for example, a Tomcat installation.

If you run Tomcat, you should make sure that the libJahSpotify dll/so/jnilib ant its
dependencies are in your path.
While running Tomcat you should provide your Spotify username and password:

    -Djahspotify.spotify.username=[your username] -Djahspotify.spotify.password=[your password]

After starting Tomcat, the application should load. You should then be able to access
it through your browser. If you know how to use Spotify you should be ok with using
WebSpotify.

### Adding songs to the queue

WebSpotify uses 2 queue types. The default action is to add tracks to the 'request' queue.
Songs added to this queue will appear yellow in the 'Play queue'.

The other queue can be filled by opening a playlist, and clicking the music note next to
the playlist name (right next to the playlist image).

This way, one can add a big playlist to play tracks when nobody had requests, and all
requested tracks will go to the top of the playlist.

### Keyboard shortcuts

Some actions are easier with a keyboard, and at the moment some actions can only be done using one:
* Up/down keys can be used to change the track selection. Holding shift expands or collapses
the selection.
* Holding shift while clicking a track in a list will select all tracks between the current selection
and the new one.
* Clicking while holding control (or option) toggles the selection of tracks without clearing the
current selection.
* Enter adds the current selection to the queue.
* Control + S puts focus on the search field.
* While watching the playlist, pressing delete or backspace will remove the selected items from the queue
* ctrl + alt + d shows a popup asking to delete all tracks from the queue.
* ctrl + , invokes the previous button.
* ctrl + . invokes the next button
* ctrl + / invokes the pause button
* ctrl or alt + up/down changes the volume 

## Status

The application is fully usable and is nearly completely feature compatible with the
Spotify desktop client.

The project uses the libJahSpotify library. This library can be found here:

     https://github.com/nvdweem/libjahspotify.git

Compile instructions can be found in the readme of that repository.

To compile WebSpotify you need to have libJahSpotify in the same folder structure as WebSpotify:

    /
      libjahspotify/
        pom.xml
      WebSpotify/
        pom.xml

## Compatibility

The tool is verified to compile and run with the following operating systems:
- Windows 7
- Ubuntu 11.04
- OSX Lion

## Building

Just use Maven to build it. To run it you need to have the jahspotify native and its dependencies 
in your path.
