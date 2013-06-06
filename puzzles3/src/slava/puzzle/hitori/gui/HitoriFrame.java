package slava.puzzle.hitori.gui;

import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import slava.puzzle.hitori.gui.action.*;
import slava.puzzle.hitori.model.HitoriModel;
import slava.puzzle.template.action.PuzzleActionManager;
import slava.puzzle.template.gui.PuzzleFrame;

public class HitoriFrame {

	public static void main(String[] args) {
		PuzzleFrame frame = new PuzzleFrame();
		frame.getFrame().setTitle("Hitori");
		HitoriModel model = new HitoriModel();
		PuzzleActionManager manager = frame.getActionManager();
		manager.initActions();
		manager.addAction("solve", new HitoriActionSolve());
		manager.addAction("generate", new HitoriActionGenerate());
		manager.addAction("width", new HitoriActionSetWidth());
		manager.addAction("options", new HitoriPreferencesAction());
//		manager.addAction("solveLogically", new PentaLettersLogicalSolve());
		frame.setModel(model);
//		frame.getMenuBar().createMenuItem("Run", "Solve Logically", "solveLogically", KeyStroke.getKeyStroke(KeyEvent.VK_F9, InputEvent.CTRL_MASK));
		frame.getMenuBar().createMenuItem("Edit", "Set width", "width", null);
		frame.getMenuBar().createMenuItem("Edit", "Options", "options", KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.ALT_MASK));
		frame.setComponent(new HitoriComponent());
		frame.run();
	}

}
