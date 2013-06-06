package slava.puzzle.ellen.gui;

import java.awt.event.*;
import javax.swing.KeyStroke;
import slava.puzzle.ellen.gui.action.*;
import slava.puzzle.ellen.model.*;
import slava.puzzle.template.action.*;
import slava.puzzle.template.gui.*;

public class EllenFrame {

	public static void main(String[] args) {
		PuzzleFrame frame = new PuzzleFrame();
		frame.getFrame().setTitle("Ellen");
		EllenModel model = new EllenModel();
		PuzzleActionManager manager = frame.getActionManager();
		manager.initActions();
		manager.addAction("solve", new EllenActionSolve());
		manager.addAction("generate", new EllenActionGenerate());
///		manager.addAction("generateLogical", new EllenActionGenerateLogical());
		manager.addAction("solveLogically", new EllenLogicalSolve());
		frame.setModel(model);
///		frame.getMenuBar().createMenuItem("Run", "Generate Logical", "generateLogical", null);
		frame.getMenuBar().createMenuItem("Run", "Solve Logically", "solveLogically", KeyStroke.getKeyStroke(KeyEvent.VK_F9, InputEvent.CTRL_MASK));
		frame.setComponent(new EllenComponent());
		frame.run();
	}

}
