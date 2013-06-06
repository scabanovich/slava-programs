package ic2006_3;

import com.slava.common.TwoDimField;

public class SixteenSolver {
	TwoDimField field;
	
	int[] initialState;
	
	int[] state;
	int[] usedNumbers;
	int[][] restriction;
	
	int freeCellCount;

	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;
	
	public SixteenSolver() {}
	
	public void setField(TwoDimField f) {
		field = f;
	}
	
	public void setInitialState(int[] s) {
		initialState = s;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		usedNumbers = new int[state.length + 1];
		restriction = new int[field.getSize()][state.length + 1];
		freeCellCount = state.length;
		for (int p = 0; p < initialState.length; p++) {
			if(initialState[p] > 0) add(p, initialState[p]);
		}
		
		place = new int[state.length + 1];
		wayCount = new int[state.length + 1];
		way = new int[state.length + 1];
		ways = new int[state.length + 1][state.length];
		temp = new int[state.length];
		t = 0;
		solutionCount = 0;
	}
	
	int[] temp;
	
	void srch() {
		wayCount[t] = 0;
		int wcb = 100;
		for (int p = 0; p < state.length; p++) {
			if(state[p] > 0) continue;
			int wc = getWayCount(p);
			if(wc == 0) return;
			if(wc < wcb) {
				wcb = wc;
				for(int i = 0; i < wc; i++) ways[t][i] = temp[i];
				place[t] = p;
			}
		}
		if(wcb < 100) {
			wayCount[t] = wcb;
		}
	}
	
	int getWayCount(int p) {
		int wc = 0;
		for (int v = 1; v < usedNumbers.length; v++) {
			if(usedNumbers[v] > 0 || restriction[p][v] > 0) continue;
			temp[wc] = v;
			wc++;
		}
		return wc;
	}
	
	void move() {
		int v = ways[t][way[t]];
		int p = place[t];
		add(p, v);
		t++;
		srch();
		way[t] = -1;
	}
	
	void add(int p, int v) {
		state[p] = v;
		usedNumbers[v]++;
		if(usedNumbers[v] == 1) freeCellCount--;
		for (int vc = v - 2; vc < v + 3; vc++) {
			if(vc <= 0 || vc >= usedNumbers.length) continue;
			for (int d = 0; d < 8; d++) {
				int q = field.jump(p, d);
				if(q >= 0) restriction[q][vc]++;
			}
		}
	}
	
	void back() {
		--t;
		int v = ways[t][way[t]];
		int p = place[t];
		remove(p, v);
	}
	
	void remove(int p, int v) {
		state[p] = 0;
		usedNumbers[v]--;
		if(usedNumbers[v] == 0) freeCellCount++;
		for (int vc = v - 2; vc < v + 3; vc++) {
			if(vc <= 0 || vc >= usedNumbers.length) continue;
			for (int d = 0; d < 8; d++) {
				int q = field.jump(p, d);
				if(q >= 0) restriction[q][vc]--;
			}
		}
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) {
					return;
				}
				back();
			}
			way[t]++;
			move();
			if(freeCellCount == 0 && isSolution()) {
				solutionCount++;
//				System.out.println(solutionCount);
				printSolution();
			}
		}
	}
	
	boolean isSolution() {
		for (int p1 = 0; p1 < state.length; p1++) {
			for (int p2 = 0; p2 < state.length; p2++) {
				if(p2 == p1) continue;
				int c = -1;
				if(field.getX(p1) == field.getX(p2)) {
					c = 0;
				} else if(field.getY(p1) == field.getY(p2)) {
					c = 1;
				} else if(field.getX(p1) - field.getY(p1) == field.getX(p2) - field.getY(p2)) {
					c = 2;
				}
				if(c < 0) continue;
				for (int p3 = 0; p3 < state.length; p3++) {
					if(p3 == p1 || p3 == p2) continue;
					if(c == 0 && field.getX(p3) != field.getX(p1)) continue;
					if(c == 1 && field.getY(p3) != field.getY(p1)) continue;
					if(c == 2 && field.getX(p3) - field.getY(p3) != field.getX(p1) - field.getY(p1)) continue;
					if(state[p1] + state[p2] == state[p3]) return false;
					if(state[p1] * state[p2] == state[p3]) return false;
				}
			}
		}
		return true;
	}
	
	void printSolution() {
		System.out.println("Solution " + solutionCount);
		for (int p = 0; p < field.getSize(); p++) {
			String ch = "" + state[p];
			if(ch.length() < 2) ch = " " + ch;
			System.out.print(" " + ch);
			if(field.isRightBorder(p)) System.out.println("");
		}
		System.out.println("");
	}
	
}
