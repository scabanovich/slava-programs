package ik6_2;

public class DominoSums {
	int dominoCount;
	int[][] dominoIndex;
	
	int[][] pairs;
	int[][] expressions;
	int[] values;
	
	int[] used;
	int[] state;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	public DominoSums() {
		initDomino();
	}
	
	public void initDomino() {
		int n = 7;
		dominoCount = n * (n + 1) / 2;
		int index = 0;
		dominoIndex = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = i; j < n; j++) {
				dominoIndex[i][j] = index;
				dominoIndex[j][i] = index;
				index++;
			}
		}
	}
	
	public void setData(int[][] pairs, int[][] expressions, int[] values) {
		this.pairs = pairs;
		this.expressions = expressions;
		this.values = values;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		used = new int[dominoCount];
		state = new int[PAIRS.length];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		wayCount = new int[PAIRS.length + 1];
		way = new int[PAIRS.length + 1];
		ways = new int[PAIRS.length + 1][7];
		t = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == PAIRS.length) return;
		for (int v = 0; v < 7; v++) {
			boolean ok = add(t, v);
			if(ok) {
				ways[t][wayCount[t]] = v;
				wayCount[t]++;
			}
			remove(t, v);
		}
	}
	
	boolean add(int p, int v) {
		state[p] = v;
		boolean ok = true;
		for (int i = 0; i < pairs[p].length; i++) {
			int q = pairs[p][i];
			int d = dominoIndex[state[q]][v];
			used[d]++;
			if(used[d] > 1) ok = false;
		}
		return ok && checkEquation(p);
	}
	
	boolean checkEquation(int p) {
		if(expressions[p].length == 0) return true;
		int v = values[p];
		for (int i = 0; i < expressions[p].length; i++) {
			int c = expressions[p][i];
			if(c == 0) continue;
			v = v - c * state[i];
		}
		return v == 0;
	}
	
	void remove(int p, int v) {
		state[p] = -1;
		for (int i = 0; i < pairs[p].length; i++) {
			int q = pairs[p][i];
			int d = dominoIndex[state[q]][v];
			used[d]--;
		}
	}
	
	void move() {
		int p = t;
		int v = ways[t][way[t]];
		add(p, v);
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = t;
		int v = ways[t][way[t]];
		remove(p, v);
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
				System.out.println("t=" + t);
			}
			if(t == PAIRS.length) {
				printSolution();
			}
		}		
	}
	
	void printSolution() {
		for (int i = 0; i < t; i++) {
			char c = (char)(97 + i);
			System.out.print(" " + c + "=" + state[i]);
		}
		System.out.println("");
	}	
	
	
	
	static int[][] PAIRS = {
		{},        //0 a
		{0},       //1 b
		{1},       //2 c
		{2},       //3 d
		{0,3},     //4 e
		{2},       //5 f
		{5},       //6 g
		{6},       //7 h
		{7},       //8 i
		{3,5,8},   //9 j
		{3},       //10 k
		{9,10},    //11 l
		{4,10},    //12 m
		{12},      //13 n
		{13},      //14 o
		{10,14},   //15 p
		{8,11},    //16 q
		{16},      //17 r
		{17},     //18 s
		{11,15,18},//19 t
	};

	static int[][] EXPRESSIONS = {
		{},        //0 a
		{},        //1 b
		{},        //2 c
		{},        //3 d
		{2,2,3,3,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},       //4 e
		{},        //5 f 
		{},        //6 g
		{},        //7 h
		{},        //8 i
		{0,0,0,0,0,2,2,2,3,3,0,0,0,0,0,0,0,0,0,0},       //9 j
		{},        //10 k
		{0,0,0,1,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0},       //11 l
		{},        //12 m
		{},        //13 n
		{},        //14 o
		{0,0,0,0,0,0,0,0,0,0,3,0,3,2,2,2,0,0,0,0},       //15 p
		{},        //16 q
		{},        //17 r
		{},        //18 s
		{0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,2,2,2,3},       //19 t
	};

	static int[] VALUES = {
		0,        //0 a
		0,        //1 b
		0,        //2 c
		0,        //3 d
		47,       //4 e
		0,        //5 f 
		0,        //6 g
		0,        //7 h
		0,        //8 i
		29,       //9 j
		0,        //10 k
		9,       //11 l
		0,        //12 m
		0,        //13 n
		0,        //14 o
		27,       //15 p
		0,        //16 q
		0,        //17 r
		0,        //18 s
		35,       //19 t
	};

	public static void main(String[] args) {
		DominoSums p = new DominoSums();
		p.setData(PAIRS, EXPRESSIONS, VALUES);
		p.solve();
	}
}
