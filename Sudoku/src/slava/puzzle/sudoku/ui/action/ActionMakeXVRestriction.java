package slava.puzzle.sudoku.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import slava.puzzle.sudoku.model.SudokuDesignField;
import slava.puzzle.sudoku.model.SudokuModel;
import slava.puzzle.sudoku.model.SudokuProblemInfo;
import slava.puzzle.template.action.PuzzleAction;

public class ActionMakeXVRestriction extends PuzzleAction {

	public void actionPerformed(ActionEvent e) {
		SudokuModel model = (SudokuModel)manager.getModel();
		SudokuProblemInfo pi = model.getProblemInfo();
		int[] solution = pi.getSolution();
		if(solution == null) {
			JOptionPane.showMessageDialog(manager.getComponent(), "Please build a solution to some problem.", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		SudokuDesignField field = model.getField();
		int[][] data = pi.getXVData();
		for (int p = 0; p < data.length; p++) {
			for (int d = 0; d < 4; d++) {
				int q = field.jp(p, d);
				data[p][d] = (q < 0) ? 0 : 
						(solution[q] + solution[p] == 5) ? 1 
						: (solution[q] + solution[p] == 10) ? 2 : 0;
			}
		}
		manager.getComponent().repaint();
	}

}
