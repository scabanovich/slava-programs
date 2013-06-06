package slava.puzzle.sudoku.model;

import slava.puzzle.sudoku.solver.restriction.IRestriction;
import slava.puzzle.sudoku.solver.restriction.IRestrictionListener;

public class LessOrGreaterRestriction implements IRestriction {
	SudokuDesignField field;
	SudokuProblemInfo problem;
	int[][] data; //[p][d] --> 0..1
	
	public LessOrGreaterRestriction() {}

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
			if(q < 0 || ns[d] == 0) continue;
			if(ns[d] < 0) {
				for (int vc = 0; vc < field.getWidth(); vc++) {
					if(vc < v) listener.exclude(q, vc);
				}				
			} else {
				for (int vc = 0; vc < field.getWidth(); vc++) {
					if(vc > v) listener.exclude(q, vc);
				}				
			}
		}
	}

	public void remove(int p, int v, IRestrictionListener listener) {
		int[] ns = data[p];
		for (int d = 0; d < ns.length; d++) {
			int q = field.jp(p, d);
			if(q < 0 || ns[d] == 0) continue;
			if(ns[d] < 0) {
				for (int vc = 0; vc < field.getWidth(); vc++) {
					if(vc < v) listener.include(q, vc);
				}				
			} else {
				for (int vc = 0; vc < field.getWidth(); vc++) {
					if(vc > v) listener.include(q, vc);
				}				
			}
		}
	}
	
	public void init(IRestrictionListener listener) {
		for (int p = 0; p < field.getSize(); p++) {
			int less = countLessOrGreater(p, -1);
			for (int v = field.getWidth() - less; v < field.getWidth(); v++) {
				listener.exclude(p, v);
			}
			int greater = countLessOrGreater(p, 1);
			for (int v = 0; v < greater; v++) {
				listener.exclude(p, v);
			}
		}
	}
	
	private int countLessOrGreater(int p, int sign) {
		int[] visited = new int[field.getSize()];
		int[] stack = new int[field.getWidth() + 1];
		int[] ns = problem.getNumbers();
		int v = 1;
		int c = 0;
		stack[0] = p;
		visited[p] = 1;
		while(c < v) {
			p = stack[c];
			for (int d = 0; d < 4; d++) {
				int q = field.jp(p, d);
				if(q < 0 || visited[q] > 0 || data[p][d] != sign || ns[q] != ns[p]) continue;
				visited[q] = 1;
				stack[v] = q;
				v++;
			}
			++c;
		}
		return v - 1;		
	}

	public void dispose() {}

}
