package forsmarts.csudoku;

import com.slava.common.TwoDimField;
import com.slava.sudoku.ISudokuField;
import com.slava.sudoku.SudokuSolver;
import com.slava.sudoku.SudokuState;

public class SequenceSudokuSolver {
	ISudokuField field;
	SudokuState state = new SudokuState();
	
	SudokuSolver subSolver = new SudokuSolver();
	
	TwoDimField jField;

	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;  
	int[][] waysD; //direction, used only for records
	
	int maximum = 10;
	
	int treeCount = 0;
	    int[] ft = {
	    	1,1,1,1,1,1,0,0,0,	
	    	1,1,1,1,1,1,0,0,0,	
	    	1,1,1,1,1,1,0,0,0,	
	    	1,1,1,0,0,0,0,0,0,	
	    	1,1,1,0,0,0,0,0,0,	
	    	1,1,1,0,0,0,0,0,0,	
	    	2,2,2,2,2,2,2,2,2,	
	    	2,2,2,2,2,2,2,2,2,	
	    	2,2,2,2,2,2,2,2,2,	
	    };

	public SequenceSudokuSolver() {		
	}
	
	public void setField(ISudokuField f) {
		field = f;		
		state.setField(f);
		subSolver.setField(f);
		subSolver.setSolutionLimit(10);
	}
	
	public void setJField(TwoDimField f) {
		jField = f;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state.init();
		place = new int[field.getSize() + 1];
		wayCount = new int[field.getSize() + 1];
		way = new int[field.getSize() + 1];
		ways = new int[field.getSize() + 1][8];
		waysD = new int[field.getSize() + 1][8];
		
		visited = new int[field.getSize()];
		stack = new int[field.getSize()];
		
		t = 0;
		place[0] = 0;
		treeCount = 0;
	}

	void srch() {
		wayCount[t] = 0;
		if(t == field.getSize()) return;
		if(t < 55 && !isConnectable()) return;
		if(t > 10 && !isSolvable()) return;
		int c = t % field.getColorCount();
		if(t == 0) {
			for (int p = 0; p < 5; p++) {
				if(state.canAdd(p, c)) {
					ways[t][wayCount[t]] = p;
					waysD[t][wayCount[t]] = -1;
					wayCount[t]++;
				}
			}
			return;
		}
		int p = place[t - 1];
//		int d0 = (t < 9) ? 0 : t < 16 ? 2 : 0;
//		int d1 = (t < 9) ? 1 : t < 16 ? 3 : 8;
		for (int d = 0; d < 8; d++) {
			int q = jField.jump(p, d);
			if(q < 0 || state.getValue(q) >= 0) continue;
			if(state.canAdd(q, c)) {
					if(ft[q] != 1 && t < 27) continue;
					if(ft[q] == 0 && t < 50) continue;
				ways[t][wayCount[t]] = q;
				waysD[t][wayCount[t]] = d;
				wayCount[t]++;
			}
		}
		if(t < 35) randomize();
	}
	
	void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = wayCount[t] - 1; i >= 1; i--) {
			int j = (int)((i + 1) * Math.random());
			int s = ways[t][j];
			ways[t][j] = ways[t][i];
			ways[t][i] = s;
			s = waysD[t][j];
			waysD[t][j] = waysD[t][i];
			waysD[t][i] = s;
		}
		
	}


	void move() {
		int q = ways[t][way[t]];
		place[t] = q;
		int c = t % field.getColorCount();
		state.add(q, c);		
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int q = place[t];
		int c = t % field.getColorCount();
		state.remove(q, c);		
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
			if(wayCount[t] == 0) {
				treeCount++;
				if(treeCount % 1000000 == 0) {
					System.out.println(treeCount);
				}
				if(treeCount % 10000 == 0) {
					System.out.print("x");
					try {Thread.sleep(50); } catch (Exception e) {}
					///return;
				}
			}
			if(t > maximum && isSolvable()) {
				maximum = t;
				onSolutionFound();
			}
		}		
	}
	
	boolean isSolvable() {
		if(t < 10) return true;
		int[] initial = new int[field.getSize()];
		for (int p = 0; p < initial.length; p++) initial[p] = state.getValue(p);
		subSolver.setInitialValues(initial);
		subSolver.solve();
		return subSolver.getSolutionCount() > 0;
	}
	
	void onSolutionFound() {
		System.out.println("Maximum=" + maximum);
		if(maximum > 50) printSolution();
	}
	
	void printSolution() {
		int[] data = new int[jField.getSize()];
		for (int i = 0; i < t; i++) {
			data[place[i]] = (i + 1);
		}
		for (int p = 0; p < jField.getSize(); p++) {
			int c = data[p];
			char ch = (c == 0) ? '-' : (char)(96 + ((c - 1) % 26) + 1);
			System.out.print(" " + ch);
			if(jField.isRightBorder(p)) System.out.println("");
		}
		System.out.println("");		
		System.out.println("Key=" + getKey());
	}
	
	static String[] DIRS = {
		"R", "DR", "D", "DL", "L", "UL", "U", "UR"
	};

	String getKey() {
		StringBuffer sb = new StringBuffer();
		char x0 = (char)(64 + jField.getX(place[0]) + 1);
		int y0 = jField.getHeight() - jField.getY(place[0]);
		sb.append(x0).append(y0);
		for (int i = 1; i < t; i++) {
			int d = waysD[i][way[i]];
			sb.append('-').append(DIRS[d]);
		}
		return sb.toString();
	}


	int[] visited;
	int[] stack;

	boolean isConnectable() {
		int p0 = find();
		if(p0 < 0) return true;
		stack[0] = p0;
		visited[p0]++;
		int volume = 1;
		int c = 0;
		while(c < volume) {
			int p = stack[c];
			for (int d = 0; d < 8; d++) {
				int q = jField.jump(p, d);
				if(q < 0 || visited[q] > 0) continue;
				if(state.getValue(p) >= 0) continue;
				visited[q]++;
				stack[volume] = q;
				volume++;
			}
			++c;
		}
		int pu = findUnvisited();
		for (int i = 0; i < volume; i++) visited[stack[i]] = 0;
		return pu < 0;
	}
	
	int find() {
		for (int p = 0; p < jField.getSize(); p++) {
			if(state.getValue(p) < 0) return p;
		}
		return -1;
	}
	int findUnvisited() {
		for (int p = 0; p < jField.getSize(); p++) {
			if(state.getValue(p) < 0 && visited[p] == 0) return p;
		}
		return -1;
	}

}

