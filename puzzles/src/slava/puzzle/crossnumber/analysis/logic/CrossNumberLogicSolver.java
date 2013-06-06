package slava.puzzle.crossnumber.analysis.logic;

import slava.puzzle.crossnumber.CrossNumberField;

public class CrossNumberLogicSolver {
	CrossNumberField field;
	int size;
	Region[] regions;
	Region[][] regionsByPoint;
	boolean doNotRebuildRegions = false;
	
	RegionFiller[] regionFillers = new RegionFiller[10];

	int[] values;
	int[][] restrictions;
	
	int changeCount;
	int lastChangeCount;

	String resultInfo;
	int resultIndex; //-1 initial, 0 - solution found, 1 - unfilled cells left, 2 - contradictions
	
	public CrossNumberLogicSolver() {}

	public void setField(CrossNumberField field) {
		this.field = field;
	}
	
	public void solve() {
//		long ts = System.currentTimeMillis();
		init();
		while(!checkContradictions()) {
			lastChangeCount = changeCount;
//			System.out.println(changeCount);
			iteration();
			if(changeCount == lastChangeCount) break;
		}
		boolean c = checkContradictions();
//		long dt = System.currentTimeMillis() - ts;
//		System.out.println(dt);
		if(c) {
			resultInfo = "Contradictions found";
			resultIndex = 2;
		} else if(getUnsetValuesCount() > 0) {
			resultInfo = "" + getUnsetValuesCount() + " cells left not filled";
			resultIndex = 1;
		} else {
			resultInfo = "Logical solution found";
			resultIndex = 0;
		}
	}
	
	void init() {
		size = field.size();
		if(regions == null || !doNotRebuildRegions) {
			regions = RegionFactory.createRegions(field);
			regionsByPoint = RegionFactory.getRegionsByPoints(size, regions);
		}
		values = new int[size];
		restrictions = new int[size][10];
		applyFilters();
		setLastValues();
		for (int i = 1; i < regionFillers.length; i++) {
			regionFillers[i] = new RegionFiller();
			regionFillers[i].init(i, values, restrictions);
		}
		changeCount = 0;
		lastChangeCount = 0;
		resultInfo = null;
		resultIndex = -1;
	}
	
	private void applyFilters() {
		for (int i = 0; i < regions.length; i++) {
			if(regions[i].sum <= 0) continue;
			int[] filter = SumUtil.instance.filters[regions[i].points.length][regions[i].sum];
			for (int k = 0; k < regions[i].points.length; k++) {
				int p = regions[i].points[k];
				for (int j = 0; j < restrictions[p].length; j++) {
					if(filter[j] == 0) restrictions[p][j] = 1;
				}
			}
		}
	}
	
	private void setValue(int p, int v) {
		values[p] = v;
		boolean res = true;
		Region[] rs = regionsByPoint[p];
		if(rs != null) for (int i = 0; i < rs.length; i++) {
			for (int k = 0; k < rs[i].points.length; k++) {
				int q = rs[i].points[k];
				if(restrictions[q][v] == 0) {
					restrictions[q][v] = 1;
					++changeCount;
				}
			}
			if(!checkRegion(rs[i])) res = false;
		}
		if(!res) values[p] = -1;
	}
	
	private boolean checkRegion(Region r) {
		if(r.sum < 0) return true;
		int s = 0;
		for (int i = 0; i < r.points.length; i++) {
			int p = r.points[i];
			int v = values[p];
			if(v <= 0) return true;
			s += v;
		}
		
		return s == r.sum;
	}
	
	private void setLastValues() {
		for (int p = 0; p < size; p++) setLastValue(p);
	}
	
	private void setLastValue(int p) {
		if(values[p] > 0 || field.getStatus(p) != 4) return;
		int vi = -1;
		int vc = 0;
		for (int v = 1; v < 10 && vc < 2; v++) {
			if(restrictions[p][v] > 0) continue;
			vi = v;
			vc++;
		}
		if(vc == 1) setValue(p, vi);
	}
	
	private void iteration() {
		for (int i = 0; i < regions.length; i++) {
			if(regions[i].sum <= 0) continue;
			applyRegion(regions[i]);
			if(checkContradictions()) return;
		}
		setLastValues();
	}
	
	private void applyRegion(Region r) {
		if(!canApplyRegion(r)) return;
		RegionFiller f = regionFillers[r.points.length];
		f.setRegion(r);		
		f.solve();
//		System.out.println("->" + f.setCount);
		int[][] filter = f.getFilter();
		if(filter == null) return;
//		if(f.setCount == 0) {
//			System.out.println("No way for " + r.toString());
//			throw new RuntimeException("Contradiction");
//		}
		for (int i = 0; i < r.points.length; i++) {
			int p = r.points[i];
			if(values[p] > 0) continue;
			for (int v = 0; v < restrictions[p].length; v++) {
				if(filter[i][v] == 0 && restrictions[p][v] == 0) {
					restrictions[p][v] = 1;
					changeCount++;
				}
			}
		}
	}
	
	private boolean canApplyRegion(Region r) {
		if(r.sum <= 0) return false;
		for (int i = 0; i < r.points.length; i++) {
			int p = r.points[i];
			if(values[p] <= 0) return true;
		}
		return false;
	}
	
	private boolean checkContradictions() {
		boolean res = false;
		for (int p = 0; p < size; p++) {
			if(values[p] < 0) return true;
			if(values[p] != 0) continue;
			if(findValue(p) < 0) {
				values[p] = -1;
				res = true;
			}
		}
		return res;
	}
	
	private int findValue(int p) {
		for (int v = 1; v < 10; v++) {
			if(restrictions[p][v] == 0) return v;
		}
		return -1;
	}
	
	int getUnsetValuesCount() {
		int res = 0;
		for (int p = 0; p < size; p++) {
			if(field.getStatus(p) == 4 && values[p] <= 0) res++;
		}
		return res;
	}
	
	public int[] getSolution() {
		return values;
	}
	
	public String getResultInfo() {
		return resultInfo;
	}
	
	public int getResultIndex() {
		return resultIndex;
	}
	
}
