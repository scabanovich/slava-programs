package com.slava.sudoku;

public class SudokuSolver {
	protected ISudokuField field;
	protected int[][] regions;
	
	protected int[] initialValues;
	protected int[][] initialRangeRestrictions;
	
	protected SudokuState state = new SudokuState();

	protected int t;
	protected int[] wayCount;
	protected int[] way;
	protected int[][] waysP;
	protected int[][] waysC;
	protected int[] treeEstimate;
	
	protected boolean randomizing;
	protected int solutionCount;
	protected int solutionLimit;
	protected int treeCount;
	protected int treeCountLimit = -1;
	
	protected int[] solution;
	
	protected int stopAtVacancies;
	protected int firstForceMoveCount = -1;
	protected int lowTreeSizeMoveFactor = 4;
	protected int startTreeSizeLimit = 16;
	
	protected boolean makeDistribution = false;
	protected int[][] distribution;
	
	public SudokuSolver() {}
	
	public void setField(ISudokuField field) {
		this.field = field;
		init0();
	}
	
	public void setInitialValues(int[] vs) {
		initialValues = vs;
	}
	
	public void setInitialRangeRestrictions(int[][] rs) {
		initialRangeRestrictions = rs;
	}
	
	public void setSolutionLimit(int i) {
		solutionLimit = i;
	}
	
	/**
	 * If treeCountLimit option is set to value > 0,
	 * then too "complex" problem will be considered as 
	 * having too many solutions (even if in reality it 
	 * has a unique solution)
	 * @param i
	 */	
	public void setTreeCountLimit(int i) {
		treeCountLimit = i;
	}
	
	public void setRandomizing(boolean b) {
		randomizing = b;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init0() {
		state.setField(field);
		regions = field.getRegions();

		wayCount = new int[field.getSize() + 1];
		way = new int[field.getSize() + 1];
		waysP = new int[field.getSize() + 1][field.getColorCount()];
		waysC = new int[field.getSize() + 1][field.getColorCount()];
		treeEstimate = new int[field.getSize() + 1];
		distribution = new int[field.getSize()][field.getColorCount()];
	}
	
	protected void init() {
		treeEstimate = new int[field.getSize() + 1];
		
		state.init();

		if(initialValues != null) for (int i = 0; i < initialValues.length; i++) {
			if(initialValues[i] >= 0) state.add(i, initialValues[i]);
		}
		if(initialRangeRestrictions != null) {
			for (int i = 0; i < initialRangeRestrictions.length; i++) {
				if(initialRangeRestrictions[i] != null) state.addRangeRestriction(i, initialRangeRestrictions[i]);
			}
		}
		if(makeDistribution) {
			for (int i = 0; i < field.getSize(); i++) for (int c = 0; c < field.getColorCount(); c++) distribution[i][c] = 0;
		}
		
		t = 0;
		solutionCount = 0;
		treeCount = 0;
		solution = null;
		treeEstimate[0] = 1;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(isFinished()) return;
		if(isWrong()) return;
		doSrch();
		if(t > 0) treeEstimate[t] = treeEstimate[t - 1] * wayCount[t];
		if(firstForceMoveCount > 0 && t < firstForceMoveCount && wayCount[t] > 1) {
			wayCount[t] = 0;
		}
		if(randomizing) {
			randomize();
		}
	}
	
	protected boolean isWrong() {
		return false;
	}
	
	protected void doSrch() {
		int pb = -1;
		int w = field.getColorCount() + 1;
		for (int p = 0; p < field.getSize(); p++) {
			if(state.getValue(p) >= 0) continue;
			if(state.getFreeColorCount(p) < w) {
				w = state.getFreeColorCount(p);
				pb = p;
				if(w == 0) break;
			}
		}
		if(pb < 0 || w == 0) return;
		for (int v = 0; v < field.getColorCount(); v++) {
			if(state.isUsed(pb, v)) continue;
			waysP[t][wayCount[t]] = pb;
			waysC[t][wayCount[t]] = v;
			wayCount[t]++;
		}
		if(w == 1) return;
		for (int r = 0; r < regions.length; r++) {
			if(state.freeRegionColorCount[r] == 0) continue;
			if(regions[r].length < field.getColorCount()) continue; //small region
			for (int v = 0; v < field.getColorCount(); v++) {
				if(state.usedRegionColors[r][v] == 1) continue;
				int wr = 0;
				for (int i = 0; i < regions[r].length; i++) {
					int p = regions[r][i];
					if(state.values[p] >= 0 || state.used[p][v] > 0) continue;
					++wr;
				}
				if(wr < wayCount[t]) {
					wayCount[t] = 0;
					for (int i = 0; i < regions[r].length; i++) {
						int p = regions[r][i];
						if(state.getValue(p) >= 0 || state.isUsed(p, v)) continue;
						waysP[t][wayCount[t]] = p;
						waysC[t][wayCount[t]] = v;
						wayCount[t]++;
					}
					wr = wayCount[t];
					if(wayCount[t] == 0) return;
				}
			}
		}
	}
	
	void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = wayCount[t] - 1; i >= 1; i--) {
			int k = (int)((i + 1) * Math.random());
			if(k == i) continue;
			int c = waysC[t][i];
			waysC[t][i] = waysC[t][k];
			waysC[t][k] = c;
			c = waysP[t][i];
			waysP[t][i] = waysP[t][k];
			waysP[t][k] = c;
		}
	}
	
	protected void move() {
		int p = waysP[t][way[t]];
		int v = waysC[t][way[t]];
//		System.out.println("t=" + t + " p=" + p + " v=" + v + " wc=" + wayCount[t] + " w=" + way[t]);
		state.add(p, v);
		++t;
		srch();
		way[t] = -1;
	}
	
	protected void back() {
		--t;
		int p = waysP[t][way[t]];
		int v = waysC[t][way[t]];
		state.remove(p, v);
	}

	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(firstForceMoveCount > 0 && 
			   t < lowTreeSizeMoveFactor * firstForceMoveCount && 
			   treeEstimate[t] > startTreeSizeLimit) {
				solution = null;
				solutionCount = 0;
				return;
			}
			if(wayCount[t] == 0) {
				treeCount++;				
				if(treeCountLimit > 0 &&
						(treeCount > treeCountLimit || treeEstimate[t] > treeCountLimit)  
				) {
					solutionCount = solutionLimit + 1;
					return;
				}
			}
			if(stopAtVacancies > 0 && stopAtVacancies == state.vacancies) {
				solution = (int[])state.values.clone();
				return;
			}
			if(isFinished()) {
				solutionCount++;
				if(solutionCount == 1) {
					solution = (int[])state.values.clone();
				}
				if(solutionLimit > 0 && solutionCount >= solutionLimit) return;
				onSolutionFound();
				if(solutionCount % 100000 == 0) {
					double d = getTaskCompleteIndex();
					long estimate = d == 0 ? -1 : (long)(solutionCount / d);
					System.out.println("Solutions=" + solutionCount + " Completed=" + d + " Estimate=" + estimate);
				}
			}
		}
	}
	
	protected void onSolutionFound() {
		if(makeDistribution) {
			for (int p = 0; p < state.values.length; p++) {
				distribution[p][state.values[p]]++;
			}
		}		
	}
	
	private double getTaskCompleteIndex() {
		double p = 1d;
		double s = 0d;
		for (int i = 0; i < t; i++) {
			p = p / wayCount[i];
			s += p * way[i];
		}
		return s;
	}
	
	public int[] getNarrowestPlace() {
		if(!makeDistribution) return null;
		if(solutionCount < 2) return null;
		int q = solutionCount;
		int pb = -1;
		int cb = -1;
		for (int p = 0; p < state.values.length; p++) {
			for (int c = 0; c < field.getColorCount(); c++) {
				if(distribution[p][c] > 0 && distribution[p][c] < q) {
					q = distribution[p][c];
					pb = p;
					cb = c;
				}
			}
		}
		return new int[]{pb, cb, q};
	}
	
	protected boolean isFinished() {
		return state.vacancies == 0;
	}
	
	public int[] getSolution() {
		return solution;
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}
	
	public void printSolution(int[] solution) {
		System.out.print(solutionToString(solution));
	}
	
	public String solutionToString(int[] solution) {
		return field.printSolution(solution);
	}
	
	public int getTreeCount() {
		return treeCount;
	}

}
