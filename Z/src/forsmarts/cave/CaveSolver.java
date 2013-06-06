package forsmarts.cave;

public class CaveSolver {
	CaveField field;
	int[] data;
	
	int[] state;

	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;

	public CaveSolver() {}
	
	public void setField(CaveField field) {
		this.field = field;
	}
	
	public void setData(int[] data) {
		this.data = data;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		int size = field.getSize();
		state = new int[size];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		wayCount = new int[size + 1];
		way = new int[size + 1];
		ways = new int[size + 1][4];
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == field.getSize()) return;
		if(!isOk()) return;
		for (int v = 0; v < 2; v++) {
			ways[t][wayCount[t]] = v;
			wayCount[t]++;
		}
	}
	
	boolean isOk() {
		return check(0) && check(1) && checkData();
	}
	
	int[] stack = new int[100];
	int[] temp = new int[100];
	
	boolean check(int v) {
		for (int i = 0; i < field.getSize(); i++) temp[i] = 0;
		int pb = -1;
		int volume = 0;
		for (int p = 0; p < state.length; p++) if(state[p] == v) {
			pb = p;
			volume++;
		}
		if(volume == 0) return true;
		int vc = 1;
		int c = 0;
		volume--;
		temp[pb] = 1;
		stack[0] = pb;
		
		while(c < vc) {
			int p = stack[c];
			for (int d = 0; d < 4; d++) {
				int q = field.jp[d][p];
				if(q < 0) continue;
				if(state[q] >= 0 && state[q] != v) continue;
				if(temp[q] > 0) continue;
				temp[q] = 1;
				stack[vc] = q;
				vc++;
				if(state[q] == v) --volume;
			}
			++c;
		}
		
		return volume == 0;
	}

	boolean checkData() {
		for (int p = 0; p < data.length; p++) if(!checkData(p)) return false;
		return true;
	}
	
	boolean checkData(int p) {
		if(data[p] < 0 || state[p] < 0) return true;
		int qmin = 0;
		int qmax = 0;
		for (int d = 0; d < 4; d++) {
			int q = field.jp[d][p];
			boolean x = false;
			while(q >= 0 && (state[q] < 0 || state[q] == state[p])) {
				++qmax;
				if(state[q] < 0) x = true;
				if(!x) qmin++;
				q = field.jp[d][q];
			}
		}		
		return data[p] >= qmin && data[p] <= qmax;
	}
	
	void move() {
		int p = t;
		int v = ways[t][way[t]];
		state[p] = v;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = t;
		state[p] = -1;
	}
	
	void anal() {
		srch();
		way[t] = -1;
		int tm = 0;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t > tm) {
				tm = t;
				System.out.println(tm);
			}
			if(t == field.getSize() && isOk()) {
				++solutionCount;
				printSolution();
			}
		}
	}
	
	void printSolution() {
//		if(solutionCount > 1) {
//			if(solutionCount % 10000 == 0) {
//				System.out.println(solutionCount);
//			} else {
//				return;
//			}
//		}
		System.out.println("Solution found");
		for (int i = 0; i < state.length; i++) {
			System.out.print(" " + state[i]);
			if(field.x[i] == field.getWidth() - 1) System.out.println("");
		}
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}


}
