package olia;

import java.util.Arrays;

public class UniquePascalTriangle {
	int size;
	int[][] cmn;

	int[] coefficients;
	int[][] restCoefficients;
	
	int[] usedNumbers;
	int[][] state;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int bestSum;
	
	public UniquePascalTriangle() {}
	
	public void setSize(int s) {
		size = s;
		initCmn();
	}
	
	public void initCmn() {
		cmn = new int[size][];
		for (int i = 0; i < size; i++) {
			cmn[i] = new int[i + 1];
			cmn[i][0] = 1;
			cmn[i][i] = 1;
			for (int j = 1; j < i; j++) {
				cmn[i][j] = cmn[i - 1][j - 1] + cmn[i - 1][j];
			}
		}
		coefficients = (int[])cmn[size - 1].clone();
		restCoefficients = new int[size][];
		for (int i = 0; i < size; i++) {
			restCoefficients[i] = new int[size - i];
			System.arraycopy(coefficients, i, restCoefficients[i], 0, restCoefficients[i].length);
			backSort(restCoefficients[i]);
		}
	}
	
	void backSort(int[] a) {
		Arrays.sort(a);
		for (int i = 0; i < a.length / 2; i++) {
			int c = a[i];
			a[i] = a[a.length - i - 1];
			a[a.length - i - 1] = c;
		}
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[size][size];
		usedNumbers = new int[100000];
		wayCount = new int[size + 1];
		way = new int[size + 1];
		ways = new int[size + 1][1000];
		t = 0;
		bestSum = 100000;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == size) return;
		if(getEstimate(-1) > bestSum) return;
		for (int n = 1; n < 1000; n++) {
			if(!canAdd(n)) continue;
			add(n);
			boolean bad = getEstimate(n) > bestSum * 3 / 2;
			remove(n);
			if(bad) break;
			ways[t][wayCount[t]] = n;
			wayCount[t]++;
		}
	}
	
	boolean canAdd(int n) {
		if(usedNumbers[n] > 0) return false;
		for (int i = 1; i <= t; i++) {
			n = n + state[i - 1][t - 1];
			if(usedNumbers[n] > 0) return false;
		}		
		return true;
	}
	
	void add(int n) {
		usedNumbers[n]++;
		state[0][t] = n;
		for (int i = 1; i <= t; i++) {
			n = n + state[i - 1][t - 1];
			usedNumbers[n]++;
			state[i][t] = n;
		}		
	}
	
	void remove(int n) {
		usedNumbers[n]--;
		for (int i = 1; i <= t; i++) {
			n = n + state[i - 1][t - 1];
			usedNumbers[n]--;
		}		
	}
	
	void move() {
		int n = ways[t][way[t]];
		add(n);
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int n = ways[t][way[t]];
		remove(n);
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
			if(t == size && state[t - 1][t - 1] <= bestSum) {
				bestSum = state[t - 1][t - 1];
				printSolution();
			}
		}
	}
	
	int getEstimate(int n) {
		int s = 0;
		for (int i = 0; i < t; i++) {
			s += coefficients[i] * state[0][i];
		}
		int k = 0;
		if(n > 0) {
			s += restCoefficients[t][0] * n;
			k = 1;
		}
		for (int m = 0; m < 1000 && k < restCoefficients[t].length; m++) {
			if(usedNumbers[m] > 0) continue;
			s += restCoefficients[t][k] * m;
			++k;
		}
		return s;
	}
	
	void printSolution() {
		System.out.println("bestSum = " + bestSum);
		for (int i = 0; i < size; i++) {
			System.out.print(" " + state[0][i]);
		}
		System.out.println("");
	}
	
	
	public static void main(String[] args) {
		UniquePascalTriangle p = new UniquePascalTriangle();
		p.setSize(9);
		p.solve();
	}

}
