package forsmarts;

import com.slava.common.RectangularField;

public class TripleLoopSolver {
	static int UNACCESSIBLE = -1;
	static int[] REVERSE = {2,3,0,1};
	static int[] RIGHT = {1,2,3,0};
	static int[] LEFT = {3,0,1,2};
	RectangularField field;
	int[] initialState;
	
	//beginning points of loops (index is the loop index minus 1)
	int[] wayStarts;
	
	//index of loop
	int[] state;
	//number of currently built exits of the loop from the cell 
	int[] occupations;
	// two directions in which the loop goes out of the cell	
	int[][] accesses;
	int[] heads;
	int vacancies;
	
	int t;
	int[] wayCount;
	int[] way;
	int[] waysH;
	int[][] waysD;
	
	int solutionCount;
	
	public TripleLoopSolver () {}
	
	public void setField(RectangularField f) {
		field = f;
	}
	
	/**
	 * s[i] < 0 means that cell i is not accessible.
	 * s[i] = k for k > 0 means that cell i is part of loop k.
	 * @param s
	 */	
	public void setInitialState(int[] s) {
		initialState = s;
		int hc = 0;
		for (int i = 0; i < s.length; i++) if(isHead(i)) hc++;
		heads = new int[hc];
		hc = 0;
		for (int i = 0; i < s.length; i++) if(isHead(i)) {
			heads[hc] = i;
			hc++;
		}
		wayStarts = new int[hc / 2];
		for (int i = 0; i < s.length; i++) if(s[i] > 0 && !isHead(i)) {
			wayStarts[s[i] - 1] = i;
		}		
	}
	
	boolean isHead(int p) {
		int s = initialState[p];
		if(s <= 0) return false;
		int ns = 0;
		for (int d = 0; d < 4; d++) {
			int q = field.jump(p, d);
			if(q >= 0 && initialState[q] == s) ns++;
		}
		if(ns == 0 || ns > 2) throw new RuntimeException("Wrong initial data:" + ns + " neighbours at " + p + ".");
		return ns == 1;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		occupations = new int[field.getSize()];
		state = new int[field.getSize()];
		for (int i = 0; i < occupations.length; i++) {
			if(initialState[i] < 0) {
				occupations[i] = 3;
			} else if(initialState[i] != 0) {
				if(isHead(i)) occupations[i] = 1; else occupations[i] = 2;
				state[i] = initialState[i];
			}
		}
		accesses = new int[field.getSize()][2];
		for (int p = 0; p < accesses.length; p++) {
			if(initialState[p] <= 0) continue;
			int c = 0;
			for (int d = 0; d < 4; d++) {
				int q = field.jump(p, d);
				if(q < 0 || initialState[q] != initialState[p]) continue;
				accesses[p][c] = d;
				c++;
			}
		}
		vacancies = field.getSize();
		for (int i = 0; i < field.getSize(); i++) {
			if(occupations[i] > 0) vacancies--;
		}
		
		wayCount = new int[field.getSize()];
		way = new int[field.getSize()];
		waysH = new int[field.getSize()];
		waysD = new int[field.getSize()][4];
		
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(isFinished() && vacancies == 0) return;
		if(hasUnaccessible()) return;
		if(!areWaysCorresponding()) return;
		int wcm = 5;
		waysH[t] = -1;
		int shortestIndex = getShortestLoop();
		for (int i = 0; i < heads.length; i++) {
			int p = heads[i];
			if(occupations[p] > 1) continue;
			int wc = getWaysFromHead(i);
			if(wc < wcm || (wc == wcm && shortestIndex == state[p] - 1)) {
				if(wc == 0) return;
				wcm = wc;
				waysH[t] = i;
				for (int k = 0; k < wc; k++) waysD[t][k] = temp[k];
			}
		}
		if(wcm < 5) wayCount[t] = wcm;
	}
	
	int[] temp = new int[4];
	int getWaysFromHead(int i) {
		int p = heads[i];
		int wc = 0;
		for (int d = 0; d < 4; d++) {
			int q = field.jump(p, d);
			if(q < 0 || occupations[q] >= 2) continue;
			if(occupations[q] == 1 && state[q] != state[p]) continue;
			if(isStraightPlace(p, q)) {
				temp[0] = d;
				return 1;
			}
			temp[wc] = d;
			++wc;
		}
		return wc;		
	}
	
	boolean isStraightPlace(int p, int q) {
		if(occupations[q] > 0) return false;
		int wc = 0;
		boolean otherLoop = false;
		for (int d = 0; d < 4; d++) {
			int r = field.jump(q, d);
			if(r < 0 || occupations[r] > 1 || r == p) continue;
			if(occupations[r] == 1 && state[r] != state[p]) {
				otherLoop = true;
			} else {
				++wc;
			}
		}
		if(otherLoop) return false;
		return wc < 2;
	}
	
	int[] vs;
	int[] stack;
	
	boolean hasUnaccessible() {
		if(vs == null) {
			vs = new int[field.getSize()];
			stack = new int[field.getSize()];
		}
		for (int i = 0; i < vs.length; i++) vs[i] = 0;
		for (int i = 0; i < occupations.length; i++) {
			if(occupations.length > 0 || vs[i] > 0) continue;
			int v = 1;
			int c = 0;
			vs[i] = 1;
			stack[0] = i;
			int[] hs = new int[wayStarts.length];
			while(c < v) {
				int p = stack[c];
				for (int d = 0; d < 4; d++) {
					int q = field.jump(p, d);
					if(q < 0 || occupations[q] >= 2) continue;
					if(vs[q] > 0) continue;
					vs[q]++;
					if(occupations[q] == 1) {
						hs[state[q] - 1]++;
					} else {
						stack[v] = q;
						v++;
					}
				}
				c++;
			}
			boolean ok = false;
			for (int k = 0; k < hs.length; k++) {
				if(hs[k] == 2) ok = true;
			}
			if(!ok) return true;
		}
		return false;
	}
	
	void move() {
		int f = waysH[t];
		int d = waysD[t][way[t]];
		int pc = heads[f];
		int s = state[pc];
		accesses[pc][occupations[pc]] = d;
		occupations[pc]++;
		heads[f] = field.jump(pc, d);
		int pn = heads[f];
		accesses[pn][occupations[pn]] = REVERSE[d];
		occupations[pn]++;
		if(occupations[pn] == 1) {
			state[pn] = s;
			vacancies--;
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int f = waysH[t];
		int d = waysD[t][way[t]];
		d = REVERSE[d];
		int pc = heads[f];
		occupations[pc]--;
		if(occupations[pc] == 0) {
			state[pc] = 0;
			vacancies++;
		}
		heads[f] = field.jump(pc, d);
		int pp = heads[f];
		occupations[pp]--;
	}
	
	void anal() {
		srch();
		way[t] = -1;
		int presolutions = 0;
		int tmin = 50;
		int tm = 50;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(vacancies == 0 && isFinished()) {
				presolutions++;
				if(presolutions % 100000 == 0) System.out.println(presolutions);
			}
			if(t > tm) tm = t;
			if(tm > 50 && t < tmin) {
				tmin = t;
				System.out.println("tmin=" + tmin);
			}
			if(vacancies == 0 && isFinished() && areSameWayCodes()) {
				++solutionCount;
				printSolution();
			}
		}
	}
	
	void printSolution() {
		System.out.println("Solution!");
		for (int i = 0; i < state.length; i++) {
			System.out.print(" " + state[i]);
			if(field.getX(i) == field.getWidth() - 1) System.out.println("");
		}
		System.out.println(getSolutionCode());
//		System.out.println(getWayCode(0, 0));
//		System.out.println(getWayCode(1, 0));
//		System.out.println(getWayCode(2, 0));
	}
	
	boolean isFinished() {
		for (int i = 0; i < heads.length; i++) {
			if(occupations[heads[i]] < 2) return false;
		}
		return true;
	}
	
	boolean areSameWayCodes() {
		String s0 = getWayCode(0, 0);
		if(s0 == null) return false;
		for (int i = 1; i < wayStarts.length; i++) {
			String si = getWayCode(i, 0);
			if(s0.equals(si)) continue;
			si = getWayCode(i, 1);
			if(s0.equals(si)) continue;
			return false;
		}
		return true;
	}
	
	String getWayCode(int k, int c) {
		StringBuffer sb = new StringBuffer();
		int p = wayStarts[k];
		int d = accesses[p][c];
		int q = field.jump(p, d);
		while(q != p) {
			if(occupations[q] == 1) return sb.toString();
			int d1 = -1;
			boolean hasReverse = false;
			for (int m = 0; m < 2; m++) {
				if(accesses[q][m] != REVERSE[d]) d1 = accesses[q][m];
				else hasReverse = true;
			}
			if(d1 < 0 || !hasReverse) return null;
			if(d1 == RIGHT[d]) sb.append('R');
			else if(d1 == LEFT[d]) sb.append('L');
			d = d1;
			q = field.jump(q, d);
			if(q < 0 || state[q] != state[p]) return null;
		}
		sb.append('F');
		return sb.toString();
	}
	
	boolean areWaysCorresponding() {
		String[][] codes = new String[wayStarts.length][2];
		for (int i = 0; i < wayStarts.length; i++) {
			for (int c = 0; c < 2; c++) {
				codes[i][c] = getWayCode(i, c);
			}
		}
		for (int i1 = 0; i1 < codes.length; i1++) {
			for (int i2 = i1 + 1; i2 < codes.length; i2++) {
				if((!areCorresponding(codes[i1][0], codes[i2][0]) 
					|| !areCorresponding(codes[i1][1], codes[i2][1]))
					&&
				   (!areCorresponding(codes[i1][0], codes[i2][1]) 
					|| !areCorresponding(codes[i1][1], codes[i2][0]))) {
					return false;
				}
					
			}
		}
		return true;
	}
	boolean areCorresponding(String s1, String s2) {
		return s1.startsWith(s2) || s2.startsWith(s1);
	}
	
	int getShortestLoop() {
		int ls = 1000;
		int index = -1;
		for (int i = 0; i < wayStarts.length; i++) {
			String s = getWayCode(i, 0);
			if(s.endsWith("F")) continue;
			int l = s.length() + getWayCode(i, 1).length();
			if(l < ls) {
				ls = l;
				index = i;
			}
		}		
		return index;
	}
	
	static char[] DIRECTION_LETTERS = {'R','D','L','U'};
	String getSolutionCode() {
		String s = getWayCode(0, 0);
		int r = 0;
		for (int i = 0; i < s.length(); i++) {
			if(s.charAt(i) == 'R') ++r; 
			else if(s.charAt(i) == 'L') --r;
		}
		int c = (r > 0) ? 0 : 1;
		StringBuffer sb = new StringBuffer();
		int p = wayStarts[0];
		int d = accesses[p][c];
		sb.append(DIRECTION_LETTERS[d]);
		int q = field.jump(p, d);
		while(q != p) {
			if(occupations[q] == 1) return sb.toString();
			int d1 = -1;
			boolean hasReverse = false;
			for (int m = 0; m < 2; m++) {
				if(accesses[q][m] != REVERSE[d]) d1 = accesses[q][m];
				else hasReverse = true;
			}
			if(d1 < 0 || !hasReverse) return null;
			if(d1 != d) sb.append(DIRECTION_LETTERS[d1]);
			d = d1;
			q = field.jump(q, d);
			if(q < 0 || state[q] != state[p]) return null;
		}
		return sb.toString();
	}
	
	static void run(int width, int[] initialState) {
		RectangularField f = new RectangularField();
		f.setSize(width, width);
		TripleLoopSolver solver = new TripleLoopSolver();
		solver.setField(f);
		solver.setInitialState(initialState);
		solver.solve();
		System.out.println("SolutionCount=" + solver.solutionCount);
	}

	
	static int U = UNACCESSIBLE;
	
	static int[] INIT_A = {
		U,U,0,0,0,0,0,0,0,U,
		0,0,0,U,0,0,0,0,0,0,
		0,U,0,1,0,0,0,0,0,0,
		0,U,0,1,0,0,2,2,2,0,
		0,0,0,1,0,0,0,0,0,0,
		0,0,0,0,U,U,0,0,0,0,
		0,0,0,3,0,0,0,0,0,0,
		0,0,0,3,0,U,0,0,0,0,
		0,0,0,3,0,U,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,
	};
	static int[] INIT_B = {
		0,0,0,1,1,1,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,U,0,0,0,0,
		0,U,0,0,U,0,0,0,0,0,0,
		0,U,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,2,2,2,0,3,
		U,0,0,0,0,0,0,0,0,0,3,
		U,0,0,0,0,U,0,0,0,0,3,
		0,0,0,0,0,U,0,0,0,0,0,
		0,U,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,0,
	};
	
	public static void main(String[] args) {
		run(10, INIT_A);
		//run(11, INIT_B);
	}

}

// a) ULDLURURDLULDRDLU

// b)
/*
Solution!
1 1 1 1 1 1 1 1 1 1 1
1 1 1 1 1 1 1 1 1 1 1
1 1 1 3 3 3 0 3 3 3 3
1 0 1 3 0 3 3 3 3 3 3
1 0 1 3 3 3 3 3 3 3 3
1 1 1 3 3 3 2 2 2 3 3
0 1 1 3 3 3 2 2 2 3 3
0 1 1 1 1 0 2 2 2 3 3
1 1 1 1 1 0 2 2 2 2 2
1 0 1 1 1 2 2 2 2 2 2
1 1 1 1 1 2 2 2 2 2 2
RRLLRLLRRRRRLLRR

1) RDLDRDRURDLURULUR
2) RDLDRDRDLULDLURULUR
3) RDLDRDRURDLURULUR
4) RDLDRDRDLULDLURULUR

Solution!
 1 1 1 1 1 1 1 1 1 1 1
 1 1 1 1 1 1 1 1 1 1 1
 1 1 1 3 3 3 0 3 3 3 3
 1 0 1 3 0 3 3 3 3 3 3
 1 0 1 3 3 3 3 3 3 3 3
 1 1 1 3 3 3 2 2 2 3 3
 0 1 1 3 3 3 2 2 2 3 3
 0 1 1 1 1 0 2 2 2 3 3
 1 1 1 1 1 0 2 2 2 2 2
 1 0 1 1 1 2 2 2 2 2 2
 1 1 1 1 1 2 2 2 2 2 2
RDLDRDRURDLURULUR
Solution!
 1 1 1 1 1 1 1 1 1 1 1
 1 1 1 1 1 1 1 1 1 1 1
 1 1 1 3 3 3 0 3 3 3 3
 1 0 1 3 0 3 3 3 3 3 3
 1 0 1 3 3 3 3 3 3 3 3
 1 1 1 3 3 3 2 2 2 3 3
 0 1 1 3 3 3 2 2 2 3 3
 0 1 1 1 1 0 2 2 2 3 3
 1 1 1 1 1 0 2 2 2 2 2
 1 0 1 1 1 2 2 2 2 2 2
 1 1 1 1 1 2 2 2 2 2 2
RDLDRDRDLULDLURULUR
tmin=4
tmin=3
tmin=2
Solution!
 1 1 1 1 1 1 1 3 3 3 3
 1 1 1 1 1 1 1 3 3 3 3
 1 1 1 3 3 3 0 3 3 3 3
 1 0 1 3 0 3 3 3 3 3 3
 1 0 1 3 3 3 3 3 3 3 3
 1 1 1 3 3 3 2 2 2 3 3
 0 1 1 3 3 3 2 2 2 3 3
 0 1 1 1 1 0 2 2 2 3 3
 1 1 1 1 1 0 2 2 2 2 2
 1 0 1 1 1 2 2 2 2 2 2
 1 1 1 1 1 2 2 2 2 2 2
RDLDRDRURDLURULUR
Solution!
 1 1 1 1 1 1 1 3 3 3 3
 1 1 1 1 1 1 1 3 3 3 3
 1 1 1 3 3 3 0 3 3 3 3
 1 0 1 3 0 3 3 3 3 3 3
 1 0 1 3 3 3 3 3 3 3 3
 1 1 1 3 3 3 2 2 2 3 3
 0 1 1 3 3 3 2 2 2 3 3
 0 1 1 1 1 0 2 2 2 3 3
 1 1 1 1 1 0 2 2 2 2 2
 1 0 1 1 1 2 2 2 2 2 2
 1 1 1 1 1 2 2 2 2 2 2
RDLDRDRDLULDLURULUR
tmin=1
SolutionCount=4


*/