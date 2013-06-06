package slava.puzzles.seabattlen.gui;

//import java.awt.event.KeyEvent;
//import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import slava.puzzle.template.action.PuzzleActionManager;
import slava.puzzle.template.gui.PuzzleFrame;
import slava.puzzles.seabattle.gui.action.SeaBattleActionSetWidth;
import slava.puzzles.seabattlen.gui.action.SeaBattleActionNumberedSolve;
import slava.puzzles.seabattlen.gui.action.SeaBattleNumberedActionGenerate;
import slava.puzzles.seabattlen.model.SeaBattleNumberedModel;

public class SeaBattleNumberedFrame {
	
	public static void main(String[] args) {
		PuzzleFrame frame = new PuzzleFrame();
		frame.getFrame().setTitle("Sea Battle With Numbers");
		PuzzleActionManager manager = frame.getActionManager();
		manager.initActions();
		manager.addAction("solve", new SeaBattleActionNumberedSolve());
		manager.addAction("generate", new SeaBattleNumberedActionGenerate());
//		manager.addAction("options", new SeaBattlePreferencesAction());
		manager.addAction("width", new SeaBattleActionSetWidth());

		SeaBattleNumberedModel model = new SeaBattleNumberedModel();
		frame.setModel(model);
		frame.setComponent(new SeaBattleNumberedComponent());
		frame.getMenuBar().createMenuItem("Edit", "Set width", "width", null);
//		frame.getMenuBar().createMenuItem("Edit", "Options", "options", KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.ALT_MASK));
		frame.run();
	}

}
