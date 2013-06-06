package fitting2d;

import packing3d.Placement;

public class PackingAnalysis {
	RectangularField field;
	int size;
	Placement[][] placements;
	int[] usageLimits;
	char[] designations;
	boolean noShape = false;
	int[] fieldRestriction;
	
	int[] state;	
	int[] shape;
	int[] usage;
	
	
	
	int tMax;

	int[] figure;	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;
	
	public void setPlacements(Placement[][] placements) {
		this.placements = placements;		
	}
	
	public void setUsageLimits(int[] usageLimits) {
		this.usageLimits = usageLimits;
	}
	
	public void setField(RectangularField field) {
		this.field = field;
		this.size = field.size;
	}
	
	public void setDesignations(char[] ds) {
		designations = ds;
	}
	
	public void setShapeDisabled(boolean b) {
		noShape = b;
	}
	
	public void setFieldRestriction(int[] rs) {
		fieldRestriction = rs;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[size];
		shape = new int[size];
		figure = new int[placements.length];
		for (int i = 0; i < size; i++) state[i] = -1;
		usage = new int[usageLimits.length];
		wayCount = new int[200];
		way = new int[200];
		ways = new int[200][1000];
		t = 0;
		tMax = 0;
		for (int i = 0; i < usageLimits.length; i++) tMax += usageLimits[i];
		figure = new int[tMax + 1];
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t >= tMax) return;
		if(t == 0) {
			figure[t] = 0;
		} else {
			figure[t] = figure[t - 1];
			while(usage[figure[t]] == usageLimits[figure[t]]) figure[t]++;
		}
		int imin = 0;
		if(t > 0 && figure[t] == figure[t - 1]) imin = ways[t - 1][way[t - 1]] + 1;
		for (int i = imin; i < placements[figure[t]].length; i++) {
			if(canPlace(i)) {
				ways[t][wayCount[t]] = i;
				wayCount[t]++;
			}
		}
	}
	
	boolean canPlace(int pi) {
		int index = placements[figure[t]][pi].getIndex();
		if(usage[index] == usageLimits[index]) return false;
		int[] ps = placements[figure[t]][pi].getPoints();
		for (int i = 0; i < ps.length; i++) {
			if(fieldRestriction != null && fieldRestriction[ps[i]] > 0) return false;
			if(state[ps[i]] >= 0 || (!noShape && shape[ps[i]] > 0)) return false;
		}
		return true;
	}
	
	void move() {
		int pi = ways[t][way[t]];
		int[] ps = placements[figure[t]][pi].getPoints();
		for (int i = 0; i < ps.length; i++) {
			int p = ps[i];
			state[p] = figure[t];
			shape[p]++;
			for (int d = 0; d < 4; d++) {
				int q = field.jump(p, d);
				if(q >= 0) shape[q]++;
			}
		}
//		int index = placements[figure[t]][pi].getIndex();
		usage[figure[t]]++;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int pi = ways[t][way[t]];
		int[] ps = placements[figure[t]][pi].getPoints();
		for (int i = 0; i < ps.length; i++) {
			int p = ps[i];
			state[p] = -1;
			shape[p]--;
			for (int d = 0; d < 4; d++) {
				int q = field.jump(p, d);
				if(q >= 0) shape[q]--;
			}
		}
		usage[figure[t]]--;
	}
	
	void anal() {
		srch();
		way[t] = -1;
		//int tt = 0;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return; else back();
			}
			way[t]++;
			move();
			if(t == tMax) {
				++solutionCount;
				printSolution();
			}
		}		
	}
	
	void printSolution() {
		System.out.println("Solution " + solutionCount);
		for (int iy = field.ySize - 1; iy >= 0; iy--) {
			for (int ix = 0; ix < field.xSize; ix++) {
				int p = field.xy(ix, iy);
				char c = (state[p] < 0) ? 'B' : designations[state[p]];
				System.out.print(c);
			}
			System.out.println(",");
		}
	}

}
