package slava.puzzle.stars.gui;

import slava.puzzle.stars.gui.action.*;
import slava.puzzle.stars.model.*;
import slava.puzzle.template.action.*;
import slava.puzzle.template.gui.*;

public class StarsFrame {

	public static void main(String[] args) {
		PuzzleFrame frame = new PuzzleFrame();
		frame.getFrame().setTitle("Stars");
		StarsModel model = new StarsModel();
		PuzzleActionManager manager = frame.getActionManager();
		manager.initActions();
		manager.addAction("solve", new StarsActionSolve());
		manager.addAction("generate", new StarsActionGenerate());
		manager.addAction("width", new StarsActionSetWidth());
//		manager.addAction("generateLogical", new PentaLettersActionGenerateLogical());
//		manager.addAction("solveLogically", new PentaLettersLogicalSolve());
		frame.setModel(model);
//		frame.getMenuBar().createMenuItem("Run", "Generate Logical", "generateLogical", null);
//		frame.getMenuBar().createMenuItem("Run", "Solve Logically", "solveLogically", KeyStroke.getKeyStroke(KeyEvent.VK_F9, InputEvent.CTRL_MASK));
		frame.getMenuBar().createMenuItem("Edit", "Set width", "width", null);
		frame.setComponent(new StarsComponent());
		frame.run();
	}
}
