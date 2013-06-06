package champukr;

import com.slava.common.RectangularField;

public class UniformMatrixBuilder {
	RectangularField field;
	int linePointCount = 3;
	
	int[] state;
	int pointCount;
	int[] hPointCount;
	
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;
	
	public UniformMatrixBuilder() {}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		hPointCount = new int[field.getWidth()];
		place = new int[field.getSize() + 1];
		wayCount = new int[field.getSize() + 1];
		way = new int[field.getSize() + 1];
		ways = new int[field.getSize() + 1][2];
		pointCount = 0;
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(isSolution()) return;
		place[t] = getNextPlace();
		if(place[t] < 0) return;
		if(canLeaveEmpty(place[t])) {
			ways[t][wayCount[t]] = 0;
			wayCount[t]++;
		}
		if(canFill(place[t])) {
			ways[t][wayCount[t]] = 1;
			wayCount[t]++;
		}
	}
	
	int getNextPlace() {
		int p = (t == 0) ? 0 : place[t - 1] + 1;
		for(;;p++) {
			if(p >= field.getSize()) return -1;
			int ix = field.getX(p);
			int iy = field.getY(p);
			if(ix <= iy) continue;
			int d = Math.abs(ix - iy);
			if(d < 2 || d == field.getWidth() - 1) continue;
			break;
		}
		return p;
	}
	
	boolean canLeaveEmpty(int p) {
		int ix = field.getX(p);
		int iy = field.getY(p);
		if(hPointCount[iy] + field.getWidth() - ix <= linePointCount) return false;
		if(hPointCount[ix] + field.getWidth() - iy <= linePointCount) return false;
		return true;
	}
	
	boolean canFill(int p) {
		int ix = field.getX(p);
		int iy = field.getY(p);
		if(hPointCount[iy] >= linePointCount) return false;
		if(hPointCount[ix] >= linePointCount) return false;
		return true;
	}
	
	void move() {
		int p = place[t];
		int w = ways[t][way[t]];
		int ix = field.getX(p);
		int iy = field.getY(p);
		int p2 = field.getIndex(iy, ix);
		state[p] = w;
		state[p2] = w;
		if(w == 1) {
			hPointCount[iy]++;
			hPointCount[ix]++;
			pointCount += 2;
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = place[t];
		int w = ways[t][way[t]];
		int ix = field.getX(p);
		int iy = field.getY(p);
		int p2 = field.getIndex(iy, ix);
		state[p] = 0;
		state[p2] = 0;
		if(w == 1) {
			hPointCount[iy]--;
			hPointCount[ix]--;
			pointCount -= 2;
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
			if(isSolution() && isMatchingPlane()) {
				++solutionCount;
				printSolution();
			}
		}		
	}
	
	boolean isSolution() {
		for (int i = 0; i < hPointCount.length; i++) {
			if(hPointCount[i] != linePointCount) return false;
		}
		return true;
	}
	
	boolean isMatchingPlane() {
		for (int n = 0; n < field.getWidth(); n++) {
			int m = n - 2; if(m < 0) m += field.getWidth();
			if(state[field.getIndex(n, m)] == 0) continue;
			int m1 = m + 1;
			if(m1 >= field.getWidth()) m1 -= field.getWidth();
			int n1 = n + 1;
			if(n1 >= field.getWidth()) n1 -= field.getWidth();
			if(state[field.getIndex(n1, m1)] == 0) continue;
			if(state[field.getIndex(n1, m)] == 0) return false;
		}
		return true;
	}
	
	public void setField(RectangularField f) {
		this.field = f;
	}
	
	void printSolution() {
		for (int n1 = 1; n1 < field.getWidth(); n1++) {
			for (int n2 = 0; n2 < n1; n2++) {
				if(state[field.getIndex(n1, n2)] == 1) {
					System.out.print(" " + n1 + ":" + n2);
				}
			}
		}
		System.out.println("");
	}
	
	static void run() {
		UniformMatrixBuilder p = new UniformMatrixBuilder();
		RectangularField f = new RectangularField();
		f.setSize(8, 8);
		p.setField(f);
		p.solve();
		System.out.println("Solution count=" + p.solutionCount);
	}
	
	static void runRundom() {
		RandomPathBuilder.run(8, 3, 6);
	}
	

	public static void main(String[] args) {
//		run();
		runRundom();
	}

}
// 3:0 4:0 4:1 4:2 5:0 5:2 6:1 6:2 6:3 7:1 7:3 7:5

// 2:0 3:0 3:1 4:1 5:0 5:2 6:1 6:2 6:4 7:3 7:4 7:5
// 2:0 3:0 3:1 4:1 5:0 5:1 6:2 6:3 6:4 7:2 7:4 7:5 no