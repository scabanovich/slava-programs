package slava.puzzles.seabattlef.gui;

//import java.awt.event.KeyEvent;
//import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import slava.puzzle.template.action.PuzzleActionManager;
import slava.puzzle.template.gui.PuzzleFrame;
import slava.puzzles.seabattle.gui.action.SeaBattleActionSetWidth;
import slava.puzzles.seabattlef.gui.action.SeaBattleFirstActionGenerate;
import slava.puzzles.seabattlef.gui.action.SeaBattleFirstActionSolve;
import slava.puzzles.seabattlef.model.SeaBattleFirstModel;

public class SeaBattleFirstFrame {
	
	public static void main(String[] args) {
		PuzzleFrame frame = new PuzzleFrame();
		frame.getFrame().setTitle("Sea Battle First");
		PuzzleActionManager manager = frame.getActionManager();
		manager.initActions();
		manager.addAction("solve", new SeaBattleFirstActionSolve());
		manager.addAction("generate", new SeaBattleFirstActionGenerate());
//		manager.addAction("options", new SeaBattlePreferencesAction());
		manager.addAction("width", new SeaBattleActionSetWidth());

		SeaBattleFirstModel model = new SeaBattleFirstModel();
		frame.setModel(model);
		frame.setComponent(new SeaBattleFirstComponent());
		frame.getMenuBar().createMenuItem("Edit", "Set width", "width", null);
///		frame.getMenuBar().createMenuItem("Edit", "Options", "options", KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.ALT_MASK));
		frame.run();
	}

}
