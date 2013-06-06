package slava.puzzle.sudoku.solver;
import slava.puzzle.sudoku.solver.restriction.IRestriction;
import slava.puzzle.sudoku.solver.restriction.IRestrictionListener;

public class SudokuLogicalSolver implements IRestrictionListener {
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
	int[] stepVolume;
	
	int[] solution;
	int contradiction = -1;
	
	public SudokuLogicalSolver() {}
	
	public void setField(ISudokuField field) {
		this.field = field;
		init0();
	}
	
	public void setInitialValues(int[] vs) {
		initialValues = vs;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init0() {
		regions = field.getRegions();
		vacancies = field.getSize();
		values = new int[vacancies];
		used = new int[field.getSize()][field.getColorCount()];
		freeCount = new int[vacancies];
		usedRegionColors = new int[regions.length][field.getColorCount()];
		freeRegionColorCount = new int[regions.length];
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

		t = 0;
		solution = null;
		contradiction = -1;
		stepVolume = new int[field.getSize()];
	}
	
	int step() {
		int[] dvalues = new int[values.length];
		for (int i = 0; i < dvalues.length; i++) dvalues[i] = -1;
		for (int p = 0; p < dvalues.length; p++) {
			if(values[p] >= 0) continue;
			if(freeCount[p] == 0) {
				contradiction = p;
				return 0;
			} else if(freeCount[p] == 1) {
				dvalues[p] = findColor(p);
			}
		}
		for (int r = 0; r < freeRegionColorCount.length; r++) {
			if(freeRegionColorCount[r] == 0) continue;
			if(regions[r].length < field.getColorCount()) continue; //small region
			findColorForRegion(r, dvalues);
		}
		int dv = 0;
		for (int p = 0; p < values.length; p++) {
			if(values[p] < 0 && dvalues[p] >= 0) {
				add(p, dvalues[p]);
				dv++;
			}
		}
		return dv;
	}
	protected int findColor(int p) {
		for (int v = 0; v < field.getColorCount(); v++) {
			if(used[p][v] == 0) return v;
		}
		return -1;
	}
	protected void findColorForRegion(int r, int[] dvalues) {
		if(regions[r].length < field.getColorCount()) return; //small region
		for (int v = 0; v < field.getColorCount(); v++) {
			if(usedRegionColors[r][v] == 1) continue;
			int wr = 0;
			int pv = -1;
			for (int i = 0; i < regions[r].length; i++) {
				int p = regions[r][i];
				if(values[p] >= 0 || used[p][v] > 0) continue;
				++wr;
				pv = p;
			}
			if(wr == 1) dvalues[pv] = v;
		}
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
	
	public void include(int p, int v) {
		used[p][v]--;
		if(used[p][v] == 0) {
			freeCount[p]++;
		}
	}
	
	void anal() {
		while(contradiction < 0 && !isFinished()) {
			int dv = step();
			if(dv == 0) break;
			stepVolume[t] = dv;
			++t;
		}
		if(contradiction < 0 && isFinished()) {
			solution = (int[])values.clone();
		}
		int[] s = new int[t];
		System.arraycopy(stepVolume, 0, s, 0, t);
		stepVolume = s;
	}
	
	protected boolean onSolutionFound() {
		return false;
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
		return solution == null ? 0 : 1;
	}
	
	public int[] getStepVolumes() {
		return stepVolume;
	}

}
