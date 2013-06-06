package olia.coloring;

public class GraphGenerator {
	
	int[][] generateRandomGraph(int n, int m) {
		int[][] graph = new int[n][n];
		for (int k = 0; k < m; k++) {
			int p1 = 0;
			int p2 = 0;
			boolean b = false;
			while(!b) {
				p1 = (int)(n * Math.random());
				p2 = (int)(n * Math.random());
				b = (p1 != p2 && graph[p1][p2] == 0);
			}
			graph[p1][p2] = 1;
			graph[p2][p1] = 1;
		}
		return graph;
	}

}
