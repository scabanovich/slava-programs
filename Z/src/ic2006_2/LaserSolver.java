package ic2006_2;

public class LaserSolver implements LaserConstants {	
	LaserField field;
	int[] initialState;
	
	int[] hMirrors;
	int[] vMirrors;
	
	int[] hCrossings;
	int[] vCrossings;
	
	int[] state;
	int[] totalState;
	int totalOneCount;
	
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;
	
	public LaserSolver() {}
	
	public void setField(LaserField f) {
		field = f;
	}
	
	public void setInitialState(int[] s) {
		initialState = s;
	}
	
	public void setMirrorsRestriction(int[] hMirrors, int[] vMirrors) {
		this.hMirrors = hMirrors;
		this.vMirrors = vMirrors;
	}
	
	public void setCrossingRestriction(int[] hCrossings, int[] vCrossings) {
		this.hCrossings = hCrossings;
		this.vCrossings = vCrossings;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		totalState = new int[field.getSize()];
		place = new int[field.getSize()];
		wayCount = new int[field.getSize()];
		way = new int[field.getSize()];
		ways = new int[field.getSize()][8];
		totalOneCount = 0;
		
		for (int p = 0; p < initialState.length; p++) {
			if(initialState[p] == 0) continue;
			for (int d = 0; d < 8; d++) {
				if((initialState[p] & CODES[d]) == 0) continue;
				add(p, d);
			}
		}
		
		System.out.println("totalOneCount=" + totalOneCount);
		
		t = 0;
		solutionCount = 0;
		
	}
	
	void srch() {
		wayCount[t] = 0;
		if(totalOneCount == 0) return;
		if(!checkMirrors()) return;
		if(!checkCrossings()) return;
		int wcm = 5;
		for (int p = 0; p < field.getSize(); p++) {
			if(totalState[p] != 1) continue;
			int wc = getWays(p);
			if(wc == 0) return;
			if(wc < wcm) {
				wcm = wc;
				for (int i = 0; i < wc; i++) ways[t][i] = temp[i];
				place[t] = p;
			}
		}
		if(wcm < 5) {
			wayCount[t] = wcm;
		}
	}
	
	int getDirection(int p) {
		for (int d = 0; d < 8; d++) {
			if((state[p] & CODES[d]) != 0) return d;
		}
		return -1;
	}
	
	int[] temp = new int[8];
	
	int getWays(int p) {
		int wc = 0;
		int d0 = getDirection(p);
		for (int d = 0; d < 8; d++) {
			if(d0 == d || ((d % 2) != (d0 % 2))) continue;
			int q = field.jump(p, d, 1);
			if(q < 0 || totalState[q] > 1) continue;
			if(totalState[q] == 1) {
				int d1 = getDirection(q);
				if(d1 == REVERSE[d] || ((REVERSE[d] % 2) != (d1 % 2))) continue;
			}
			temp[wc] = d;
			wc++;
		}
		return wc;
	}
	
	void move() {
		int p = place[t];
		int d = ways[t][way[t]];
		add(p, d);
		++t;
		srch();
		way[t] = -1;
	}
	
	void add(int p, int d) {
		if(totalState[p] == 0) {
			totalOneCount++; 
		} else {
			if(totalState[p] == 1)	totalOneCount--;
		}
		totalState[p]++;
		state[p] = state[p] + CODES[d];
		int q = field.jump(p, d, 1);
		if(q < 0) return;
		if(totalState[q] == 0) totalOneCount++; else if(totalState[q] == 1)	totalOneCount--;
		totalState[q]++;
		state[q] = state[q] + CODES[REVERSE[d]];
	}
	
	void back() {
		--t;
		int p = place[t];
		int d = ways[t][way[t]];
		remove(p, d);
	}
	
	void remove(int p, int d) {
		totalState[p]--;
		if(totalState[p] == 0) totalOneCount--; else if(totalState[p] == 1)	totalOneCount++;
		state[p] = state[p] - CODES[d];
		int q = field.jump(p, d, 1);
		if(q < 0) return;
		totalState[q]--;
		if(totalState[q] == 0) totalOneCount--; else if(totalState[q] == 1)	totalOneCount++;
		state[q] = state[q] - CODES[REVERSE[d]];
	}
	
	void anal() {
		srch();
		way[t] = -1;
		int q = 0;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(isFinished() && checkMirrors() && checkConnectedness() && checkCrossings()) {
				++solutionCount;
				onSolutionFound();
			}
			q++;
			if(q == 10000000) {
				q = 0;
				System.out.println("-->" + getPercent());
			}
		}
	}
	
	boolean checkMirrors() {
		for (int iy = 0; iy < hMirrors.length; iy++) {
			if(hMirrors[iy] < 0) continue;
			int m = countHMirrors(iy);
			if(m > hMirrors[iy]) return false;
			if(isFinished() && m < hMirrors[iy]) return false;			
		}
		for (int ix = 0; ix < vMirrors.length; ix++) {
			if(vMirrors[ix] < 0) continue;
			int m = countVMirrors(ix);
			if(m > vMirrors[ix]) return false;
			if(isFinished() && m < vMirrors[ix]) return false;			
		}
		return true;
	}
	
	int countHMirrors(int iy) {
		int m = 0;
		for (int ix = 0; ix < field.getWidth(); ix++) {
			int p = field.getIndex(ix, iy);
			if(isMirror(p)) m++;
		}
		return m;
	}
	
	int countVMirrors(int ix) {
		int m = 0;
		for (int iy = 0; iy < field.getWidth(); iy++) {
			int p = field.getIndex(ix, iy);
			if(isMirror(p)) m++;
		}
		return m;
	}
	
	boolean isMirror(int p) {
		if(totalState[p] != 2) return false;
		int d = getDirection(p);
		return (state[p] & CODES[REVERSE[d]]) == 0;
	}

	boolean checkCrossings() {
		for (int iy = 0; iy < hCrossings.length; iy++) {
			if(hCrossings[iy] < 0) continue;
			int m = countHCrossings(iy);
			if(m > hCrossings[iy]) return false;
			if(isFinished() && m < hCrossings[iy]) return false;			
		}
		for (int ix = 0; ix < vCrossings.length; ix++) {
			if(vCrossings[ix] < 0) continue;
			int m = countVCrossings(ix);
			if(m > vCrossings[ix]) return false;
			if(isFinished() && m < vCrossings[ix]) return false;			
		}
		return true;
	}
	
	static int[] DOWN_DIRECTIONS = {1,2,3};
	static int[] RIGHT_DIRECTIONS = {0,1,7};

	int countHCrossings(int iy) {
		int c = 0;
		for (int ix = 0; ix < field.getWidth(); ix++) {
			int p = field.getIndex(ix, iy);
			for (int di = 0; di < DOWN_DIRECTIONS.length; di++) {
				int d = DOWN_DIRECTIONS[di];
				if(field.jump(p, d, 1) < 0) continue;
				if((state[p] & CODES[d]) != 0) c++;
			}
		}
		return c;
	}
	
	int countVCrossings(int ix) {
		int c = 0;
		for (int iy = 0; iy < field.getWidth(); iy++) {
			int p = field.getIndex(ix, iy);
			for (int di = 0; di < RIGHT_DIRECTIONS.length; di++) {
				int d = RIGHT_DIRECTIONS[di];
				if(field.jump(p, d, 1) < 0) continue;
				if((state[p] & CODES[d]) != 0) c++;
			}
		}
		return c;
	}	
	
	void onSolutionFound() {
		System.out.println("Solution " + solutionCount);
		for (int i = 0; i < state.length; i++) {
			System.out.print(" ");
			int c = 0;
			for (int d = 0; d < 8 && c < 2; d++) {
				if((state[i] & CODES[d]) != 0) {
					System.out.print("" + d);
					c++;
				}
			}
			for (;c < 2; c++) System.out.print(" ");
			if(field.isRightBorder(i)) System.out.println("");
		}
		System.out.println("");
		System.out.print("HCrossings=");
		for (int iy = 0; iy < field.getWidth(); iy++) {
			System.out.print(" " + countHCrossings(iy));
		}
		System.out.println("");
		System.out.print("VCrossings=");
		for (int ix = 0; ix < field.getWidth(); ix++) {
			System.out.print(" " + countVCrossings(ix));
		}
		System.out.println("");
		System.out.println("");
		System.out.print("HMirrors=");
		for (int iy = 0; iy < field.getWidth(); iy++) {
			System.out.print(" " + countHMirrors(iy));
		}
		System.out.println("");
		System.out.print("VMirrors=");
		for (int ix = 0; ix < field.getWidth(); ix++) {
			System.out.print(" " + countVMirrors(ix));
		}
		System.out.println("");
		System.out.println("Key=" + getKey());
	}
	
	boolean isFinished() {
		return totalOneCount == 0;
	}

	double getPercent() {
		double s = 0;
		double p = 1;
		for (int i = 0; i < t; i++) {
			p = p / wayCount[i];
			s += p * way[i];
		}
		return s;
	}
	
	boolean checkConnectedness() {
		int[] s = new int[state.length];
		int[] k = new int[state.length];
		int parts = 0;
		for (int p = 0; p < s.length; p++) {
			if(state[p] == 0 || s[p] != 0) continue;
			parts++;
			int v = 1;
			int c = 0;
			k[0] = p;
			s[p] = 1;
			while(c < v) {
				p = k[c];
				for (int d = 0; d < 8; d++) {
					if((state[p] & CODES[d]) == 0) continue;
					int q = field.jump(p, d, 1);
					if(q < 0 || s[q] > 0) continue;
					k[v] = q;
					s[q] = 1;
					++v;
				}
				++c;
			}
		}
		return parts == 1;
	}
	
	String getKey() {
		int md = 0, mv = 0, mh = 0;
		for (int p = 0; p < state.length; p++) {
			if(totalState[p] != 2) continue;
			int d = getDirection(p);
			if((state[p] & CODES[REVERSE[d]]) != 0) continue;
			if(d % 2 == 0) {
				md++;
			} else if(d == 5) {
				mh++;
			} else if(d == 3) {
				mv++;
			} else if((state[p] & CODES[3]) != 0) {
				mh++;
			} else {
				mv++;
			}
		}
		return "" + mh + "," + mv + "," + md;
	}

}
