package spotify;

/**
 * Provides functions to determine if an object is completed.
 * @author Niels
 */
public class Completable {
	private boolean complete;
	
	public Completable() {
		this.complete = false;
	}
	
	public void setComplete() {
		complete = true;
	}
	
	public boolean isComplete() {
		return complete;
	}
	
	/**
	 * Wait for maxWait * 10 seconds or until the object is marked complete.
	 * @param maxWait
	 * @return
	 */
	public boolean waitFor(int maxWait) {
		for (int i = maxWait; i > 0; i--) {
			if (isComplete()) return true;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				break;
			}
		}
		return complete;
	}
}
