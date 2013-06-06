package com.slava.knight;

import com.slava.common.TwoDimField;

/**
 * Split checkboard into pairs by knight moves.
 * 
 * @author slava
 *
 */
public class KnightPairsSolver {
	ISolutionListener solutionListener;
	IPairRestriction pairRestriction;
	
	int[] fieldRestriction;

	TwoDimField field;
	int[] state;
	int pairsToDo;

	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;

	public KnightPairsSolver() {}

	public void setField(TwoDimField field) {
		this.field = field;
	}

	public void setFieldRestriction(int[] s) {
		fieldRestriction = s;
	}

	public void setPairRestriction(IPairRestriction r) {
		pairRestriction = r;
	}

	public void setSolutionListener(ISolutionListener l) {
		solutionListener = l;
	}

	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		pairsToDo = state.length;
		for (int p = 0; p < state.length; p++) {
			state[p] = -1;
			if(fieldRestriction != null && fieldRestriction[p] == 1) {
				state[p] = p;
				pairsToDo--;
			}
		}
		if(pairsToDo % 2 == 1) {
			throw new RuntimeException("Wrong area=" + pairsToDo);
		}
		pairsToDo = pairsToDo / 2;

		place = new int[field.getSize()];
		wayCount = new int[field.getSize()];
		way = new int[field.getSize()];
		ways = new int[field.getSize()][8];
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(pairsToDo == 0) return;
		int wcm = 9;
		for (int p = 0; p < state.length; p++) {
			if(state[p] >= 0) continue;
			int wc = getWayCount(p);
			if(wc == 0) return;
			if(wc < wcm) {
				wcm = wc;
				place[t] = p;
				for (int k = 0; k < wc; k++) ways[t][k] = temp[k];
				if(wc == 1) break;
			}			
		}
		if(wcm < 9) {
			wayCount[t] = wcm;
		}
	}

	int[] temp = new int[8];
	
	int getWayCount(int p) {
		int wc = 0;
		for (int d = 0; d < 8; d++) {
			int q = field.jump(p, d);
			if(q >= 0 && state[q] < 0) {
				if(pairRestriction == null || pairRestriction.accept(state, p, q)) {
					temp[wc] = q;
					wc++;
				}
			}
		}
		return wc;
	}

	void move() {
		int p1 = place[t];
		int p2 = ways[t][way[t]];
		state[p1] = p2;
		state[p2] = p1;
		pairsToDo--;
		++t;
		srch();
		way[t] = -1;
	}

	void back() {
		--t;
		int p1 = place[t];
		int p2 = ways[t][way[t]];
		state[p1] = -1;
		state[p2] = -1;
		pairsToDo++;
	}
	
	void anal() {
		srch();
		way[t] = -1;
		int tm = 0;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t > tm) {
				tm = t;
//				System.out.println("t=" + tm);
			}
			if(pairsToDo == 0) {
				++solutionCount;
//				if(solutionCount == 1) printSolution();
				if(solutionListener != null) {
					solutionListener.solutionFound((int[])state.clone());
				}
			}
		}
	}

	void printSolution() {
		System.out.println("Solution:");
		for (int p = 0; p < state.length; p++) {
			String s = "" + state[p];
			if(s.length() < 2) s = " " + s;
			System.out.print(" " + s);
			if(field.isRightBorder(p)) {
				System.out.println("");
			}
		}
	}

	public int getSolutionCount() {
		return solutionCount;
	}

	public static void main(String[] args) {
		KnightField f = new KnightField();
		f.setSize(8, 6);
		KnightPairsSolver solver = new KnightPairsSolver();
		solver.setField(f);

		int[] fieldRestriction = new int[f.getSize()];
		fieldRestriction[0] = 1;
		fieldRestriction[5] = 1;
		solver.setFieldRestriction(fieldRestriction);

		solver.solve();
		System.out.println("Solutions=" + solver.getSolutionCount());
	}
	
}
