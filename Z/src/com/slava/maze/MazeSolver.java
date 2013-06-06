package com.slava.maze;

import com.slava.common.RectangularField;

public class MazeSolver {
	RectangularField field;
	int[] walls;
	int start;

	int[] state;

	public MazeSolver() {}

	public void setField(RectangularField field) {
		this.field = field;
	}

	public void setProblem(MazeProblem s) {
		walls = s.getWalls();
		start = s.getStart();
	}

	public void solve() {
		init();
		int t = 0;
		while(iterate(t++) > 0);
	}

	void init() {
		state = new int[field.getSize()];
		for (int p = 0; p < state.length; p++) {
			state[p] = -1;
		}
		state[start] = 0;
	}

	int iterate(int t) {
		int count = 0;
		for (int p = 0; p < state.length; p++) {
			if(state[p] == t) {
				for (int d = 0; d < 4; d++) {
					int q = field.jump(p, d);
					while(q >= 0 && walls[q] == 0) {
						int q1 = field.jump(q, d);
						if(q1 < 0) break;
						if(walls[q1] > 0) {
							if(state[q] < 0) {
								state[q] = t + 1;
								count++;
							}
							break;
						}
						q = q1;
					}
				}
			}
		}
		return count;
	}

	public void printSolution() {
		for (int p = 0; p < state.length; p++) {
			String c = "-";
			if(walls[p] > 0) {
				c = "*";
			} else if(state[p] >= 0) {
				c = "" + state[p];
				if(state[p] > 9) {
					c = "" + (char)(97 + state[p] - 10);
				}
			}
			System.out.print(" " + c);
			if(field.isRightBorder(p)) {
				System.out.println("");
			}
		}
	}

	public int getMaximum() {
		int m = 0;
		for (int p = 0; p < state.length; p++) {
			if(state[p] > m) m = state[p];
		}
		return m;
	}

	public int getVisited() {
		int c = 0;
		for (int p = 0; p < state.length; p++) {
			if(state[p] > -1) c++;
		}
		return c;
	}
}
