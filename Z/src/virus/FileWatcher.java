package virus;

import java.io.File;
import java.util.Date;

public class FileWatcher {
	String path = "/home/slava/Documents/мука.txt";

	public FileWatcher() {
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void deploy() {
		System.out.println("Monitoring " + path);
		Thread t = new Thread(new R(), "File Watcher: " + path);
		t.start();
		
	}

	class R implements Runnable {
		public void run() {
			while(true) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {}
				File f = new File(path);
				if(f.isFile()) {
					System.out.println("Created at " + new Date(f.lastModified()));
					System.out.println("Detected at " + new Date(System.currentTimeMillis()));
					f.delete();
				}
			}
		}
	}

	public static void main(String[] args) {
		new FileWatcher().deploy();
	}
}
