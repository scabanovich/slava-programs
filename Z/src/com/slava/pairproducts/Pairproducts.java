package com.slava.pairproducts;

import java.util.*;

public class Pairproducts {
	Map problems = new TreeMap();	
	
	public void execute() {
		int[] products = new int[6];
		for (int i1 = 2; i1 < 25; i1++) {
			for (int i2 = 2; i2 < 25; i2++) {
				products[0] = i1 * i2;
				if(products[0] < 10 || products[0] >= 100) continue;
				for (int i3 = 2; i3 < 25; i3++) {
					products[1] = i1 * i3;
					if(products[1] < 10 || products[1] >= 100) continue;
					products[2] = i2 * i3;
					if(products[2] < 10 || products[2] >= 100) continue;
					for (int i4 = 2; i4 < 25; i4++) {
						products[3] = i1 * i4;
						if(products[3] < 10 || products[3] >= 100) continue;
						products[4] = i2 * i4;
						if(products[4] < 10 || products[4] >= 100) continue;
						products[5] = i3 * i4;
						if(products[5] < 10 || products[5] >= 100) continue;
						int[] numbers = new int[]{i1, i2, i3, i4};
						createProblems(numbers, products);
					}
				}
			}
		}
		int problemCount = 0;
		Iterator it = problems.keySet().iterator();
		while(it.hasNext()) {
			String problem = it.next().toString();
			ProblemInfo info = (ProblemInfo)problems.get(problem);
			if(info.solutionCount != 1) continue;
			if(info.isCanonic()) {
				problemCount++;
				if(Math.random() < 0.01)
					System.out.println("" + problemCount + "  " + info.toString());
			}
		}
	}
	
	private void createProblems(int[] numbers, int[] products) {
		for (int k = 0; k < 64; k++) {
			createProblem(numbers, products, k);
		}
	}

	private void createProblem(int[] numbers, int[] products, int k) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 6; i++) {
			int m = (k % 2);
			k = k / 2;
			String s = "" + products[i];
			if(m == 0) {
				sb.append('-').append(s.charAt(1));
			} else {
				sb.append(s.charAt(0)).append('-');
			}
		}
		String problem = sb.toString();
		ProblemInfo info = (ProblemInfo)problems.get(problem);
		if(info == null) {
			info = new ProblemInfo(problem, numbers);
			problems.put(problem, info);
		}
		info.solutionCount++;
	}

	public static void main(String[] args) {
		Pairproducts p = new Pairproducts();
		p.execute();
	}

}

class ProblemInfo {
	private int[] numbers;
	int solutionCount;
	String problem;

	public ProblemInfo(String problem, int[] numbers) {
		this.numbers = numbers;
		this.problem = problem;
	}
	
	public boolean isCanonic() {
		for (int i = 1; i < numbers.length; i++) {
			if(numbers[i] < numbers[i - 1]) return false;
		}
		return true;
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}
	
	public int[] getNumbers() {
		return numbers;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(problem);
		for (int i = 0; i < numbers.length; i++) {
			if(i > 0) sb.append(','); else sb.append(' ');
			sb.append(numbers[i]);
		}
		return sb.toString();
	}
}