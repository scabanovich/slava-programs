package slava.puzzle.paths.walker.gui;

import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;

import slava.puzzle.paths.walker.gui.action.WalkerPathsActionGenerate;
import slava.puzzle.paths.walker.gui.action.WalkerPathsActionSetHeight;
import slava.puzzle.paths.walker.gui.action.WalkerPathsActionSetWidth;
import slava.puzzle.paths.walker.gui.action.WalkerPathsActionSolve;
import slava.puzzle.paths.walker.gui.action.WalkerPathsPreferencesAction;
import slava.puzzle.paths.walker.model.WalkerPathsModel;
import slava.puzzle.template.action.PuzzleActionManager;
import slava.puzzle.template.gui.PuzzleFrame;

public class WalkerPathsFrame {

	public WalkerPathsFrame() {}
	
	public static void main(String[] args) {
		PuzzleFrame frame = new PuzzleFrame();
		frame.getFrame().setTitle("Walker Paths");
		PuzzleActionManager manager = frame.getActionManager();
		manager.initActions();
		manager.addAction("solve", new WalkerPathsActionSolve());
		manager.addAction("generate", new WalkerPathsActionGenerate());
		manager.addAction("options", new WalkerPathsPreferencesAction());
		manager.addAction("width", new WalkerPathsActionSetWidth());
		manager.addAction("height", new WalkerPathsActionSetHeight());

		WalkerPathsModel model = new WalkerPathsModel();
		frame.setModel(model);
		frame.setComponent(new WalkerPathsComponent());
		frame.getMenuBar().createMenuItem("Edit", "Set width", "width", null);
		frame.getMenuBar().createMenuItem("Edit", "Set height", "height", null);
		frame.getMenuBar().createMenuItem("Edit", "Options", "options", KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.ALT_MASK));
		frame.run();
	}

}
