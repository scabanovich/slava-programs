package slava.puzzles.seabattle.gui;

//import java.awt.event.KeyEvent;
//import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import slava.puzzle.template.action.PuzzleActionManager;
import slava.puzzle.template.gui.PuzzleFrame;
import slava.puzzles.seabattle.gui.action.SeaBattleActionGenerate;
import slava.puzzles.seabattle.gui.action.SeaBattleActionSetWidth;
import slava.puzzles.seabattle.gui.action.SeaBattleActionSolve;
import slava.puzzles.seabattle.gui.action.SeaBattlePreferencesAction;
import slava.puzzles.seabattle.model.SeaBattleModel;

public class SeaBattleFrame {
	
	public static void main(String[] args) {
		PuzzleFrame frame = new PuzzleFrame();
		frame.getFrame().setTitle("Sea Battle");
		PuzzleActionManager manager = frame.getActionManager();
		manager.initActions();
		manager.addAction("solve", new SeaBattleActionSolve());
		manager.addAction("generate", new SeaBattleActionGenerate());
		manager.addAction("options", new SeaBattlePreferencesAction());
		manager.addAction("width", new SeaBattleActionSetWidth());

		SeaBattleModel model = new SeaBattleModel();
		frame.setModel(model);
		frame.setComponent(new SeaBattleComponent());
//		frame.getMenuBar().createMenuItem("Edit", "Draw field", "set_drawing", KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
//		frame.getMenuBar().createMenuItem("Edit", "Make puzzle", "set_puzzle", KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
		frame.getMenuBar().createMenuItem("Edit", "Set width", "width", null);
		frame.getMenuBar().createMenuItem("Edit", "Options", "options", KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.ALT_MASK));
		frame.run();
	}

}
