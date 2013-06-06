package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.SudokuField;

public class PawnSudokuField extends SudokuField {
	PawnSudokuRestriction r;
	
	public PawnSudokuField(int[][] data) {
		setWidth(9, false);
		if(data == null) {
			r = new PawnSudokuGeneratedRestriction();
		} else {
			r = new PawnSudokuRestriction();
			r.setData(data);
		}
		r.setField(this);
		addRestriction(r);
	}

	public String printSolution(int[] solution) {
		StringBuffer sb = new StringBuffer();
		sb.append(super.printSolution(solution));
		if(isProblem(solution)) {
			int total = 0;
			for (int p = 0; p < solution.length; p++) {
				for (int d = 0; d < 4; d++) {
					int q = r.pawnField.jump(p, d);
					if(q >= p && r.data[p][d] == 1) {
						writeCoordinates(p, sb);
						writeCoordinates(q, sb);
						sb.append(",");
						total++;
					}
				}
			}
			sb.append(" total=" + total);
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
		char c = (char)(97 + r.pawnField.getX(p));
		sb.append(c).append("" + (r.pawnField.getY(p) + 1));
	}

}
