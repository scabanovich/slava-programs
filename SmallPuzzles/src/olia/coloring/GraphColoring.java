package olia.coloring;

public class GraphColoring {
	int colorCount = 3;
	int[][] graph;
	int size;
	
	int[] values;

	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;

	int solutionCount;
	int solutionCountLimit = 30000;
	
	public GraphColoring() {}
	
	public void setGraph(int[][] graph) {
		this.graph = graph;
		size = graph.length;
	}

	public void solve() {
		init();
		anal();
	}
	
	void init() {
		values = new int[size];
		for (int i = 0; i < size; i++) {
			values[i] = -1;
		} 
		place = new int[size];
		wayCount = new int[size + 1];
		way = new int[size + 1];
		ways= new int[size + 1][colorCount];
		t = 0;
		solutionCount = 0;
		
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t >= size) return;
		place[t] = -1;
		int wc = colorCount + 1;
		for (int p = 0; p < size; p++) {
			if(values[p] >= 0) continue;
			int w = getWayCount(p);
			if(w < wc) {
				place[t] = p;
				wc = w;
				for (int i = 0; i < w; i++) ways[t][i] = temp[i];
			}
			if(w == 0) return;
		}
		if(wc <= colorCount) {
			wayCount[t] = wc;
		}
		
	}
	
	int[] temp = new int[colorCount];
	
	int getWayCount(int p) {
		int w = 0;
		for (int q = 0; q < colorCount; q++) {
			if(canPut(p, q)) {
				temp[w] = q;
				++w;
			}
		}
		return w;
	}
	
	boolean canPut(int p, int q) {
		for (int n = 0; n < size; n++) {
			if(n != p && graph[p][n] == 1 && values[n] == q) return false;
		}
		return true;
	}
	
	void move() {
		int q = ways[t][way[t]];
		int p = place[t];
		values[p] = q;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = place[t];
		values[p] = -1;
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return; else back();
			}
			way[t]++;
			move();
			if(t == size) {
				++solutionCount;
				if(solutionCountLimit > 0 && solutionCount >= solutionCountLimit) return;
			} 
		}		
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}

}
