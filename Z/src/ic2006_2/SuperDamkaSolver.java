package ic2006_2;

public class SuperDamkaSolver {
	static int EMPTY = 0;
	static int UP = 1;   //d = 2
	static int DOWN = 2; //d = 6
	static int DAMKA = 3;
	static int D_UP = 2;
	static int D_DOWN = 6;

	SuperDamkaField field;
	int[] initialState;
	int initialPlace;
	
	int[][] state;
	int[] sc;
	
	int[] place;
	int t;
	int[] wayCount;
	int[] way;
	int[][] waysD;
	int[][] waysR;
	
	int bestScore;

	public SuperDamkaSolver() {}
	
	public void setField(SuperDamkaField field) {
		this.field = field;
	}
	
	public void setInitialState(int[] s) {
		initialState = s;
	}
	
	public void setInitialPlace(int p) {
		initialPlace = p;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		int tmax = 14;
		state = new int[tmax][field.getSize()];
		state[0] = (int[])initialState.clone();
		sc = new int[field.getSize()];

		place = new int[tmax];
		place[0] = initialPlace;
		if(state[0][place[0]] != 0) throw new RuntimeException("Place " + place[0] + " is occupied.");
		state[0][place[0]] = DAMKA;

		wayCount = new int[tmax];
		way = new int[tmax];
		waysD = new int[tmax][30];
		waysR = new int[tmax][30];
		t = 0;
		bestScore = 11;
	}
	
	void srch() {
		wayCount[t] = 0;
		int pawns = getPawnCount();
		if(pawns == 0 && place[t] == place[0]) return;
		if(getScore() + pawns > bestScore) return;
		for (int d = 0; d < 8; d++) {
			int p = place[t];
			for (int r = 1; r < field.getWidth(); r++) {
				p = field.jump(p, d, 1);
				if(p < 0) break;
				if(state[t][p] == 0) {
					waysD[t][wayCount[t]] = d;
					waysR[t][wayCount[t]] = r;
					wayCount[t]++;
				}
			}
		}
	}
	
	void move() {
		int d = waysD[t][way[t]];
		int r = waysR[t][way[t]];
		int p = field.jump(place[t], d, r);
		int t1 = t + 1;
		place[t1] = p;		

		for (int i = 0; i < field.getSize(); i++) {
			sc[i] = state[t][i];
			state[t1][i] = 0;
		}
		sc[place[t]] = 0;
		sc[place[t1]] = DAMKA;
		state[t1][place[t1]] = DAMKA;

		if(r % 2 == 0) {
			int q2 = field.jump(place[t], d, r / 2);
			if(sc[q2] == UP || sc[q2] == DOWN) sc[q2] = 0; 
		}

		movePawns(sc);

		++t;
		srch();
		way[t] = -1;
	}
	
	void movePawns(int[] sc) {
		int[] s1 = state[t + 1];
		for (int i = 0; i < s1.length; i++) {
			if(sc[i] != UP && sc[i] != DOWN) continue;
			if(isBetweenDamkaAndEdge(sc, i)) {
				s1[i] = sc[i];
			} else if(isNearDamkaOrEdge(sc, i, D_UP)) {
				int q = field.jump(i, D_DOWN, 1);
				s1[q] = DOWN;
			} else if(isNearDamkaOrEdge(sc, i, D_DOWN)) {
				int q = field.jump(i, D_UP, 1);
				s1[q] = UP;
			} else if(sc[i] == UP) {
				if(isApproachingPawn(sc, i, D_UP)) {
					int q = field.jump(i, D_DOWN, 1);
					s1[q] = DOWN;
				} else {
					int q = field.jump(i, D_UP, 1);
					s1[q] = UP;
				}
			} else if(sc[i] == DOWN) {
				if(isApproachingPawn(sc, i, D_DOWN)) {
					int q = field.jump(i, D_UP, 1);
					s1[q] = UP;
				} else {
					int q = field.jump(i, D_DOWN, 1);
					s1[q] = DOWN;
				}
			}
		}
	}
	boolean isBetweenDamkaAndEdge(int[] sc, int i) {
		return isNearDamkaOrEdge(sc, i, D_UP) && isNearDamkaOrEdge(sc, i, D_DOWN);
	}
	boolean isNearDamkaOrEdge(int[] sc, int i, int d) {
		int q = field.jump(i, d, 1);
		return (q < 0 || sc[q] == DAMKA);
	}
	boolean isApproachingPawn(int[] sc, int i, int d) {
		int q = field.jump(i, d, 1);
		if(q < 0) return false;
		if(sc[q] == 3 - sc[i]) return true;
		if(sc[q] == sc[i]) return isNearDamkaOrEdge(sc, q, d);
		q = field.jump(q, d, 1);
		if(q < 0) return false;
		if(sc[q] == 3 - sc[i]) return true;
		if(sc[q] == sc[i]) return isNearDamkaOrEdge(sc, q, d);
		return false;
	}

	void back() {
		--t;
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
			if(getPawnCount() == 0 && place[t] == place[0]) {
				int score = getScore();
				if(score < bestScore) {
					bestScore = score;
					printSolution();
				}
			}
		}
	}
	
	int getPawnCount() {
		int[] s = state[t];
		int q = 0;
		for (int i = 0; i < s.length; i++) {
			if(s[i] == 1 || s[i] == 2) q++;
		}
		return q;
	}
	
	int getScore() {
		int s = t;
		int[] h = new int[field.getWidth()];
		int[] v = new int[field.getWidth()];
		for (int i = 0; i <= t; i++) {
			int p = place[i];
			h[field.getX(p)]++;
			v[field.getY(p)]++;
		}
		for (int i = 0; i < h.length; i++) {
			if(h[i] > 2) s += (h[i] - 2);
			if(v[i] > 2) s += (v[i] - 2);
		}
		return s;
	}
	
	void printSolution() {
		System.out.println("Score = " + bestScore);
		for (int i = 0; i <= t; i++) {
			System.out.print(" " + field.getDesignation(place[i]));
		}
		System.out.println("");
		for (int i = 0; i <= t; i++) {
			System.out.println("t=" + i);
			for (int p = 0; p < state[i].length; p++) {
				System.out.print(" " + state[i][p]);
				if(field.isRightBorder(p)) System.out.println("");
			}
			System.out.println("");
		}
	}

}
