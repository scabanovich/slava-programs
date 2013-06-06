package smallpuzzles.jumpsbyarrows;

import com.slava.common.RectangularField;

public class JumpsByArrowsSolver {
	RectangularField field;
	
	int[] arrows;
	int[] timesheet;
	
	int solutionLimit;
	
	int startpoint;
	int endpoind;
	
	int[] state;
	
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;
	int[] solution;
	
	int[][] distribution;
	
	public JumpsByArrowsSolver() {}
	
	public void setField(RectangularField field) {
		this.field = field;
	}
	
	public void setArrows(int[] arrows) {
		this.arrows = arrows;
	}
	
	public void setTimesheet(int[] ts) {
		timesheet = ts;
	}
	
	public void setSolutionLimit(int s) {
		solutionLimit = s;
	}
	
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		startpoint = 0;
		endpoind = field.getSize() - 1;
		state = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		
		place = new int[field.getSize() + 1];
		wayCount = new int[field.getSize() + 1];
		way = new int[field.getSize() + 1];
		ways = new int[field.getSize() + 1][field.getWidth()];
		
		place[0] = startpoint;
		state[startpoint] = t;
		
		distribution = new int[field.getSize()][field.getSize()];
		
		t = 0;
		solutionCount = 0;
		solution = null;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == state.length - 1) return;
		if(!check()) return;
		int p = place[t];
		int d = arrows[p];
		int q = field.jump(p, d);
		while(q >= 0) {
			if(canGo(q)) {
				ways[t][wayCount[t]] = q;
				wayCount[t]++;
			}
			q = field.jump(q, d);
		}
	}
	
	boolean canGo(int q) {
		if(q < 0 || state[q] >= 0) return false;
		if(timesheet != null && timesheet[q] >= 0) return timesheet[q] == t + 1;
		if(q != endpoind) return true;
		return t == state.length - 2;
	}
	
	boolean check() {
		for (int p = 0; p < state.length; p++) {
			if(state[p] >= 0) continue;
			if(!canGoIn(p) || !canGoOut(p)) return false;
		}
		return true;
	}
	
	boolean canGoOut(int p) {
		if(p == endpoind) return true;
		if(state[p] >= 0) return true;
		int q = field.jump(p, arrows[p]);
		while(q >= 0) {
			if(state[q] < 0) return true;
			q = field.jump(q, arrows[p]);
		}
		return false;
	}
	
	boolean canGoIn(int p) {
		if(state[p] >= 0) return true;
		for (int d = 0; d < 4; d++) {
			int q = field.jump(p, d);
			while(q >= 0) {
				if(canGoFromTo(q, reverse[d], p)) return true;
				q = field.jump(q, d);
			}
		}		
		return false;
	}
	
	int[] reverse = {2,3,0,1};
	
	boolean canGoFromTo(int b, int d, int e) {
		if(b < 0 || b == endpoind) return false;
		if(arrows[b] != d) return false;
		if(state[b] >= 0) return state[b] == t;		
		return true;
	}

	void move() {
		int p = ways[t][way[t]];
		state[p] = t + 1;
		place[t + 1] = p;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = ways[t][way[t]];
		state[p] = -1;
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
			if(t == field.getSize() - 1) {
				solutionCount++;
				onSolutionFound();
				if(solutionLimit > 0 && solutionLimit == solutionCount) return;
			}
		}
		
	}
	
	void onSolutionFound() {
		if(solutionCount == 1) {
			solution = (int[])state.clone();
		}
		for (int p = 0; p < state.length; p++) {
			distribution[p][state[p]]++;
		}		
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}
	
	int[] getSolution() {
		return solution;
	}
	
	public int[] getNarrowestPointData() {
		int sc = 1000000;
		int pb = -1;
		int tb = -1;
		for (int p = 0; p < distribution.length; p++) {
			int i = getBestVariant(p);
			if(i < 0) continue;
			int s = distribution[p][i];
			if(s >= sc) continue;
			sc = s;
			pb = p;
			tb = i;
		}
		return new int[]{pb, tb, sc};
	}
	int getBestVariant(int p) {
		int sc = 1000000;
		int tb = -1;
		for (int i = 0; i < distribution[p].length; i++) {
			int s = distribution[p][i];
			if(s == 0 || s >= sc) continue;
			sc = s;
			tb = i;
		}		
		return tb;
	}
	
	public void printSolution(int[] s) {
		System.out.println("Arrows");
		for (int p = 0; p < arrows.length; p++) {
			String c = "" + arrows[p];
			System.out.print(" " + c);
			if(field.isRightBorder(p)) System.out.println("");
		}
		System.out.println("Solution");
		
		for (int p = 0; p < s.length; p++) {
			String c = "" + s[p];
			if(c.length() < 2) c = " " + c;
			System.out.print(" " + c);
			if(field.isRightBorder(p)) System.out.println("");
		}
		System.out.println("");
	}

}
