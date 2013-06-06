package com.slava.kropki;

import com.slava.common.RectangularField;

public class KropkiSolver {
	RectangularField field;
	int[][] vData;
	int[][] hData;
	
	int[] state;
	int[][] usage;  //[place][value]
	
	int t;
	int[] wayCount;
	int[] way;
	int[] place;
	int[][] ways;
	
	int solutionCount;
	
	public KropkiSolver() {}
	
	public void setField(RectangularField field) {
		this.field = field;
	}
	
	public void setData(int[][] vData, int[][] hData) {
		this.vData = vData;
		this.hData = hData;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		int size = field.getSize();
		state = new int[size];
		for (int i = 0; i < size; i++) state[i] = -1;
		usage = new int[size][field.getWidth()];
		wayCount = new int[size + 1];
		way = new int[size + 1];
		place = new int[size + 1];
		ways = new int[size + 1][field.getWidth()];
		temp = new int[field.getWidth()];
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == field.getSize()) return;
		int wcm = field.getWidth() + 1;
		place[t] = -1;
		for (int p = 0; p < field.getSize(); p++) {
			if(state[p] >= 0) continue;
			int wc = getWays(p);
			if(wc < wcm) {
				wcm = wc;
				if(wcm == 0) return;
				place[t] = p;
				for (int k = 0; k < wc; k++) ways[t][k] = temp[k];
			}
		}
		if(place[t] < 0) return;
		wayCount[t] = wcm;		
	}
	
	int[] temp;
	
	int getWays(int p) {
		int wc = 0;
		for (int v = 0; v < usage[p].length; v++) {
			if(usage[p][v] > 0) continue;
			temp[wc] = v;
			++wc;
		}
		return wc;
	}
	
	void move() {
		int p = place[t];
		int v = ways[t][way[t]];
		state[p] = v;
		int ix = field.getX(p);
		int iy = field.getY(p);
		for (int k = 0; k < field.getWidth(); k++) {
			int q = field.getIndex(ix, k);
			usage[q][v]++;
			q = field.getIndex(k, iy);
			usage[q][v]++;
		}
		for (int d = 0; d < 4; d++) {
			int q = field.jump(p, d);
			if(q < 0) continue;
			int data = (d == 0) ? hData[iy][ix] :
			           (d == 1) ? vData[iy][ix] :
			           (d == 2) ? hData[iy][ix - 1] :
			           (d == 3) ? vData[iy - 1][ix] : -1;
			addRestrictions(v, q, data);
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void addRestrictions(int v, int q, int data) {
		if(data == 0) {
			if(v - 1 >= 0) usage[q][v - 1]++;
			if(v + 1 < field.getWidth()) usage[q][v + 1]++;
			if(2 * v + 1 < field.getWidth()) usage[q][2 * v + 1]++;
			if(v >= 1 && (v % 2 == 1)) usage[q][(v - 1) / 2]++;
		} else if(data == 1) {
			for (int u = 0; u < field.getWidth(); u++) {
				if(u != v + 1 && u != v - 1) usage[q][u]++;
			}
		} else if(data == 2) {
			for (int u = 0; u < field.getWidth(); u++) {
				if(u != 2 * v + 1 && 2 * u + 1 != v) usage[q][u]++;
			}
		}
	}
	
	void back() {
		--t;
		int p = place[t];
		int v = ways[t][way[t]];
		state[p] = -1;
		int ix = field.getX(p);
		int iy = field.getY(p);
		for (int k = 0; k < field.getWidth(); k++) {
			int q = field.getIndex(ix, k);
			usage[q][v]--;
			q = field.getIndex(k, iy);
			usage[q][v]--;
		}
		for (int d = 0; d < 4; d++) {
			int q = field.jump(p, d);
			if(q < 0) continue;
			int data = (d == 0) ? hData[iy][ix] :
 		               (d == 1) ? vData[iy][ix] :
		               (d == 2) ? hData[iy][ix - 1] :
		               (d == 3) ? vData[iy - 1][ix] : -1;
			removeRestrictions(v, q, data);
		}
	}
	
	void removeRestrictions(int v, int q, int d) {
		if(d == 0) {
			if(v - 1 >= 0) usage[q][v - 1]--;
			if(v + 1 < field.getWidth()) usage[q][v + 1]--;
			if(2 * v + 1 < field.getWidth()) usage[q][2 * v + 1]--;
			if(v >= 1 && (v % 2 == 1)) usage[q][(v - 1) / 2]--;
		} else if(d == 1) {
			for (int u = 0; u < field.getWidth(); u++) {
				if(u != v + 1 && u != v - 1) usage[q][u]--;
			}
		} else if(d == 2) {
			for (int u = 0; u < field.getWidth(); u++) {
				if(u != 2 * v + 1 && 2 * u + 1 != v) usage[q][u]--;
			}
		}
	}
	
	void anal() {
		srch();
		way[t] = -1;
//		int tm = 0;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t == field.getSize()) {
				++solutionCount;
				printSolution();
			}
		}
	}
	
	void printSolution() {
		System.out.println("Solution found");
		for (int i = 0; i < state.length; i++) {
			System.out.print(" " + (state[i] + 1));
			if(field.isRightBorder(i)) System.out.println("");
		}
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}

}
