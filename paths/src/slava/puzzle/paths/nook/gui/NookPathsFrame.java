package slava.puzzle.paths.nook.gui;

//import java.awt.event.KeyEvent;
//import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;

import slava.puzzle.paths.nook.gui.action.NookPathsActionGenerate;
import slava.puzzle.paths.nook.gui.action.NookPathsActionSetHeight;
import slava.puzzle.paths.nook.gui.action.NookPathsActionSetWidth;
import slava.puzzle.paths.nook.gui.action.NookPathsActionSolve;
import slava.puzzle.paths.nook.gui.action.NookPathsPreferencesAction;
import slava.puzzle.paths.nook.model.NookPathsModel;
import slava.puzzle.template.action.PuzzleActionManager;
import slava.puzzle.template.gui.PuzzleFrame;

public class NookPathsFrame {
	
	public static void main(String[] args) {
		PuzzleFrame frame = new PuzzleFrame();
		frame.getFrame().setTitle("Nook Paths");
		PuzzleActionManager manager = frame.getActionManager();
		manager.initActions();
		manager.addAction("solve", new NookPathsActionSolve());
		manager.addAction("generate", new NookPathsActionGenerate());
		manager.addAction("options", new NookPathsPreferencesAction());
		manager.addAction("width", new NookPathsActionSetWidth());
		manager.addAction("height", new NookPathsActionSetHeight());

		NookPathsModel model = new NookPathsModel();
		frame.setModel(model);
		frame.setComponent(new NookPathsComponent());
		frame.getMenuBar().createMenuItem("Edit", "Set width", "width", null);
		frame.getMenuBar().createMenuItem("Edit", "Set height", "height", null);
		frame.getMenuBar().createMenuItem("Edit", "Options", "options", KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.ALT_MASK));
		frame.run();
	}

}
