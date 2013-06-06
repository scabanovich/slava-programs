package com.slava.sudoku;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class SudokuMaximumGenerator {
	SudokuSolver solver = new SudokuSolver();
	SudokuSolver solver2 = new SudokuSolver();
	int[] problem;
	int[] solution;
	int treeCount;
	
	int[] start;
	
	int expectedResult;
	int[] forceRemoveNumbersFrom;
	
	public void setForceRemoveNumbersFrom(int[] fs) {
		forceRemoveNumbersFrom = fs;
	}
	
	public void setField(ISudokuField field) {
		solver.setField(field);
		solver.setRandomizing(true);
		solver.setSolutionLimit(1);
		solver2.setField(field);
		solver2.setSolutionLimit(2);
	}
	
	public void setExpectedResult(int c) {
		expectedResult = c;
	}
	
	public int[] generate(int minNumberCount) {
		problem = null;
		while(problem == null) {
			solver.solve();
			problem = solver.solution;
		}
		solution = (int[])problem.clone();
		solver2.setInitialValues(problem);
		if(reduce(minNumberCount)) {
			return problem;
		} else {
			return null;
		}
	}
	
	public void generateMaximum() {
		int mnc = 20;
		int attemptCount = 0;
		while(true) {
			attemptCount++;
			int[] p = generate(mnc);
			if(p != null) {
				mnc = getNumberCount();
				System.out.println("mnc=" + mnc);
				StringBuffer sb = new StringBuffer();
				sb.append("Problem " + mnc + " (Attempt=" + attemptCount + ")").append("\n");
				sb.append(solver.solutionToString(problem));
				sb.append("Solution " + " Complexity=" + treeCount).append("\n");
				sb.append(solver.solutionToString(solution));
				String out = sb.toString();
				System.out.print(out);
				printToFile(out, mnc);
			}
		}
	}

	boolean reduce(int minNumberCount) {
		int nc = getNumberCount();
		solver2.setTreeCountLimit(-1);
		int[] check = getRandomOrder();
		if(forceRemoveNumbersFrom != null) {
			for (int i = 0; i < forceRemoveNumbersFrom.length; i++) {
				problem[forceRemoveNumbersFrom[i]] = -1;
			}
		}
		for (int i = 0; i < check.length; i++) {
			int p = check[i];
			int v = problem[p];
			if(v < 0) continue;
			problem[p] = -1;
			solver2.solve();
			if(solver2.solutionCount != 1) {
				problem[p] = v;
			} else {
				treeCount = solver2.treeCount;
				nc--;
				if(nc <= minNumberCount) return false;
			}
		}
		return true;
	}
	
	int[] getRandomOrder() {
		int[] check = new int[problem.length];
		for (int i = 0; i < check.length; i++) check[i] = i;
		for (int i = check.length - 1; i >= 0; i--) {
			int j = (int) ((i + 1) * Math.random());
			if(i != j) {
				int k = check[i];
				check[i] = check[j];
				check[j] = k;
			}
		}
		return check;
	}
	
	void printToFile(String out, int s) {
		File f = new File("./sudokuMax" + s + ".txt");
		System.out.println(f.getAbsolutePath());
		try {
			if(!f.isFile()) f.createNewFile();
			StringBuffer existing = new StringBuffer();
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String ds = null;
			while((ds = br.readLine()) != null) {
				existing.append(ds).append("\n");
			}
			existing.append(out);
			existing.append("\n");
			br.close();
			FileWriter fw = new FileWriter(f);
			fw.write(existing.toString());
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		System.out.println("Saved to file " + f.getAbsolutePath());
	}

	int getNumberCount() {
		int c = 0;
		for (int i = 0; i < problem.length; i++) if(problem[i] >= 0) ++c;
		return c;
	}

	public static void main(String[] args) {
		SudokuField field = new SudokuField();
		field.setWidth(9, false);
		SudokuMaximumGenerator g = new SudokuMaximumGenerator();
		g.setField(field);
		g.generateMaximum();
	}

}
