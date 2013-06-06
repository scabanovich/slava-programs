package slava.puzzle.sudoku.ui;

import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import slava.puzzle.sudoku.model.*;
import slava.puzzle.sudoku.ui.action.*;
import slava.puzzle.template.action.PuzzleActionManager;
import slava.puzzle.template.gui.PuzzleFrame;

public class SudokuFrame {

	public static void main(String[] args) {
		PuzzleFrame frame = new PuzzleFrame();
		frame.getFrame().setTitle("Sudoku");
		SudokuModel model = new SudokuModel();
		PuzzleActionManager manager = frame.getActionManager();
		manager.initActions();
		manager.addAction("generate", new SudokuActionGenerate());
		manager.addAction("width", new SudokuActionSetWidth());
		manager.addAction("clean", new SudokuActionClean());
		manager.addAction("options", new SudokuPreferencesAction());
		manager.addAction("difference-one", new ActionMakeDifferenceOneRestriction());
		manager.addAction("less-greater", new ActionMakeLessOrGreaterRestriction());
		manager.addAction("X-V", new ActionMakeXVRestriction());
		manager.addAction("sums", new ActionMakePartitionSumsRestriction());
//		manager.addAction("set_problem", new CardnetActionSetProblem());
//		manager.addAction("set_generator", new CardnetActionSetGenInfo());
		manager.addAction("solve", new SudokuActionSolve());
		manager.addAction("solve_logically", new SudokuActionSolveLogically());
		frame.setModel(model);
		frame.setComponent(new SudokuComponent());
		frame.getMenuBar().createMenuItem("Edit", "Set width", "width", KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.ALT_MASK));
		frame.getMenuBar().createMenuItem("Edit", "Clean", "clean", KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.ALT_MASK));
		frame.getMenuBar().createMenuItem("Edit", "Options", "options", KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.ALT_MASK));
		frame.getMenuBar().createMenuItem("Edit", "Build Diff-One", "difference-one", null);
		frame.getMenuBar().createMenuItem("Edit", "Build Less-Greater", "less-greater", null);
		frame.getMenuBar().createMenuItem("Edit", "Build X-V", "X-V", null);
		frame.getMenuBar().createMenuItem("Edit", "Build Sums", "sums", null);
//		frame.getMenuBar().createMenuItem("Edit", "Set problem", "set_problem", null);
//		frame.getMenuBar().createMenuItem("Edit", "Set generator", "set_generator", null);
		frame.getMenuBar().createMenuItem("Run", "Solve Logically", "solve_logically", KeyStroke.getKeyStroke(KeyEvent.VK_F9, KeyEvent.SHIFT_MASK));
		frame.run();
	}

}
