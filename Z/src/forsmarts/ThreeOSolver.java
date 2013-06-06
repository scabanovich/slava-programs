package forsmarts;

public class ThreeOSolver {
	ThreeOField field;

	int[] state;
	int[] placementFreeCellCount;
	int[] placementOCellCount;
	
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int treeCount;
	int solutionCount;
	
	public ThreeOSolver() {}
	
	public void setField(ThreeOField f) {
		field = f;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		
		placementFreeCellCount = new int[field.placements.length];
		placementOCellCount = new int[field.placements.length];
		for (int i = 0; i < field.placements.length; i++) {
			int c = 0;
			for (int k = 0; k < field.placements[i].length; k++) {
				int p = field.placements[i][k];
				if(field.dots[p] == 0) ++c;
			}
			placementFreeCellCount[i] = c;
		}		
		
		place = new int[state.length];
		wayCount = new int[state.length];
		way = new int[state.length];
		ways = new int[state.length][2];
		t = 0;
		solutionCount = 0;
		treeCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		int p = getNarrowPlace();
		if(p < 0) return;
		place[t] = p;
		for (int k = 0; k < 2; k++) {
			if(!canAdd(p, k)) continue;
			ways[t][wayCount[t]] = k;
			wayCount[t]++;
		}
	}
	
	static int[] WEIGHT = {10000, 10000, 5000, 2000, 1000, 500, 200, 60, 20, 6, 1, 1, 1, 1, 1};
	
	int getNarrowPlace() {
		int pc = -1;
		int v = 0;
		for (int p = 0; p < state.length; p++) {
			if(field.form[p] < 1 || field.dots[p] == 1) continue;
			if(state[p] >= 0) continue;
			int[] pi = field.placementsByCell[p];
			int vp = 0;
			for (int i = 0; i < pi.length; i++) {
				int f = placementFreeCellCount[pi[i]];
				int u = placementOCellCount[pi[i]];
				if(f <= 1) return p; //force
				if(u >= 3 || f + u <= 3) return p; //force
				vp += WEIGHT[f];
			}
			if(vp > v) {
				v = vp;
				pc = p;
			}			
		}		
		return pc;
	}
	
	boolean canAdd(int p, int k) {
		int[] pi = field.placementsByCell[p];
		if(pi == null) return false;
		for (int i = 0; i < pi.length; i++) {
			int f = placementFreeCellCount[pi[i]];
			int u = placementOCellCount[pi[i]];
			if(k == 0 && (f - 1) + u < 3) return false;
			if(k == 1 && u >= 3) return false;
		}
		return true;
	}
	
	void move() {
		int k = ways[t][way[t]];
		int p = place[t];
		state[p] = k;
		int[] pi = field.placementsByCell[p];
		if(pi != null) {
			for (int i = 0; i < pi.length; i++) {
				placementFreeCellCount[pi[i]]--;
				if(k == 1) placementOCellCount[pi[i]]++;
			}
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int k = ways[t][way[t]];
		int p = place[t];
		state[p] = -1;
		int[] pi = field.placementsByCell[p];
		if(pi != null) {
			for (int i = 0; i < pi.length; i++) {
				placementFreeCellCount[pi[i]]++;
				if(k == 1) placementOCellCount[pi[i]]--;
			}
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
			if(wayCount[t] == 0) treeCount++;
			if(isSolution()) {
				printSolution();
				++solutionCount;
			}
		}
	}
	
	boolean isSolution() {
		for (int p = 0; p < state.length; p++) {
			if(field.dots[p] == 1 || field.form[p] == 0) continue;
			if(state[p] < 0) return false;
		}
		return true;
	}
	
	void printSolution() {
		System.out.println("t=" + t);
		for (int i = 0; i < field.getSize(); i++) {
			char c = field.form[i] == 0 ? '+' : field.dots[i] == 1 ? '*' :
				(state[i] <= 0) ? '.' : 'O';
			System.out.print(" " + c);
			if(field.isRightBorder(i)) System.out.println("");
		}
		System.out.println(getCode());
	}
	
	String getCode() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < field.getWidth(); i++) {
			int p = field.getIndex(i, i);
			if(p >= 0) {
				char c = state[p] != 1 ? '-' : 'O';
				sb.append(c);
			}
		}
		sb.append(',');
		for (int i = 0; i < field.getWidth(); i++) {
			int p = field.getIndex(i, 4);
			if(p >= 0) {
				char c = state[p] != 1 ? '-' : 'O';
				sb.append(c);
			}
		}
		sb.append(',');
		for (int i = 0; i < field.getWidth(); i++) {
			int p = field.getIndex(4, 8 - i);
			if(p >= 0) {
				char c = state[p] != 1 ? '-' : 'O';
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
