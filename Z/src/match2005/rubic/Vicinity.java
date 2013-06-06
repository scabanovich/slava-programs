package match2005.rubic;

import java.io.File;
import java.io.FileInputStream;

public class Vicinity {
	VicinityTask task;
	byte[] states;
	public Vicinity(VicinityTask task) {
		this.task = task;
		try {
			states = loadStates(task.file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static byte[] loadStates(String fileName) throws Exception {
		byte[] states = null;
		FileInputStream is = new FileInputStream(fileName);
		states = new byte[(int)(new File(fileName).length())];
		int i = 0;
		while(i < states.length) {
			int di = is.read(states, i, states.length - i);
			if(di < 0) break;
			i += di;
		}
		System.out.println("States loaded from " + fileName);
		return states;
	}

}
