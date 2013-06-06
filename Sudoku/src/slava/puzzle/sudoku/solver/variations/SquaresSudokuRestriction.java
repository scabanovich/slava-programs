package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.model.SudokuDesignField;
import slava.puzzle.sudoku.solver.ISudokuField;
import slava.puzzle.sudoku.solver.restriction.IGeneratedRestriction;
import slava.puzzle.sudoku.solver.restriction.IRestrictionListener;

public class SquaresSudokuRestriction implements IGeneratedRestriction {
	int[][] squares;
	ISudokuField f;
	SudokuDesignField field;

	int[] generated;
	int[][] data; //[p][d] 0: is not square, 1: square
	
	int[] state;
	

	public SquaresSudokuRestriction() {}
	
	public void setField(ISudokuField f) {
		this.f = f;
		squares = new int[f.getColorCount()][f.getColorCount()];
		for (int i = 0; i < 10; i++) {
			int s = i * i;
			if(s < 10) continue;
			int a = (s / 10) - 1;
			int b = (s % 10) - 1;
			if(a >= squares.length || b >= squares.length) continue;
			squares[a][b] = 1;
		}
		field = new SudokuDesignField();
		field.setSize(f.getColorCount());
	}

	public void reset() {
		generated = null;
		state = null;
		data = null;
	}

	public void setGeneratedSolution(int[] generated) {
		this.generated = generated;
		data = new int[generated.length][2];
		for (int p = 0; p < data.length; p++) {
			for (int d = 0; d < 2; d++) {
				int q = field.jp(p, d);
				if(q < 0) continue;
				data[p][d] = squares[generated[p]][generated[q]];
			}
		}
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
		for (int d = 0; d < 2; d++) {
			int q = field.jp(p, d);
			if(q < 0 || state[q] >= 0) continue;
			for (int vq = 0; vq < f.getColorCount(); vq++) {
				if(data[p][d] != squares[v][vq]) {
					listener.exclude(q, vq);
				}
			}
		}
		for (int d = 2; d < 4; d++) {
			int q = field.jp(p, d);
			if(q < 0 || state[q] >= 0) continue;
			for (int vq = 0; vq < f.getColorCount(); vq++) {
				if(data[q][d - 2] != squares[vq][v]) {
					listener.exclude(q, vq);
				}
			}
		}
	}

	public void remove(int p, int v, IRestrictionListener listener) {
		if(data == null) return;
		state[p] = -1;
		for (int d = 0; d < 2; d++) {
			int q = field.jp(p, d);
			if(q < 0 || state[q] >= 0) continue;
			for (int vq = 0; vq < f.getColorCount(); vq++) {
				if(data[p][d] != squares[v][vq]) {
					listener.include(q, vq);
				}
			}
		}
		for (int d = 2; d < 4; d++) {
			int q = field.jp(p, d);
			if(q < 0 || state[q] >= 0) continue;
			for (int vq = 0; vq < f.getColorCount(); vq++) {
				if(data[q][d - 2] != squares[vq][v]) {
					listener.include(q, vq);
				}
			}
		}
	}

	public void dispose() {}

}
