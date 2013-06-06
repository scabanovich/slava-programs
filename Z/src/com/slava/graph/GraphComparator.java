package com.slava.graph;

import java.util.*;

public class GraphComparator {
	int[][] graph1;
	int[][] graph2;
	int[] weights1;
	int[] weights2;
	
	int[] mapping_1by2;  // graph1 node by graph2 node
	int[] mapping_2by1;  // graph1 node by graph2 node
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	// This is the number of isomorphic mappings 
	int solutionCount;
	
	public GraphComparator() {}
	
	public void setGraph1(int[][] g) {
		graph1 = g;
		weights1 = getNodeWeights(g);
	}
	
	public void setGraph2(int[][] g) {
		graph2 = g;
		weights2 = getNodeWeights(g);
	}
	
	public int getIsomorhismCount(int[][] g1, int[][] g2) {
		setGraph1(g1);
		setGraph2(g2);
		solve();
		return solutionCount;
	}

	public void solve() {
		solutionCount = 0;
		if(!areEqualArrays(weights1, weights2)) return;
		init();
		anal();
	}
	
	boolean areEqualArrays(int[] ws1, int[] ws2) {
		if(ws1 == null || ws2 == null) return false;
		if(ws1.length != ws2.length) return false;
		int[] ws1a = (int[])ws1.clone();
		int[] ws2a = (int[])ws2.clone();
		Arrays.sort(ws1a);
		Arrays.sort(ws2a);
		for (int i = 0; i < ws1.length; i++) {
			if(ws1a[i] != ws2a[i]) return false;
		}
		return true;
	}
	
	void init() {
		mapping_1by2 = new int[weights2.length];
		mapping_2by1 = new int[weights1.length];
		for (int i = 0; i < weights1.length; i++) {
			mapping_1by2[i] = -1;
			mapping_2by1[i] = -1;
		}
		wayCount = new int[weights1.length + 1];
		way = new int[weights1.length + 1];
		ways = new int[weights1.length + 1][weights1.length];
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == graph1.length) return;
		int n2 = t;
		for (int n1 = 0; n1 < graph1.length; n1++) {
			if(mapping_2by1[n1] >= 0) continue;
			if(canMap(n1, n2)) {
				ways[t][wayCount[t]] = n1;
				wayCount[t]++;
			}
		}
	}
	
	boolean canMap(int n1, int n2) {
		for (int k1 = 0; k1 < graph1.length; k1++) {
			if(k1 == n1) continue;
			int k2 = mapping_2by1[k1];
			if(k2 < 0) continue;
			if(graph1[k1][n1] != graph2[k2][n2]) return false;
		}
		return true;
	}
	
	void move() {
		int n2 = t;
		int n1 = ways[t][way[t]];
		mapping_1by2[n2] = n1;
		mapping_2by1[n1] = n2;		
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int n2 = t;
		int n1 = ways[t][way[t]];
		mapping_1by2[n2] = -1;
		mapping_2by1[n1] = -1;		
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
			if(t == graph1.length) {
				++solutionCount;
			}
		}
	}
	
	//utilities
	
	public int[][] copyGraph(int[][] graph) {
		int[][] g = new int[graph.length][graph.length];
		for (int i = 0; i < g.length; i++) {
			for (int j = 0; j < g.length; j++) {
				g[i][j] = graph[i][j];
			}
		}		
		return g;
	}
	
	public int[] getNodeWeights(int[][] graph) {
		int[][] matrix = square(graph);
		matrix = square(matrix);
		int[] w = new int[matrix.length];
		for (int i = 0; i < w.length; i++) w[i] = matrix[i][i];
		return w;
	}
	
	public int[][] square(int[][] matrix) {
		int[][] m = new int[matrix.length][matrix.length];
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m.length; j++) {
				m[i][j] = 0;
				for (int k = 0; k < m.length; k++) {
					m[i][j] += matrix[i][k] * matrix[k][j];
				}
			}
		}
		return m;
	}
	
	public int[][] generateRardomGraph(int n, int m) {
		if(2 * m > (n * (n - 1))) return null;
		int[][] graph = new int[n][n];
		while(m > 0) {
			int i = (int)(Math.random() * n);
			int j = (int)(Math.random() * n);
			if(i == j || graph[i][j] == 1) continue;
			graph[i][j] = 1;
			graph[j][i] = 1;
			--m;
		}
		return graph;
	}
	
	public void printGraph(int[][] graph) {
		for (int i = 0; i < graph.length; i++) {
			for (int j = 0; j < graph.length; j++) {
				System.out.print(" " + graph[i][j]);
			}
			System.out.println("");
		}
		System.out.println("");
	}
	
	public String graphToString(int[][] graph) {
		StringBuffer sb = new StringBuffer();
		sb.append(graph.length).append(":");
		for (int i = 0; i < graph.length; i++) {
			for (int j = i + 1; j < graph.length; j++) {
				if(graph[i][j] == 1) {
					sb.append("" + (i + 1)).append('-').append("" + (j + 1)).append(',');
				}
			}
		}
		return sb.toString();
	}
	
	
	static void test1(int n, int m) {
		GraphComparator gc = new GraphComparator();
		int[][] graph = gc.generateRardomGraph(n, m);
		int[] ws = gc.getNodeWeights(graph);
		gc.printGraph(graph);
		System.out.println("");
		for (int i = 0; i < graph.length; i++) { 
			System.out.print(" " + ws[i]);
		}
		System.out.println("");
	}
	
	static void test2(int n, int m) {
		GraphComparator gc = new GraphComparator();
		int[][] graph1 = gc.generateRardomGraph(n, m);
		int[][] graph2 = gc.copyGraph(graph1);
		gc.setGraph1(graph1);
		gc.setGraph2(graph2);
		gc.solve();
		gc.printGraph(graph1);
		System.out.println("Isomorhism number=" + gc.solutionCount);
	}
	
	public static void main(String[] args) {
		test2(10, 20);
	}

}
