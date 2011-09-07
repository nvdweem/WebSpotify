package spotify;

import java.io.InputStream;
import java.nio.ByteBuffer;

import com.vdweem.webspotify.Main;

/**
 * Holds image data.
 * @author Niels
 */
public class Image extends Completable {
	private final Type type;
	private byte[] bytes;
	
	public enum Type {
		artist, album
	}
	
	public Image(Type type) {
		this.type = type;
	}
	
	public void unComplete() {
		if (bytes == null || bytes.length == 0) 
			setComplete(false);
	}
	
	public void setImage(byte[] bytes) {
		this.bytes = bytes;
	}
	
	public byte[] getBytes() {
		if (bytes == null || bytes.length == 0) return defaultImage();
		return bytes;
	}
	
	/**
	 * Returns the image data or a placeholder when no data is available.
	 * @return
	 */
	private byte[] defaultImage() {
		InputStream in = null;
		
		switch (type) {
			case album: in = Main.class.getResourceAsStream("resources/images/noAlbum.png"); break;
			case artist: in = Main.class.getResourceAsStream("resources/images/noArtist.png"); break;
		}
		if (in == null) return new byte[0];
		
		ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
		byte[] buff = new byte[1024];
		int read;
		try {
			while ((read = in.read(buff)) != -1)
				buffer.put(buff, 0, read);
		} catch (Exception e) {
			System.err.println("Cant read default image.");
			return null;
		}
		return buffer.array();
	}
}
