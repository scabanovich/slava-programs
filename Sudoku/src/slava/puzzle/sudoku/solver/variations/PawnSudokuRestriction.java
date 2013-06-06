package slava.puzzle.sudoku.solver.variations;

import com.slava.common.TwoDimField;

import slava.puzzle.sudoku.model.SudokuDesignField;
import slava.puzzle.sudoku.solver.ISudokuField;
import slava.puzzle.sudoku.solver.restriction.IRestriction;
import slava.puzzle.sudoku.solver.restriction.IRestrictionListener;

public class PawnSudokuRestriction implements IRestriction {
	static int[][] PAWN_ORTS = {
		{1,1}, {1,-1}, {-1,-1}, {-1,1}
	};
	ISudokuField f;
	TwoDimField pawnField;

	SudokuDesignField field;

	int[][] data; //[p][d] 0: is not equal, 1: is equal
	
	int[] state;
	
	public PawnSudokuRestriction() {
		pawnField = new TwoDimField();
		pawnField.setOrts(PAWN_ORTS);
		pawnField.setSize(9, 9);
	}
	
	public void setData(int[][] data) {
		this.data = data;
	}

	public void setField(ISudokuField f) {
		this.f = f;
		field = new SudokuDesignField();
		field.setSize(f.getColorCount());
	}

	public void init(IRestrictionListener listener) {
		state = new int[f.getSize()];
		for (int p = 0; p < state.length; p++) {
			state[p] = -1;
		}
	}

	public void add(int p, int v, IRestrictionListener listener) {
		if(data == null) return;
		state[p] = v;
		for (int d = 0; d < PAWN_ORTS.length; d++) {
			int q = pawnField.jump(p, d);
			if(q < 0 || state[q] >= 0) continue;
			for (int vq = 0; vq < f.getColorCount(); vq++) {
				int dt = vq == v ? 1 : 0;
				if(data[p][d] != dt) {
					listener.exclude(q, vq);
				}
			}
		}
	}

	public void remove(int p, int v, IRestrictionListener listener) {
		if(data == null) return;
		state[p] = -1;
		for (int d = 0; d < PAWN_ORTS.length; d++) {
			int q = pawnField.jump(p, d);
			if(q < 0 || state[q] >= 0) continue;
			for (int vq = 0; vq < f.getColorCount(); vq++) {
				int dt = vq == v ? 1 : 0;
				if(data[p][d] != dt) {
					listener.include(q, vq);
				}
			}
		}
	}

	public void dispose() {}

}
