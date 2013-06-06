package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.AbstractSudokuField;
import slava.puzzle.sudoku.solver.SudokuField;

public class DoubleSudokuField extends AbstractSudokuField {

	public DoubleSudokuField() {
		init();
	}

	void init() {
		size = 81 * 2;
		SudokuField f = new SudokuField();
		f.setWidth(9, false);
		int[][] regions1 = f.getRegions();
		regions = new int[regions1.length * 2][9];
		for (int i = 0; i < regions1.length; i++) {
			for (int j = 0; j < 9; j++) {
				regions[i][j] = regions1[i][j];
				if(i + regions1.length < regions.length) {
					regions[i + regions1.length][j] = regions1[i][j] + 81;
				}
			}
		}
		buildPlaceToRegions();
		addRestriction(new DoubleSudokuRestriction());
	}

	public int getColorCount() {
		return 9;
	}

	public String printSolution(int[] solution) {
		StringBuffer sb = new StringBuffer();
		for (int p = 0; p < 81; p++) {
			sb.append(" ");
			char c = (solution[p] < 0) ? '+' : ("" + (solution[p] + 1)).charAt(0);
			sb.append(c);
			c = (solution[p + 81] < 0) ? '+' : ("" + (solution[p + 81] + 1)).charAt(0);
			sb.append(c);
			if(p % 9 == 8) sb.append("\n");
		}
		return sb.toString();
	}


}
