package com.slava.graph;

import java.util.StringTokenizer;

public class Pegs implements IPegs {
	GraphComparator gc = new GraphComparator();
	int[][] graph;
	
	public void setGraph(int[][] g) {
		graph = g;
	}
	
	public boolean run() {
		boolean ok = true;
		int ic = gc.getIsomorhismCount(graph, gc.copyGraph(graph));
		if(ic > 1) {
			ok = false;
			System.out.println("Automorpism number=" + ic);
		}
		if(ok) for (int n1 = 0; n1 < graph.length; n1++) {
			for (int n2 = n1 + 1; n2 < graph.length; n2++) {
				if(graph[n1][n2] != 1) continue;
				for (int k1 = 0; k1 < graph.length; k1++) {
					for (int k2 = k1 + 1; k2 < graph.length; k2++) {
						if(graph[k1][k2] != 0) continue;
						int[][] g2 = gc.copyGraph(graph);
						g2[n1][n2] = 0;
						g2[n2][n1] = 0;
						g2[k1][k2] = 1;
						g2[k2][k1] = 1;
						ic = gc.getIsomorhismCount(graph, g2);
						if(ic != 0) {
							ok = false;
							String ms = "When " + n1 + "-" + n2 + " removed, it can be put to " + k1 + "-" + k2 + ".";
							System.out.println(ms);
						}
					}					
				}
			}
		}
		if(ok) {
			System.out.println("Graph is ok");
		} else {
			System.out.println("Graph is not ok");
		}
		return ok;
	}
	
	public boolean check(String answer) throws Exception {
		int[][] graph = parse(answer);
		setGraph(graph);
		return run();
	}
	
	public int[][] parse(String answer) throws Exception {
		answer = answer.trim();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < answer.length(); i++) {
			char c = answer.charAt(i);
			if(!Character.isWhitespace(c)) sb.append(c);
		}
		answer = sb.toString();
		int k = answer.indexOf(':');
		if(k < 0) throw new Exception("Cannot find symbol :");
		int n = Integer.parseInt(answer.substring(0, k));
		int[][] g = new int[n][n];
		StringTokenizer st = new StringTokenizer(answer.substring(k + 1), ",.");
		while(st.hasMoreTokens()) {
			String t = st.nextToken();
			k = t.indexOf('-');
			if(k < 0) throw new Exception("Cannot find separator '-' in '" + t + "'");
			int v1 = Integer.parseInt(t.substring(0, k)) - 1;
			int v2 = Integer.parseInt(t.substring(k + 1)) - 1;
			if(v1 == v2) throw new Exception("Bind from node to itself is not allowed.");
			if(v1 < 0 || v1 >= n || v2 < 0 || v2 >= n) {
				throw new Exception("Node index is out of range.");
			}
			g[v1][v2] = 1;
			g[v2][v1] = 1;
		}
		return g;
	}
	
	static void generate() {
		while(true) {
			Pegs p = new Pegs();
			p.setGraph(p.gc.generateRardomGraph(8, 11));
			p.gc.printGraph(p.graph);
			if(p.run()) {
				System.out.println("Key " + p.gc.graphToString(p.graph));
				break;
			}
		}
	}
	
	static void test() {
		IPegs p = new Pegs();
		try {
			p.check("8 : 1-3,1-4,1-7,2-4,2-5,2-6,2-7,2-8,3-6,3-7,4-5,4-6,4-7,5-8,7-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		test();
	}

}

/*
8:1-3,1-4,1-7,2-4,2-5,2-6,2-7,2-8,3-6,3-7,4-5,4-6,4-7,5-8,7-8
 0 0 1 1 0 1 1 1
 0 0 1 1 0 0 1 0
 1 1 0 1 1 1 0 0
 1 1 1 0 1 0 0 1
 0 0 1 1 0 0 0 1
 1 0 1 0 0 0 1 0
 1 1 0 0 0 1 0 0
 1 0 0 1 1 0 0 0

Graph is ok
*/