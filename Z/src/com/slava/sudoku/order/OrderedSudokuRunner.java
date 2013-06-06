package com.slava.sudoku.order;

import com.slava.sudoku.*;

public class OrderedSudokuRunner {

	static void runTestProblem() {
		SudokuField field = new SudokuField();
		field.setWidth(6, false);
		solveVisual(field, null, ProblemData0.NUMBERS);
	}
	
	static void runFirstProblem() {
		SudokuField field = new SudokuField();
		field.setWidth(9, false);
		solveVisual(field, null, ProblemData1.NUMBERS);
	}
	
	static void runSecondProblem() {
		SudokuField field = new SudokuField();
		field.setWidth(9, false);
		solveVisual(field, null, ProblemData2.NUMBERS);
	}
	
	public static void solveVisual(AbstractSudokuField field, int[] visualProblem, int[][] numbers) {
		int[] problem = null;
		if(visualProblem != null) {
			problem = (int[])visualProblem.clone();
			for (int i = 0; i < problem.length; i++) problem[i]--;
		}
		solve(field, problem, numbers);
	}
	
	public static void solve(AbstractSudokuField field, int[] problem, int[][] numbers) {
		OrderedSudokuSolver solver = new OrderedSudokuSolver();
		solver.setField(field);
		solver.setSolutionLimit(1200000);
//		solver.makeDistribution = true;
		solver.setTreeCountLimit(-1);
		solver.setInitialValues(problem);
//		solver.setInitialRangeRestrictions(initialRangeRestrictions);
		solver.setNumbers(numbers);
		solver.solve();
		System.out.println("SolutionCount=" + solver.getSolutionCount() + " TreeCount=" + solver.getTreeCount());
		if(solver.getSolution() != null) {
			solver.printSolution(solver.getSolution());
			int[] narrowest = solver.getNarrowestPlace();
			if(narrowest != null)
			System.out.println("Narrowest p=" + narrowest[0] + " c=" + narrowest[1] + " q=" + narrowest[2]);
		}
	}
	public static void main(String[] args) {
		runTestProblem();
		runFirstProblem();
		runSecondProblem();
	}

}
