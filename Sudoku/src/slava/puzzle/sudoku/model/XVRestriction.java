package slava.puzzle.sudoku.model;

import slava.puzzle.sudoku.solver.restriction.IRestriction;
import slava.puzzle.sudoku.solver.restriction.IRestrictionListener;

public class XVRestriction implements IRestriction {
	SudokuDesignField field;
	SudokuProblemInfo problem;
	int[][] data; //[p][d] --> 0..1
	
	public XVRestriction() {}

	public void setData(SudokuDesignField field, int[][] data) {
		this.field = field;
		this.data = data;
	}
	
	public void setProblem(SudokuProblemInfo problem) {
		this.problem = problem;
	}

	public void add(int p, int v, IRestrictionListener listener) {
		int[] ns = data[p];
		for (int d = 0; d < ns.length; d++) {
			int q = field.jp(p, d);
			if(q < 0) continue;
			if(ns[d] <= 0) {
				for (int vc = 0; vc < field.getWidth(); vc++) {
					if(v + vc == 3 || v + vc == 8) listener.exclude(q, vc);
				}
			} else {
				int qv = (ns[d] == 1) ? 3 - v : 8 - v;
				for (int vc = 0; vc < field.getWidth(); vc++) {
					if(qv != vc) listener.exclude(q, vc);
				}
			}
		}		
	}

	public void remove(int p, int v, IRestrictionListener listener) {
		int[] ns = data[p];
		for (int d = 0; d < ns.length; d++) {
			int q = field.jp(p, d);
			if(q < 0) continue;
			if(ns[d] <= 0) {
				for (int vc = 0; vc < field.getWidth(); vc++) {
					if(v + vc == 3 || v + vc == 8) listener.include(q, vc);
				}
			} else {
				int qv = (ns[d] == 1) ? 3 - v : 8 - v;
				for (int vc = 0; vc < field.getWidth(); vc++) {
					if(qv != vc) listener.include(q, vc);
				}
			}
		}		
	}

	public void init(IRestrictionListener listener) {
		for (int p = 0; p < field.getSize(); p++) {
			int[] ns = data[p];
			for (int d = 0; d < ns.length; d++) {
				int q = field.jp(p, d);
				if(q < 0 || ns[d] <= 0) continue;
				int qm = (ns[d] == 1) ? 3 : 8;
				for (int vc = 0; vc < field.getWidth(); vc++) {
					if(qm < vc) listener.exclude(q, vc);
				}
			}		
		}
	}

	public void dispose() {}

}
