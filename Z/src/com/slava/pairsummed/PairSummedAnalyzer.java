package com.slava.pairsummed;

public class PairSummedAnalyzer {
	PairSummedField field;
	int[] values;
	int size;
	
	int[] presetPositions;
	
	int[] state;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;
	int[][] solutionDist; // [t][p]
	
	public void setField(PairSummedField field) {
		this.field = field;
		values = new int[field.getSize()];
		for (int i = 0; i < values.length; i++) values[i] = i + 1;
	}
	
	public void setPresetPositions(int[] pp) {
		presetPositions = pp;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		size = field.getSize();
		state = new int[size];
		for (int i = 0; i < size; i++) state[i] = -1;
		wayCount = new int[size + 1];
		way = new int[size + 1];
		ways = new int[size + 1][size];
		t = 0;
		solutionCount = 0;
		solutionDist = new int[size][size];
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == size) return;
		if(t > 2 && !checkDistribution()) return;
		int number = values[t];
		if(presetPositions != null && presetPositions[t] >= 0 && presetPositions[t] < size) {
			int p = presetPositions[t];
			if(state[p] >= 0) return;
			if(!canPlace(p, number)) return;
			ways[t][wayCount[t]] = p;
			wayCount[t]++;
			return;
		}
		for (int p = 0; p < size; p++) {
			if(state[p] >= 0) continue;
			if(!canPlace(p, number)) continue;
			ways[t][wayCount[t]] = p;
			wayCount[t]++;
		}
	}
	
	boolean canPlace(int p, int number) {
		if(number < 3) return true;
		for (int d1 = 0; d1 < 8; d1++) {
			int p1 = field.jump(p, d1);
			if(p1 < 0 || state[p1] < 0) continue;
			for (int d2 = d1 + 1; d2 < 8; d2++) {
				int p2 = field.jump(p, d2);
				if(p2 < 0 || state[p2] < 0) continue;
				if(values[state[p1]] + values[state[p2]] == number) return true;
			}
		}
		return false;
	}
	
	boolean checkDistribution() {
		int max = values[size - 1];
		int[] dist = new int[size];
		for (int p = 0; p < size; p++) {
			if(state[p] >= 0) continue;
			int min = values[t];
			for (int d1 = 0; d1 < 8; d1++) {
				int p1 = field.jump(p, d1);
				if(p1 < 0 || state[p1] < 0) continue;
				if(values[state[p1]] < min) min = values[state[p1]];			
			}
			int min2 = values[t];
			for (int d1 = 0; d1 < 8; d1++) {
				int p1 = field.jump(p, d1);
				if(p1 < 0 || state[p1] < 0 || values[state[p1]] == min) continue;
				if(values[state[p1]] < min2) min2 = values[state[p1]];			
			}
			if(min + min2 > max) return false;
			int tc = min + min2 - 1; // 
			dist[tc]++;
		}
		int av = 0, rq = 0;
		for (int i = size - 1; i >= t; i--) {
			av += 1;
			rq += dist[i];
			if(rq > av) return false;
		}
		return true;
	}
	
	void move() {
		int p = ways[t][way[t]];
		state[p] = t;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = ways[t][way[t]];
		state[p] = -1;
	}
	
	void anal() {
//		int tm = 5;
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t == size) {
				for (int i = 0; i < size; i++) {
					solutionDist[i][ways[i][way[i]]]++;
				}
				++solutionCount;
				if(solutionCount == 1) printSolution();
///				if(Math.random() < 0.001) printSolution();
///				if(solutionCount % 100 == 0) System.out.println("-->" + solutionCount);
				//return;
			}
		}
		
	}
	
	void printSolution() {
		for (int p = 0; p < size; p++) {
			String s = (state[p] < 0) ? "-" : "" + values[state[p]];
			while(s.length() < 3) s = " " + s;
			System.out.print(s);
			if(field.isRightBorder(p)) System.out.println("");
		}
		System.out.println("");
	}
	
	void printDistribution() {
		System.out.println("Distribution");
		for (int i = 0; i < size; i++) {
			System.out.println("value=" + values[i]);
			for (int p = 0; p < size; p++) {
				String s = "" + solutionDist[i][p];
				while(s.length() < 5) s = " " + s;
				System.out.print(s);
				if(field.isRightBorder(p)) System.out.println("");
			}
			System.out.println("");
			
		}
	}
	
	
	public static void main(String[] args) {
		PairSummedAnalyzer a = new PairSummedAnalyzer();
		PairSummedField f = new PairSummedField();
		f.setSize(5, 5);
		int[] pp = new int[f.getSize()];
		for (int i = 0; i < pp.length; i++) pp[i] = -1;
		pp[1] = 0;
		a.setField(f);
		a.setPresetPositions(pp);
		a.solve();
		System.out.println("Solution count=" + a.solutionCount);
		a.printDistribution();
	}

}
// 4x4 - 7080
// 5x5 - 56816
// 6x6 - 17408
// 6x7 - 72

/*
36 24 26 14 18 28
23 12  1  4 10 35
19 11  3  6 17 27
30  8  5  2 15 32
21 13 22  7  9 31
34 33 20 29 16 25

 42 24 30 19 33 34
 35 18  6 20 13 21
 28 17  1  5  8 29
 38 11  4  3 10 22
 27 15  7  2 12 36
 31 16  9 23 14 26
 41 25 39 32 37 40
*/