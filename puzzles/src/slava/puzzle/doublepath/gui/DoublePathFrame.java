package slava.puzzle.doublepath.gui;

import slava.puzzle.doublepath.gui.action.*;
import slava.puzzle.doublepath.model.DoublePathModel;
import slava.puzzle.template.action.PuzzleActionManager;
import slava.puzzle.template.gui.PuzzleFrame;

public class DoublePathFrame {

	public static void main(String[] args) {
		PuzzleFrame frame = new PuzzleFrame();
		frame.getFrame().setTitle("Double Path");
		DoublePathModel model = new DoublePathModel();
		PuzzleActionManager manager = frame.getActionManager();
		manager.initActions();
		manager.addAction("solve", new DoublePathActionSolve());
		manager.addAction("width", new DoublePathActionSetWidth());
		manager.addAction("height", new DoublePathActionSetHeight());
		frame.setModel(model);
		frame.setComponent(new DoublePathComponent());
		frame.getMenuBar().createMenuItem("Edit", "Set width", "width", null);
		frame.getMenuBar().createMenuItem("Edit", "Set height", "height", null);
		frame.run();
	}

}
