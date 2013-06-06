package slava.algebra;

public class PaintGraphAnalysis {
	protected int[][] neigbours;
	protected int colorCount;
	protected int[] initialColoring;
	
	protected int[] coloring;
	protected int sizeToColor;
	
	protected int[][] dynamicRestrictions;
	protected int t;
	int[][] ways;
	int[] way;
	int[] node;
	int[] wayCount;
	
	int solutionCount;
	int[] solution;
	int solutionLimit = 1000;
	
	public PaintGraphAnalysis() {}
	
	public void setGraph(int[][] graph) {
		neigbours = new int[graph.length][];
		for (int i = 0; i < graph.length; i++) {
			int l = 0;
			for(int j = 0; j < graph[i].length; j++) {
				if(graph[i][j] != 0) ++l;
			} 
			neigbours[i] = new int[l];
			l = 0;
			for(int j = 0; j < graph[i].length; j++) {
				if(graph[i][j] != 0) {
					neigbours[i][l] = j;
					++l;
				} 
			} 
		}
	}
	
	public void setInitialColoring(int[] initialColoring) {
		this.initialColoring = initialColoring;
	}
	
	public void setColorCount(int c) {
		colorCount = c;
		temp = new int[c];
	}
	
	public void setSolutionLimit(int i) {
		solutionLimit = i;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		dynamicRestrictions = new int[neigbours.length][colorCount];
		coloring = new int[neigbours.length];
		for (int i = 0; i < coloring.length; i++) coloring[i] = -1;
		sizeToColor = coloring.length;
		if(initialColoring != null) {
			for (int i = 0; i < coloring.length; i++) {
				if(initialColoring[i] < 0) continue;
				setColor(i, initialColoring[i]);
				--sizeToColor;
			}
		}
		ways = new int[sizeToColor + 1][colorCount];
		node = new int[sizeToColor + 1];
		wayCount = new int[sizeToColor + 1];
		way = new int[sizeToColor + 1];
		t = 0;
		solutionCount = 0;
	}
	
	void setColor(int i, int c) {
		coloring[i] = c;
		for (int j = 0; j < neigbours[i].length; j++) {
			int p = neigbours[i][j];
			dynamicRestrictions[p][c]++;
		}
	}
	
	void unsetColor(int i) {
		int c = coloring[i];
		if(c < 0) return;
		coloring[i] = -1;
		for (int j = 0; j < neigbours[i].length; j++) {
			int p = neigbours[i][j];
			dynamicRestrictions[p][c]--;
		}
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == sizeToColor) return;
		int pm = -1;
		int qm = colorCount + 1;
		for (int i = 0; i < coloring.length; i++) {
			if(coloring[i] >= 0) continue;
			int q = computeWayCount(i);
			if(q == 0) return;
			if(q >= qm) continue;
			qm = q;
			pm = i;
			for (int k = 0; k < qm; k++) ways[t][k] = temp[k];
		}
		if(pm < 0) return;
		node[t] = pm;
		wayCount[t] = qm;
		randomizeWays();
	}
	
	int[] temp;
	int computeWayCount(int p) {
		int q = 0;
		for (int c = 0; c < colorCount; c++) {
			if(dynamicRestrictions[p][c] > 0) continue;
			temp[q] = c;
			++q;
		}
		return q;
	}
	
	void randomizeWays() {
		if(wayCount[t] < 2) return;
		for (int i = wayCount[t] - 1; i > 0; i--) {
			int j = (int)((i + 1) * Math.random());
			int c = ways[t][i];
			ways[t][i] = ways[t][j];
			ways[t][j] = c;
		}
	}
	
	void move() {
		int p = node[t];
		int c = ways[t][way[t]];
		setColor(p, c);
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = node[t];
		unsetColor(p);
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
			if(t == sizeToColor) {
				++solutionCount;
				if(solutionCount == 1) solution = (int[])coloring.clone();
				if(solutionCount >= solutionLimit) return;
			}
		}
	}

	public int getSolutionCount() {
		return solutionCount;
	}
	
	public int[] getSolution() {
		return solution == null ? null : (int[])solution.clone();
	}
	
	
	public static void main(String[] args) {
		int[][] graph = new int[][]{
			{0,1,1,1},
			{1,0,1,1},
			{1,1,0,1},
			{1,1,1,0},
		};
		int[] initialColoring = new int[]{-1,-1,-1,2};
		PaintGraphAnalysis g = new PaintGraphAnalysis();
		g.setColorCount(4);
		g.setGraph(graph);
		g.setInitialColoring(initialColoring);
		g.solve();
		System.out.println(g.getSolutionCount());
	}
}
