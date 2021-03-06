package slava.puzzle.sudoku.model;

import slava.puzzle.sudoku.solver.restriction.IRestriction;
import slava.puzzle.sudoku.solver.restriction.IRestrictionListener;

public class DifferenceNRestriction implements IRestriction {
	int diff = 3;
	SudokuDesignField field;
	int[][] data; //[p][d] --> 0..1
	
	public DifferenceNRestriction() {
	}

	public void setDiff(int c) {
		diff = c;
	}
	
	public void setData(SudokuDesignField field, int[][] data) {
		this.field = field;
		this.data = data;
	}

	public void add(int p, int v, IRestrictionListener listener) {
		int[] ns = data[p];
		for (int d = 0; d < ns.length; d++) {
			int q = field.jp(p, d);
			if(q < 0) continue;
			boolean need_1 = (ns[d] == 1);
			for (int vc = 0; vc < field.getWidth(); vc++) {
				boolean is_d = vc == v + diff || v == vc + diff || v + vc + 2 == diff;
				if(need_1 != is_d) listener.exclude(q, vc);
			}
		}
	}

	public void remove(int p, int v, IRestrictionListener listener) {
		int[] ns = data[p];
		for (int d = 0; d < ns.length; d++) {
			int q = field.jp(p, d);
			if(q < 0) continue;
			boolean need_1 = (ns[d] == 1);
			for (int vc = 0; vc < field.getWidth(); vc++) {
				boolean is_d = vc == v + diff || v == vc + diff || v + vc + 2 == diff;
				if(need_1 != is_d) listener.include(q, vc);
			}
		}
	}

	public void init(IRestrictionListener listener) {
		// TODO Auto-generated method stub		
	}


	public void dispose() {}

}
