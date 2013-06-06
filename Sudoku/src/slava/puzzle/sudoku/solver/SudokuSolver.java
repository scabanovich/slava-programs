package slava.puzzle.sudoku.solver;

import slava.puzzle.sudoku.solver.restriction.*;

public class SudokuSolver implements IRestrictionListener {
	ISudokuField field;
	int[][] regions;
	
	int[] initialValues;
	
	int[][] used; // [p][v]
	int[] freeCount; // [p]
	
	int[][] usedRegionColors;
	int[] freeRegionColorCount;
	
	int[] values;
	int vacancies;

	int t;
	int[] wayCount;
	int[] way;
	int[][] waysP;
	int[][] waysC;
	int[] treeEstimate;
	
	boolean randomizing;
	int solutionCount;
	int solutionLimit;
	int treeCount;
	int treeCountLimit = -1;
	
	int[] solution;
	
	int stopAtVacancies;
	int firstForceMoveCount = -1;
    int lowTreeSizeMoveFactor = 4;
    int startTreeSizeLimit = 16;
	
	public boolean makeDistribution = false;
	int[][] distribution;
	
	public SudokuSolver() {}
	
	public void setField(ISudokuField field) {
		this.field = field;
		init0();
	}
	
	public void setInitialValues(int[] vs) {
		initialValues = vs;
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
		IRestriction[] rs = field.getRestrictions();
		if(rs != null) for (int i = 0; i < rs.length; i++) rs[i].dispose();
	}
	
	void init0() {
		regions = field.getRegions();
		vacancies = field.getSize();
		values = new int[vacancies];
		used = new int[field.getSize()][field.getColorCount()];
		freeCount = new int[vacancies];
		usedRegionColors = new int[regions.length][field.getColorCount()];
		freeRegionColorCount = new int[regions.length];
		wayCount = new int[field.getSize() + 1];
		way = new int[field.getSize() + 1];
		waysP = new int[field.getSize() + 1][field.getColorCount()];
		waysC = new int[field.getSize() + 1][field.getColorCount()];
		treeEstimate = new int[field.getSize() + 1];
		distribution = new int[field.getSize()][field.getColorCount()];
	}
	
	void init() {
		vacancies = field.getSize();
		for (int i = 0; i < values.length; i++) values[i] = -1;
		
		for (int i = 0; i < field.getSize(); i++) for (int j = 0; j < field.getColorCount(); j++) used[i][j] = 0;
		for (int i = 0; i < freeCount.length; i++) freeCount[i] = field.getColorCount();
		
		for (int i = 0; i < regions.length; i++) for (int j = 0; j < field.getColorCount(); j++) usedRegionColors[i][j] = 0;
		for (int i = 0; i < freeRegionColorCount.length; i++) freeRegionColorCount[i] = field.getColorCount();
		
		IRestriction[] rs = field.getRestrictions();
		if(rs != null) for (int i = 0; i < rs.length; i++) rs[i].init(this);

		if(initialValues != null) for (int i = 0; i < initialValues.length; i++) {
			if(initialValues[i] >= 0) add(i, initialValues[i]);
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
		doSrch();
		if(t > 0) treeEstimate[t] = treeEstimate[t - 1] * wayCount[t];
		if(firstForceMoveCount > 0 && t < firstForceMoveCount && wayCount[t] > 1) {
			wayCount[t] = 0;
		}
		if(randomizing) {
			randomize();
		}
	}
	
	protected void doSrch() {
		int pb = -1;
		int w = field.getColorCount() + 1;
		for (int p = 0; p < field.getSize(); p++) {
			if(values[p] >= 0) continue;
			if(freeCount[p] < w) {
				w = freeCount[p];
				pb = p;
				if(w == 0) break;
			}
		}
		if(pb < 0 || w == 0) return;
		for (int v = 0; v < field.getColorCount(); v++) {
			if(used[pb][v] > 0) continue;
			waysP[t][wayCount[t]] = pb;
			waysC[t][wayCount[t]] = v;
			wayCount[t]++;
		}
		if(w == 1) return;
		for (int r = 0; r < freeRegionColorCount.length; r++) {
			if(freeRegionColorCount[r] == 0) continue;
			if(regions[r].length < field.getColorCount()) continue; //small region
			for (int v = 0; v < field.getColorCount(); v++) {
				if(usedRegionColors[r][v] == 1) continue;
				int wr = 0;
				for (int i = 0; i < regions[r].length; i++) {
					int p = regions[r][i];
					if(values[p] >= 0 || used[p][v] > 0) continue;
					++wr;
				}
				if(wr < wayCount[t]) {
					wayCount[t] = 0;
					for (int i = 0; i < regions[r].length; i++) {
						int p = regions[r][i];
						if(values[p] >= 0 || used[p][v] > 0) continue;
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
		add(p, v);
		++t;
		srch();
		way[t] = -1;
	}
	
	protected void add(int p, int v) {
		vacancies--;
		values[p] = v;
		int[] ri = field.getPlaceToRegions()[p];
		for (int r = 0; r < ri.length; r++) {
			int[] region = regions[ri[r]];
			for (int k = 0; k < region.length; k++) {
				exclude(region[k], v);
			}
			usedRegionColors[ri[r]][v] = 1;
			freeRegionColorCount[ri[r]]--;
		}
		IRestriction[] rs = field.getRestrictions();
		if(rs != null) for (int i = 0; i < rs.length; i++) rs[i].add(p, v, this);
	}
	
	public void exclude(int p, int v) {
		used[p][v]++;
		if(used[p][v] == 1) {
			freeCount[p]--;
		}
	}
	
	protected boolean canAdd(int p, int v) {
		return (values[p] == -1) && (used[p][v] == 0);
	}
	
	protected void back() {
		--t;
		int p = waysP[t][way[t]];
		int v = waysC[t][way[t]];
		remove(p, v);
	}

	protected void remove(int p, int v) {
		vacancies++;
		values[p] = -1;
		int[] ri = field.getPlaceToRegions()[p];
		for (int r = 0; r < ri.length; r++) {
			int[] region = regions[ri[r]];
			for (int k = 0; k < region.length; k++) {
				include(region[k], v);
			}
			usedRegionColors[ri[r]][v] = 0;
			freeRegionColorCount[ri[r]]++;
		}
		IRestriction[] rs = field.getRestrictions();
		if(rs != null) for (int i = 0; i < rs.length; i++) rs[i].remove(p, v, this);
	}

	public void include(int p, int v) {
		used[p][v]--;
		if(used[p][v] == 0) {
			freeCount[p]++;
		}
	}
	
	void anal() {
		srch();
		way[t] = -1;
		int tm = 80;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t > tm) {
				System.out.println("-->" + t);
				tm = t;
			}
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
			if(stopAtVacancies > 0 && stopAtVacancies == vacancies) {
				solution = (int[])values.clone();
				return;
			}
			if(isFinished()) {
				solutionCount++;
				if(solutionCount == 1) {
					solution = (int[])values.clone();
				}
				if(solutionLimit > 0 && solutionCount >= solutionLimit) return;
				if(onSolutionFound()) {
					//returns true to stop process
					return;
				}
			}
		}
	}
	
	protected boolean onSolutionFound() {
		if(makeDistribution) {
			for (int p = 0; p < values.length; p++) {
				distribution[p][values[p]]++;
			}
		}
		return false;
	}
	
	public int[] getNarrowestPlace() {
		if(!makeDistribution) return null;
		if(solutionCount < 2) return null;
		int q = solutionCount;
		int pb = -1;
		int cb = -1;
		for (int p = 0; p < values.length; p++) {
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
		return vacancies == 0;
	}
	
	public int[] getSolution() {
		return solution;
	}
	
	public void printSolution(int[] solution) {
		System.out.print(solutionToString(solution));
	}
	
	public String solutionToString(int[] solution) {
		return field.printSolution(solution);
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}

	public int getTreeCount() {
		return treeCount;
	}

}
