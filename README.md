# WebSpotify

## About

The goal of this project is to create a web based Spotify client which can be
used with more than one person in a room. The host can specify a playlist
which should be played, and everybody can add requests to that playlist. This
way it will be possible to have Spotify running during parties or at the office.

## Status

The project uses an altered version the Jah'Spotify api, services and native part. The changes
have made the Jah'Spotify library work under all major Operating Systems. These are Windows,
Linux and Mac OSX.
To compile it you need to have my Jah'Spotify fork in the same folder structure as WebSpotify:
    /
      jahspotify/
      	pom.xml
      WebSpotify
        pom.xml

## Compatibility

The tool is verified to compile and run with the following operating systems:
- Windows 7
- Ubuntu 11.04
- OSX Lion

## Building

Just use Maven to build it. To run it you need to have the jahspotify native and its dependencies 
in your path.
