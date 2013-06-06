package slava.puzzle.avoidfour.gui;

import java.awt.event.*;
import javax.swing.KeyStroke;
import slava.puzzle.avoidfour.gui.action.*;
import slava.puzzle.avoidfour.model.AvoidFourModel;
import slava.puzzle.template.action.*;
import slava.puzzle.template.gui.*;

public class AvoidFourFrame {

	public static void main(String[] args) {
		PuzzleFrame frame = new PuzzleFrame();
		frame.getFrame().setTitle("Cards");
		PuzzleActionManager manager = frame.getActionManager();
		manager.initActions();
		manager.addAction("set_puzzle", new AvoidFourActionSetPuzzle());
		manager.addAction("set_drawing", new AvoidFourActionSetDrawing());
		AvoidFourModel model = new AvoidFourModel();
		frame.setModel(model);
		frame.setComponent(new AvoidFourComponent());
		frame.getMenuBar().createMenuItem("Edit", "Draw field", "set_drawing", KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
		frame.getMenuBar().createMenuItem("Edit", "Make puzzle", "set_puzzle", KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
		frame.run();

	}

}
