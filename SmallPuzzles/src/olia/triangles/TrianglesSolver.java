package olia.triangles;

/**
 * 
 * @author Slava
 *
 * The task is to color vertices of triangular lattice 
 * in 3 colors in such a way that vertices of any 
 * equilateral triangle were colored at least with 
 * 2 different colors.
 * 
 */

public class TrianglesSolver {
	int colorCount = 3;
	TrianglesField field;
	/**
	 * The field defines a rectangular region with
	 * binds (1,0), (0,1), (1,1). To exclude some node
	 * set form[i] = 0, to include it set form[i] = 1.
	 */
	int[] form;
	
	/**
	 * If you want to make a puzzle with unique solution,
	 * you need to set colors at some nodes. 
	 * initialState[i] = -1; means that the nodes is left 
	 * uncolored.
	 */
	int[] initialState;
	
	int solutionLimit = 1000;

	int[] state;
	int[][] ruledOut;
	int cellsToFill;

	int[] place;
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;

	public TrianglesSolver() {}
	
	public void setField(TrianglesField f) {
		field = f;
	}
	
	public void setColorCount(int q) {
		colorCount = q;
		temp = new int[colorCount];
	}
	
	public void setForm(int[] r) {
		form = r;
	}
	
	public void setInitialState(int[] s) {
		initialState = s;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		ruledOut = new int[field.getSize()][colorCount];
		cellsToFill = 0;
		for (int i = 0; i < state.length; i++) {
			state[i] = -1;
			if(form[i] == 0) continue;
			cellsToFill++;
		}
		if(initialState != null) {
			for (int i = 0; i < state.length; i++) {
				if(form[i] == 0) continue;
				if(initialState != null && initialState[i] != -1) {
					add(i, initialState[i]);
				}
			}
		}
		place = new int[field.getSize()];
		wayCount = new int[field.getSize() + 1];
		way = new int[field.getSize() + 1];
		ways = new int[field.getSize() + 1][colorCount];
		t = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(cellsToFill == 0) return;
		int wcm = colorCount + 1;
		place[t] = -1;
		for (int p = 0; p < state.length; p++) {
			if(state[p] >= 0 || form[p] == 0) continue;
			int wc = getWays(p);
			if(wc < wcm) {
				wcm = wc;
				place[t] = p;
				if(wc == 0) return;
				for (int i = 0; i < wc; i++) ways[t][i] = temp[i];
			}
		}
		if(wcm <= colorCount) wayCount[t] = wcm;
	}
	
	int[] temp = new int[colorCount];
	
	int getWays(int p) {
		int wc = 0;
		for (int v = 0; v < colorCount; v++) {
			if(ruledOut[p][v] == 0) {
				temp[wc] = v;
				wc++;
			}
		}
		return wc;
	}
	
	void move() {
		int p = place[t];
		int v = ways[t][way[t]];
		add(p, v);
		++t;
		srch();
		way[t] = -1;
	}
	
	void add(int p, int v) {
		--cellsToFill;
		state[p] = v;
		for (int q = 0; q < state.length; q++) {
			if(state[q] != v || form[q] == 0 || q == p) continue;
			int r = field.getThirdVertexA(p, q);
			if(r >= 0 && form[r] == 1) {
				ruledOut[r][v]++;
			}
			r = field.getThirdVertexB(p, q);
			if(r >= 0 && form[r] == 1) {
				ruledOut[r][v]++;
			}			
		}
	}
	
	void back() {
		--t;
		int p = place[t];
		int v = ways[t][way[t]];
		remove(p, v);
	}
	
	void remove(int p, int v) {
		++cellsToFill;
		state[p] = -1;
		for (int q = 0; q < state.length; q++) {
			if(state[q] != v || form[q] == 0 || q == p) continue;
			int r = field.getThirdVertexA(p, q);
			if(r >= 0 && form[r] == 1) {
				ruledOut[r][v]--;
			}
			r = field.getThirdVertexB(p, q);
			if(r >= 0 && form[r] == 1) {
				ruledOut[r][v]--;
			}			
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
			way[t]++;
			move();
			if(cellsToFill == 0) {
				solutionCount++;
				if(solutionCount == 1) printSolution();
				if(solutionLimit >= 0 && solutionLimit == solutionCount) {
					return;
				}
			}
		}
		
	}
	
	void printSolution() {
		for (int i = 0; i < state.length; i++) {
			String c = (form[i] == 0) ? "." : "" + state[i];
			System.out.print(" " + c);
			if(field.x(i) == field.getWidth() - 1) System.out.println("");
		}
		System.out.println("");
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}
	
}
