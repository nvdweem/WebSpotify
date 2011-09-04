package com.vdweem.webspotify;

import spotify.PlaylistListenerImpl;
import spotify.Search;
import spotify.Session;
import spotify.SessionListenerImpl;

/**
 * File to perform some tests.
 * @author Niels
 *
 */
public class Test {
	public static void main(String[] args) throws InterruptedException {
		Session session = Session.getInstance();
		session.initialize(new SessionListenerImpl(), new PlaylistListenerImpl());
		
		Thread.sleep(1000);
		
		Search search = session.search("slipknot", 10, 10, 10);
		search.waitFor(10);
		
		System.out.println(search.getAlbums().get(1));
		System.out.println(search.getArtists().get(0));
		
//		Image albumImage = session.readAlbumImage(search.getAlbums().get(0).getId());
//		Image artistImage = session.readArtistImage(search.getArtists().get(0).getId());
//		
//		Player player = new Player(session);
//		player.play(search.getTracks().get(0));
//		System.out.println(search);
		
		//spotify:artist:05fG473iIaoy82BF1aGhL8
//		Thread.sleep(10000);
	}
}
