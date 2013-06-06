package slava.puzzle.sudoku.ui.action;

import java.awt.event.ActionEvent;

import slava.puzzle.sudoku.model.SudokuModel;
import slava.puzzle.sudoku.model.SudokuProblemInfo;
import slava.puzzle.sudoku.ui.SudokuComponent;
import slava.puzzle.template.action.PuzzleAction;

public class SudokuActionClean extends PuzzleAction {

	public void actionPerformed(ActionEvent e) {
		SudokuModel model = (SudokuModel)manager.getModel();
		SudokuProblemInfo pi = model.getProblemInfo();
		pi.setSolution(null);
		model.setSolutionInfo(null);
		int[] ns = pi.getNumbers();
		for (int i = 0; i < ns.length; i++) {
			ns[i] = 0;
		}
		manager.getComponent().repaint();
	}

}
