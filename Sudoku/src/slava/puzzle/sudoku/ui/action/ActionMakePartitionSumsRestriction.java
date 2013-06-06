package slava.puzzle.sudoku.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import slava.puzzle.sudoku.model.SudokuDesignField;
import slava.puzzle.sudoku.model.SudokuModel;
import slava.puzzle.sudoku.model.SudokuProblemInfo;
import slava.puzzle.template.action.PuzzleAction;

public class ActionMakePartitionSumsRestriction extends PuzzleAction {

	public void actionPerformed(ActionEvent e) {
		SudokuModel model = (SudokuModel)manager.getModel();
		SudokuProblemInfo pi = model.getProblemInfo();
		int[] solution = pi.getSolution();
		if(solution == null) {
			JOptionPane.showMessageDialog(manager.getComponent(), "Please build a solution to some problem.", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
//		SudokuDesignField field = model.getField();
		int[] data = pi.getPartitioning();
		int max = -1;
		for (int p = 0; p < data.length; p++) if(data[p] > max) max = data[p];
		if(max < 0) {
			pi.setSums(new int[0]);
			return;
		}
		int[] sums = new int[max + 1];
		for (int p = 0; p < data.length; p++) {
			if(data[p] < 0) continue;
			sums[data[p]] += solution[p];
		}
		pi.setSums(sums);
		pi.setUsingPartitioningSumRestriction(true);
		manager.getComponent().repaint();
	}

}
