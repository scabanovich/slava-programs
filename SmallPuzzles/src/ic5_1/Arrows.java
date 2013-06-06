package ic5_1;

public class Arrows {
	int width;
	int height;
	int size;
	int[] x,y;
	int[][] xy;
	int[][] jp;
	
	int[] arrows;
	int arrowsCount;
	
	int[] initialState;
	int[] state;
	int[] arrowState;

	int t;
	int[] wayCount;
	int[] way;
	int[] place;
	int[][] ways;
	
	public void setSize(int width) {
		this.width = width;
		this.height = width;
		size = width * height;
		x = new int[size];
		y = new int[size];
		xy = new int[width][height];
		for (int i = 0; i < size; i++) {
			x[i] = (i % width);
			y[i] = (i / width);
			xy[x[i]][y[i]] = i;
		}
		jp = new int[8][size];
		for (int i = 0; i < size; i++) {
			jp[0][i] = (x[i] == width - 1) ? -1 : i + 1;
			jp[1][i] = (x[i] == width - 1 || y[i] == height - 1) ? -1 : i + 1 + width;
			jp[2][i] = (y[i] == height - 1) ? -1 : i + width;
			jp[3][i] = (x[i] == 0 || y[i] == height - 1) ? -1 : i - 1 + width;
			jp[4][i] = (x[i] == 0) ? -1 : i - 1;
			jp[5][i] = (x[i] == 0 || y[i] == 0) ? -1 : i - 1 - width;
			jp[6][i] = (y[i] == 0) ? -1 : i - width;
			jp[7][i] = (x[i] == width - 1 || y[i] == 0) ? -1 : i + 1 - width;
		}
	}
	
	public void setInitialState(int[] s) {
		initialState = s;
		state = new int[s.length];
		for (int i = 0; i < size; i++) state[i] = s[i];
	}
	
	public void setArrows(int[] arrows) {
		this.arrows = arrows;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		arrowsCount = 0;
		for (int i = 0; i < size; i++) {
			if(arrows[i] >= 0) ++arrowsCount;
		}
		arrowState = new int[size];
		for (int i = 0; i < size; i++) arrowState[i] = -1;

		wayCount = new int[arrowsCount + 1];
		way = new int[arrowsCount + 1];
		place = new int[arrowsCount + 1];
		ways = new int[arrowsCount + 1][8];
		tempWays = new int[8];
		t = 0;
	}
	
	int[] tempWays;
	
	void srch() {
		wayCount[t] = 0;
		if(!canFinish()) return;
		if(arrowsCount == 0) return;
		int wcm = 9;
		place[t] = -1;
		for (int p = 0; p < size; p++) {
			if(arrows[p] < 0 || arrowState[p] >= 0) continue;
			int wc = getWayCount(p);
			if(wc < wcm) {
				if(wc == 0) return;
				wcm = wc;
				for (int i = 0; i < wc; i++) ways[t][i] = tempWays[i];
				place[t] = p;
			}
		}
		if(place[t] >= 0) wayCount[t] = wcm;
	}
	
	boolean canFinish() {
		for (int p = 0; p < size; p++) {
			if(state[p] <= 0) continue;
			int s = state[p];
			for (int d = 0; d < 8 && s > 0; d++) {
				if(canBeAttacked(p, d)) --s;
			}
			if(s > 0) return false;
		}
		return true;
	}
	
	boolean canBeAttacked(int p, int d) {
		while(p >= 0 && initialState[p] >= 0) p = jp[d][p];
		return (p >= 0 && arrows[p] >= 0 && arrowState[p] < 0);
	}
	
	int getWayCount(int p) {
		int wc = 0;
		for (int d = 0; d < 8; d++) {
			int p1 = jp[d][p];
			if(p1 < 0 || initialState[p1] < 0) continue;
			if(canAddArrow(p, d)) {
				tempWays[wc] = d;
				wc++;
			}
		}
		return wc;
	}
	
	boolean canAddArrow(int p, int d) {
		int p1 = jp[d][p];
		if(p1 < 0 || initialState[p1] < 0) return false;
		while(p1 >= 0 && initialState[p1] >= 0) {
			if(state[p1] <= 0) return false;
			p1 = jp[d][p1];
		}
		return true;
	}
	
	void move() {
		int d = ways[t][way[t]];
		int p = place[t];
		placeArrow(p, d);
		++t;
		srch();
		way[t] = -1;
	}
	
	void placeArrow(int p, int d) {
		arrowsCount--;
		arrowState[p] = d;
		int p1 = jp[d][p];
		while(p1 >= 0 && initialState[p1] >= 0) {
			state[p1]--;
			p1 = jp[d][p1];
		}
	}
	
	void back() {
		--t;
		int d = ways[t][way[t]];
		int p = place[t];
		removeArrow(p, d);
	}
	
	void removeArrow(int p, int d) {
		arrowsCount++;
		arrowState[p] = -1;
		int p1 = jp[d][p];
		while(p1 >= 0 && initialState[p1] >= 0) {
			state[p1]++;
			p1 = jp[d][p1];
		}
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return; else back();
			}
			++way[t];
			move();
			if(arrowsCount == 0 && canFinish()) {
				onSolutionFound();
			}
		}
	}
	
	void onSolutionFound() {
		printSolution();
	}
	
	void printSolution() {
		System.out.println("");
		for (int i = 0; i < size; i++) {
			int as = arrowState[i];
			String c = "" + (char)(97 + as);
			if(as < 0) {
				c = "*";
				if(state[i] >= 0) c = "" + state[i];
			} 
			System.out.print(" " + c);
			if(x[i] == width - 1) System.out.println("");
		}
		printOfficialSolution();
	}
	
	private void printOfficialSolution() {
		int[] q = new int[5];
		for (int p = 0; p < size; p++) {
			if(arrowState[p] >= 0) {
				if(arrowState[p] % 2 == 1) q[ARROWS[p]]++;
			}
		}
		for (int i = 0; i < q.length; i++) {
			if(i > 0) System.out.print(",");
			System.out.print(q[i]);		}
		System.out.println("");
	}

	static int E = -1;
	
	static int[] INITIAL_STATE = new int[]{
		E,E,E,E,E,E,E,E,E,E,E,
		E,4,3,2,5,3,4,2,4,4,E,
		E,3,2,1,3,3,1,3,2,5,E,
		E,3,2,E,E,E,E,E,4,3,E,
		E,5,4,E,1,1,0,E,3,5,E,
		E,4,3,E,2,2,3,E,3,4,E,
		E,3,1,E,1,1,2,E,2,3,E,
		E,2,3,E,E,E,E,E,1,2,E,
		E,5,2,3,3,2,3,2,2,3,E,
		E,5,3,2,4,3,1,3,2,3,E,
		E,E,E,E,E,E,E,E,E,E,E,
	};
	
	//value is arrow group index, for computing official results.
	static int[] ARROWS = new int[] {
		E,0,0,0,0,0,0,0,0,0,E,
		3,E,E,E,E,E,E,E,E,E,1,
		3,E,E,E,E,E,E,E,E,E,1,
		3,E,E,E,4,4,4,E,E,E,1,
		3,E,E,4,E,E,E,4,E,E,1,
		3,E,E,4,E,E,E,4,E,E,1,
		3,E,E,4,E,E,E,4,E,E,1,
		3,E,E,E,4,4,4,E,E,E,1,
		3,E,E,E,E,E,E,E,E,E,1,
		3,E,E,E,E,E,E,E,E,E,1,
		E,2,2,2,2,2,2,2,2,2,E,
	};

	public static void main(String[] args) {
		Arrows a = new Arrows();
		a.setSize(11);
		a.setInitialState(INITIAL_STATE);
		a.setArrows(ARROWS);
		a.solve();
	}
}
/*
 * c c b c b d b c c *
 a 0 0 0 0 0 0 0 0 0 e
 a 0 0 0 0 0 0 0 0 0 d
 a 0 0 * b d f * 0 0 d
 a 0 0 e 0 0 0 h 0 0 d
 a 0 0 a 0 0 0 e 0 0 f
 h 0 0 f 0 0 0 e 0 0 f
 b 0 0 * c h d * 0 0 f
 b 0 0 0 0 0 0 0 0 0 e
 h 0 0 0 0 0 0 0 0 0 e
 * g f g h f f g f g *
4,6,5,4,7
*/