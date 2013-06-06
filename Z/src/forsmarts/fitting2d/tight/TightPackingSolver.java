package forsmarts.fitting2d.tight;

import com.slava.common.RectangularField;
import forsmarts.fitting2d.Placement;

public class TightPackingSolver {
	protected RectangularField field;
	int size;

	Placement[][] placements;
	int[] usageLimits;
	int[] fillLimits;
	int showSolutionLimit = -1;
	int solutionLimit = -1;
	
	protected int[] state;
	int[] usage;	
	
	int tMax;

	protected int t;
	protected int[] wayCount;
	protected int[] way;
	protected int[][] waysF;
	protected int[][] waysI;
	
	protected int solutionCount;
	
	public TightPackingSolver() {}

	public void setPlacements(Placement[][] placements) {
		this.placements = placements;		
	}
	
	public void setUsageLimits(int[] usageLimits) {
		this.usageLimits = usageLimits;
	}
	
	public void setField(RectangularField field) {
		this.field = field;
		this.size = field.getSize();
	}
	
	public void setFillLimits(int[] rs) {
		fillLimits = rs;
	}
	
	public void setShowSolutionLimit(int c) {
		showSolutionLimit = c;
	}
	
	public void setSolutionLimit(int c) {
		solutionLimit = c;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[size];
		for (int i = 0; i < size; i++) state[i] = 0;
		usage = new int[usageLimits.length];
		wayCount = new int[200];
		way = new int[200];
		waysF = new int[200][1000];
		waysI = new int[200][1000];
		t = 0;
		tMax = 0;
		for (int i = 0; i < usageLimits.length; i++) tMax += usageLimits[i];
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(getLostCellsCount() > 6) return;
		int p = getNextP();
		if(p < 0) return;

		for (int f = 0; f < placements.length; f++) {
			if(usage[f] >= usageLimits[f]) continue;
			for (int i = 0; i < placements[f].length; i++) {
				if(canPlace(f, i, p)) {
					waysF[t][wayCount[t]] = f;
					waysI[t][wayCount[t]] = i;
					wayCount[t]++;
				}
			}
		}
	}
	
	int getLostCellsCount() {
		int c = 0;
		for (int p = 0; p < size; p++) {
			if(field.getX(p) == 0 || field.isRightBorder(p) 
//				|| field.getY(p) == 0
				) {
				c += state[p];
			}
		}
		return c;
	}
	
	int completed;
	
	int getNextP() {
		completed = 0;
		for (int y = 0; y < field.getHeight() - 1; y++) {
			for( int x = 1; x < field.getWidth() - 1; x++) {
				int p = field.getIndex(x, y);
				if(state[p] < fillLimits[p]) return p;
				completed++;
			}
		}
//		for (int p = 0; p < size; p++) {
//			if(state[p] < fillLimits[p]) return p;
//		}
		return -1;
	}
	
	boolean canPlace(int f, int i, int p) {
		int index = placements[f][i].getIndex();
		int[] points = placements[f][i].getPoints();
		if(usage[index] >= usageLimits[index]) return false;
		boolean hasP = false;
		for (int qi = 0; qi < points.length; qi++) {
			int q = points[qi];
			if(q == p) hasP = true;
			if(state[q] >= fillLimits[q]) return false;
		}
		return hasP;
	}
	
	void move() {
		int f = waysF[t][way[t]];
		int pi = waysI[t][way[t]];
		int[] ps = placements[f][pi].getPoints();
		for (int i = 0; i < ps.length; i++) {
			int p = ps[i];
			state[p]++;
			//visual
		}
		usage[f]++;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int f = waysF[t][way[t]];
		int pi = waysI[t][way[t]];
		int[] ps = placements[f][pi].getPoints();
		for (int i = 0; i < ps.length; i++) {
			int p = ps[i];
			state[p]--;
			//visual
		}
		usage[f]--;
	}
	
	void anal() {
		srch();
		way[t] = -1;
		int tm = 0, cm = 0;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return; else back();
			}
			way[t]++;
			move();
			getNextP();
			if(completed > cm) {
				cm = completed;
				System.out.println("t=" + t + " " + completed);
			}
			if(checkSolution()) {
				++solutionCount;
				if(showSolutionLimit < 0 || showSolutionLimit >= solutionCount) printSolution();
				if(solutionLimit > -1 && solutionCount >= solutionLimit) return;
			}
		}		
	}
	
	void printSolution() {
		System.out.println("Solution");
	}
	
	/**
	 * Override if need to check matching additional conditions.
	 * @return
	 */	
	protected boolean checkSolution() {
		int p = getNextP();
		return p < 0 || completed >= 36;
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}
	

}
