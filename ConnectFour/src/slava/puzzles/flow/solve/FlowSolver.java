package slava.puzzles.flow.solve;

import java.util.Random;

public class FlowSolver {
	// 1 - in; -1 - out.
	int[][] matrix;
	int[][] neighbours;

	int edges;
	int edgesToFill;

	int[][] filled;
	int[] filledCount;
	int[] usedNumbers;

	int t;
	int[] place1, place2;
	int[] wayCount;
	int[] way;
	int[][] ways;

	int solutionCount;
	int treeCount;

	public FlowSolver() {
	}

	public void setMatrix(int[][] matrix) {
		this.matrix = matrix;
		buildNeigbours();
	}

	void buildNeigbours() {
		neighbours = new int[matrix.length][];
		edgesToFill = 0;
		for (int i = 0; i < matrix.length; i++) {
			int k = 0;
			for (int j = 0; j < matrix[i].length; j++) {
				if(matrix[i][j] != 0) k++;
			}
			edgesToFill += k;
			neighbours[i] = new int[k];
			k = 0;
			for (int j = 0; j < matrix[i].length; j++) {
				if(matrix[i][j] != 0) {
					neighbours[i][k] = j;
					k++;
				}
			}
		}
		edgesToFill = edgesToFill / 2;
		edges = edgesToFill;
		System.out.println("edges=" + edgesToFill);
	}

	public void solve() {
		init();
		anal();
	}

	void init() {
		filled = new int[matrix.length][matrix.length];
		filledCount = new int[matrix.length];
		usedNumbers = new int[edges + 1];
		
		wayCount = new int[edges + 1];
		way = new int[edges + 1];
		ways = new int[edges + 1][edges + 1];
		place1 = new int[edges + 1];
		place2 = new int[edges + 1];

		t = 0;
		solutionCount = 0;
		treeCount = 0;
	}

	void srch() {
		wayCount[t] = 0;
		if(isCompleted()) {
			return;
		}
		if(!findPlace()) {
			System.out.println("fuck");
			return;
		}
		int n1 = place1[t];
		int n2 = place2[t];
		if(isLast(n1) && isLast(n2)) {
			int v1 = compute(n1);
			int v2 = compute(n2);
			if(v1 != v2 || v1 < 1 || v1 > edges || usedNumbers[v1] > 0) return;
			ways[t][0] = v1;
			wayCount[t] = 1;
		} else if(isLast(n1)) {
			int v = compute(n1);
			if(v < 1 || v > edges || usedNumbers[v] > 0) return;
			ways[t][0] = v;
			wayCount[t] = 1;
		} else if(isLast(n2)) {
			int v = compute(n2);
			if(v < 1 || v > edges || usedNumbers[v] > 0) return;
			ways[t][0] = v;
			wayCount[t] = 1;
		} else {
			for (int v = 1; v <= edges; v++) {
				if(usedNumbers[v] > 0) continue;
				ways[t][wayCount[t]] = v;
				wayCount[t]++;
			}
		}
	}

	boolean findPlace() {
		int bk = 100;
		int bn1 = -1;
		int bn2 = -1;
		for (int n1 = 0; n1 < matrix.length; n1++) {
			if(neighbours[n1].length == filledCount[n1]) continue;
			int k = 0;
			int n = -1;
			for (int j = 0; j < neighbours[n1].length; j++) {
				int n2 = neighbours[n1][j];
				if(filled[n1][n2] == 0) {
					if(k == 0) n = n2;
					k++;
				}
			}
			if(k > 0 && k < bk) {
				bk = k;
				bn1 = n1;
				bn2 = n;
			}
		}
		if(bk == 100) {
			return false;
		}
		place1[t] = bn1;
		place2[t] = bn2;
		return true;
	}

	boolean isLast(int i) {
		return neighbours[i].length == filledCount[i] + 1;
	}

	int compute(int n1) {
		int s = 0;
		int q = 0;
		for (int j = 0; j < neighbours[n1].length; j++) {
			int n2 = neighbours[n1][j];
			if(filled[n1][n2] != 0) {
				s += filled[n1][n2];
			} else if(matrix[n1][n2] != 0) {
				q = matrix[n1][n2];
			}
		}
		if(q == 0) {
			throw new RuntimeException();
		}
		return s * -q;
	}

	void move() {
		int p1 = place1[t];
		int p2 = place2[t];
		int v = ways[t][way[t]];
		filled[p1][p2] = matrix[p1][p2] * v;
		filled[p2][p1] = matrix[p2][p1] * v;
		filledCount[p1]++;
		filledCount[p2]++;
		usedNumbers[v] = 1;
		edgesToFill--;
		++t;
		srch();
		way[t] = -1;
	}

	void back() {
		--t;
		int p1 = place1[t];
		int p2 = place2[t];
		int v = ways[t][way[t]];
		filled[p1][p2] = 0;
		filled[p2][p1] = 0;
		filledCount[p1]--;
		filledCount[p2]--;
		usedNumbers[v] = 0;
		edgesToFill++;
	}

	void anal() {
		srch();
		way[t] = -1;
		int maxT = 0;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t > maxT) {
				maxT = t;
			}
			if(wayCount[t] == 0) {
				treeCount++;
			}
			if(isCompleted()) {
				solutionCount++;
				if(solutionCount == 1) printSolution();
			}
		}
	}

	boolean isCompleted() {
		return edgesToFill == 0;
	}

	void printSolution() {
		for (int i = 0; i < filled.length; i++) {
			for (int j = 0; j < filled.length; j++) {
				String s = "" + filled[i][j];
				while(s.length() < 4) s = " " + s;
				System.out.print(s);
			}
			System.out.println("");
		}
	}

	/**
	 * Generate random oriented matrix with exactly 3 edges at each node,
	 * at list 1 edge incoming, and at least 1 edge outcoming.
	 * @return
	 */
	public static int[][] generateRandomMatrix(int n) {
		Random seed = new Random();
		int[][] matrix = new int[n][n];
		int m = (n / 2) * 3;
		int[] usage = new int[n];
		int[] posUsage = new int[n];
		int s = 0;
		while(m > 0) {
			s++;
			if(s >= 10000) {
				//dead end
				return null;
			}
			int n1 = seed.nextInt(n);
			int n2 = seed.nextInt(n);
			if(n1 == n2 || matrix[n1][n2] != 0) continue;
			if(usage[n1] == 3 || usage[n2] == 3) continue;
			int c = seed.nextInt(2) == 0 ? -1 : 1;
			if(usage[n1] == 2) {
				if(posUsage[n1] == 2) c = -1; else if(posUsage[n1] == 0) c = 1;
			} else if(usage[n2] == 2) {
				if(posUsage[n2] == 2) c = 1; else if(posUsage[n2] == 0) c = -1;
			}
			matrix[n1][n2] = c;
			matrix[n2][n1] = -c;
			usage[n1]++;
			usage[n2]++;
			if(c == 1) posUsage[n1]++; else posUsage[n2]++;
			m--;
		}
		for (int i = 0; i < n; i++) {
			if(posUsage[i] == 0 || posUsage[i] == 3) {
				//unacceptable
				return null;
			}
		}
		return matrix;
	}

	static void printMatrix(int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				char ch = matrix[i][j] == 1 ? '+' : matrix[i][j] == -1 ? '-' : '*';
				System.out.print(" " + ch);
			}
			System.out.println("");
		}
	}

	static void test() {
		int sAttempts = 0;
		while(true) {
			sAttempts++;
			int[][] matrix = null;
			int attempts = 0;
			while(matrix == null) {
				attempts++;
				matrix = generateRandomMatrix(10);
				if(matrix == null) continue;
				if(!GraphUtil.isDoubleConnectedOrientedGraph(matrix)) {
					matrix = null;
					continue;
				}
				int edges = GraphUtil.getNumberOfEdges(matrix);
				int ce1 = GraphUtil.findMaximumContributingEdges(matrix, true);
				int ce2 = GraphUtil.findMaximumContributingEdges(matrix, false);
				System.out.println("-->" + ce1 + " " + ce2);
				if(ce1 * (ce1 + 1) > edges * 2 || ce2 * (ce2 + 1) > edges * 2) {
					matrix = null;
					continue;
				}
				
			}
//			printMatrix(matrix);
//			System.out.println("Attempts=" + attempts);
		
			FlowSolver p = new FlowSolver();
			p.setMatrix(matrix);
			p.solve();
			System.out.println("Solutions=" + p.solutionCount + " tree=" + p.treeCount);
			if(p.solutionCount == 1) break;
		}
		System.out.println("Attempts to solve=" + sAttempts);
	}

	public static void main(String[] args) {
		//test();
		int n = 600; int e = (int)(n * (n - 1) / 2 * 0.3);
		int[][] matrix = GraphUtil.createRandomGraph(n, e);
		int f = GraphUtil.getMaximumFullGraph(matrix);
		System.out.println("---->" + f);
	}

}
