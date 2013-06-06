package champ2005;

public class CubePath {
	CubeField field;
	int start;

	int[] visited;
	int[] position;
	
	int[][][] proectionState;
	int[] proectionBindCount;
	int freeBindCount;
	
	int tLimit;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	public CubePath() {}
	
	public void setField(CubeField f) {
		field = f;
	}
	
	public void setStart(int s) {
		start = s;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		visited = new int[field.size];
			ruleOutInnerNodes();
		position = new int[field.size + 1];
		way = new int[field.size + 1];
		wayCount = new int[field.size + 1];
		ways = new int[field.size + 1][6];
		position[0] = start;
		proectionState = new int[3][field.size][field.size];
		proectionBindCount = new int[3];
		freeBindCount = field.width * (field.width - 1) * 6;
		tLimit = freeBindCount / 2 + 5;
		t = 0;
	}
	
	private void ruleOutInnerNodes() {
		for (int i = 0; i < field.size; i++) {
			if(isInnerNode(i)) visited[i]++;
		}
	}
	
	private boolean isInnerNode(int i) {
		for (int d = 0; d < 6; d++) {
			if(field.jp[d][i] < 0) return false;
		}
		return true;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t > 0 && position[t] == start) return;		
		if(freeBindCount > (tLimit - t) * 2) return;
		if(isFinished()) return;
		int p = position[t];
		for (int d = 0; d < 6; d++) {
			int q = field.jp[d][p];
			if(q < 0 || visited[q] > 0) continue;
			ways[t][wayCount[t]] = d;
			wayCount[t]++;
		}
		if(t < 6) randomize();
	}
	
	void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = wayCount[t] - 1; i>= 1; i--) {
			int j = (int)(Math.random() * (i + 1));
			if(j == i) continue;
			int c = ways[t][i];
			ways[t][i] = ways[t][j];
			ways[t][j] = c;
		}
	}
	
	void move() {
		int p = position[t];
		int d = ways[t][way[t]];
		int q = field.jp[d][p];
		for (int a = 0; a < 3; a++) {
			int pa = field.proections[a][p];
			int qa = field.proections[a][q];
			if(pa == qa) continue;
			if(qa < pa) {
				int c = pa;
				pa = qa;
				qa = c;
			}
			proectionState[a][pa][qa]++;
			if(proectionState[a][pa][qa] == 1) {
				proectionBindCount[a]++;
				freeBindCount--;
			}
		}
		position[t + 1] = q;
		visited[q]++;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = position[t];
		int d = ways[t][way[t]];
		int q = field.jp[d][p];
		position[t + 1] = -1;
		visited[q]--;
		for (int a = 0; a < 3; a++) {
			int pa = field.proections[a][p];
			int qa = field.proections[a][q];
			if(pa == qa) continue;
			if(qa < pa) {
				int c = pa;
				pa = qa;
				qa = c;
			}
			proectionState[a][pa][qa]--;
			if(proectionState[a][pa][qa] == 0) {
				proectionBindCount[a]--;
				freeBindCount++;
			}
		}
	}
	
	void anal() {
		srch();
		way[t] = -1;
		int tm = field.size + 1;
		int tt = 25;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(freeBindCount < tt) {
				tt = freeBindCount;
				System.out.println("bc=" + freeBindCount);
			}
			if(isFinished() && t < tm) {
				tm = t;
				printSolution();
			}			
		}
	}
	
	boolean isFinished() {
		return freeBindCount == 0;
	}
	
	void printSolution() {
		System.out.println("t=" + t);
		System.out.println("start=" + start);
		for (int i = 0; i < t; i++) {
			System.out.print(" " + ways[i][way[i]]);
		}
		System.out.println("");
	}

	public static void main(String[] args) {
		CubePath p = new CubePath();
		CubeField f = new CubeField();
		f.setSize(4);
		p.setField(f);
		p.setStart(0);
		p.solve();
	}

}

/*
t=23
start=0
 0 1 0 4 2 3 2 0 1 5 1 2 3 4 3 1 5 0 5 3 4 2 4
t=23
start=1
 1 0 1 2 3 5 3 4 2 1 2 0 4 3 4 5 0 2 0 1 5 4 5

4x4x4
t=42
start=0
 2 0 5 0 0 1 1 1 2 2 2 3 3 3 4 5 0 1 5 0 4 0 2 4 3 2 1 3 4 4 5 0 5 1 5 3 3 1 2 4 2 4

*/