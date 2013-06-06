package ic2006_2;

public class MirrorSeaBattle {
	int[] hRestrictions;
	int[] vRestrictions;

	MirrorQuorterField qField;
	MirrorQuorterField field;
	
	int[][] quorterStates;
	
	int[] state;
	int[] restriction;

	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int stateCount;
	
	public MirrorSeaBattle() {}
	
	public void setHVRestrictions(int[] hR, int[] vR) {
		hRestrictions = hR;
		vRestrictions = vR;
	}
	
	public void setField(MirrorQuorterField f) {
		qField = f;
		field = new MirrorQuorterField();
		field.setSize(f.getWidth() * 2);
	}
	
	public void setQuorterStates(int[][] s) {
		quorterStates = s;
	}
	
	public void solve() {
		init();
		anal();
	}

	void init() {
		state = new int[field.getSize()];
		restriction = new int[field.getSize()];
		wayCount = new int[5];
		way = new int[5];
		ways = new int[5][field.getSize()];
		t = 0;
		stateCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == 4) return;
		if(!checkRestrictions()) return;
		int rm = t == 0 ? 1 : 8;
		for(int r = 0; r < rm; r++) {
			ways[t][wayCount[t]] = r;
			wayCount[t]++;
		}
	}
	
	boolean checkRestrictions() {
		for (int p = 0; p < state.length; p++) {
			if(state[p] > 0 && restriction[p] > 0) return false; 
		}
		return true;
	}
	
	void move() {
		int k = ways[t][way[t]];
		int[] qs = quorterStates[k];
		int dx = 5 * (t % 2);
		int dy = 5 * (t / 2);
		for (int i = 0; i < qs.length; i++) {
			if(qs[i] == 0) continue;
			int p = field.getIndex(qField.getX(i) + dx, qField.getY(i) + dy);
			add(p);
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void add(int p) {
		state[p] = 1;
		for (int d = 1; d < 8; d += 2) { //diagonals
			int q = field.jump(p, d, 1);
			if(q >= 0) restriction[q]++;
		}
	}

	void remove(int p) {
		state[p] = 0;
		for (int d = 1; d < 8; d += 2) { //diagonals
			int q = field.jump(p, d, 1);
			if(q >= 0) restriction[q]--;
		}
	}
	

	void back() {
		--t;
		int k = ways[t][way[t]];
		int[] qs = quorterStates[k];
		int dx = 5 * (t % 2);
		int dy = 5 * (t / 2);
		for (int i = 0; i < qs.length; i++) {
			if(qs[i] == 0) continue;
			int p = field.getIndex(qField.getX(i) + dx, qField.getY(i) + dy);
			remove(p);
		}
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] ==wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t == 4 
					&& checkRestrictions() 
					&& checkNeighbours()
					&& checkHVRestrictions()) {
				stateCount++;
				onSolutionCount();
			}
		}
	}
	
	boolean checkNeighbours() {
		int[] ns = new int[8];
		for (int p = 0; p < state.length; p++) {
			if(state[p] == 0) continue;
			int n = getNeighbours(p);
			ns[n]++;
		}
		if(ns[0] != 6) return false;
		if(ns[1] != 12) return false;
		if(ns[2] != 2) return false;
		return true;
	}
	
	int getNeighbours(int p) {
		int n = 0;
		for (int d = 0; d < 8; d += 2) {
			int q = field.jump(p, d, 1);
			if(q < 0 || state[q] == 0) continue;
			++n;
		}
		return n;
	}
	
	boolean checkHVRestrictions() {
		for (int iy = 0; iy < field.getWidth(); iy++) {
			if(hRestrictions[iy] < 0) continue;
			int k = 0;
			for (int ix = 0; ix < field.getWidth(); ix++) {
				int p = field.getIndex(ix, iy);
				if(state[p] > 0) k++;
			}
			if(hRestrictions[iy] != k) return false;
		}
		for (int ix = 0; ix < field.getWidth(); ix++) {
			if(vRestrictions[ix] < 0) continue;
			int k = 0;
			for (int iy = 0; iy < field.getWidth(); iy++) {
				int p = field.getIndex(ix, iy);
				if(state[p] > 0) k++;
			}
			if(vRestrictions[ix] != k) return false;
		}
		return true;
	}
	
	void onSolutionCount() {
		for (int p = 0; p < state.length; p++) {
			System.out.print(" " + state[p]);
			if(field.isRightBorder(p)) System.out.println("");
		}
		System.out.println("");
	}
}
