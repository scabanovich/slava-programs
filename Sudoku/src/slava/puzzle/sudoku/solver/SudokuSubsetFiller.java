package slava.puzzle.sudoku.solver;

public class SudokuSubsetFiller extends SudokuSolver {
	SudokuSolver solver;
	
	int[] places;
	int[] colorUsage;
	int tripleColorCount;
	
	int[] problem;
	int solvableProblemCount;
	
	public void setSolver(SudokuSolver solver) {
////		solver.firstForceMoveCount = 4;
		this.solver = solver;
			setRandomizing(true);
		setField(solver.field);
		int[] iv = new int[field.getSize()];
		for (int i = 0; i < iv.length; i++) iv[i] = -1;
		solver.setInitialValues(iv);
		best = 20;
		solver.setSolutionLimit(best);
		solver.setTreeCountLimit(5000);
	}
	
	public void setForm(int[] places) {
		this.places = places;
	}
	
	void init0() {
		super.init0();
		colorUsage = new int[field.getColorCount()];
	}
	
	void init() {
		super.init();
		for (int c = 0; c < colorUsage.length; c++) colorUsage[c] = 0;
		tripleColorCount = 0;
		problem = null;
		solvableProblemCount = 0;
	}
	
	protected void doSrch() {
		if(t == places.length) return;
		int p = places[t];
////		boolean isNew = false;
		for (int c = 0; c < colorUsage.length; c++) {
			if(colorUsage[c] == 0) {
////				if(isNew) break; else isNew = true;
			}
			if(canAdd(p, c)) {
////				if(isNew && places.length - t < 9 - c) wayCount[t] = 0;
				waysP[t][wayCount[t]] = p;
				waysC[t][wayCount[t]] = c;
				wayCount[t]++;
			}
		}
	}
	
	protected boolean isFinished() {
		return t == places.length 
//		       && tripleColorCount == 0
		       ;
	}
	
	protected boolean canAdd(int p, int v) {
		return super.canAdd(p, v)
//		  && (colorUsage[v] < 2 || (colorUsage[v] == 2 && tripleColorCount == 0))
		;
	}

	protected void add(int p, int v) {
		super.add(p, v);
		colorUsage[v]++;
		if(colorUsage[v] == 3) tripleColorCount++;
	}
	
	protected void remove(int p, int v) {
		super.remove(p, v);
		if(colorUsage[v] == 3) tripleColorCount--;
		colorUsage[v]--;
	}
	
	int best;

	protected boolean onSolutionFound() {
		if((solutionCount % 20000) == 0 && solvableProblemCount * 200 < solutionCount) {
			while(t > 0) back();
		}
		if(solutionCount % 50000 == 0) System.out.println("-->" + solutionCount + " " + solvableProblemCount);
		if(solutionCount > 1000000) {
			while(t > 0) back();
			return false;
		}

//		if(true) return;
		for (int i = 0; i < places.length; i++) {
			int p = places[i];
			solver.initialValues[p] = values[p];
		}
		solver.solve();
		int sc = solver.solutionCount;
		if(solver.solution != null) {
			solvableProblemCount++;
//			if(solvableProblemCount == 1) solver.printSolution(solver.initialValues);
		}
		if(sc > 0 && sc < best) {
			best = sc;
			if(best == 1) {
//				solver.printSolution(solver.initialValues);
				problem = (int[])solver.initialValues.clone();
				return true;
			} else {
				solver.setSolutionLimit(best);
			}
		}
		
		return (treeCountLimit > 0 && treeCount > treeCountLimit);
	}
	
	public int[] getProblem() {
		return problem;
	}

}
