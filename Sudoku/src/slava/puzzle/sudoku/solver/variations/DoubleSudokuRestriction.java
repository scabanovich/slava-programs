package slava.puzzle.sudoku.solver.variations;

import slava.puzzle.sudoku.solver.restriction.IRestriction;
import slava.puzzle.sudoku.solver.restriction.IRestrictionListener;

public class DoubleSudokuRestriction implements IRestriction {
	int[] values;
	int[] used;
	
	public DoubleSudokuRestriction() {
		values = new int[81 * 2];
		for (int i = 0; i < values.length; i++) values[i] = -1;
		used = new int[81];
	}

	public void add(int p, int v, IRestrictionListener listener) {
		values[p] = v;
		int v1, v2;
		if(p >= 81) {
			int q = p - 81;
			v2 = v;
			if(values[q] < 0) {
				for (v1 = 0; v1 < 9; v1++) {
					int u = v1 * 9 + v2;
					if(used[u] > 0) listener.exclude(q, v1);
				}
				return;
			}
			v1 = values[q];
		} else {
			int q = p + 81;
			v1 = v;
			if(values[q] < 0) {
				for (v2 = 0; v2 < 9; v2++) {
					int u = v1 * 9 + v2;
					if(used[u] > 0) listener.exclude(q, v2);
				}
				return;
			}
			v2 = values[q];			
		}
		int u = v1 * 9 + v2;
		used[u]++;
		for (int r1 = 0; r1 < 81; r1++) {
			int r2 = r1 + 81;
			if(values[r1] == v1 && r2 != p && r1 != p) {
				listener.exclude(r2, v2);
			}
			if(values[r2] == v2 && r1 != p && r2 != p) {
				listener.exclude(r1, v1);
			}
		}
	}

	public void init(IRestrictionListener listener) {
	}

	public void remove(int p, int v, IRestrictionListener listener) {
		values[p] = -1;
		int v1, v2;
		if(p >= 81) {
			int q = p - 81;
			v2 = v;
			if(values[q] < 0) {
				for (v1 = 0; v1 < 9; v1++) {
					int u = v1 * 9 + v2;
					if(used[u] > 0) listener.include(q, v1);
				}
				return;
			}
			v1 = values[q];
		} else {
			int q = p + 81;
			v1 = v;
			if(values[q] < 0) {
				for (v2 = 0; v2 < 9; v2++) {
					int u = v1 * 9 + v2;
					if(used[u] > 0) listener.include(q, v2);
				}
				return;
			}
			v2 = values[q];			
		}
		int u = v1 * 9 + v2;
		used[u]--;
		for (int r1 = 0; r1 < 81; r1++) {
			int r2 = r1 + 81;
			if(values[r1] == v1 && r2 != p && r1 != p) {
				listener.include(r2, v2);
			}
			if(values[r2] == v2 && r1 != p && r2 != p) {
				listener.include(r1, v1);
			}
		}
	}

	public void dispose() {
		values = new int[81 * 2];
		for (int i = 0; i < values.length; i++) values[i] = -1;
		used = new int[81];
	}

}
