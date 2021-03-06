package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.restriction.IGeneratedRestriction;

public class PawnSudokuGeneratedRestriction extends PawnSudokuRestriction implements IGeneratedRestriction {
	int[] generated;
	
	public PawnSudokuGeneratedRestriction() {}

	public void reset() {
		generated = null;
		state = null;
		data = null;
	}

	public void setGeneratedSolution(int[] generated) {
		this.generated = generated;
		state = new int[f.getSize()];
		data = new int[f.getSize()][PAWN_ORTS.length];
		for (int p = 0; p < data.length; p++) {
			for (int d = 0; d < PAWN_ORTS.length; d++) {
				int q = pawnField.jump(p, d);
				if(q >= 0 && generated[q] == generated[p]) {
					data[p][d] = 1;
				}
			}
		}
		
	}

}
