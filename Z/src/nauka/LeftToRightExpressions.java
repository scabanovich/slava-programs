package nauka;

import java.util.*;

public class LeftToRightExpressions {
	int[] numbers;
	int maxValue = 100;
	
	int[] usedNumbers;
	int[] state;
	
	int tLim;
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int[] distribution;
	Map results = new HashMap();
	
	public LeftToRightExpressions() {}	
	
	public void setNumbers(int[] ns) {
		numbers = ns;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		tLim = 2 * numbers.length - 1;
		distribution = new int[maxValue + 1];
		usedNumbers = new int[numbers.length];
		state = new int[tLim];
		wayCount = new int[tLim + 1];
		way = new int[tLim + 1];
		ways = new int[tLim + 1][numbers.length];
		t = 0;
		results.clear();
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == tLim) return;
		if(t % 2 == 0) {
			for (int i = 0; i < numbers.length; i++) {
				if(usedNumbers[i] > 0) continue;
				ways[t][wayCount[t]] = i;
				wayCount[t]++;
			}
		} else {
			//sign
			wayCount[t] = 4;
			for (int i = 0; i < 4; i++) ways[t][i] = i;
		}
	}
	
	void move() {
		int w = ways[t][way[t]];
		state[t] = w;
		if(t % 2 == 0) {
			usedNumbers[w] = 1;
		}
		t++;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int w = ways[t][way[t]];
		if(t % 2 == 0) {
			usedNumbers[w] = 0;
		}
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] >= wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t == tLim) {
				onSolutionFound();
			}
		}
	}
	
	void onSolutionFound() {
		double d = compute();
		if(d < 0 || d >= maxValue) return;
		int n = (int)Math.round(d);
		if(Math.abs(d - n) > 0.000001) return;
		if(n < 0 || n > maxValue) return;
		distribution[n]++;
		if(distribution[n] > 1) return;
		String r = "" + n + "=" + printState();
		results.put(new Integer(n), r);
	}
	
	double compute() {
		double r = numbers[state[0]];
		for (int i = 1; i < numbers.length; i++) {
			int tt = i * 2;
			r = operation(r, numbers[state[tt]], state[tt - 1]);
		}
		return r;
	}
	
	double operation(double a, int b, int op) {
		if(op == 0) {
			return a + b;
		} else if(op == 1) {
			return a - b;
		} else if(op == 2) {
			return a * b;
		} else {
			if(b == 0) return Double.MAX_VALUE;
			return a / b;
		}
	}
	
	static char[] SIGNS = {'+','-','*','/'};
	
	String printState() {
		StringBuffer sb = new StringBuffer();
		for (int k = 0; k < t; k++) {
			int s = state[k];
			if(k % 2 == 0) {
				sb.append(numbers[s]);
			} else {
				sb.append(SIGNS[s]);
			}			
		}
		return sb.toString();
	}
	
	public void printUniqueResults() {
		for (int n = 0; n < distribution.length; n++) {
			if(distribution[n] == 1) {
				Integer ni = new Integer(n);
				String s = (String)results.get(ni);
				if(s != null) System.out.println(s);
			}
		}
	}
	
	static int[] generateNumbers(int min, int max, int q) {
		int[] used = new int[max + 1];
		int[] r = new int[q];
		for (int i = 0; i < q; i++) {
			while(true) {
				int c = (int)((max - min + 1) * Math.random()) + min;
				if(used[c] == 1) continue;
				used[c] = 1;
				r[i] = c;
				break;
			}
		}
		Arrays.sort(r);
		return r;
	}
	
	public static void runExample() {
		LeftToRightExpressions p = new LeftToRightExpressions();
		p.setNumbers(new int[]{12,16,18,22,23});
		p.solve();
		p.printUniqueResults();
	}
	
	public static void searchForExample() {
		int n = 10;  // result
		while(true) {
			LeftToRightExpressions p = new LeftToRightExpressions();
			int[] ns = generateNumbers(3, 22, 5); // (min, max, quantity)
			p.setNumbers(ns);
			p.maxValue = 30;
			p.solve();
			if(p.distribution[n] == 1) {
				p.printUniqueResults();
				break;
			}
			System.out.println(p.distribution[n]);
			
		}
	}
	
	static double f(double p, double k) {
		double a = (1d - p) / 2d;
		double b = k;
		double c = a - b;
		return (a) * Math.log(a) - b * Math.log(b)
		- (c) * Math.log(c) - (3d * p - 1d) / 2d * Math.log(2d);
	}
	
	static void sf() {
		double p = 0.4d;
		double k = 0.001;
		double dk = 0.001;
		while(k < 0.99d) {
			double r = f(p, k);
			if(r > 0) {
				System.out.println("p=" + p + " k=" + k);
				double s = (1d + p) / 2d - k;
				System.out.println("s=" + s);
				return;
			}
			k += dk;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(19d/29);
		sf();
//		runExample();
		searchForExample();
	}

}

//2=22-19*15+11/28
//10=22-15*19-23/11
//3=23-16*12-18/22

//18=41-22*47-29/18-30

//1=35/8-4*32-11
//1=15/30+4*8-35
//1=17/34-2*18+28
//1=49/7-1*2-11
