package olia;

import match2005.PushField;

public class Sixteen2x2Set {
	PushField field;
	
	int[] presetValues;
	
	int[] usedCombinations;
	int[] state;

	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;
	
	public Sixteen2x2Set() {}
	
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
		usedCombinations = new int[16];
		wayCount = new int[field.getSize() + 1];
		way = new int[field.getSize() + 1];
		ways = new int[field.getSize() + 1][2];
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == field.getSize()) return;
		int p = t;
		if(isBorder(p)) {
			for (int v = 0; v < 2; v++) {
				if(presetValues[p] >= 0 && presetValues[p] != v) continue;
				ways[t][wayCount[t]] = v;
				wayCount[t]++;				
			}
		} else {
			for (int v = 0; v < 2; v++) {
				if(presetValues[p] >= 0 && presetValues[p] != v) continue;
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
		return ((((i1 * 2) + i2) * 2 + i3) * 2 + i4);
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
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t == field.getSize()) {
				solutionCount++;
				if(solutionCount < 10) {
					printSolution();
				}
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
	
	static int[] PRESET_VALUES = {
		e,e,e,e,x,
		x,e,e,e,e,
		x,e,x,e,e,
		e,e,x,x,e,
		x,e,e,x,e,
	};

	public static void main(String[] args) {
		Sixteen2x2Set p = new Sixteen2x2Set();
		PushField f = new PushField();
		f.setSize(5, 5);
		p.setField(f);
		p.setPresetValues(PRESET_VALUES);
		p.solve();
		System.out.println("Solution count=" + p.solutionCount);
	}

}
