package slava.puzzle.avoidthree.gui;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import slava.puzzle.avoidthree.gui.action.AvoidThreeAndKnightActionSetDrawing;
import slava.puzzle.avoidthree.gui.action.AvoidThreeAndKnightActionSetPuzzle;
import slava.puzzle.avoidthree.model.AvoidThreeAndKnightModel;
import slava.puzzle.template.action.PuzzleActionManager;
import slava.puzzle.template.gui.PuzzleFrame;

public class AvoidThreeAndKnightFrame {

	public static void main(String[] args) {
		PuzzleFrame frame = new PuzzleFrame();
		frame.getFrame().setTitle("Avoid Three");
		PuzzleActionManager manager = frame.getActionManager();
		manager.initActions();
		manager.addAction("set_puzzle", new AvoidThreeAndKnightActionSetPuzzle());
		manager.addAction("set_drawing", new AvoidThreeAndKnightActionSetDrawing());
		AvoidThreeAndKnightModel model = new AvoidThreeAndKnightModel();
		frame.setModel(model);
		frame.setComponent(new AvoidThreeAndKnightComponent());
		frame.getMenuBar().createMenuItem("Edit", "Draw field", "set_drawing", KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
		frame.getMenuBar().createMenuItem("Edit", "Make puzzle", "set_puzzle", KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
		frame.run();
	}

}
