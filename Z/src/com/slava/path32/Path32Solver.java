package com.slava.path32;

import com.slava.common.RectangularField;

public class Path32Solver {
	RectangularField field;
	int length;
	int[] data;
	boolean checkAccessibility = true;
	
	int[] state;
	int[] usedNumbers;
	
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;

	//auxiliary
	int[] stack;
	int[] visited;
	int[] accessibleNumbers;

	int solutionCount;
	int[] solution;
	
	int treeCount;
	
	public Path32Solver() {}
	
	public void setField(RectangularField f) {
		field = f;
		length = f.getSize() / 2;
	}
	
	public void setData(int[] data) {
		this.data = data;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		int size = field.getSize();
		state = new int[size];
		usedNumbers = new int[length + 1];
		
		place = new int[length + 1];
		wayCount = new int[length + 1];
		way = new int[length + 1];
		ways = new int[length + 1][4];

		stack = new int[field.getSize()];
		visited = new int[field.getSize()];
		accessibleNumbers = new int[length + 1];

		t = 0;
		solutionCount = 0;
		treeCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == length) return;
		if(t == 0) {
			for (int p = 0; p < data.length; p++) {
				if(data[p] == 1) {
					ways[t][wayCount[t]] = p;
					wayCount[t]++;
				}
			}
		} else {
			if(checkAccessibility && !checkAccessible()) return;
			int p = place[t - 1];
			for (int d = 0; d < 4; d++) {
				int q = field.jump(p, d);
				if(q < 0 || state[q] > 0 || usedNumbers[data[q]] > 0) continue;
				ways[t][wayCount[t]] = q;
				wayCount[t]++;
			}
		}
	}
	
	boolean checkAccessible() {
		for (int p = 0; p < visited.length; p++) visited[p] = 0;
		for (int i = 0; i < accessibleNumbers.length; i++) accessibleNumbers[i] = 0;
		int p = place[t - 1];
		int vc = 0;
		int vs = 1;
		stack[0] = p;
		visited[p] = 1;
		visited[place[0]] = 1;
		boolean connected = false;
		while(vc < vs) {
			p = stack[vc];
			for (int d = 0; d < 4; d++) {
				int q = field.jump(p, d);
				if(q == place[0]) connected = true;
				if(q < 0 || state[q] > 0 || usedNumbers[data[q]] > 0 || visited[q] > 0) continue;
				accessibleNumbers[data[q]]++;
				visited[q] = 1;
				stack[vs] = q;
				vs++;
			}
			vc++;
		}		                      
		if(!connected) return false;
		for (int z = 0; z < 10; z++) {
			boolean change = false;
			for (int q = 0; q < visited.length; q++) {
				if(visited[q] == 0 || state[q] > 0) continue;
				int h = 0;
				for (int d = 0; d < 4; d++) {
					int u = field.jump(q, d);
					if(u < 0 || visited[u] == 0) continue;
					h++;
				}
				if(h < 2) {
					change = true;
					accessibleNumbers[data[q]]--;
					if(accessibleNumbers[data[q]] == 0) return false; 
					visited[q] = 0;
				}
			}
			if(!change) break;
		}
		for (int i = 1; i < usedNumbers.length; i++) {
			if(usedNumbers[i] == 0 && accessibleNumbers[i] == 0) return false;
		}
		return true;
	}
	
	void move() {
		int p = ways[t][way[t]];
		place[t] = p;
		usedNumbers[data[p]]++;
		state[p] = t + 1;		
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = ways[t][way[t]];
		usedNumbers[data[p]]--;
		state[p] = 0;
	}
	
	void anal() {
		srch();
		way[t] = -1;
		int tm = 0;
		while(true) {
			while(way[t] >= wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t > tm) {
				tm = t;
//				System.out.println(tm);
			}
			if(wayCount[t] == 0) treeCount++;
			if(t == length && isSolution()) {
				solutionCount++;
//				if(solutionCount == 1) {
//					printSolution(field, data, state);
//				}
				solution = (int[])state.clone();
			}
		}
	}
	
	boolean isSolution() {
		int p1 = place[0];
		int p2 = place[length - 1];
		for (int d = 0; d < 4; d++) {
			if(field.jump(p1, d) == p2) return true;
		}
		return false;
	}
	
	static void printSolution(RectangularField field, int[] data, int[] state) {
		System.out.println("Data");
		for (int p = 0; p < state.length; p++) {
			String v = " " + data[p];
			if(v.length() < 3) v = " " + v;
			System.out.print(" " +  v);
			if(field.isRightBorder(p)) System.out.println("");
		}

		System.out.println("Path");
		for (int p = 0; p < state.length; p++) {
			int v = state[p];
			char c = (v <= 0) ? '+' : (char)(97 + ((v - 1) % 26));
			System.out.print(" " +  c);
			if(field.isRightBorder(p)) System.out.println("");
		}
	}
	
	static int[] generateData(int size) {
		int[] result = new int[size];
		for (int i = 1; i < size / 2 + 1; i++) {
			for (int j = 0; j < 2; j++) {
				int p = (int)(size * Math.random());
				while(result[p] != 0) {
					p = (int)(size * Math.random());
				}
				result[p] = i;
			}
		}
		return result;
	}
	
	static void generate(int width, int height) {
		int attemptCount = 0;
		Path32Solver solver = null;
		while(true) {
			++ attemptCount;
			solver = new Path32Solver();
			RectangularField f = new RectangularField();
			f.setSize(width, height);
			solver.setField(f);
			int[] data = generateData(f.getSize());
			solver.setData(data);
			solver.solve();
			System.out.println("Solution count=" + solver.solutionCount + " Tree count=" + solver.treeCount);
			if(solver.solutionCount == 2) {
				Path32Solver.printSolution(f, data, solver.solution);
				break;
			}
		}
		System.out.println("Attempts=" + attemptCount);
	}
	
	public static void testSolver() {
		Path32Solver solver = new Path32Solver();
		RectangularField f = new RectangularField();
		f.setSize(6, 6);
		solver.setField(f);
		int[] data = generateData(f.getSize());
		solver.setData(data);
		solver.solve();
		System.out.println("Solution count=" + solver.solutionCount);
		if(solver.solutionCount > 0) {
			Path32Solver.printSolution(f, data, solver.solution);
		}
	}
	
	public static void main(String[] args) {
		generate(6, 6);
	}

}
