package slava.puzzle.cardnet.gui;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import slava.puzzle.cardnet.gui.action.*;
import slava.puzzle.cardnet.model.CardnetModel;
import slava.puzzle.template.action.PuzzleActionManager;
import slava.puzzle.template.gui.*;

public class CardnetFrame {

	public static void main(String[] args) {
		PuzzleFrame frame = new PuzzleFrame();
		frame.getFrame().setTitle("Cards");
		CardnetModel model = new CardnetModel();
		PuzzleActionManager manager = frame.getActionManager();
		manager.initActions();
		manager.addAction("generate", new CardnetActionGenerate());
		manager.addAction("set_problem", new CardnetActionSetProblem());
		manager.addAction("set_generator", new CardnetActionSetGenInfo());
		manager.addAction("solve", new CardnetActionSolve());
		manager.addAction("solve_logically", new CardnetActionSolveLogically());
		frame.setModel(model);
		frame.setComponent(new CardnetComponent());
		frame.getMenuBar().createMenuItem("Edit", "Set problem", "set_problem", null);
		frame.getMenuBar().createMenuItem("Edit", "Set generator", "set_generator", null);
		frame.getMenuBar().createMenuItem("Run", "Solve Logically", "solve_logically", KeyStroke.getKeyStroke(KeyEvent.VK_F9, KeyEvent.SHIFT_MASK));
		frame.run();
	}
}
