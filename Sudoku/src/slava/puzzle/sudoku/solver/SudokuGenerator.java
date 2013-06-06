package slava.puzzle.sudoku.solver;

import java.io.*;

import slava.puzzle.sudoku.solver.restriction.IGeneratedRestriction;
import slava.puzzle.sudoku.solver.restriction.IRestriction;

public class SudokuGenerator {
	ISudokuField field;
	SudokuSolver solver = new SudokuSolver();
	SudokuSolver solver2 = new SudokuSolver();
	int[] problem;
	int[] solution;
	int treeCount;
	String symmetry = Symmetries.NONE;
	
	int[] start;
	
	int expectedResult;
	int[] forceRemoveNumbersFrom;
	
	public void setForceRemoveNumbersFrom(int[] fs) {
		forceRemoveNumbersFrom = fs;
	}
	
	public void setField(ISudokuField field) {
		this.field = field;
		solver.setField(field);
		solver.setRandomizing(true);
		solver.setSolutionLimit(1);
		solver2.setField(field);
		solver2.setSolutionLimit(2);
	}

	public void setSymmetry(String s) {
		symmetry = s;
	}
	
	public void setExpectedResult(int c) {
		expectedResult = c;
	}
	
	public int[] generate(int[] template, int matTreeCount) {
		while(true) {
			problem = null;
			solver.setTreeCountLimit(10000);
				IRestriction[] rs = solver.field.getRestrictions();
				if(rs != null) for (int i = 0; i < rs.length; i++) {
					if(rs[i] instanceof IGeneratedRestriction) ((IGeneratedRestriction)rs[i]).reset();
				}
			while(problem == null) {
				solver.solve();
				problem = solver.solution;
			}
			solution = (int[])problem.clone();
			for (int i = 0; i < solution.length; i++) {
				if(template[i] == 0) problem[i] = -1;
			}
				if(rs != null) for (int i = 0; i < rs.length; i++) {
					if(rs[i] instanceof IGeneratedRestriction) ((IGeneratedRestriction)rs[i]).setGeneratedSolution((int[])problem.clone());
				}
			solver2.setInitialValues(problem);
			if(matTreeCount > 0) solver2.setTreeCountLimit(matTreeCount);
			solver2.solve();
			if(solver2.solutionCount != 1) continue;
			treeCount = solver2.getTreeCount();
			if(matTreeCount > 0 && treeCount > matTreeCount) continue;
			return problem;
		}
	}

	public int[] generate() {
		problem = null;
		while(problem == null) {
			solver.solve();
			problem = solver.solution;
		}
		solution = (int[])problem.clone();
		solver2.setInitialValues(problem);
		reduce(5, -1, -1);
		if(expectedResult < getNumberCount() - 1) return problem;
		reduce(1000, -1, -1);
		return problem;
	}

	public int[] generateLogical() {
		return generate(1, -1, -1);
	}

	public int[] generateLogical(int stepsLimit, int narrowest) {
		return generate(1, stepsLimit, narrowest);
	}

	public int[] generate(int complexity, int stepsLimit, int narrowest) {
		problem = null;
		solver.setTreeCountLimit(30000);
		IRestriction[] rs = solver.field.getRestrictions();
		if(rs != null) for (int i = 0; i < rs.length; i++) {
			if(rs[i] instanceof IGeneratedRestriction) ((IGeneratedRestriction)rs[i]).reset();
		}
		while(problem == null) {
			solver.solve();
			problem = solver.solution;
		}
		solution = (int[])problem.clone();
		if(rs != null) for (int i = 0; i < rs.length; i++) {
			if(rs[i] instanceof IGeneratedRestriction) ((IGeneratedRestriction)rs[i]).setGeneratedSolution((int[])problem.clone());
		}
		solver2.setInitialValues(problem);
		reduce(complexity, stepsLimit, narrowest);
		return problem;
	}

	void reduce(int complexity, int stepsLimit, int narrowest) {
		solver2.setTreeCountLimit(complexity);
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
			int q = field.getSymmetry(symmetry, p);
			int vq = -1;
			if(q >= 0) {
				if(q < p) continue;
				if(q > p) {
					vq = problem[q];
					problem[q] = -1;
				}
			}

			problem[p] = -1;
			
			solver2.solve();
			if(solver2.solutionCount != 1) {
				problem[p] = v;
				if(q > p) {
					problem[q] = vq;
				}
			} else {
				if(complexity == 1 && stepsLimit > 0 && narrowest > 0) {
					SudokuLogicalSolver logical = new SudokuLogicalSolver();
					logical.setField(solver.field);
					logical.setInitialValues(problem);
					logical.solve();
					if(!isAcceptableLogicalSolution(logical, stepsLimit, narrowest)) {
						problem[p] = v;
						if(q > p) {
							problem[q] = vq;
						}
						continue;
					}
				}
				treeCount = solver2.treeCount;
			}
		}
	}

	private boolean isAcceptableLogicalSolution(SudokuLogicalSolver logical, int stepsLimit, int narrowest) {
		if(logical.getSolution() == null) return false;
		int[] steps = logical.getStepVolumes();
		if(steps.length > stepsLimit) return false;
		for (int i = 0; i < steps.length - 1; i++) {
			if(steps[i] < narrowest) return false;
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
	
	public void generateMinimum() {
		int attemptCount = 0;
		int bestProblemCount = 0; 
		int netxToBestCount = 0;
		int best = 100;
		while(true) {
			attemptCount++;
			problem = generate();
			int c = getNumberCount();
			if(c == best) bestProblemCount++;
			else if(c == best + 1) netxToBestCount++;
			if(attemptCount % 1000 == 0) {
				System.out.println("Attempts=" + attemptCount + " Best Problems=" + bestProblemCount + "/" + netxToBestCount);
			}
			if(c >= best) continue;
			netxToBestCount = bestProblemCount;
			bestProblemCount = 1;
			best = c;
			StringBuffer sb = new StringBuffer();
			sb.append("Problem " + c + " (Attempt=" + attemptCount + ")").append("\n");
			sb.append(solver.solutionToString(problem));
			sb.append("Solution " + " Complexity=" + treeCount).append("\n");
			sb.append(solver.solutionToString(solution));
			String out = sb.toString();
			System.out.print(out);
			if(c <= expectedResult) printToFile(out, c);
		}
	}
	int x = 1, Y = 0;
	int[] FORM = { //10 + 2
		x,0,0,0,0,x,0,0,0,	
		0,0,0,0,0,0,0,x,0,	
		0,0,0,0,0,0,0,0,0,	
		0,0,0,0,0,0,0,0,0,	
		x,0,0,0,0,x,0,0,0,	
		0,0,0,0,0,0,0,x,0,	
		0,0,x,x,x,0,0,0,0,	
		0,0,0,0,0,0,0,0,x,	
		0,0,0,0,x,0,0,0,x,	
	};
	
	public void processForms() {
//		int begin = 7;
//		for (int i = begin; i < FORM.length; i++) {
//			if(FORM[i] == 1) continue;
//			System.out.println("i=" + i);
//			FORM[i] = 1;
//			for (int j = i + 1; j < FORM.length; j++) {
//				if(FORM[j] == 1) continue;
//				FORM[j] = 1;
//				processForm();
//				FORM[j] = 0;
//			}
//			FORM[i] = 0;
//		}
		while(true) {
			generateForm(17);
			processForm();
		}
		
	}
	
	private void generateForm(int size) {
		for (int i = 0; i < FORM.length; i++) FORM[i] = 0;
		int k = 0;
//		int[] set = {0,70,32,17};
		int[] set = {0,2,17,28};
		for (int i = 0; i < set.length; i++) {
			FORM[set[i]] = 1;
			k++;
		}
		while(k < size) {
			int i = (int)(FORM.length * Math.random());
			if(FORM[i] == 1) continue;
			FORM[i] = 1;
			k++;
		}
	}
	
	public int[] processForm(int[] form, int maxTreeCount) {
		int[] problem = null;
		while(problem == null) {
			SudokuSubsetFiller filler = new SudokuSubsetFiller();
			filler.setTreeCountLimit(1);
			filler.setSolver(solver2);
			if(maxTreeCount > 0) {
				solver2.setTreeCountLimit(maxTreeCount);
			}
			int pc = 0;
			for (int i = 0; i < form.length; i++) {
				if(form[i] > 0) pc++;
			}
			int[] places = new int[pc];
			pc = 0;
			for (int i = 0; i < form.length; i++) {
				if(form[i] > 0) {
					places[pc] = i;
					++pc;
				}
			}
			filler.setForm(places);
			filler.solve();
			problem = filler.getProblem();
		}
		treeCount = solver2.getTreeCount();
		return problem;
	}

	public void processForm() {
		SudokuSubsetFiller filler = new SudokuSubsetFiller();
		filler.setSolver(solver2);
		int pc = 0;
		for (int i = 0; i < FORM.length; i++) {
			if(FORM[i] == 1) pc++;
		}
		int[] places = new int[pc];
		pc = 0;
		for (int i = 0; i < FORM.length; i++) {
			if(FORM[i] == 1) {
				places[pc] = i;
				++pc;
			}
		}
		filler.setForm(places);
		filler.solve();
		if(filler.solvableProblemCount != 0) {
			System.out.println(filler.solutionCount + " : " + filler.solvableProblemCount);
		}
		int[] problem = filler.getProblem();
		if(problem != null) {
			String out = solver2.solutionToString(problem);
			printToFile(out, pc);			
		}
	}
	
	void printToFile(String out, int s) {
		File f = new File("./sudoku" + s + ".txt");
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
	
	public int getTreeCount() {
		return treeCount;
	}

}
