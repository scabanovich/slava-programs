package forsmarts;

import com.slava.common.TwoDimField;

public class ApplesAndArrowsSolver {
	TwoDimField field;
	ArrowsPlacer arrowsPlacer;

	int[] state;
	int[] restriction;

	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;

	public ApplesAndArrowsSolver() {}

	public void setField(TwoDimField f) {
		field = f;
	}
	
	public void setArrowsPlacer(ArrowsPlacer p) {
		arrowsPlacer = p;
	}

	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		restriction = new int[field.getSize()];

		wayCount = new int[30];
		way = new int[30];
		ways = new int[30][200];
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == field.getHeight()) return;
		int iy = t;
		for (int ix = 0; ix < field.getWidth(); ix++) {
			int p = field.getIndex(ix, iy);
			if(restriction[p] > 0) continue;
			ways[t][wayCount[t]] = p;
			wayCount[t]++;
		}
	}
	
	void move() {
		int p = ways[t][way[t]];
		add(p);
		++t;
		srch();
		way[t] = -1;
	}
	
	void add(int p) {
		state[p] = 1;
		restriction[p]++;
		for (int d = 0; d < 8; d++) {
			int q = field.jump(p, d);
			if(q >= 0) restriction[q]++;
		}
		int ix = field.getX(p);
		for (int iy = 0; iy < field.getHeight(); iy++) {
			int q = field.getIndex(ix, iy);
			restriction[q]++;
		}
	}
	
	void back() {
		--t;
		int p = ways[t][way[t]];
		remove(p);
	}
	
	void remove(int p) {
		state[p] = 0;
		restriction[p]--;
		for (int d = 0; d < 8; d++) {
			int q = field.jump(p, d);
			if(q >= 0) restriction[q]--;
		}
		int ix = field.getX(p);
		for (int iy = 0; iy < field.getHeight(); iy++) {
			int q = field.getIndex(ix, iy);
			restriction[q]--;
		}
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
//				System.out.println("t=" + tm);
			}
			if(t == field.getHeight()) {
				++solutionCount;
				onSolutionCount();
			}
		}
	}
	
	void onSolutionCount() {
		arrowsPlacer.setApples(state);
		arrowsPlacer.solve();
		if(arrowsPlacer.solutionCount > 0) {
			System.out.println("->" + arrowsPlacer.solutionCount);
		}
	}
	
	
}
