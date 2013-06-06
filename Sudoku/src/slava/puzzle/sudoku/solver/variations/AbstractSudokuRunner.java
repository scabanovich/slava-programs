package slava.puzzle.sudoku.solver.variations;

import java.io.*;
import slava.puzzle.sudoku.solver.*;

public class AbstractSudokuRunner {
	File f = null;
	int[] currentProblem;
	
	public void setFile(File f) {
		this.f = f;
	}
	
	public void setCurrenProblem(int[] problem) {
		currentProblem = problem;
	}

	protected AbstractSudokuField createSudokeField() {
		return null;
	}
	
	protected void setUpGenerator(AbstractSudokuField field, SudokuGenerator g) {
		
	}

	public void solve() {
		if(currentProblem == null) {
			throw new RuntimeException("Problem is not set");
		}
//		for (int i = 0; i < PROBLEM.length; i++) System.out.print(PROBLEM[i]);
//		System.out.println("");
		int[] problem = (int[])currentProblem.clone();
		for (int i = 0; i < problem.length; i++) problem[i]--;
		solve(problem);		
	}


	public void solve(int[] problem) {
		AbstractSudokuField field = createSudokeField();
		solve(problem, field);
	}

	public void solve(int[] problem, StringBuffer out) {
		AbstractSudokuField field = createSudokeField();
		solve(problem, field, out);
	}

	protected void solve(int[] problem, AbstractSudokuField field) {
		StringBuffer out = new StringBuffer();
		solve(problem, field, out);
		System.out.print(out.toString());
	}

	protected void solve(int[] problem, AbstractSudokuField field, StringBuffer out) {
		SudokuSolver solver = new SudokuSolver();
		solver.setField(field);
		solver.setSolutionLimit(200000);
		solver.makeDistribution = true;
		solver.setTreeCountLimit(-1);
		solver.setInitialValues(problem);
		solver.solve();
		out.append("SolutionCount=" + solver.getSolutionCount() + " TreeCount=" + solver.getTreeCount()).append("\n");
		if(solver.getSolution() != null) {
			String s = solver.solutionToString(solver.getSolution());
			out.append(s);
			int[] narrowest = solver.getNarrowestPlace();
			if(narrowest != null) {
				out.append("Narrowest p=" + narrowest[0] + " c=" + narrowest[1] + " q=" + narrowest[2]).append("\n");
			}
		}
		if(solver.getTreeCount() == 1 && solver.getSolution() != null) {
			solveLogically(problem, out);
		}
	}
	
	public void solveLogically(int[] problem) {
		StringBuffer out = new StringBuffer();
		solveLogically(problem, out);
		System.out.print(out.toString());
	}

	public void solveLogically(int[] problem, StringBuffer out) {
		AbstractSudokuField field = createSudokeField();
		SudokuLogicalSolver logical = new SudokuLogicalSolver();
		logical.setField(field);
		logical.setInitialValues(problem);
		logical.solve();
		if(logical.getSolution() == null) {
			out.append("No logical solution").append("\n");
		} else {
			int[] steps = logical.getStepVolumes();
			String message = "Solved logically in " + steps.length + " steps: ";
			for (int i = 0; i < steps.length; i++) message += " " + steps[i];
			out.append(message).append("\n");
		}
	}

	public void generate(int steps, int narrowest) {
		StringBuffer out = new StringBuffer();
		generate(Symmetries.NONE, steps, narrowest, out);
		System.out.print(out.toString());
	}

	public void generate(String symmetry, int steps, int narrowest, StringBuffer out) {
		AbstractSudokuField field = createSudokeField();
		SudokuGenerator g = new SudokuGenerator();
//		g.setExpectedResult(extectedResult);
		setUpGenerator(field, g);
		if(symmetry != null) {
			g.setSymmetry(symmetry);
		}
		g.setField(field);
		int[] problem = g.generateLogical(steps, narrowest);
		String problemString = field.printSolution(problem);
		out.append("Problem").append("\n");
		out.append(problemString).append("\n");
		if(f != null) saveToFile(problemString);
		solve(problem, field, out);
		for (int i = 0; i < problem.length; i++) problem[i]++;
		currentProblem = problem;
	}

	private void saveToFile(String s) {
		try {
			if(!f.exists()) {
				f.getParentFile().mkdirs();
				f.createNewFile();
			}
			FileWriter w = new FileWriter(f, true);
			w.write(s);
			w.write("\n");
			w.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void generateMany(int steps, int narrowest) {
		while(true) {
			generate(steps, narrowest);
			System.out.print("Generate one more?(y/n):");
			int i = 13;
			try {
				while((i != (int)'y') && (i != (int)'n')) i = System.in.read();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(i != (int)'y') break;
		}
	}
	
	public void run(String[] args) {
		if(args.length > 0 && args[0].equals("-s")  ) {
			solve();
			return;
		}
		File file = null;
		if(args != null && args.length > 0 && !args[0].startsWith("-")) {
			String f = args[0];
			file = new File(f);
		}
		setFile(file);
		int steps = readIntArg(args, "-steps", -1);
		int narrowest = readIntArg(args, "-narrowest", steps < 0 ? -1 : 1);
		generateMany(steps, narrowest);
		solve();
	}

	private int readIntArg(String[] args, String name, int defaultValue) {
		for (int i = 0; i < args.length - 1; i++) {
			if(name.equals(args[i])) {
				String q = args[i + 1];
				try {
					return Integer.parseInt(q);
				} catch (NumberFormatException e) {
					System.out.println("Argument " + name + " must be integer: " + q);
					return defaultValue;
				}
			}
		}
		return defaultValue;
	}
	
	public static void main(String[] args) {
		AbstractSudokuRunner runner = new AbstractSudokuRunner();
		runner.run(args);
	}

}
