package com.slava.sudoku;

public class SudokuSubsetFiller extends SudokuSolver {
	SudokuSolver solver;
	
	int[] places;
	int[] colorUsage;
	int tripleColorCount;
	
	int[] problem;
	int solvableProblemCount;
	
	public SudokuSubsetFiller() {
		state = new SudokuSubsetState();
	}
	
	public void setSolver(SudokuSolver solver) {
		solver.firstForceMoveCount = 4;
		this.solver = solver;
			setRandomizing(true);
		setField(solver.field);
		int[] iv = new int[field.getSize()];
		for (int i = 0; i < iv.length; i++) iv[i] = -1;
		solver.setInitialValues(iv);
		best = 20;
		solver.setSolutionLimit(best);
		solver.setTreeCountLimit(150);
	}
	
	public void setForm(int[] places) {
		this.places = places;
	}
	
	void init0() {
		super.init0();
		colorUsage = new int[9];
	}
	
	protected void init() {
		super.init();
		for (int c = 0; c < colorUsage.length; c++) colorUsage[c] = 0;
		tripleColorCount = 0;
		problem = null;
		solvableProblemCount = 0;
	}
	
	protected void doSrch() {
		if(t == places.length) return;
		int p = places[t];
		boolean isNew = false;
		for (int c = 0; c < colorUsage.length; c++) {
			if(colorUsage[c] == 0) {
				if(isNew) break; else isNew = true;
			}
			if(state.canAdd(p, c)) {
				if(isNew && places.length - t < 9 - c) wayCount[t] = 0;
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
	
	int best;

	protected void onSolutionFound() {
		if((solutionCount % 20000) == 0 && solvableProblemCount * 200 < solutionCount) {
			while(t > 0) back();
		}
		if(solutionCount % 50000 == 0) System.out.println("-->" + solutionCount + " " + solvableProblemCount);
		if(solutionCount > 1000000) {
			while(t > 0) back();
			return;
		}
//		if(true) return;
		for (int i = 0; i < places.length; i++) {
			int p = places[i];
			solver.initialValues[p] = state.getValue(p);
		}
		solver.solve();
		int sc = solver.solutionCount;
		if(solver.solution != null) {
			solvableProblemCount++;
//			if(solvableProblemCount == 1) solver.printSolution(solver.initialValues);
		}
		if(sc > 0 && sc < best) {
			best = sc;
			System.out.println(best);
			if(best == 1) {
				solver.printSolution(solver.initialValues);
				problem = (int[])solver.initialValues.clone();
				return;
			} else {
				solver.setSolutionLimit(best);
			}
		}
	}
	
	public int[] getProblem() {
		return problem;
	}
	
	class SudokuSubsetState extends SudokuState {
		public boolean canAdd(int p, int v) {
			return super.canAdd(p, v)
			  && (colorUsage[v] < 2 || (colorUsage[v] == 2 && tripleColorCount < 5))
			;
		}

		public void add(int p, int v) {
			super.add(p, v);
			colorUsage[v]++;
			if(colorUsage[v] == 3) tripleColorCount++;
		}
		
		public void remove(int p, int v) {
			super.remove(p, v);
			if(colorUsage[v] == 3) tripleColorCount--;
			colorUsage[v]--;
		}
	}

}

