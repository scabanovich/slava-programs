package forsmarts;

import com.slava.common.TwoDimField;

public class XPlusYCircles {
	int[][] figureDirections = {
		{1,3,5,7}, //x
		{0,2,4,6}, //+
		{2,5,7}    //y
	};
	TwoDimField field;
	
	int[] initialState;
	int circleLimit;
	int[] neighbours;
	
	int[] state;	
	int[] restriction;
	int[] hCircleCount;
	int[] vCircleCount;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;

	public XPlusYCircles() {}
	
	public void setField(TwoDimField f) {
		field = f;
	}
	
	public void setInitialState(int[] s) {
		initialState = s;
	}
	
	public void setCircleLimit(int c) {
		circleLimit = c;
	}
	
	public void setNeighbours(int[] n) {
		neighbours = n;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		restriction = new int[field.getSize()];
		hCircleCount = new int[field.getHeight()];
		vCircleCount = new int[field.getWidth()];
		for (int i = 0; i < initialState.length; i++) {
			if(initialState[i] > 0) add(i);
		}
		wayCount = new int[30];
		way = new int[30];
		ways = new int[30][200];
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(circleLimit == 0 || t == 25) return;
		int ix = (t % 5) * 2;
		int iy = (t / 5) * 2;
		int p = field.getIndex(ix, iy);
		addVar(p);
		for (int d = 0; d < 3; d++) {
			int q = field.jump(p, d);
			addVar(q);
		}
		if(circleLimit + t <= 25) {
			ways[t][wayCount[t]] = -1;
			wayCount[t]++;
		}
	}
	
	void addVar(int p) {
		if(restriction[p] > 0 || state[p] > 0) return;
		if(hCircleCount[field.getY(p)] >= 3) return;
		if(vCircleCount[field.getX(p)] >= 3) return;
		ways[t][wayCount[t]] = p;
		wayCount[t]++;
	}
	
	void move() {
		int p = ways[t][way[t]];
		if(p >= 0) add(p);
		++t;
		srch();
		way[t] = -1;
	}
	
	void add(int p) {
		--circleLimit;
		state[p] = 1;
		restriction[p]++;
		hCircleCount[field.getY(p)]++;
		vCircleCount[field.getX(p)]++;
		for (int d = 0; d < 8; d++) {
			int q = field.jump(p, d);
			if(q >= 0) restriction[q]++;
		}
	}
	
	void back() {
		--t;
		int p = ways[t][way[t]];
		if(p >= 0) remove(p);
	}
	
	void remove(int p) {
		++circleLimit;
		state[p] = 0;
		restriction[p]--;
		hCircleCount[field.getY(p)]--;
		vCircleCount[field.getX(p)]--;
		for (int d = 0; d < 8; d++) {
			int q = field.jump(p, d);
			if(q >= 0) restriction[q]--;
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
			if(circleLimit == 0 && isSolution()) {
				++solutionCount;
				onSolutionFound();
//				printSolution();
			}
		}
	}
	
	boolean isSolution() {
		for (int p = 0; p < neighbours.length; p++) {
			if(neighbours[p] < 0) continue;
			int fg = -1;
			for (int k = 0; k < figureDirections.length && fg < 0; k++) {
				if(getNeighbours(p, figureDirections[k]) == neighbours[p]) fg = k;
			}
			if(fg < 0) return false;
		}
		return true;
	}
	
	void onSolutionFound() {
		XPlusYLetters p = new XPlusYLetters();
		p.setCircleProgram(this);
		p.solve();
	}
	
	int getNeighbours(int p, int[] ds) {
		int c = 0;
		for (int i = 0; i < ds.length; i++) {
			c += getNeighbours(p, ds[i]);
		}
		return c;
	}
	
	int getNeighbours(int p, int d) {
		int q = field.jump(p, d);
		int c = 0;
		while(q >= 0) {
			if(state[q] > 0) ++c;
			q = field.jump(q, d);
		}
		return c;
	}
	
	void printSolution() {
		for (int i = 0; i < state.length; i++) {
			char c = (state[i] == 0) ? '.' : 'o';
			System.out.print(" " + c);
			if(field.isRightBorder(i)) System.out.println("");
		}
		System.out.println("");
	}

}
