package slava.puzzle.sudoku.solver;

public class SudokuRunner {
	
	public static void run(boolean isSuper) {
		SudokuField field = new SudokuField();
		field.setWidth(9, isSuper);
		int extectedResult = (isSuper) ? 14 : 19;
		SudokuGenerator g = new SudokuGenerator();
		g.setExpectedResult(extectedResult);
		g.setField(field);
//		g.generateMinimum();
		g.processForms();
	}	

	public static void runStandard() {
		run(false);
	}
	
	public static void runSuper() {
		run(true);
	}
	
	static int[] PROBLEM = {
		1,7,0,6,8,3,0,4,0,
		0,2,0,0,0,7,0,0,5,
		0,0,3,0,0,0,6,0,0,
		0,0,0,4,0,0,0,5,0,
		6,0,0,0,5,0,0,0,4,
		0,5,0,0,0,6,0,0,0,
		0,0,4,0,1,0,7,0,0,
		7,0,0,3,0,0,0,8,0,
		0,8,0,0,2,0,0,0,9,
	};
	
	public static void solve() {
		SudokuField field = new SudokuField();
		field.setWidth(9, false);
		SudokuSolver solver = new SudokuSolver();
		solver.setField(field);
		solver.setSolutionLimit(200000);
		solver.makeDistribution = true;
		solver.setTreeCountLimit(-1);
		int[] problem = (int[])PROBLEM.clone();
		for (int i = 0; i < problem.length; i++) problem[i]--;
		solver.setInitialValues(problem);
		solver.solve();
		System.out.println("SolutionCount=" + solver.solutionCount + " TreeCount=" + solver.treeCount);
		if(solver.solution != null) {
			solver.printSolution(solver.solution);
			int[] narrowest = solver.getNarrowestPlace();
			if(narrowest != null)
			System.out.println("Narrowest p=" + narrowest[0] + " c=" + narrowest[1] + " q=" + narrowest[2]);
		}
	}
	
	public static void main(String[] args) {
//		SudokuRunner.runStandard();
//		SudokuRunner.runSuper();
		solve();
	}

}

/**
Problem 13 (Attempt=668833)
 8 + 9 + + + + + +
 + + + + + 2 + 6 +
 + + + + + + + + +
 + + + 5 + + 7 + +
 + 1 + + + + + + +
 + 2 6 + + + + + +
 + + + 2 + + + + +
 5 + + + + + + 3 +
 + + + + + + + 1 +
Solution  Complexity=4
 8 6 9 1 3 7 2 4 5
 1 4 3 9 5 2 8 6 7
 2 5 7 4 8 6 1 9 3
 4 9 8 5 6 3 7 2 1
 3 1 5 7 2 9 6 8 4
 7 2 6 8 4 1 3 5 9
 6 3 4 2 1 5 9 7 8
 5 7 1 6 9 8 4 3 2
 9 8 2 3 7 4 5 1 6
 
Problem 19 (Attempt=1355109)
 + + + + + + 6 4 +
 + + + 3 + + 1 + +
 + 9 + 2 5 + + + +
 6 + + + + + + + +
 + + + 1 + + + + 8
 + 4 + + + + + 7 +
 + 2 3 + 7 9 + + +
 + + + + 4 + + + +
 8 + + + + + + + 5
Solution  Complexity=1
Problem 19 (Attempt=1079724)
 + + 5 + + + + + +
 + + 1 + 8 + + + +
 + + + 5 + 9 3 + 7
 5 + + 6 3 + + + +
 + + + + + + + 1 +
 + + + + + + + 4 2
 + + 4 + + + + 8 +
 + + + + 7 + + + 9
 6 + + 4 + + + + +
Solution  Complexity=1
Problem 19 (Attempt=4700281)
 + + + + + + + + +
 + + + 9 + 4 + + 7
 + 6 + + + 3 5 + +
 + 2 7 + + + + + +
 + + + + 6 5 + 8 +
 9 + + + + + + + +
 + + 3 + + + + + 4
 + + 4 + 5 + + + +
 + + + + 7 + + 6 8
Solution  Complexity=1
Problem 19 (Attempt=348237)
 + + 8 1 9 + + + 6
 + + 3 7 + + + + 1
 + 5 + + + + + + +
 + + 6 + + + + + +
 + + + + + 2 7 5 +
 + + + + + + 4 + +
 5 + + + + + + 4 +
 + + + + + + + + 8
 9 2 + + + 7 + + +
Solution  Complexity=1
 
*/
