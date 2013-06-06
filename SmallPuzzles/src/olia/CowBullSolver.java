package olia;

import java.util.ArrayList;

public class CowBullSolver {
	boolean canStartWithZero = true;
	int numberSize = 5;
	
	int[][] numbers;
	
	int[][] seeds;
	int[] bulls;
	int[] cows;
	
	int solutionCount;
	
	int[] solutionIndices = new int[100];
	
	public CowBullSolver() {}
	
	public void setNumberLength(int n) {
		numberSize = n;
	}
	
	void generateNumbers() {
		ArrayList list = new ArrayList();
		int[] usedDigits = new int[10];
		int t = 0;
		int[] number = new int[numberSize];
		int[] way = new int[numberSize + 1];
		way[0] = -1;
		boolean completed = false;
		while(!completed) {
			while(t == numberSize || way[t] == 9) {
				if(t == 0) {
					completed = true;
					break;
				}
				--t;
				usedDigits[way[t]]--;
			}
			if(completed) break;
			way[t]++;
			if(t == 0 && way[t] == 0 && !canStartWithZero) continue;
			if(usedDigits[way[t]] > 0) continue;
			number[t] = way[t];
			usedDigits[way[t]]++;
			++t;
			way[t] = -1;
			if(t == numberSize) {
				list.add(number.clone());
			}
		}
		numbers = (int[][])list.toArray(new int[0][]);
		System.out.println(numbers.length);
	}
	
	/**
	 * {  { number, bulls, cows }, ... }
	 * @param data
	 */	
	public void setData(int[][] data) {
		seeds = new int[data.length][];
		bulls = new int[data.length];
		cows = new int[data.length];
		for (int i = 0; i < data.length; i++) {
			seeds[i] = digits(data[i][0]);
			bulls[i] = data[i][1];
			cows[i] = data[i][2];
		}
	}
	
	int[] digits(int number) {
		int[] ns = new int[numberSize];
		for (int i = numberSize - 1; i >= 0; i--) {
			ns[i] = number % 10;
			number = number / 10;
		}
		return ns;
	}
	
	public void solve() {
		solutionCount = 0;
		for (int i = 0; i < numbers.length; i++) {
			if(matches(numbers[i])) {
				if(solutionCount < solutionIndices.length) {
					solutionIndices[solutionCount] = i;
				}
				solutionCount++;
			}
		}
	}
	
	boolean matches(int[] number) {
		for (int i = 0; i < seeds.length; i++) {
			if(!matches(number, i)) return false;
		}
		return true;
	}
	
	boolean matches(int[] number, int seedIndex) {
		return getBullCount(number, seeds[seedIndex]) == bulls[seedIndex] 
            && getCowCount(number, seeds[seedIndex]) == cows[seedIndex];
	}
	
	int getBullCount(int[] n1, int[] n2) {
		int c = 0;
		for (int i = 0; i < n1.length; i++) {
			if(n1[i] == n2[i]) c++;
		}		
		return c;
	}
	
	int getCowCount(int[] n1, int[] n2) {
		int c = 0;
		for (int i = 0; i < n2.length; i++) {
			boolean b = false;
			for (int j = 0; j < n1.length; j++) {
				if(i == j) continue;
				if(n1[j] == n2[i]) {
					b = true;
					break;
				}
			}
			if(b) c++;
		}		
		return c;
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}
	
	public void printSolutions() {
		for (int i = 0; i < solutionCount && i < solutionIndices.length; i++) {
			int[] number = numbers[solutionIndices[i]];
			for (int j = 0; j < number.length; j++) {
				System.out.print(number[j]);
			}
			System.out.println("");
		}
	}
	

	static int[][] DATA = {
//		{56079, 0, 2},
//		{12308, 1, 2},
//		{45691, 2, 0},
//		{30742, 2, 1},
		
		{1324, 0, 2},
		{5761, 1, 1},
    	{7819, 2, 1},
    	{2673, 1, 1},
//	    {2719, 4, 0}
	
	};
	
	public static void main(String[] args) {
		CowBullSolver solver = new CowBullSolver();
		solver.setNumberLength(4);
		solver.generateNumbers();
		solver.setData(DATA);
		solver.solve();
		System.out.println("Solutions=" + solver.getSolutionCount());
		if(solver.getSolutionCount() <= 10) {
			solver.printSolutions();
		}
	}
}
