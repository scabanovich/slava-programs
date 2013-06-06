package slava.puzzle.domino.gui;

import slava.puzzle.domino.gui.action.DominoActionGenerate;
import slava.puzzle.domino.model.DominoModel;
import slava.puzzle.template.action.PuzzleActionManager;
import slava.puzzle.template.gui.PuzzleFrame;

public class DominoFrame {

	public static void main(String[] args) {
		PuzzleFrame frame = new PuzzleFrame();
		frame.getFrame().setTitle("Domino");
		DominoModel model = new DominoModel();
		PuzzleActionManager manager = frame.getActionManager();
		manager.initActions();
		manager.addAction("generate", new DominoActionGenerate());
		manager.removeAction("solve");
		frame.setModel(model);
		frame.setComponent(new DominoComponent());
		frame.run();
	}

}
