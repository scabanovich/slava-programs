package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.restriction.IGeneratedRestriction;

public class KnightSudokuGeneratedRestriction extends KnightSudokuRestriction implements IGeneratedRestriction {
	int[] generated;
	
	public KnightSudokuGeneratedRestriction() {}

	public void reset() {
		generated = null;
		state = null;
		data = null;
	}

	public void setGeneratedSolution(int[] generated) {
		this.generated = generated;
		state = new int[f.getSize()];
		data = new int[f.getSize()][KNIGHT_ORTS.length];
		for (int p = 0; p < data.length; p++) {
			for (int d = 0; d < KNIGHT_ORTS.length; d++) {
				int q = knightField.jump(p, d);
				if(q >= 0 && generated[q] == generated[p]) {
					data[p][d] = 1;
				}
			}
		}
		
	}

}
