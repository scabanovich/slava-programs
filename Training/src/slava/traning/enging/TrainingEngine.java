package slava.traning.enging;

import slava.traning.model.TrainingModel;
import slava.traning.model.TrainingPair;

public class TrainingEngine implements Runnable {
	TrainingModel model;
	TrainingListener listener;
	Thread thread;
	boolean checkNow = false;
	
	int success = 0;
	int failure = 0;

	public TrainingEngine() {}

	public void setModel(TrainingModel model) {
		this.model = model;
	}

	public void setListener(TrainingListener listener) {
		this.listener = listener;
	}

	public void start() {
		thread = new Thread(this);
		thread.setName("Training");
		thread.start();
	}

	public void run() {
		try {
			checkNow = false;
			TrainingPair p = model.getRandomPair();
			listener.setPair(p.getKey(), "");
			listener.setTimer(20);
			for (int i = 19; i > 0 && !checkNow; i--) { 
				Thread.sleep(1000);
				listener.setTimer(i);
			}
			listener.checkValue(p.getValue());
		} catch (Exception e) {
			return;
		} finally {
			thread = null;
			checkNow = false;
		}
	}

	public void checkNow() {
		checkNow = true;
	}

	public boolean isRunning() {
		return thread != null;
	}

	public int getSuccessCount() {
		return success;
	}

	public int getFailureCount() {
		return failure;
	}

	public void success() {
		success++;
	}

	public void failure() {
		failure++;
	}

}
