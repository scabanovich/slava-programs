package slava.puzzle.crossnumber.gui;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;
import slava.puzzle.crossnumber.CrossNumberModel;
import slava.puzzle.crossnumber.gui.action.*;
import slava.puzzle.template.action.PuzzleActionManager;
import slava.puzzle.template.gui.PuzzleFrame;

public class CrossNumberFrame {

	public static void main(String[] args) {
		PuzzleFrame frame = new PuzzleFrame();
		frame.getFrame().setTitle("Crossnumber");
		CrossNumberModel model = new CrossNumberModel();
		PuzzleActionManager manager = frame.getActionManager();
		manager.initActions();
		manager.addAction("generate", new CrossNumberActionGenerate());
		manager.addAction("solve", new CrossNumberActionSolve());
		manager.addAction("solve_logically", new CrossNumberActionSolveLogically());
		manager.addAction("width", new CrossNumberActionSetWidth());
		manager.addAction("height", new CrossNumberActionSetHeight());
		manager.addAction("options", new CrossNumberPreferencesAction());
		frame.setModel(model);
		frame.setComponent(new CrossNumberComponent());
		frame.getMenuBar().createMenuItem("Run", "Solve Logically", "solve_logically", KeyStroke.getKeyStroke(KeyEvent.VK_F9, KeyEvent.SHIFT_MASK));
		frame.getMenuBar().createMenuItem("Edit", "Set width", "width", null);
		frame.getMenuBar().createMenuItem("Edit", "Set height", "height", null);
		frame.getMenuBar().createMenuItem("Edit", "Options", "options", KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.ALT_MASK));
		frame.getMenuBar().createMenuItem("Edit", "Undo", "undo", KeyStroke.getKeyStroke("control Z"));
		frame.getMenuBar().createMenuItem("Edit", "Redo", "redo", KeyStroke.getKeyStroke("control Y"));
		frame.run();
	}

}
