package spotify;

/**
 * Processes the spotify events.
 * @author Niels
 */
public class EventProcessor extends Thread {

	private boolean running = true;
	
	public void run() {
		doRun();
	}
	
	public synchronized void processEventsNow() {
		notify();
	}
	
	private synchronized void doRun() {
		while(running) {
			int waitFor = processEvents();
			try {
				wait(waitFor);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private int processEvents() {
		return Session.getInstance().ProcessEvents();
	}
	
}
