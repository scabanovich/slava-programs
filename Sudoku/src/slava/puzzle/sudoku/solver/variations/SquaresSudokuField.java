package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.SudokuField;

public class SquaresSudokuField extends SudokuField {
	SquaresSudokuRestriction r;
	
	public SquaresSudokuField() {
		setWidth(9, false);
		r = new SquaresSudokuRestriction();		
		r.setField(this);
		addRestriction(r);
	}

	public String printSolution(int[] solution) {
		StringBuffer sb = new StringBuffer();
		sb.append(super.printSolution(solution));
		if(isProblem(solution)) {
			for (int p = 0; p < solution.length; p++) {
				String m = "";
				if(r.data[p][0] == 1 && r.data[p][1] == 1) {
					m = "x";
				} else if(r.data[p][0] == 1) {
					m = "r";
				} else if(r.data[p][1] == 1) {
					m = "d";
				} else {
					m = "-";
				}
				sb.append(" " + m);
				if(r.field.x(p) == r.field.getWidth() - 1) {
					sb.append("\n");
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

}
