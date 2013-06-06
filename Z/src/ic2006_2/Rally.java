package ic2006_2;

public class Rally {
	int[][] matrix;
	
	int[] used;
	int[] place;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int path;
	int minimumPath;
	int start;
	
	public Rally() {}
	
	public void setMatrix(int[][] m) {
		matrix = m;
		validate();
	}
	
	public void validate() {
		for (int i = 0; i < matrix.length; i++) {
			if(matrix[i][i] != 0) System.out.println("Error:" + i);
			for (int j = i + 1; j < matrix.length; j++) {
				if(matrix[i][j] != matrix[j][i]) {
					System.out.println("Error:" + i + ":" + j);
				}
			}
		}
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		//129: F E D J L N R P S O K M G C B A H Q U T I
		//129: N R P S U T I B C A H Q L J D E F G M K O
		//129: Q H A C B I T U S P R N L J D E F G M K O
		//129: U T I B C A H Q L J D E F G M K O S P R N
		start = 13;
		used = new int[matrix.length];
		place = new int[matrix.length + 1];
		place[0] = start;
		used[start] = 1;
		wayCount = new int[matrix.length + 1];
		way = new int[matrix.length + 1];
		ways = new int[matrix.length + 1][matrix.length];
		t = 0;
		minimumPath = 10000;
		path = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		int p = place[t];
		for (int q = 0; q < matrix.length; q++) {
			if(matrix[p][q] == 0 || used[q] > 0) continue;
			ways[t][wayCount[t]] = q;
			wayCount[t]++;
		}
	}
	
	void move() {
		int p = place[t];
		int q = ways[t][way[t]];
		place[t + 1] = q;
		used[q] = 1;
		path = path + matrix[p][q];
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = place[t];
		int q = place[t + 1];
		used[q] = 0;
		path = path - matrix[p][q];
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
			if(t == matrix.length - 1 && path <= minimumPath) {
				minimumPath = path;
				printSolution();
			}
		}
	}
	
	void printSolution() {
		System.out.println("path=" + minimumPath);
		System.out.print((char)(65 + start));
		for (int i = 0; i < t; i++) System.out.print(" " + (char)(65 + ways[i][way[i]]));
		System.out.println("");
	}
	
	
	static int[][] MATRIX = {
	//   A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U	
/*A*/	{0, 6, 5, 9, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	
/*B*/	{6, 0, 5, 0, 0, 0, 7, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	
/*C*/	{5, 5, 0, 7, 8, 9, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	
/*D*/	{9, 0, 7, 0, 6, 0, 0, 8, 0, 5, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0},	
/*E*/	{0, 0, 8, 6, 0, 8, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	
/*F*/	{0, 0, 9, 0, 8, 0, 8, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	
/*G*/	{0, 7, 6, 0, 0, 8, 0, 0, 8, 0, 9, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0},	
/*H*/	{6, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, 0, 7, 0, 0, 0, 0},	
/*I*/	{0, 8, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 7, 0},	
/*J*/	{0, 0, 0, 5, 8, 0, 0, 0, 0, 0, 0, 6, 0, 9, 0, 0, 0, 0, 0, 0, 0},	
/*K*/	{0, 0, 0, 0, 0, 9, 9, 0, 0, 0, 0, 0, 6, 0, 6, 0, 0, 0, 0, 0, 0},	
/*L*/	{0, 0, 0, 5, 0, 0, 0, 7, 0, 6, 0, 0, 0, 7, 0, 0, 7, 9, 0, 0, 0},	
/*M*/	{0, 0, 0, 0, 0, 0, 6, 0, 9, 0, 6, 0, 0, 0, 9, 0, 0, 0, 8, 8, 0},	
/*N*/	{0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0, 7, 0, 0, 0, 7, 0, 6, 0, 0, 0},	
/*O*/	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 0, 9, 0, 0, 9, 0, 0, 9, 0, 0},	
/*P*/	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 9, 0, 0, 6, 6, 0, 0},	
/*Q*/	{0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, 7, 0, 0, 0, 0, 0, 9, 0, 0, 9},	
/*R*/	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0, 6, 0, 6, 9, 0, 8, 0, 9},	
/*S*/	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 9, 6, 0, 8, 0, 9, 9},	
/*T*/	{0, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, 8, 0, 0, 0, 0, 0, 9, 0, 6},	
/*U*/	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 9, 9, 6, 0},	
	//   A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U	
	};

	public static void main(String[] args) {
		Rally p = new Rally();
		p.setMatrix(MATRIX);
		p.solve();
	}

}
