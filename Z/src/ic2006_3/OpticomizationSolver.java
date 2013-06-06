package ic2006_3;

import com.slava.common.RectangularField;
import com.slava.common.TwoDimField;

public class OpticomizationSolver {
	int[] ships = {5, 4, 4, 3, 3, 3, 2, 2, 2, 2};
	RectangularField field;
	TwoDimField octafield;
	
	OpticomizationFiller filler = new OpticomizationFiller();
	
	int[] state;
	int[] restriction;	
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] waysP;
	int[][] waysD;
	
	int solutionCount;
	int goodShadowsCount = 0;
	
	int maxGoodShipCount = 0;
	int maxGoodRayCount = 0;
	
	public OpticomizationSolver() {}
	
	public void setField(RectangularField f) {
		field = f;
		filler.setField(f);
		octafield = new TwoDimField();
		octafield.setOrts(TwoDimField.DIAGONAL_ORTS);
		octafield.setSize(f.getWidth(), f.getHeight());
	}
	
	public void setNumbers(int[] ns) {
		filler.setNumbers(ns);
	}	
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		filler.setState(state);
		restriction = new int[field.getSize()];
		
		wayCount = new int[ships.length + 1];
		way = new int[ships.length + 1];
		waysP = new int[ships.length + 1][200];
		waysD = new int[ships.length + 1][200];
		t = 0;
		
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == ships.length) return;
		int p0 = 0;
		if(t > 0 && ships[t] == ships[t - 1]) {
			p0 = waysP[t - 1][way[t - 1]];
		}
		for (int p = p0; p < field.getSize(); p++) {
			for (int d = 0; d < 2; d++) {
				if(canAdd(p, d)) {
					waysP[t][wayCount[t]] = p;
					waysD[t][wayCount[t]] = d;
					wayCount[t]++;
				}
			}
		}
		if(t < 5) randomize();
	}
	
	void randomize() {
		if(wayCount[t] < 2) return;
		int m = 10;;
		if(wayCount[t] < m) m = wayCount[t];
		for (int i = 0; i < m; i++) {
			int j = i + (int)((wayCount[t] - i) * Math.random());
			int s = waysD[t][i]; waysD[t][i] = waysD[t][j]; waysD[t][j] = s;
			s = waysP[t][i]; waysP[t][i] = waysP[t][j]; waysP[t][j] = s;
		}
		wayCount[t] = m;
	}

	boolean canAdd(int p, int d) {
		int length = ships[t];
		for (int i = 0; i < length; i++) {
			if(p < 0 || state[p] >= 0 || restriction[p] > 0) return false;
			p = field.jump(p, d);
		}		
		return true;
	}
	
	void move() {
		int p = waysP[t][way[t]];
		int d = waysD[t][way[t]];
		add(p, d);
		++t;
		srch();
		way[t] = -1;
	}
	
	void add(int p, int d) {
		int length = ships[t];
		for (int i = 0; i < length; i++) {
			state[p] = t;
			for (int k = 0; k < 8; k++) {
				int q = octafield.jump(p, k);
				if(q >= 0) restriction[q]++;				
			}			
			p = field.jump(p, d);
		}
	}
	
	void back() {
		--t;
		int p = waysP[t][way[t]];
		int d = waysD[t][way[t]];
		remove(p, d);
	}
	
	void remove(int p, int d) {
		int length = ships[t];
		for (int i = 0; i < length; i++) {
			state[p] = -1;
			for (int k = 0; k < 8; k++) {
				int q = octafield.jump(p, k);
				if(q >= 0) restriction[q]--;				
			}			
			p = field.jump(p, d);
		}
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
			if(t == ships.length) {
				solutionCount++;
				onSolutionFound();
			}
		}
	}
	
	void onSolutionFound() {
//		if(solutionCount % 100000 == 0) {
//			System.out.println(solutionCount);
//		}
		boolean b = filler.createShadows();
		if(!b) return;
		goodShadowsCount++;
		if(solutionCount % 10000 == 0) {
			System.out.println(solutionCount + ":" + goodShadowsCount);
		}
		int sc = filler.getGoodShipCount();
		if(sc > maxGoodShipCount) {
			maxGoodShipCount = sc;
			System.out.println("maxGoodShipCount=" + maxGoodShipCount);
			printSolution();
		}
		
		int rc = filler.getGoodRayCount();
		if(rc > maxGoodRayCount) {
			maxGoodRayCount = rc;
			System.out.println("maxGoodRayCount=" + maxGoodRayCount);
			printSolution();
		}
	}
	
	void printSolution() {
		for (int p = 0; p < state.length; p++) {
			char c = (state[p] < 0) ? '+' : 'x';
			System.out.print(" " + c);
			if(field.isRightBorder(p)) System.out.println("");
		}
		System.out.println("");
	}
	                         

}
