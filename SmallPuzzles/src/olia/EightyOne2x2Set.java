package olia;

import match2005.PushField;

public class EightyOne2x2Set {
	PushField field;
	
	int[] presetValues;
	
	int[] usedCombinations;
	int[] state;

	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;
	
	public EightyOne2x2Set() {}
	
	public void setField(PushField f) {
		field = f;
	}
	
	public void setPresetValues(int[] vs) {
		presetValues = vs;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		usedCombinations = new int[81];
		wayCount = new int[field.getSize() + 1];
		way = new int[field.getSize() + 1];
		ways = new int[field.getSize() + 1][3];
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == field.getSize()) return;
		int p = t;
		if(isBorder(p)) {
			for (int v = 0; v < 3; v++) {
				if(presetValues != null && presetValues[p] >= 0 && presetValues[p] != v) continue;
				ways[t][wayCount[t]] = v;
				wayCount[t]++;				
			}
		} else {
			for (int v = 0; v < 3; v++) {
				if(presetValues != null && presetValues[p] >= 0 && presetValues[p] != v) continue;
				if(usedCombinations[getCombination(p, v)] == 0) {
					ways[t][wayCount[t]] = v;
					wayCount[t]++;				
				}
			}
		}
	}
	
	boolean isBorder(int p) {
		return field.x(p) == 0 || field.y(p) == 0;
	}
	
	int getCombination(int p, int i4) {
		int ix = field.x(p);
		int iy = field.y(p);
		int p1 = field.getIndex(ix - 1, iy - 1);
		int p2 = field.getIndex(ix, iy - 1);
		int p3 = field.getIndex(ix - 1, iy);
		return getCombination(state[p1], state[p2], state[p3], i4);
	}
	
	int getCombination(int i1, int i2, int i3, int i4) {
		return ((((i1 * 3) + i2) * 3 + i3) * 3 + i4);
	}
	
	void move() {
		int p = t;
		int v = ways[t][way[t]];
		state[p] = v;
		if(!isBorder(p)) {
			usedCombinations[getCombination(p, v)]++;
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = t;
		int v = ways[t][way[t]];
		state[p] = -1;
		if(!isBorder(p)) {
			usedCombinations[getCombination(p, v)]--;
		}
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
				System.out.println("t=" + t);
			}
			if(t == field.getSize()) {
				solutionCount++;
				if(solutionCount < 10) {
					printSolution();
				}
				if(solutionCount % 100 == 0) System.out.println(solutionCount);
			}
		}
	}
	
	void printSolution() {
		for (int i = 0; i < state.length; i++) {
			System.out.print(" " + state[i]);
			if(field.x(i) == field.getWidth() - 1) {
				System.out.println("");
			}
		}
		System.out.println("");
		System.out.println("");
	}
	
	static int e = -1;
	static int o = 0;
	static int x = 1;
	static int y = 2;
	
	static int[] PRESET_VALUES = {
		x,e,e,e,e,e,e,e,e,y,
		e,x,e,e,e,e,e,e,y,e,
		e,e,x,e,e,e,e,y,e,e,
		e,e,e,x,e,e,y,e,e,e,
		e,e,e,e,x,y,e,e,e,e,
		e,e,e,e,x,y,e,e,e,e,
		e,e,e,x,e,e,y,e,e,e,
		e,e,x,e,e,e,e,y,e,e,
		e,x,e,e,e,e,e,e,y,e,
		x,e,e,e,e,e,e,e,e,y,
	};

	public static void main(String[] args) {
		EightyOne2x2Set p = new EightyOne2x2Set();
		PushField f = new PushField();
		f.setSize(10, 10);
		p.setField(f);
		p.setPresetValues(PRESET_VALUES);
		p.solve();
		System.out.println("Solution count=" + p.solutionCount);
	}

}
