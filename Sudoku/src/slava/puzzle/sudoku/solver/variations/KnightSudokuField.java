package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.SudokuField;

public class KnightSudokuField extends SudokuField {
	KnightSudokuRestriction r;
	
	public KnightSudokuField(int[][] data) {
		setWidth(9, false);
		if(data == null) {
			r = new KnightSudokuGeneratedRestriction();
		} else {
			r = new KnightSudokuRestriction();
			r.setData(data);
		}
		r.setField(this);
		addRestriction(r);
	}

	public String printSolution(int[] solution) {
		StringBuffer sb = new StringBuffer();
		sb.append(super.printSolution(solution));
		if(isProblem(solution)) {
			for (int p = 0; p < solution.length; p++) {
				for (int d = 0; d < 4; d++) {
					int q = r.knightField.jump(p, d);
					if(q >= 0 && r.data[p][d] == 1) {
						writeCoordinates(p, sb);
						writeCoordinates(q, sb);
						sb.append(",");
					}
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	boolean isProblem(int[] s) {
		for (int i = 0; i < s.length; i++) {
			if(s[i] < 0) return true;
		}
		return false;
	}
	
	void writeCoordinates(int p, StringBuffer sb) {
		char c = (char)(97 + r.knightField.getX(p));
		sb.append(c).append("" + (r.knightField.getY(p) + 1));
	}

}
