package forsmarts;

import com.slava.common.TwoDimField;

public class ArrowsPlacer {
	static String[] ARROW_DIRECTION = {
		"E", "SE", "S", "SW", "W", "NW", "N", "NE"
	};
	
	TwoDimField field;
	
	int[] hDistance;
	int[] vDistance;
	int[] hDirection;
	int[] vDirection;
	
	int[] apples;
	
	int[] state;
	int[] restriction;
	int[] applesHit;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] waysP;
	int[][] waysD;
	
	int solutionCount;

	public ArrowsPlacer() {}
	
	public void setField(TwoDimField f) {
		field = f;
	}
	
	public void setArrowData(int[] hDistance, int[] vDistance, int[] hDirection, int[] vDirection) {
		this.hDistance = hDistance;
		this.vDistance = vDistance;
		this.hDirection = hDirection;
		this.vDirection = vDirection;
	}
	
	public void setApples(int[] apples) {
		this.apples = (int[])apples.clone();
	}

	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		restriction = new int[field.getSize()];
		
		for (int i = 0; i < apples.length; i++) {
			if(apples[i] == 1) addApple(i);
		}
		applesHit = new int[field.getSize()];

		wayCount = new int[30];
		way = new int[30];
		waysP = new int[30][200];
		waysD = new int[30][200];
		t = 0;
		solutionCount = 0;
	}
	
	void addApple(int p) {
		restriction[p]++;
		for (int d = 0; d < 8; d++) {
			int q = field.jump(p, d);
			if(q >= 0) restriction[q]++;
		}
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == field.getHeight()) return;
		int iy = t;
		for (int ix = 0; ix < field.getWidth(); ix++) {
			int p = field.getIndex(ix, iy);
			if(restriction[p] > 0) continue;
			for (int d = 0; d < 8; d++) {
				if(hDirection[iy] >= 0 && hDirection[iy] != d) continue;
				if(vDirection[ix] >= 0 && vDirection[ix] != d) continue;
				int apple = getApple(p, d);
				if(apple < 0 || applesHit[apple] > 0) continue;
				int s = getDistanceToApple(p, d);
				if(hDistance[iy] >= 0 && hDistance[iy] != s) continue;
				if(vDistance[ix] >= 0 && vDistance[ix] != s) continue;
				waysP[t][wayCount[t]] = p;
				waysD[t][wayCount[t]] = d;
				wayCount[t]++;
			}
		}
	}
	
	int getApple(int p, int d) {
		int apple = -1;
		int q = field.jump(p, d);
		while(q >= 0) {
			if(apples[q] > 0) {
				if(apple >= 0) return -1;
				apple = q;
					//???
					return q;
			}
			q = field.jump(q, d);
		}		
		return apple;
	}
	
	int getDistanceToApple(int p, int d) {
		int q = field.jump(p, d);
		int s = 1;
		while(q >= 0) {
			if(apples[q] > 0) {
				return s;
			}
			q = field.jump(q, d);
			++s;
		}		
		return -1;
	}
	
	void move() {
		int p = waysP[t][way[t]];
		int d = waysD[t][way[t]];
		add(p, d);
		++t;
		srch();
		way[t] = -1;
	}
	
	void add(int p, int d0) {
		int apple = getApple(p, d0);
		applesHit[apple]++;
		state[p] = d0;
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
		int p = waysP[t][way[t]];
		int d = waysD[t][way[t]];
		remove(p, d);
	}
	
	void remove(int p, int d0) {
		int apple = getApple(p, d0);
		applesHit[apple]--;
		state[p] = -1;
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
		printSolution();
	}
	
	void printSolution() {
		for (int i = 0; i < field.getSize(); i++) {
			if(apples[i] > 0) {
				System.out.print(" a");
			} else if(state[i] >= 0) {
				System.out.print(" " + state[i]);
			} else {
				System.out.print(" .");
			}
			if(field.isRightBorder(i)) System.out.println("");
		}
		System.out.println("");
		for (int i = 0; i < field.getSize(); i++) {
			if(state[i] >= 0) {
				System.out.print(" ," + ARROW_DIRECTION[state[i]]);
			}
		}
		System.out.println("");		
	}	

}
