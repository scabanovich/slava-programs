package slava.puzzle.pentasets.gui;

import slava.puzzle.pentaletters.gui.action.PentaLettersActionSetHeight;
import slava.puzzle.pentaletters.gui.action.PentaLettersActionSetWidth;
import slava.puzzle.pentasets.gui.action.*;
import slava.puzzle.pentasets.model.PentaSetsModel;
import slava.puzzle.template.action.PuzzleActionManager;
import slava.puzzle.template.gui.PuzzleFrame;

public class PentaSetsFrame {

	public static void main(String[] args) {
		PuzzleFrame frame = new PuzzleFrame();
		frame.getFrame().setTitle("1-2-3-4-5 in Pentamino");
		PentaSetsModel model = new PentaSetsModel();
		PuzzleActionManager manager = frame.getActionManager();
		manager.initActions();
		manager.addAction("solve", new PentaSetsActionSolve());
		manager.addAction("generate", new PentaSetsActionGenerate());
///		manager.addAction("generateLogical", new PentaLettersActionGenerateLogical());
///		manager.addAction("solveLogically", new PentaLettersLogicalSolve());
		manager.addAction("width", new PentaLettersActionSetWidth());
		manager.addAction("height", new PentaLettersActionSetHeight());
		frame.setModel(model);
///		frame.getMenuBar().createMenuItem("Run", "Generate Logical", "generateLogical", null);
///		frame.getMenuBar().createMenuItem("Run", "Solve Logically", "solveLogically", KeyStroke.getKeyStroke(KeyEvent.VK_F9, InputEvent.CTRL_MASK));
		frame.getMenuBar().createMenuItem("Edit", "Set width", "width", null);
		frame.getMenuBar().createMenuItem("Edit", "Set height", "height", null);
		frame.setComponent(new PentaSetsComponent());
		frame.run();
	}


}
