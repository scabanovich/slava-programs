package com.slava.binary;

public class DoubleBeginningInBinarySequence {
	int length;
	int doubleLength;
	int[] state;

	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;
	
	public void setLength(int length) {
		this.length = length;
		doubleLength = length * 2;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[doubleLength];
		wayCount = new int[length + 1];
		way = new int[length + 1];
		ways = new int[length + 1][2];
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == length) return;
		if(!test(t)) return;
		for (int i = 0; i < 2; i++) {
			ways[t][wayCount[t]] = i;
			wayCount[t]++;
		}
	}
	
	void move() {
		int p = t;
		int c = ways[t][way[t]];
		state[p] = c;
		state[p + length] = c;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
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
			if(t == length && test(doubleLength - 1)) {
				solutionCount++;
//				printSolution();
			}
		}
	}
	
	boolean test(int limit) {
		int limitHalf = limit / 2;
		for (int h = 1; h <= limitHalf; h++) {
			boolean q = true;
			for (int c = 0; c < h && q; c++) {
				q = state[c] == state[c + h];
			}
			if(q) return false;
		}
		return true;
	}
	
	void printSolution() {
		for (int i = 0; i < doubleLength; i++) {
			System.out.print(state[i]);
		}
		System.out.println("");
	}
	
	public static void compute() {
		int max = 30;
		int[] m = new int[max];
		m[1] = 2;
		double sum = 0;
		long factor = 4;
		sum += 1d * m[1] / factor;
		for (int i = 2; i < max; i++) {
			m[i] = compute(i);
			int s = 2 * m[i - 1] - m[i];
			factor *= 4;
			sum += 1d * m[i] / factor;
			double guess = sum + 1d * m[i] / factor;
			System.out.println("2 * m[" + (i - 1) + "] - m[" + i + "]=" + s + " sum=" + guess);
		}
	}
	
	static int compute(int length) {
		DoubleBeginningInBinarySequence s = new DoubleBeginningInBinarySequence();
		s.setLength(length);
		s.solve();
		return s.solutionCount;
	}
	
	public static void main(String[] args) {
		compute();
	}

}
