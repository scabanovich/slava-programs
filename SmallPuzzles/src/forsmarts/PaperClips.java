package forsmarts;

public class PaperClips {
	PaperClipsField field;
	int[] vRounds;
	int[] hRounds;
	
	int[] state;
	int[] restriction;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] waysP;
	int[][] waysD;
	int[][] waysL;

	public PaperClips() {}
	
	public void setField(PaperClipsField field) {
		this.field = field;
	}
	
	public void setRounds(int[] v, int[] h) {
		vRounds = v;
		hRounds = h;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		int maxTime = 200;
		way = new int[maxTime];
		wayCount = new int[maxTime];
		waysP = new int[maxTime][100];
		waysD = new int[maxTime][100];
		waysL = new int[maxTime][100];
		state = new int[field.size];
		restriction = new int[field.size];
		t = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(isFinished() || !isCorrect()) return;
		int p = getNextP();
		if(p < 0 || p >= field.size) return;
		waysP[t][wayCount[t]] = p;
		waysD[t][wayCount[t]] = -1;
		waysL[t][wayCount[t]] = -1;
		wayCount[t]++;
		for (int d = 0; d < 4; d++) {
			for (int l = 3; l < 6; l++) {
				if(canAdd(p, d, l)) {
					waysP[t][wayCount[t]] = p;
					waysD[t][wayCount[t]] = d;
					waysL[t][wayCount[t]] = l;
					wayCount[t]++;
				}
			}
		}
	}
	
	int getNextP() {
		if(t == 0) return 0;
		int p = waysP[t - 1][way[t - 1]] + 1;
		while(p < field.size && restriction[p] > 0) ++p;
		return p;
	}
	
	boolean isFinished() {
		for (int i = 0; i < vRounds.length; i++) {
			if(vRounds[i] > 0) return false;
		}
		for (int i = 0; i < hRounds.length; i++) {
			if(hRounds[i] > 0) return false;
		}
		return true;
	}

	boolean isCorrect() {
		for (int i = 0; i < vRounds.length; i++) {
			if(vRounds[i] < 0) return false;
		}
		for (int i = 0; i < hRounds.length; i++) {
			if(hRounds[i] < 0) return false;
		}
		return true;
	}
	
	boolean canAdd(int p, int d, int l) {
		int q = p;
		if(restriction[q] > 0) return false;
		for (int i = 0; i < l; i++) {
			q = field.jp[d][q];
			if(q < 0 || restriction[q] > 0) return false;
		}
		if(d == 0 || d == 2) {
			int iy = field.y[p];
			if(hRounds[iy] < 3) return false;
			int ix = field.x[p];
			if(vRounds[ix] == 0) return false;
			if(d == 0 && vRounds[ix + 1] == 0) return false;
			if(d == 0 && vRounds[ix + l] == 0) return false;
			if(d == 2 && vRounds[ix - 1] == 0) return false;
			if(d == 2 && vRounds[ix - l] == 0) return false;
		} else {
			int ix = field.x[p];
			if(vRounds[ix] < 3) return false;
			int iy = field.y[p];
			if(hRounds[iy] == 0) return false;
			if(d == 1 && hRounds[iy + 1] == 0) return false;
			if(d == 1 && hRounds[iy + l] == 0) return false;
			if(d == 3 && hRounds[iy - 1] == 0) return false;
			if(d == 3 && hRounds[iy - l] == 0) return false;
		}
		return true;
	}
	
	void add(int p, int d, int l) {
		int q = p;
		state[q] = 2;
		for (int i = 0; i < l; i++) {
			q = field.jp[d][q];
			state[q] = 1;
			if(i == 0) state[q] = 3;
			else if(i == l - 1) state[q] = 2;
		}
		int x1,x2,y1,y2;
		if(d == 0) {
			x1 = field.x[p] - 1;
			x2 = x1 + l + 2;
			y1 = field.y[p] - 1;
			y2 = y1 + 2;
		} else if(d == 1) {
			x1 = field.x[p] - 1;
			x2 = x1 + 2;
			y1 = field.y[p] - 1;
			y2 = y1 + l + 2;
		} else if(d == 2) {
			x2 = field.x[p] + 1;
			x1 = x2 - l - 2;
			y1 = field.y[p] - 1;
			y2 = y1 + 2;
		} else {
			x1 = field.x[p] - 1;
			x2 = x1 + 2;
			y2 = field.y[p] + 1;
			y1 = y2 - l - 2;
		}
		for (int ix = x1; ix <= x2; ix++) {
			for (int iy = y1; iy <= y2; iy++) {
				int qq = field.getIndex(ix, iy);
				if(qq >= 0) restriction[qq]++;
			}
		}
		if(d == 0 || d == 2) {
			int iy = field.y[p];
			hRounds[iy] -= 3;
			int ix = field.x[p];
			vRounds[ix]--;
			if(d == 0) vRounds[ix + 1]--; else vRounds[ix - 1]--; 
			if(d == 0) vRounds[ix + l]--; else vRounds[ix - l]--; 
		} else {
			int ix = field.x[p];
			vRounds[ix] -= 3;
			int iy = field.y[p];
			hRounds[iy]--;
			if(d == 1) hRounds[iy + 1]--; else hRounds[iy - 1]--;
			if(d == 1) hRounds[iy + l]--; else hRounds[iy - l]--;
		}
	}
	
	void remove(int p, int d, int l) {
		int q = p;
		state[q] = 0;
		for (int i = 0; i < l; i++) {
			q = field.jp[d][q];
			state[q] = 0;
		}
		int x1,x2,y1,y2;
		if(d == 0) {
			x1 = field.x[p] - 1;
			x2 = x1 + l + 2;
			y1 = field.y[p] - 1;
			y2 = y1 + 2;
		} else if(d == 1) {
			x1 = field.x[p] - 1;
			x2 = x1 + 2;
			y1 = field.y[p] - 1;
			y2 = y1 + l + 2;
		} else if(d == 2) {
			x2 = field.x[p] + 1;
			x1 = x2 - l - 2;
			y1 = field.y[p] - 1;
			y2 = y1 + 2;
		} else {
			x1 = field.x[p] - 1;
			x2 = x1 + 2;
			y2 = field.y[p] + 1;
			y1 = y2 - l - 2;
		}
		for (int ix = x1; ix <= x2; ix++) {
			for (int iy = y1; iy <= y2; iy++) {
				int qq = field.getIndex(ix, iy);
				if(qq >= 0) restriction[qq]--;
			}
		}
		if(d == 0 || d == 2) {
			int iy = field.y[p];
			hRounds[iy] += 3;
			int ix = field.x[p];
			vRounds[ix]++;
			if(d == 0) vRounds[ix + 1]++; else vRounds[ix - 1]++; 
			if(d == 0) vRounds[ix + l]++; else vRounds[ix - l]++; 
		} else {
			int ix = field.x[p];
			vRounds[ix] += 3;
			int iy = field.y[p];
			hRounds[iy]++;
			if(d == 1) hRounds[iy + 1]++; else hRounds[iy - 1]++;
			if(d == 1) hRounds[iy + l]++; else hRounds[iy - l]++;
		}
	}
	
	void move() {
		int p = waysP[t][way[t]];
		int d = waysD[t][way[t]];
		int l = waysL[t][way[t]];
		if(d >= 0) add(p, d, l);
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = waysP[t][way[t]];
		int d = waysD[t][way[t]];
		int l = waysL[t][way[t]];
		if(d >= 0) remove(p, d, l);
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
//			if(waysD[t - 1][way[t - 1]] >= 0) {
//				printState();
//				System.out.println("");
//				for (int i = 0; i < vRounds.length; i++) System.out.print(" " + vRounds[i]);
//				System.out.println("");
//				for (int i = 0; i < hRounds.length; i++) System.out.print(" " + hRounds[i]);
//				System.out.println("");
//			}
			if(isFinished() && isCorrect()) {
				printSolution();
			}
		}		
	}
	
	void printState() {
		for (int i = 0; i < field.size; i++) {
			System.out.print(" " + state[i]);
			if(field.x[i] == field.width - 1) System.out.println("");
		}
		System.out.println("");
//		for (int i = 0; i < field.size; i++) {
//			System.out.print(" " + restriction[i]);
//			if(field.x[i] == field.width - 1) System.out.println("");
//		}
	}
	
	void printSolution() {
		System.out.println("Solution:");
		printState();
		for (int i = 0; i < field.size; i++) {
			if(state[i] != 3) continue;
			int ix = field.x[i], iy = field.y[i];
			char c = (char)(65 + ix);
			int yy = iy + 1;
			System.out.print("," + c + "" + yy);
		}
		System.out.println("");
	}
	
	
	static int[] V_ROUNDS = {3,0,1,4,0,1}; // upper side digits
	static int[] H_ROUNDS = {3,1,2,0,1,2,0}; // left side digits

	static int[] V_ROUNDS_1 = {6,0,4,2,2,0,4,3,0,3};
	static int[] H_ROUNDS_1 = {4,0,0,4,2,2,2,4,0,6};

	static int[] V_ROUNDS_2 = {4,0,4,2,2,1,0,4,1};
	static int[] H_ROUNDS_2 = {4,1,2,3,0,3,1,1,3};

	public static void main(String[] args) {
		PaperClipsField f = new PaperClipsField();
		int[] vR = V_ROUNDS_2;
		int[] hR = H_ROUNDS_2;
		f.setSize(vR.length, hR.length);
		PaperClips p = new PaperClips();
		p.setField(f);
		p.setRounds(vR, hR);
		p.solve();
	}

}
