package slava.puzzles.hexa.rook.gui;

//import java.awt.event.KeyEvent;
//import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import slava.puzzle.template.action.PuzzleActionManager;
import slava.puzzle.template.gui.PuzzleFrame;
import slava.puzzles.hexa.rook.gui.action.HexaRookActionClean;
import slava.puzzles.hexa.rook.gui.action.HexaRookActionSetWidth;
import slava.puzzles.hexa.rook.gui.action.HexaRookActionSolve;
import slava.puzzles.hexa.rook.model.HexaRookModel;

public class HexaRookFrame {
	
	public static void main(String[] args) {
		PuzzleFrame frame = new PuzzleFrame();
		frame.getFrame().setTitle("Hexagonal Rook");
		PuzzleActionManager manager = frame.getActionManager();
		manager.initActions();
		manager.addAction("solve", new HexaRookActionSolve());
//		manager.addAction("generate", new SeaBattleActionGenerate());
//		manager.addAction("options", new SeaBattlePreferencesAction());
		manager.addAction("width", new HexaRookActionSetWidth());
		manager.addAction("clean", new HexaRookActionClean());

		HexaRookModel model = new HexaRookModel();
		frame.setModel(model);
		frame.setComponent(new HexaRookComponent());
		frame.getMenuBar().createMenuItem("Edit", "Set width", "width", null);
		frame.getMenuBar().createMenuItem("Edit", "Clean", "clean", null);
//		frame.getMenuBar().createMenuItem("Edit", "Options", "options", KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.ALT_MASK));
		frame.run();
	}

}
