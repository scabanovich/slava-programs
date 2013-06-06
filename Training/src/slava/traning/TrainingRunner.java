package slava.traning;

import java.io.File;

import slava.traning.model.TrainingModel;
import slava.traning.ui.TrainingFrame;

public class TrainingRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TrainingModel model = new TrainingModel();
		model.setKeyName("State");
		model.setValueName("Capital");
		model.load(new File("./examples/states.properties"));
		TrainingFrame frame = new TrainingFrame();
		frame.setModel(model);
		frame.open();
		
	}

}
