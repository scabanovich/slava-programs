package forsmarts;

import forsmarts.distances.DistancesField;

public class EasyScyscripersSolver {
	DistancesField field;
	//0,1,2 - letter 4,5,6 - houses
	int[][] letterRestrictions;
	int[][] houseRestrictions;
	
	int[] state;
	int[][] restrictions;  //[p][v]

	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	int[] temp;
	
	int solutionCount;

	public EasyScyscripersSolver() {}
	
	public void setField(DistancesField f) {
		field = f;
	}
	
	public void setRestrictions(int[][] letterRestrictions, int[][] houseRestrictions) {
		this.letterRestrictions = letterRestrictions;
		this.houseRestrictions = houseRestrictions;		
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		restrictions = new int[field.getSize()][field.getWidth()];
		initRestrictions();
	
		place = new int[field.getSize() + 1];
		wayCount = new int[field.getSize() + 1];
		way = new int[field.getSize() + 1];
		ways = new int[field.getSize() + 1][field.getWidth()];
		temp = new int[field.getWidth()];
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(!checkRestrictions()) return;
		int wcb = field.getWidth() + 1;
		place[t] = -1;
		for (int p = 0; p < state.length; p++) {
			if(state[p] >= 0) continue;
			int wc = computeWays(p);
			if(wc < wcb) {
				if(wc == 0) return;
				wcb = wc;
				place[t] = p;
				for (int i = 0; i < wc; i++) ways[t][i] = temp[i];
			}
		}
		if(place[t] >= 0) {
			wayCount[t] = wcb;
		}		
	}
	
	void initRestrictions() {
		for (int i = 0; i < letterRestrictions.length; i++) {
			int[] rs = letterRestrictions[i];
			int p = field.getIndex(rs[0], rs[1]);
			for (int v = 0; v < 3; v++) {
				if(v != rs[3]) restrictions[p][v]++;
			}
		}
	}
	
	int computeWays(int p) {
		int wc = 0;
		for (int i = 0; i < field.getWidth(); i++) {
			if(restrictions[p][i] > 0) continue;
			temp[wc] = i;
			++wc;
		}
		return wc;
	}
	
	boolean checkRestrictions() {
		for (int i = 0; i < letterRestrictions.length; i++) {
			if(!checkLetterRestriction(letterRestrictions[i])) return false;			
		}
		for (int i = 0; i < houseRestrictions.length; i++) {
			if(!checkHouseRestriction(houseRestrictions[i])) return false;
		}
		return true;
	}
	
	boolean checkLetterRestriction(int[] rs) {
		int p = field.getIndex(rs[0], rs[1]);
		int d = rs[2];
		int empty = 0;
		while(p >= 0) {
			if(state[p] == rs[3]) return true;
			if(state[p] < 0) {
				empty++;
			} else if(state[p] < 3) {
				return empty > 0; //another letter
			}
			p = field.jump(p, d);
		}
		return true;
	}
	
	boolean checkHouseRestriction(int[] rs) {
		int p = field.getIndex(rs[0], rs[1]);
		int d = rs[2];
		int empty = 0;
		int visible = 0;
		int h = 2;
		while(p >= 0) {
			if(state[p] > h) {
				h = state[p];
				visible++;
//				if(visible > rs[3] && empty == 0) return false;
			} else if(state[p] < 0) {
				empty++;
			}
			p = field.jump(p, d);
		}
		if(empty == 0 && visible != rs[3]) return false;		
		return true;
	}
	
	void move() {
		int p = place[t];
		int v = ways[t][way[t]];
		state[p] = v;
		int ix, iy;
		ix = field.getX(p);
		for (iy = 0; iy < field.getWidth(); iy++) {
			int q = field.getIndex(ix, iy);
			restrictions[q][v]++;
		}
		iy = field.getY(p);
		for (ix = 0; ix < field.getWidth(); ix++) {
			int q = field.getIndex(ix, iy);
			restrictions[q][v]++;
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = place[t];
		int v = ways[t][way[t]];
		state[p] = -1;
		int ix, iy;
		ix = field.getX(p);
		for (iy = 0; iy < field.getWidth(); iy++) {
			int q = field.getIndex(ix, iy);
			restrictions[q][v]--;
		}
		iy = field.getY(p);
		for (ix = 0; ix < field.getWidth(); ix++) {
			int q = field.getIndex(ix, iy);
			restrictions[q][v]--;
		}
	}
	
	void anal() {
		srch();
		way[t] = -1;
		int tm = 0;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t > tm) {
				tm = t;
//				System.out.println(t);
			}
			if(t == field.getSize() && checkRestrictions()) {
				++solutionCount;
				if(solutionCount % 10000 == 0) System.out.println(solutionCount);
				if(solutionCount < 2) {
					printSolution();
				}
			}
		}
	}
	
	static char[] DESIGNATION = {'A', 'B', 'C', '1', '2', '3'};
	
	void printSolution() {
		for (int i = 0; i < state.length; i++) {
			char c = (state[i] < 0) ? '+' : DESIGNATION[state[i]];
			System.out.print(" " + c);
			if(field.getX(i) == field.getWidth() - 1) System.out.println("");
		}
		System.out.println("");
	}

}
