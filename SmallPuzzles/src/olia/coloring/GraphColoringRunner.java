package olia.coloring;

public class GraphColoringRunner {
	GraphGenerator g = new GraphGenerator();
	GraphColoring a = new GraphColoring();
	int[][] graph;
	
	public int runRandom(int n, int m) {
		while(true) {
			graph = g.generateRandomGraph(n, m);
			if(!hasLowBoundVertices()) break;
		}
		a.setGraph(graph);
		a.solve();
		int s = a.solutionCount;
		if(s % 6 != 0) throw new RuntimeException("Wrong solution " + s);
		s = s / 6;
		return s;
//		System.out.println(s);
	}
	
	public void runRandomSeries(int n, int m) {
		int[] achieved = new int[6000];
		while(true) {
			int s = runRandom(n, m);
			if(s >= achieved.length || achieved[s] == 1) continue;
			if(!isNot2or3(s)) continue;
		if(s < 1000 || s > 3000) continue;
			achieved[s] = 1;
			System.out.println(s);
			if(s == 2005) {
				printGraph();
				return;
			} 
			
		}
	}
	
	boolean isPrime(int s) {
		if(s < 1) return false;
		int q = (int)Math.sqrt(s);
		for (int i = 2; i <= q; i++) {
			if(s % i == 0) return false;
		}
		return true;
	}
	boolean isNot2or3(int s) {
		return s % 2 != 0 && s % 3 != 0;
	}
	boolean hasLowBoundVertices() {
		for (int i = 0; i < graph.length; i++) {
			int k = 0;
			for (int j = 0; j < graph.length; j++) {
				if(graph[i][j] > 0) ++k;
			}
			if(k < 2) return true;
		}
		return false;
	}
	
	public void printGraph() {
		for (int p1 = 0; p1 < graph.length; p1++) {
			for (int p2 = p1 + 1; p2 < graph.length; p2++) {
				if(graph[p1][p2] == 1) System.out.println(p1 + "-" + p2);
			}
		}
	}

	public static void main(String[] args) {
		GraphColoringRunner runner = new GraphColoringRunner();
		runner.runRandomSeries(15, 18);
	}
}

/* s=401
0-6 0-10 1-2 1-11 2-4 2-6 3-8 3-9 3-10 4-6 4-9 5-10 5-12 6-7 6-11 7-12
8-13 10-11 12-13

2005
0-3 0-9 1-3 1-6 1-13 2-6 2-8 2-12 2-13 4-13 4-14 5-8 5-10
6-11 7-10 7-14 8-11 9-12

2005
0-4 0-7 1-2 1-3 1-12 1-14 2-9 2-10 3-6 3-9 3-11 4-10 5-7 5-12 6-11
6-12 8-13 8-15 9-15 13-14
*/