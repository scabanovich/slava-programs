package com.slava.patience.piramida;

import com.slava.common.RectangularField;

public class IogaSolver {
	RectangularField field;
	int[] form;
	int[] initial;
	
	int[] state;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] waysP;
	int[][] waysD;
	int[][][] forbidden;
	
	int picesCount;
	
	public IogaSolver() {}
	
	public void setField(RectangularField f) {
		field = f;
	}
	
	public void setData(int[] form, int[] initial) {
		this.form = form;
		this.initial = initial;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		picesCount = 0;
		for (int i = 0; i < state.length; i++) {
			state[i] = initial[i];
			if(state[i] != 0 && form[i] != 0) ++picesCount;
		}
		wayCount = new int[picesCount];
		way = new int[picesCount];
		waysP = new int[picesCount][100];
		waysD = new int[picesCount][100];
		forbidden = new int[picesCount][field.getSize()][4];
		t = 0;
		
	}
	
	void srch() {
		wayCount[t] = 0;
		if(picesCount < 2) return;
		for (int p = 0; p < state.length; p++) {
			if(form[p] == 0 || state[p] == 0) continue;
			for (int d = 0; d < 4; d++) {
				if(!canMove(p, d)) continue;
				if(forbidden[t][p][d] != 0) continue;
				waysP[t][wayCount[t]] = p;
				waysD[t][wayCount[t]] = d;
				wayCount[t]++;
			}
		}
	}
	
	boolean canMove(int p, int d) {
		if(state[p] == 0) return false;
		int p1 = field.jump(p, d);
		if(p1 < 0 || form[p1] == 0 || state[p1] == 0) return false;
		int p2 = field.jump(p1, d);
		if(p2 < 0 || form[p2] == 0 || state[p2] != 0) return false;
		return true;
	}
	
	void move() {
		int p = waysP[t][way[t]];
		int d = waysD[t][way[t]];
		int p1 = field.jump(p, d);
		int p2 = field.jump(p1, d);
		state[p] = 0;
		state[p1] = 0;
		state[p2] = 1;
		picesCount--;
		updateForbidden();
		++t;
		srch();
		way[t] = -1;
	}
	
	void updateForbidden() {
		int p0 = waysP[t][way[t]];
		int d0 = waysD[t][way[t]];
		int p1 = field.jump(p0, d0);
		int t1 = t + 1;
		for (int p = 0; p < state.length; p++) {
			for (int d = 0; d < 4; d++) {
				forbidden[t1][p][d] = 0;
			}
		}

		for (int p = 0; p < state.length; p++) {
			if(form[p] == 0) continue;
			if(p == p1) continue;
			for (int d = 0; d < 4; d++) {
				if(forbidden[t][p][d] != 0) {
					if(canMove(p, d)) forbidden[t1][p][d] = 1;
				}
			}			
		}
		for (int i = 0; i < way[t]; i++) {
			int p = waysP[t][i];
			int d = waysD[t][i];
			if(canMove(p, d)) {
				forbidden[t1][p][d] = 1;
			}
		}
	}
	
	void back() {
		--t;
		int p = waysP[t][way[t]];
		int d = waysD[t][way[t]];
		int p1 = field.jump(p, d);
		int p2 = field.jump(p1, d);
		state[p] = 1;
		state[p1] = 1;
		state[p2] = 0;
		picesCount++;
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(picesCount == 1) {
				printSolution();
			}
		}
	}
	
	static char[] DIRS = {'e', 's', 'w', 'n'};
	
	void printSolution() {
		for (int i = 0; i < t; i++) {
			int p = waysP[i][way[i]];
			int d = waysD[i][way[i]];
			int x = field.getX(p), y = field.getY(p) + 1;
			char cx = (char)(97 + x);
			System.out.print(" " + cx + y + DIRS[d]);
		}
		System.out.println("");
	}

}
