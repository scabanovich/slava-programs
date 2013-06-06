package ic2006_3;

import com.slava.common.RectangularField;
import com.slava.common.TwoDimField;

public class PentaPathSolver {
	RectangularField field;
	TwoDimField octafield;
	PentaPathPlacement[][] placements; //[point][index]
	
	int[] pentaState;
	int[] pentaRestrictions;
	int[] pathState;
	char[] charState;
	
	int[] usedFigures;
	
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] waysD;
	int[][] waysP;
	int[][] waysF;
	
	int turnCount;
	int turnCountLimit = 23;
	
	int treeCount;
	
	public PentaPathSolver() {}
	
	public void setField(RectangularField f) {
		field = f;
		octafield = new TwoDimField();
		octafield.setOrts(TwoDimField.DIAGONAL_ORTS);
		octafield.setSize(f.getWidth(), f.getHeight());
	}
	
	public void setPlacementsForPoints(PentaPathPlacement[][] ps) {
		placements = ps;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		pentaState = new int[field.getSize()];
		pentaRestrictions = new int[field.getSize()];
		pathState = new int[field.getSize()];
		charState = new char[field.getSize()];
		for (int i = 0; i  < charState.length; i++) charState[i] = '-';
		
		usedFigures = new int[12];
		
		place = new int[200];
		wayCount = new int[200];
		way = new int[100];
		waysD = new int[100][200];
		waysP = new int[100][200];
		waysF = new int[100][200];
		
		t = 0;
		turnCount = 0;
		treeCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == 56) return;
		if(turnCount > turnCountLimit) return;
		if(t < 40 && turnCount > 10) return;
//		if(t < 50 && turnCount > 10) return;
		if(t == 0) {
			int q = 59; //starting point p=70 : best = 20
			PentaPathPlacement[] ps = placements[q];
			for (int k = 0; k < ps.length; k++) {
				if(canAdd(ps[k])) {
					waysD[t][wayCount[t]] = -1;
					waysP[t][wayCount[t]] = q;
					waysF[t][wayCount[t]] = k;
					wayCount[t]++;
				}
			}
		} else if(t % 5 == 0) {
			int p = place[t - 1];
			for (int d = 0; d < 4; d++) {
				int q = field.jump(p, d);
				if(q < 0 || pentaState[q] > 0 || pathState[q] > 0) continue;

				PentaPathPlacement[] ps = placements[q];
				for (int k = 0; k < ps.length; k++) {
					if(canAdd(ps[k])) {
						waysD[t][wayCount[t]] = d;
						waysP[t][wayCount[t]] = q;
						waysF[t][wayCount[t]] = k;
						wayCount[t]++;
					}
				}
			}
		} else {
			int p = place[t - 1];
			for (int d = 0; d < 4; d++) {
				int q = field.jump(p, d);
				if(q < 0 || pentaState[q] > 0 || pathState[q] > 0) continue;

				waysD[t][wayCount[t]] = d;
				waysP[t][wayCount[t]] = q;
				waysF[t][wayCount[t]] = -1;
				wayCount[t]++;
			}
		}
		if(t < 30) randomize();
	}
	
	void randomize() {
		if(wayCount[t] < 2) return;
		int m = 5;
		if(wayCount[t] < m) m = wayCount[t];
		for (int i = 0; i < m; i++) {
			int j = i + (int)((wayCount[t] - i) * Math.random());
			int s = waysD[t][i]; waysD[t][i] = waysD[t][j]; waysD[t][j] = s;
			s = waysP[t][i]; waysP[t][i] = waysP[t][j]; waysP[t][j] = s;
			s = waysF[t][i]; waysF[t][i] = waysF[t][j]; waysF[t][j] = s;
		}
		wayCount[t] = m;
	}
	
	boolean canAdd(PentaPathPlacement placement) {
		if(usedFigures[placement.getIndex()] > 0) return false;
		int[] points = placement.getPoints();
		for (int i = 0; i < points.length; i++) {
			int p = points[i];
			if(pentaState[p] > 0 || pentaRestrictions[p] > 0 || pathState[p] > 0) return false;
		}
		return true;
	}
	
	void move() {
		int p = waysP[t][way[t]];
		int k = waysF[t][way[t]];
		place[t] = p;
		pathState[p] = t + 1;
		charState[p] = '+';
		if(k >= 0) {
			PentaPathPlacement placement = placements[p][k];
			add(placement);
		}
		if(t > 1 && waysD[t][way[t]] != waysD[t - 1][way[t - 1]]) {
			turnCount++;
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void add(PentaPathPlacement placement) {
		usedFigures[placement.getIndex()]++;
		int[] points = placement.getPoints();
		for (int i = 0; i < points.length; i++) {
			int p = points[i];
			pentaState[p]++;
			charState[p] = PentaPathFigures.DESIGNATIONS[placement.getIndex()];
			for (int d = 0; d < 8; d++) {
				int q = octafield.jump(p, d);
				if(q < 0) continue;
				pentaRestrictions[q]++;
			}
		}
	}
	
	void back() {
		--t;
		int p = waysP[t][way[t]];
		int k = waysF[t][way[t]];
		pathState[p] = 0;
		charState[p] = '-';
		if(k >= 0) {
			PentaPathPlacement placement = placements[p][k];
			remove(placement);
		}
		if(t > 1 && waysD[t][way[t]] != waysD[t - 1][way[t - 1]]) {
			turnCount--;
		}
	}
	
	void remove(PentaPathPlacement placement) {
		usedFigures[placement.getIndex()]--;
		int[] points = placement.getPoints();
		for (int i = 0; i < points.length; i++) {
			int p = points[i];
			pentaState[p]--;
			charState[p] = '-';
			for (int d = 0; d < 8; d++) {
				int q = octafield.jump(p, d);
				if(q < 0) continue;
				pentaRestrictions[q]--;
			}
		}
	}

	void anal() {
		int tm = 0;
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(wayCount[t] == 0) {
				treeCount++;
				if(treeCount > 100000) {
					if(tm == 55) System.out.println("x " + tm);
					if(tm < 56) {
						return;
					}
					if(treeCount > 10000000) return;
				}
			}
			if(t > tm) {
				tm = t;
//				System.out.println(tm);
			}
			if(t == 56 && turnCount < turnCountLimit) {
				printSolution();
				turnCountLimit = turnCount;
			}
		}
	}
	
	void printSolution() {
		System.out.println("Turns=" + turnCount);
		System.out.println("Path");
		for (int p = 0; p < pathState.length; p++) {
			char ch = (char)(97 + ((pathState[p] - 1) % 26));
			System.out.print(" " + ch);
			if(field.isRightBorder(p)) System.out.println("");
		}
		System.out.println("Penta");
		for (int p = 0; p < pentaState.length; p++) {
			System.out.print(" " + charState[p]);
			if(field.isRightBorder(p)) System.out.println("");
		}
		System.out.println("");
		
	}
	
}
