package spotify;

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
	
	public boolean waitFor(int maxWait) {
		for (int i = maxWait * 100; i > 0; i--) {
			if (complete) return true;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				break;
			}
		}
		return complete;
	}
}
