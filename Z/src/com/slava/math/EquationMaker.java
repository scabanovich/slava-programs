package com.slava.math;

/**
 * This program tries to insert arithmetic signs and parenthesis
 * between given ordered set of numbers so that the expression
 * obtained resulted to the given number;  
 */

public class EquationMaker {
	static char[] SIGNS = new char[]{'+', '-', '*', '/'};

	double[] initialNumbers;
	double result;
	boolean showResult;
	
	int[] signs;
	int[] order;
	
	int[] solutionSigns;
	int[] solutionOrder;
	
	public void setShowResult(boolean b) {
		showResult = b;
	}
	
	public void init(double[] initialNumbers, double result) {
		this.initialNumbers = initialNumbers;
		this.result = result;
		signs = new int[initialNumbers.length - 1];
		order = new int[initialNumbers.length - 1];
		solutionSigns = null;
		solutionOrder = null;
	}
	
	public void solve() {
		int[] cs = new int[order.length];
		for (int i = 0; i < cs.length; i++) cs[i] = i;
		solve(initialNumbers, cs);
	}
	
	public boolean isSolutionFound() {
		return solutionSigns != null;
	}
	
	void solve(double[] numbers, int[] correspondence) {
		if(solutionOrder != null) return;
		if(numbers.length == 1) {
			if(Math.abs(numbers[0] - result) < 0.0000001d) {
				solutionOrder = (int[])order.clone();
				solutionSigns = (int[])signs.clone();
				if(showResult) printResult();
			}
			return;
		}
		for (int i = 0; i < numbers.length - 1; i++) {
			int[] cs = new int[correspondence.length - 1];
			System.arraycopy(correspondence, 0, cs, 0, i);
			System.arraycopy(correspondence, i + 1, cs, i, cs.length - i);
			order[correspondence[i]] = initialNumbers.length - numbers.length;
			for (int s = 0; s < 4; s++) {
				signs[correspondence[i]] = s;
				double[] ns = new double[numbers.length - 1];
				System.arraycopy(numbers, 0, ns, 0, i);
				if(ns.length - i - 1 > 0) System.arraycopy(numbers, i + 2, ns, i + 1, ns.length - i - 1);
				double a = numbers[i];
				double b = numbers[i + 1];
				if(s == 0) {
					ns[i] = a + b;
				} else if(s == 1) {
					ns[i] = a - b;
				} else if(s == 2) {
					ns[i] = a * b;
				} else {
					if(Math.abs(b) < 0.0000001d) continue;
					ns[i] = a / b;
				}
				solve(ns, cs);				
			}
		}
	}
	
	void printResult() {
		System.out.println("Solution=");
		for (int i = 0; i < initialNumbers.length; i++) {
			System.out.print(" " + initialNumbers[i]);
		}
		for (int i = 0; i < order.length; i++) {
			System.out.print(" " + SIGNS[signs[i]]);
		}
		System.out.println("");
		for (int i = 0; i < order.length; i++) {
			System.out.print(" " + order[i]);
		}
		System.out.println("");
	}
	
	static void test() {
		EquationMaker p = new EquationMaker();
		p.setShowResult(true);
///		for (int i = 1; i < 100; i++) {
		double[] d = new double[]{17d, 74d, 44d};
		double result = 2006d;
		p.init(d, result);
		p.solve();
///		}
	}

	public static void main(String[] args) {
		test();
	}

}
