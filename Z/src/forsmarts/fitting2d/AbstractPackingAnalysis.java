package forsmarts.fitting2d;

import com.slava.common.RectangularField;
import com.slava.common.TwoDimField;

public class AbstractPackingAnalysis {
	public static int NO_SHAPE = 0;
	public static int HV_SHAPE = 1;
	public static int HVD_SHAPE = 2;
	
	protected RectangularField field;
	protected TwoDimField octafield;
	protected int size;

	protected Placement[][] placements;

	protected int[] usageLimits;
	protected char[] designations;
	protected int shapeMode = HV_SHAPE;
	protected int[] fieldRestriction;

	int showSolutionLimit = -1;
	int solutionLimit = -1;
	
	protected int[] state;
	protected int[] shape;
	protected int[] usage;
	protected int tMax;

	protected int t;
	protected int[] wayCount;
	protected int[] way;
	
	protected int solutionCount;

	public AbstractPackingAnalysis() {}

	public void setPlacements(Placement[][] placements) {
		this.placements = placements;		
	}
	
	public void setField(RectangularField field) {
		this.field = field;
		this.size = field.getSize();
		octafield = new TwoDimField();
		octafield.setOrts(TwoDimField.DIAGONAL_ORTS);
		octafield.setSize(field.getWidth(), field.getHeight());
	}
	
	public void setUsageLimits(int[] usageLimits) {
		this.usageLimits = usageLimits;
	}
	
	public void setDesignations(char[] ds) {
		designations = ds;
	}
	
	public void setShapeMode(int s) {
		shapeMode = s;
	}
	
	public void setFieldRestriction(int[] rs) {
		fieldRestriction = rs;
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
	
	protected void init() {
		state = new int[size];
		shape = new int[size];
		for (int i = 0; i < size; i++) state[i] = -1;
		usage = new int[usageLimits.length];

		t = 0;
		tMax = 0;
		for (int i = 0; i < usageLimits.length; i++) tMax += usageLimits[i];
		solutionCount = 0;
	}

	protected void srch() {}

	protected void move() {}

	protected void back() {}

	protected void anal() {
		srch();
		way[t] = -1;
		int tm = 0;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return; else back();
			}
			way[t]++;
			move();
			if(t > tm) {
				tm = t;
				System.out.println("t=" + tm);
			}
			if(t == tMax && checkSolution()) {
				++solutionCount;
				if(showSolutionLimit < 0 || showSolutionLimit >= solutionCount) printSolution();
				if(solutionLimit > -1 && solutionCount >= solutionLimit) return;
			}
		}		
	}
	
	/**
	 * Override if need to check matching additional conditions.
	 * @return
	 */	
	protected boolean checkSolution() {
		return true;
	}
	
	static char BLANK = '-';
	static char FILTER = '*'; //BLANK;
	
	protected void printSolution() {
		System.out.println("Solution " + solutionCount);
		for (int iy = field.getHeight() - 1; iy >= 0; iy--) {
			for (int ix = 0; ix < field.getWidth(); ix++) {
				int p = field.getIndex(ix, iy);
				char c = (fieldRestriction != null && fieldRestriction[p] > 0) ? FILTER
					: (state[p] < 0) ? BLANK : designations[state[p]];
				System.out.print(c);
			}
			System.out.println(",");
		}
		printFilter();
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}
	
	void printFilter() {
		if(fieldRestriction == null) return;
		for (int p = 0; p < state.length; p++) {
			if(fieldRestriction[p] > 0) {
				System.out.print(" " + coordinates(p));
			}
		}
		System.out.println("");
	}
	String coordinates(int p) {
		int y = field.getY(p) + 1;
		char x = (char)(65 + field.getX(p));
		return "" + x + "" + y;
	}

}
