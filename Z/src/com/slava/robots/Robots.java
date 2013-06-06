package com.slava.robots;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.slava.common.RectangularField;

public class Robots {
	RectangularField field;
	int timeLimit = 10;
	int[] initialState;

	int[] state;

	int t;
	int[] wayCount;
	int[] way;
	int[][] waysP;
	int[][] waysE;

	int treeCount;
	int solutionCount;

	Set<String> visited = new HashSet<String>();
	
	public Robots() {}

	public void setField(RectangularField field) {
		this.field = field;
	}

	public void setInitialState(int[] s) {
		initialState = s;
	}

	public void setTimeLimit(int l) {
		timeLimit = l;
	}

	public void solve() {
		init();
		anal();
	}

	void init() {
		state = new int[field.getSize()];
		for (int p = 0; p < state.length; p++) state[p] = initialState[p];
		wayCount = new int[timeLimit + 1];
		way = new int[timeLimit + 1];
		waysP = new int[timeLimit + 1][state.length];
		waysE = new int[timeLimit + 1][state.length];

		drawn = new int[state.length];
		stack = new int[state.length];

		t = 0;
		treeCount = 0;
		solutionCount = 0;

		processNewState();
	}

	void srch() {
		wayCount[t] = 0;
		if(t == timeLimit) return;
		if(getRobotCount() < 5) return;

		for (int p = 0; p < state.length; p++) {
			if(state[p] == 1) {
				for (int d = 0; d < 4; d++) {
					int e = getEnd(p, d);
					if(e != p) {
						doStep(p, e);
						String code = code();
						undoStep(p, e);
						if(!visited.contains(code)) {
							waysP[t][wayCount[t]] = p;
							waysE[t][wayCount[t]] = e;
							wayCount[t]++;
						}
					}
				}
			}
		}
	}

	int getRobotCount() {
		int c = 0;
		for (int p = 0; p < state.length; p++) {
			if(state[p] == 1) c++;
		}
		return c;
	}

	int getEnd(int p, int d) {
		int q = field.jump(p, d);
		while(q >= 0) {
			if(state[q] == 1) return p;
			p = q;
			q = field.jump(p, d);
		}
		return -1;
	}

	String code() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < state.length; i++) {
			if(state[i] == 1) sb.append(i).append(';');
		}
		return sb.toString();
	}

	void move() {
		int p = waysP[t][way[t]];
		int e = waysE[t][way[t]];
		doStep(p, e);

		processNewState();
		
		++t;
		srch();
		way[t] = -1;
	}

	void doStep(int p, int e) {
		state[p] = 0;
		if(e >= 0) {
			state[e] = 1;
		}
	}

	void back() {
		--t;
		int p = waysP[t][way[t]];
		int e = waysE[t][way[t]];
		undoStep(p, e);
	}

	void undoStep(int p, int e) {
		state[p] = 1;
		if(e >= 0) {
			state[e] = 0;
		}
	}

	int[] drawn;
	int[] stack;

	void processNewState() {
		visited.add(code());
		for (int p = 0; p < state.length; p++) drawn[p] = 0;
		for (int p = 0; p < state.length; p++) {
			if(state[p] == 0 || drawn[p] == 1) continue;
			int v = 1;
			drawn[p] = 1;
			stack[0] = p;
			int c = 0;
			while(c < v) {
				int q = stack[c];
				for (int d = 0; d < 4; d++) {
					int r = field.jump(q, d);
					if(r >= 0 && state[r] == 1 && drawn[r] == 0) {
						drawn[r] = 1;
						stack[v] = r;
						v++;
					}
				}
				c++;
			}
			if(v == 5) {
				String f = PentaminoRecognizer.getFigureCode(field, stack, 5);
				if(!figures.contains(f)) {
					figures.add(f); 
//					System.out.println("Figure " + f);
//					printState();
				}
			}
		}
	}

	Set<String> figures = new TreeSet<String>();

	void printState() {
		for (int i = 0; i < state.length; i++) {
			System.out.print(" " + state[i]);
			if(field.isRightBorder(i)) System.out.println("");
		}
		System.out.println("");
	}

	void anal() {
		srch();
		way[t] = -1;
		int tm = 0;
		while(true) {
			while(way[t] >= wayCount[t] - 1) {
				if(t == 0) {
					System.out.println("Max t=" + tm);
					return;
				}
				back();
			}
			way[t]++;
			move();
			if(wayCount[t] == 0) {
				treeCount++;
			}
			if(t > tm) {
//				System.out.println(t);
				tm = t;
			}
			if(isSolution()) {
				solutionCount++;
			}
			if(figures.size() == 12) return;
		}
	}

	public boolean isSolution() {
		return false;
	}

	public void printFigures() {
		System.out.print("Figures:");
		for (String s: figures) {
			System.out.print(" " + s);
		}
		System.out.println(" (" + figures.size() + ")");
	}

	static int[] INITIAL_STATE = {
		0,1,0,0,0,0,0,0,
		0,0,0,1,0,1,0,1,
		0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,1,
		0,0,1,0,1,0,0,0,
	};

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Robots p = new Robots();
		RectangularField f = new RectangularField();
		f.setSize(8, 5);
		p.setField(f);
		p.setTimeLimit(30);
		p.setInitialState(INITIAL_STATE);
		p.solve();

		System.out.println("States=" + p.visited.size() + " treeCount=" + p.treeCount);
		p.printFigures();
	}

}

