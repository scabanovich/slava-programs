package pqrst10;

import java.util.*;

public class CoverBySquaresAnalysis {
	int width;
	int height;
	int size;
	int[] x;
	int[] y;
	int[][] xy;

	int[] mask;
	
	int[][] regions;
	int[][] graph;
	int[] nodeValue;
	
	//analyses
	int[] nodeOrder;
	int[] nodeRestriction;
	int[] nodeUsage;
	int value;
	int t;
	int[] node;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int bestValue;
	int[] bestNodeUsage;
	int[] bestCovering;
	int coveringSize;
	
	int maxPrintableCoveringSize = 44;
	int minCoveringSize = 41;
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		size = width * height;
		x = new int[size];
		y = new int[size];
		xy = new int[width][height];
		for (int i = 0; i < size; i++) {
			x[i] = (i % width);
			y[i] = (i / width);
			xy[x[i]][y[i]] = i;
		}
	}
	
	public void setMask(int[] mask) {
		this.mask = mask;
		createRegions();
		buildGraph();
	}
	
	void createRegions() {
		List l = new ArrayList();
		for (int i = 0; i < size; i++) {
			if(mask[i] == 0) continue;
			for (int d = 2; ; d++) {
				if(!checkNextRegion(i, d)) break;
				if(d > 2) {
					l.add(new int[]{i, d});
				}
			}			
		}
		regions = (int[][])l.toArray(new int[0][]);
	}
	private boolean checkNextRegion(int i, int d) {
		int xd = x[i] + d - 1;
		int yd = y[i] + d - 1;
		if(xd >= width || yd >= height) return false;
		for (int k = x[i]; k <= xd; k++) {
			int j = xy[k][yd];
			if(mask[j] == 0) return false;
		}
		for (int k = y[i]; k <= yd; k++) {
			int j = xy[xd][k];
			if(mask[j] == 0) return false;
		}
		return true;
	}
	
	private void buildGraph() {
		graph = new int[regions.length][regions.length];
		nodeValue = new int[regions.length];
		for (int i = 0; i < graph.length; i++) {
			nodeValue[i] = regions[i][1] * regions[i][1] - 1;
			for (int j = 0; j < graph.length; j++) {
				if(i != j && overlap(regions[i], regions[j])) graph[i][j] = 1;
			}
		}
	}
	private boolean overlap(int[] regionA, int[] regionB) {
		int xa1 = x[regionA[0]];
		int xa2 = xa1 + regionA[1] - 1;
		int xb1 = x[regionB[0]];
		int xb2 = xb1 + regionB[1] - 1;
		if(xa2 < xb1 || xa1 > xb2) return false;
		int ya1 = y[regionA[0]];
		int ya2 = ya1 + regionA[1] - 1;
		int yb1 = y[regionB[0]];
		int yb2 = yb1 + regionB[1] - 1;
		if(ya2 < yb1 || ya1 > yb2) return false;
		return true;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		initNodeOrder();
		node = new int[regions.length];
		nodeRestriction = new int[regions.length];
		nodeUsage = new int[regions.length];
		bestValue = 0;
		bestNodeUsage = null;
		bestCovering = null;
		coveringSize = 1000;
		wayCount = new int[regions.length + 1];
		way = new int[regions.length + 1];
		ways = new int[regions.length + 1][2];
		t = 0;
	}
	private void initNodeOrder() {
		nodeOrder = new int[regions.length];
		for (int i = 0; i < graph.length; i++) {
			for (int j = 0; j < graph.length; j++) {
				if(graph[i][j] == 1) nodeOrder[i]++;
			}
		}
	}
	
	void srch() {
		wayCount[t] = 0;
		int n = getUsableNode();
		if(n < 0) return;
		node[t] = n;
		if(nodeOrder[n] == 0) {
			ways[t][0] = 1;
			wayCount[t] = 1;
		} else {
			ways[t][0] = 1;
			ways[t][1] = 0;
			wayCount[t] = 2;
		}		
	}
	private int getUsableNode() {
		int n = -1;
		int v = 0;
		int order = 0;
		for (int i = 0; i < graph.length; i++) {
			if(nodeRestriction[i] > 0) continue;
			if(nodeOrder[i] == 0) return i;
			if(nodeValue[i] < v) continue;
			if(nodeValue[i] == v) {
				if(nodeOrder[i] > order) {
					n = i;
					order = nodeOrder[i];
				}
			} else {
				n = i;
				order = nodeOrder[i];
				v = nodeValue[i];
			}
		}
		return n;
	}
	
	void move() {
		int n = node[t];
		int w = ways[t][way[t]];
		if(w == 0) {
			rejectNode(n);
		} else {
			acceptNode(n);
		}		
		++t;
		srch();
		way[t] = -1;
	}
	
	void rejectNode(int n) {
		nodeRestriction[n]++;
		if(nodeRestriction[n] > 1) return;
		for (int i = 0; i < graph.length; i++) {
			if(graph[n][i] == 1) nodeOrder[i]--;
		}
	}
	
	void acceptNode(int n) {
		nodeUsage[n] = 1;
		value += nodeValue[n];
		nodeRestriction[n]++;
		for (int i = 0; i < graph.length; i++) {
			if(graph[n][i] == 1) rejectNode(i);
		}
	}
	
	void back() {
		--t;
		int n = node[t];
		int w = ways[t][way[t]];
		if(w == 0) {
			unrejectNode(n);
		} else {
			unacceptNode(n);
		}		
	}

	void unrejectNode(int n) {
		nodeRestriction[n]--;
		if(nodeRestriction[n] > 0) return;
		for (int i = 0; i < graph.length; i++) {
			if(graph[n][i] == 1) nodeOrder[i]++;
		}
	}
	
	void unacceptNode(int n) {
		nodeUsage[n] = 0;
		value -= nodeValue[n];
		nodeRestriction[n]--;
		for (int i = 0; i < graph.length; i++) {
			if(graph[n][i] == 1) unrejectNode(i);
		}
	}
	
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			++way[t];
			move();
			if(wayCount[t] == 0) {
				int cs = createCovering();
				if(cs < coveringSize) {
					coveringSize = cs;
					bestCovering = (int[])covering.clone();
					if(coveringSize <= maxPrintableCoveringSize) {
						printBestCovering();
					}
				}
				if(coveringSize <= minCoveringSize) return;
			}
		}
	}
	
	int[] covering;
	
	int createCovering() {
		int[] ns = new int[size];
		int q = 0;
		for (int i = 0; i < nodeUsage.length; i++) {
			if(nodeUsage[i] == 0) continue;
			++q;
			int p = regions[i][0];
			int d = regions[i][1];
			int xb = x[p];
			int yb = y[p];
			for (int ix = xb; ix < xb + d; ix++) {
				for (int iy = yb; iy < yb + d; iy++) {
					ns[xy[ix][iy]] = q;
				}
			}
		}
		int s = 0;
		for (int i = 0; i < size; i++) {
			if(mask[i] == 0 || ns[i] > 0) continue;
			if(x[i] == width - 1 || y[i] == height - 1) {
				++s; 
				continue; 
			}
			int i1 = i + 1, i2 = i + width, i3 = i + width + 1;
			if(ns[i1] > 0 || mask[i1] == 0 || ns[i2] > 0 || mask[i2] == 0 || ns[i3] > 0 || mask[i3] == 0) {
				++s;
				continue;
			}
			++q;
			ns[i] = q; ns[i1] = q; ns[i2] = q; ns[i3] = q;
			
		}
		covering = ns;
		return q + s;
	}
	
	public void printBestCovering() {
		if(bestCovering == null) return;
		System.out.println(coveringSize);
		for (int i = 0; i < size; i++) {
			char ch = bestCovering[i] == 0 ? '.' : (char)(96 + bestCovering[i]);
			if(mask[i] == 0) ch = '*';
			System.out.print(" " + ch);
			if(x[i] == width - 1) System.out.println("");
		}
	}
	
	static int[] MASK = {
		1,1,1,1,1,1,0,1,1,1,1,1,1,1,
		1,1,1,1,1,1,0,1,1,1,1,1,0,1,
		1,1,1,1,1,1,0,1,1,1,1,1,1,1,
		1,1,1,1,1,1,0,1,1,1,1,1,1,1,
		1,1,1,1,1,1,0,1,1,1,1,1,1,1,
		0,1,1,1,1,1,0,1,1,1,0,1,1,1,
		0,1,1,0,1,1,0,1,1,1,0,1,0,1,
		0,1,1,1,1,1,0,1,1,1,0,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,
	};
	
	public static void search() {
		int width = 14;
		int height = 13;
		for (int i = 0; i < MASK.length; i++) {
			if(MASK[i] == 0) continue;
			if(!isPerspective(MASK, i, width, height)) continue;
			MASK[i] = 0;
			System.out.println("--------->" + i);
			for (int j = i + 1; j < MASK.length; j++) {
				if(MASK[j] == 0) continue;
				if(!isPerspective(MASK, j, width, height)) continue;
				System.out.println("--------->" + j);
				MASK[j] = 0;
				CoverBySquaresAnalysis g = new CoverBySquaresAnalysis();
				g.setSize(width, height);
				g.setMask(MASK);
				g.solve();
				if(g.coveringSize > g.minCoveringSize) return;
				MASK[j] = 1;
			}
			MASK[i] = 1;
		}
	}
	private static boolean isPerspective(int[] mask, int i, int width, int height) {
		if(mask[i] == 0) return false;
		int x = (i % width);
		int y = (i / width);
		if(x == 0 || x == width - 1) return false;
		if(y == 0 || y == height - 1) return false;
		if(mask[i + 1] == 0) return false;
		if(mask[i + width] == 0) return false;
		if(mask[i - 1] == 0) return false;
		if(mask[i - width] == 0) return false;
		return true;
	}
	
	public static void solve_() {
		CoverBySquaresAnalysis g = new CoverBySquaresAnalysis();
		g.setSize(14, 13);
		g.setMask(MASK);
		g.solve();
	}
	
	public static void main(String[] args) {
		search();
		///solve_();
	}

}

/*
43
 a a a a g g * . . h h i i .
 a a a a g g * . * h h i i .
 a a a a j j * b b b . * . .
 a a a a j j * b b b . c c c
 . k k l l . * b b b . c c c
 * k k l l . * . . . * c c c
 * m m * n n * . * . * d d d
 * m m . n n * . . . * d d d
 e e e e e f f f f f . d d d
 e e e e e f f f f f o o p p
 e e e e e f f f f f o o p p
 e e e e e f f f f f q q * .
 e e e e e f f f f f q q . .

41
 a a a a h h * . . b b b . .
 a a a a h h * . * b b b * .
 a a a a i i * . . b b b . .
 a a a a i i * c c c . d d d
 . j j k k . * c c c . d d d
 * j j k k . * c c c * d d d
 * l l * m m * . * . * e e e
 * l l . m m * . . . * e e e
 f f f f f g g g g g . e e e
 f f f f f g g g g g n n o o
 f f f f f g g g g g n n o o
 f f f f f g g g g g p p * .
 f f f f f g g g g g p p . .

40
 a a a b b b * c c c c . j j
 a a a b b b * c c c c * j j
 a a a b b b * c c c c d d d
 k k . e e e * c c c c d d d
 k k . e e e * . . . . d d d
 * l l e e e * . * . * . . .
 * l l * m m * n n . * . * .
 * o o . m m * n n . * f f f
 . o o . g g g g g p p f f f
 h h h h g g g g g p p f f f
 h h h h g g g g g i i i . .
 h h h h g g g g g i i i * .
 h h h h g g g g g i i i . .
*/