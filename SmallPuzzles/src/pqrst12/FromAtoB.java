package pqrst12;

public class FromAtoB {
	int[][] graph;
	
	int start = 0;
	int end;
	
	int[] usedNodes;
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;
	
	public void setGraph(int[][] graph, int end) {
		this.graph = graph;
		this.end = end;	
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		usedNodes = new int[graph.length];
		wayCount = new int[graph.length + 1];
		way = new int[graph.length + 1];
		ways = new int[graph.length + 1][6];
		place = new int[graph.length + 1];
		t = 0;
		place[0] = start;
		solutionCount = 0;
		usedNodes[start]++;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(place[t] == end) return;
		for (int i = 0; i < graph[place[t]].length; i++) {
			int p = graph[place[t]][i];
			if(usedNodes[p] == 1) continue;
			ways[t][wayCount[t]] = p;
			++wayCount[t];
		}
	}
	
	void move() {
		int p = ways[t][way[t]];
		usedNodes[p]++;
		++t;
		place[t] = p;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = ways[t][way[t]];
		usedNodes[p]--;
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
			if(place[t] == end) {
				++solutionCount;
				printSolution();
			}
		}
	}
	
	private void printSolution() {
		for (int i = 0; i <= t; i++) {
			System.out.print(" " + place[i]);
		}
		System.out.println("");
	}

	public static void main(String[] args) {
		FromAtoB p = new FromAtoB();
		p.setGraph(GRAPH_1, 17);
//		p.setGraph(GRAPH_0, 11);
		p.solve();
		System.out.println("solutionCount=" + p.solutionCount);
	}
	
	static int[][] GRAPH_0 = new int[][]{
		{1,2,3},       //0
		{0,4},         //1
		{0,5},         //2
		{0,4,5},       //3
		{1,3,6,7},     //4
		{2,3,6,8,9},   //5
		{4,5,10},      //6
		{4,10},        //7
		{5,11},        //8
		{5,10,11},     //9
		{6,7,9,12},    //10
		{8,9,12},      //11
		{10,11}        //12
	};
	
	static int[][] GRAPH_1 = new int[][]{
		{1,2,3},       //0
		{0,4},         //1
		{0,5},         //2
		{0,4,5},       //3
		{1,3,6,7},     //4
		{2,3,6,8},     //5
		{4,5,9},       //6
		{4,9,10},      //7
		{5,9,11},      //8
		{6,7,8,12,13}, //9
		{7,13},        //10
		{8,12,14,15},  //11
		{9,11,16},     //12
		{9,10,16},     //13
		{11,17},       //14
		{11,16,17},    //15
		{12,13,15,18}, //16
		{14,15,18},    //17
		{16,17}        //18
	};

}
