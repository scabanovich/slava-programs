package diogen2005;

public class TickTackToeSolver {
	TickTackToeField field;
	int[] initialValues;
	int crossCount;
	
	int solutionLimit;
	boolean randomizing;
	int treeCountLimit;
	
	boolean makeDistribution = false;
	
	int[] values;
	int vacancies;
	int currentCrossCount;

	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;

	int treeCount;
	int solutionCount;
	int[] solution;
	
	int[][] distribution;

	public TickTackToeSolver() {}
	
	public void setField(TickTackToeField field) {
		this.field = field;
		init0();
	}

	public void setInitialValues(int[] vs) {
		initialValues = vs;
	}
	
	public void setCrossCount(int c) {
		crossCount = c;
	}
	
	public void setSolutionLimit(int i) {
		solutionLimit = i;
	}
	
	public void setTreeCountLimit(int i) {
		treeCountLimit = i;
	}
	
	public void setRandomizing(boolean b) {
		randomizing = b;
	}
	
	public void setMakeDistribution(boolean b) {
		makeDistribution = b;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init0() {
		vacancies = field.size;
		values = new int[vacancies];
		place = new int[field.size + 1];
		wayCount = new int[field.size + 1];
		way = new int[field.size + 1];
		ways = new int[field.size + 1][2];
	}
	
	void init() {
		vacancies = field.size;
		currentCrossCount = 0;
		for (int i = 0; i < values.length; i++) values[i] = -1;
		
		if(initialValues != null) for (int i = 0; i < initialValues.length; i++) {
			if(initialValues[i] >= 0) add(i, initialValues[i]);
		}
		if(makeDistribution) {
			distribution = new int[field.size][2];
		}
		
		t = 0;
		solutionCount = 0;
		solution = null;
		treeCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(isFinished()) return;
		if(crossCount >= 0) {
			if(vacancies + currentCrossCount < crossCount) return;
			if(currentCrossCount > crossCount) return;
		}
		doSrch();
		if(randomizing) {
			randomize();
		}
	}
	
	protected void doSrch() {
		int w = 3;
		boolean s0 = crossCount < 0 || currentCrossCount + vacancies > crossCount;
		boolean s1 = crossCount < 0 || currentCrossCount < crossCount;
		for (int p = 0; p < field.size; p++) {
			if(values[p] >= 0) continue;
			boolean c0 = s0 && canPut(p, 0);
			boolean c1 = s1 && canPut(p, 1);
			int wc = (c0) ? 1 : 0;
			if(c1) wc++;
			if(wc >= w) continue;
			place[t] = p;
			w = wc;
			if(w == 0) return;
			if(c0) {
				ways[t][0] = 0;
				if(c1) ways[t][1] = 1;
			} else {
				if(c1) ways[t][0] = 1;
			}
		}
		if(w < 3) wayCount[t] = w;
	}
	
	boolean canPut(int p, int v) {
		for (int d = 0; d < 4; d++) {
			if(is4inRow(p, d, v)) return false;
		}
		return true;
	}

	boolean is4inRow(int p, int d, int v) {
		int c = 0;
		int q = field.jump(p, d);
		while(q >= 0 && values[q] == v) {
			++c;
			q = field.jump(q, d);
		}
		d += 4;
		q = field.jump(p, d);
		while(q >= 0 && values[q] == v) {
			++c;
			q = field.jump(q, d);
		}
		return c >= 3;
	}
	
	void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = wayCount[t] - 1; i >= 1; i--) {
			int k = (int)((i + 1) * Math.random());
			if(k == i) continue;
			int c = ways[t][i];
			ways[t][i] = ways[t][k];
			ways[t][k] = c;
		}
	}
	
	void move() {
		int p = place[t];
		int v = ways[t][way[t]];
		add(p, v);
		++t;
		srch();
		way[t] = -1;
	}
	
	void add(int p, int v) {
		vacancies--;
		values[p] = v;
		currentCrossCount += v;
	}
	
	void back() {
		--t;
		int p = place[t];
		int v = ways[t][way[t]];
		remove(p, v);
	}

	void remove(int p, int v) {
		vacancies++;
		values[p] = -1;
		currentCrossCount -= v;
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
			if(wayCount[t] == 0) {
				treeCount++;				
				if(treeCountLimit > 0 && treeCount > treeCountLimit) {
					solutionCount = solutionLimit + 1;
					return;
				}
			}
			if(isFinished()) {
				solutionCount++;
				if(solutionCount == 1) {
					solution = (int[])values.clone();
				}
				onSolutionFound();
				if(solutionLimit > 0 && solutionCount >= solutionLimit) return;
			}
		}
	}
	
	void onSolutionFound() {
		if(makeDistribution && distribution != null) {
			for (int i = 0; i < values.length; i++) {
				distribution[i][values[i]]++;
			}
		}
	}
	
	protected boolean isFinished() {
		return vacancies == 0 && (currentCrossCount == crossCount || crossCount < 0);
	}
	
	public int[] getSolution() {
		return solution;
	}
	
	public void printSolution(int[] solution) {
		System.out.print(solutionToString(solution));
	}
	
	public String solutionToString(int[] solution) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < field.size; i++) {
			char c = (solution[i] == 0) ? 'o' : solution[i] == 1 ? 'x' : '+';
			sb.append(" " + c);
			if(field.x[i] == field.width - 1) sb.append("\n");
		}
		return sb.toString();
	}
	
	public int[] getNarrowestPlaceInfo() {
		int[] nw = new int[]{-1,-1,-1};
		if(distribution == null || !makeDistribution) return nw;
		for (int p = 0; p < distribution.length; p++) {
			for (int v = 0; v < 2; v++) {
				if(initialValues[p] >= 0) continue;
				int w = distribution[p][v];
				if(w == 0) continue;
				if(nw[2] < 0 || w < nw[2]) {
					nw[0] = p;
					nw[1] = v;
					nw[2] = w;
				}
			}
		}
		return nw;
	}

}

/*
Problem 23 (Attempt=17529)
 + o + + o + o + + o
 + + + + + + + + o o
 + + + + o + + + + o
 o o + o + + + x + +
 + + + + + + + + + +
 + + + + + + + + + o
 + x + o o o + + o +
 + + + + o + + + o +
 + + + + + o + + + +
 + + o + + + + + o +
Solution  Complexity=3
Problem 24 (Attempt=45)
 + + o + + x + + + +
 x x + + + x + + + +
 o + o o + + + + + x
 + + o + + x + + x +
 + + + + + + + + x +
 + + + + + + + + x +
 + x + o + + + + + +
 x + + + + + + + + +
 + x + + x + + + o x
 + + o o + + + + o +
Solution  Complexity=183
 o o o x o x o x o o
 x x x o x x x o o x
 o x o o x o x o o x
 o x o x o x x o x x
 x o o o x x o x x o
 x o x o x x o o x o
 o x x o x o o x o o
 x x o x o x x o x x
 o x o x x o x o o x
 x o o o x o x o o x
*/
