package match2005;

public class PushAnalyzer {
	int CODEBASE = 61000007;
	PushField field;
	int[][][] figures;
	int[] state;
	int[] initialState;
	int[] finalState;
	
	int t;
	int moveLimit;
	int[] moveNumber;
	int[] wayCount;
	int[] way;
	int[][] waysF;
	int[][] waysB;
	int[][] waysE;
	
	byte[] movesToState = new byte[CODEBASE];
	byte[] moves2ToState = new byte[CODEBASE];
	byte[] passedStates = new byte[CODEBASE];
	int passedStateCount;
	int moveCount;
	
	public PushAnalyzer() {}
	
	public void setField(PushField field) {
		this.field = field;
	}
	
	public void setInitialState(int[] state) {
		initialState = state;
	}
	
	public void setFinalState(int[] state) {
		finalState = state;
	}
	
	public void setFigures(int[][][] figures) {
		this.figures = figures;
	}
	
	public void solve() {
		init();
		ganal();
	}
	
	void init() {
		wayCount = new int[1000];
		way = new int[1000];
		waysF = new int[1000][40];
		waysB = new int[1000][40];
		waysE = new int[1000][40];
		moveNumber = new int[1000];
		moveNumber[0] = 0;
		state = new int[initialState.length];
		for (int i = 0; i < initialState.length; i++) {
			state[i] = initialState[i];
			field.addFigure(state[i], figures[i], i);
		}
		int code = code(state);
		for (int i = 0; i < CODEBASE; i++) {
			movesToState[i] = -1;
			moves2ToState[i] = -1;
		} 
		movesToState[code] = 0;
		moves2ToState[code] = 0;
		t = 0;
		moveLimit = 1;
		passedStateCount = 0;
		writeState();
	}
	
	void srch() {
		wayCount[t] = 0;
		if((t > 0 && moveNumber[t - 1] + estimateMoves() > moveLimit)) return;
		boolean isLimit = (t > 0 && moveNumber[t - 1] + estimateMoves() >= moveLimit);
///		int unstableFigure = getUnstable();
		for (int f = 0; f < figures.length; f++) {
///			if(unstableFigure >= 0 && unstableFigure != f) continue;
			if(isLimit && f != waysF[t - 1][way[t - 1]]) continue;
			int pb = state[f];
			int mn = (t == 0) ? 1 : (waysF[t - 1][way[t - 1]] == f) ? moveNumber[t - 1] : moveNumber[t - 1] + 1;
			for (int d = 0; d < 4; d++) {
				int pe = field.jp[d][pb];
				if(pe < 0 || !field.canAddFigure(pe, figures[f], f)) continue;
				state[f] = pe;
				int code = code(state);
				state[f] = pb;
///				System.out.println("t=" + t + " code=" + code + " mn=" + mn + " moves=" + movesToState[code] + " moves2=" + moves2ToState[code] + "ps=" + passedStates[code]);
				if(movesToState[code] < 0 ||
				   movesToState[code] > mn ||
				   (movesToState[code] == mn &&
						(moves2ToState[code] < 0 || moves2ToState[code] > t + 1 || (moves2ToState[code] == t + 1 && passedStates[code] == 0)))) {
					waysF[t][wayCount[t]] = f;	
					waysB[t][wayCount[t]] = pb;	
					waysE[t][wayCount[t]] = pe;
					wayCount[t]++;
				}	
			}			
		}
	}
	
	int estimateMoves() {
		int q = 0;
		for (int i = 0; i < state.length; i++) {
			if(state[i] != finalState[i]) ++q;
		}
		if(q > 0) q--;
		return q;
	}

	//hack	
	int[] stablePoints = new int[]{
		1,0,0,1,0,0,1,0,0,0,
		0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,
		1,0,0,1,0,0,1,0,0,0,
		0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,
		1,0,0,1,0,0,1,0,0,0,
		0,0,0,0,0,0,1,1,0,0,
		0,0,0,0,0,0,0,0,0,0,
	};
	private int getUnstable() {
		for (int i = 0; i < state.length; i++) {
			if(stablePoints[state[i]] != 1) return i;
		}
		return -1;
	}
	
	void move() {
		int f = waysF[t][way[t]];
		int pb = waysB[t][way[t]];
		int pe = waysE[t][way[t]];
		field.moveFigure(pb, pe, figures[f], f);
		moveNumber[t] = (t == 0) ? 1 : (waysF[t - 1][way[t - 1]] == f) ? moveNumber[t - 1] : moveNumber[t - 1] + 1;
		state[f] = pe;

		++t;

		int code = code(state);
		if(movesToState[code] < 0 || movesToState[code] >= moveNumber[t - 1]) {
			if(movesToState[code] < 0) {
				passedStateCount++;
				if(passedStateCount % 100000 == 0) System.out.println("passedStateCount=" + passedStateCount);
			}
			movesToState[code] = (byte)moveNumber[t - 1];
			if(moves2ToState[code] < 0 || moves2ToState[code] > t) {
				moves2ToState[code] = (byte)t;
			}
			passedStates[code] = 1;
		}
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int f = waysF[t][way[t]];
		int pb = waysB[t][way[t]];
		int pe = waysE[t][way[t]];
		field.moveFigure(pe, pb, figures[f], f);
		state[f] = pb;
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
			moveCount++;
			if(isFinished()) {
				printSolution();
				return;
			} 
		}
	}
	
	boolean isFinished() {
		for (int i = 0; i < state.length; i++) {
			if(state[i] != finalState[i]) return false;
		}
		return true;
	}
	
	void ganal() {
		while(!isFinished()) {
			System.out.println("moveLimit=" + moveLimit + " " + t);
			moveCount = 0;
			anal();
			System.out.println("moveCount=" + moveCount);
			++moveLimit;
			for (int i = 0; i < CODEBASE; i++) {
				passedStates[i] = 0;
			}
		}
	}
	
	int code(int[] state) {
		int c = 0;
		for (int i = 0; i < state.length; i++) {
			c = (c * 107 + state[i]) % CODEBASE;
		}
		c = c % CODEBASE;
		if(c < 0) c+= CODEBASE;
		return c;
	}
	
	void writeState() {
		for (int i = 0; i < field.size; i++) {
			int v = field.values[i];
			int cs = field.constraint[i];
			char c = (v < 0) ? (cs == 1 ? '*' : ' ') : (char)(97 + v);
			System.out.print(" " + c);
			if(field.x[i] == field.width - 1) System.out.println("");
		}
	}
	
	void printSolution() {
		System.out.println();
		int f = -1;
		char[] ds = {'r', 'd', 'l', 'u'};
		for (int i = 0; i < t; i++) {
			int fi = waysF[i][way[i]];
			if(fi != f) {
				System.out.print((fi + 1));
				f = fi;
			}
			int pb = waysB[i][way[i]];
			int pe = waysE[i][way[i]];
			char c = '?';
			for (int d = 0; d < 4 && c == '?'; d++) {
				if(pe == field.jp[d][pb]) c = ds[d];
			}
			System.out.print(c);
		}
		System.out.println();
	}

	static int[] INITIAL_STATE = new int[]{0,3,6,30,33,36,63,60}; 	
	static int[] FINAL_STATE = new int[]{0,3,6,30,33,36,60,63}; 	
	static int[] CONSTRAINT = new int[] { ///
		1,1,1,1,1,1,1,1,1,0,
		1,1,1,1,1,1,1,1,1,0,
		1,1,1,1,1,1,1,1,1,0,
		1,1,1,1,1,1,1,1,1,0,
		1,1,1,1,1,1,1,1,1,0,
		1,1,1,1,1,1,1,1,1,0,
		1,1,1,1,1,1,1,1,1,0,
		1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,
		0,0,0,0,0,0,1,1,1,1
	};
	
	static int[][][] FIGURES = new int[][][]{
		{{0,0}, {1,0},               {1,1},        {0,2}, {1,2}, {2,2}},
		{{0,0}, {1,0}, {2,0}, {0,1}, {1,1}, {2,1}, {0,2}, {1,2}, {2,2}},
		{{0,0}, {1,0}, {2,0}, {0,1}, {1,1}, {2,1}, {0,2}, {1,2}, {2,2}},
		{{0,0},        {2,0}, {0,1}, {1,1}, {2,1},               {2,2}},
		{{0,0}, {1,0}, {2,0}, {0,1}, {1,1}, {2,1}, {0,2}, {1,2}, {2,2}},
		{{0,0}, {1,0}, {2,0}, {0,1}, {1,1}, {2,1}, {0,2}, {1,2}, {2,2}},
		{{0,0}, {1,0}, {2,0},               {2,1},               {2,2}},
		{{0,0}, {1,0}, {2,0}, {0,1}, {1,1}, {2,1}, {0,2}, {1,2}, {2,2}},
	};

	public static void main(String[] args) {
		PushAnalyzer a = new PushAnalyzer();
		a.setFigures(FIGURES);
		a.setInitialState(INITIAL_STATE);
		a.setFinalState(FINAL_STATE);
		PushField field = new PushField();
		field.setSize(10, 10);   
		field.setConstraint(CONSTRAINT);
		a.setField(field);
		a.solve();
	}
}
//back
//6d 5r 4r 7u 8l 6l 5d 3d 2r 4u 7r 1d 4l 7u 4drrddr 1u 4l 7d 2l 3u 5u 6r 7d 5l 6u

//forward
//6d 5r 7u 6l 5d 3d 2r 7u 4r 1d 4luullu 7d 4r 1u 7l 4d 2l 3u 5u 6r 8r 7d 4l 5l 6u

//another
//6d 5r 7u 6l 5d 3d 2r 7uulu 4r 1d 7lldd 4u 7rrdr 1u 7l 4d 2l 3u 5u 6r 8r 7d 4l 5l 6u
