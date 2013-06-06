package match2005;

public class SudokuRunner {

	public static void run(boolean isSuper) {
	  SudokuField field = new SudokuField();
	  field.setWidth(9, isSuper);
	  int extectedResult = (isSuper) ? 14 : 19;
	  SudokuGenerator g = new SudokuGenerator();
	  g.setExpectedResult(extectedResult);
	  g.setField(field);
	  g.generateMinimum();
	}

	public static void runStandard() {
	  run(false);
	}

	public static void runSuper() {
	  run(true);
	}
	
	static int[] PROBLEM = {
		1,0,6, 0,0,0, 0,0,0,
		0,0,0, 2,8,0, 0,0,0,
		0,0,0, 0,4,0, 0,3,0,

		0,0,0, 1,0,0, 5,0,0,
		0,2,0, 0,0,0, 0,4,0,
		0,0,5, 6,0,7, 0,8,0,

		0,0,0, 7,0,0, 0,0,6,
		4,8,0, 0,0,0, 0,0,0,
		0,9,0, 0,0,0, 0,0,0,
	};
	
	public static void solve() {
		SudokuField field = new SudokuField();
		field.setWidth(9, false);
		SudokuSolver solver = new SudokuSolver();
		solver.setField(field);
		for (int i = 0; i < PROBLEM.length; i++) PROBLEM[i]--;
		solver.setInitialValues(PROBLEM);
		solver.setSolutionLimit(10000000);
		solver.solve();
		System.out.println("Solutions=" + solver.solutionCount);
		int[] best = solver.getNarrowest();
		System.out.println("Narrow: " + best[0] + " " + best[1] + " " + best[2]);
	}

	public static void main(String[] args) {
//	  SudokuRunner.runStandard();
//		SudokuRunner.runSuper();
		solve();
	}

}
