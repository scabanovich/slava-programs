package slava.puzzle.scyscripers.analysis;

import com.slava.common.RectangularField;

public class ScyscripersSolver2 {
	RectangularField field;
	int[][] startPoint; // [d][i] -> [p - first visible cell]
	int[][] visible; //[d][i] -> [number of visible houses]
	
	int solutionLimit;
	int treeCountLimit = -1;
	
	int[] state;
	int[][][] usage; //[d][i][h]
	int[] totalUsage;
	int[][] restrictions;
	int emptyValues;
	
	int t;
	int[] color;
	int[] wayCount;
	int[] way;
	int[][] ways;
	int[] temp;
	
	int treeCount;
	int solutionCount;
	int[] solution;
	
	public ScyscripersSolver2() {
	}
	
	public void setField(RectangularField f) {
		field = f;
		initStartPoint();
	}
	
	/**
	 * 
	 * @param v
	 */	
	public void setVisibles(int[][] v) {
		visible = v;
	}
	
	public void setSolutionLimit(int s) {
		solutionLimit = s;
	}
	
	public void setTreeCountLimit(int s) {
		treeCountLimit = s;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void initStartPoint() {
		startPoint = new int[4][field.getWidth()];
		for (int d = 0; d < 4; d++) {
			for (int i = 0; i < field.getWidth(); i++) {
				int ix = (d == 0) ? 0 : (d == 2) ? field.getWidth() - 1 : i;
				int iy = (d == 1) ? 0 : (d == 3) ? field.getHeight() - 1 : i;
				int p = field.getIndex(ix, iy);
				startPoint[d][i] = p;
			}
		}
	}
	
	void init() {
		state = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		restrictions = new int[field.getSize()][field.getWidth()];
		usage = new int[4][field.getWidth()][field.getWidth()];
		totalUsage = new int[field.getWidth()];
		
		emptyValues = 0;
		for (int i = 0; i < state.length; i++) {
			if(state[i] < 0) emptyValues++;
		}
		
		applyVisibles0();
		
		color = new int[field.getSize() + 1];
		wayCount = new int[field.getSize() + 1];
		way = new int[field.getSize() + 1];
		ways = new int[field.getSize() + 1][field.getWidth()];
		temp = new int[field.getWidth()];

		t = 0;
		solution = null;
		solutionCount = 0;
		treeCount = 0;
	}
	
	void applyVisibles0() {
		if(visible == null) return;
		for (int d = 0; d < 4; d++) {
			for (int i = 0; i < startPoint[d].length; i++) {
				int c = visible[d][i];
				if(c <= 0) continue;
				int min = field.getWidth() - c + 1;
				int p = startPoint[d][i];
				while(p >= 0 && min < field.getWidth()) {
					for (int k = min; k < field.getWidth(); k++) {
						restrictions[p][k]++;
					}
					p = field.jump(p, d);
					min++;
				}
			}
		}
		
	}
	
	void srch() {
		wayCount[t] = 0;
		if(emptyValues == 0) return;
		if(!accept()) return;
		color[t] = getNextColor();
		int h = color[t];
		if(color[t] < 0) return;
		int wcm = 100;
		for (int d = 0; d < 2; d++) {
			for (int i = 0; i < field.getWidth(); i++) {
				if(usage[d][i][h] > 0) continue;
				int wc = getWayCount(d, i, h);
				if(wc == 0) return;
				if(wc < wcm) {
					wcm = wc;
					for (int k = 0; k < wc; k++) ways[t][k] = temp[k];
				}
			}
		}
		if(wcm == 100) return;
		wayCount[t] = wcm;
	}
	
	int getNextColor() {
		for (int c = field.getWidth() - 1; c >= 0; c--) {
			if(totalUsage[c] < field.getWidth()) return c;
		}
		return -1;		
	}
	
	int getWayCount(int d, int i, int h) {
		int wc = 0;
		int p = startPoint[d][i];
		for (int j = 0; j < field.getWidth(); j++) {
			int q = field.jump(p, d, j);
			if(canAdd(q, h)) {
				temp[wc] = q;
				wc++;
			}
		}
		return wc;
	}
	
	boolean canAdd(int p, int h) {
		if(state[p] >= 0 || restrictions[p][h] > 0) return false;
		if(h < 2) return true;
		int ix = field.getX(p);
		int iy = field.getY(p);
		for (int d = 0; d < 4; d++) {
			int i = (d == 0 || d == 2) ? iy : ix;
			int hi = visible[d][i];
			if(hi <= 0) continue;
			if(hi > getEstimation(d, i, p, h, false)) return false;
			if(hi < getEstimation(d, i, p, h, true)) return false;
		}
		return true;
	}
	
	int getEstimation(int d, int i, int pc, int hc, boolean minimum) {
		int p = startPoint[d][i];
		int h = -1;
		int q = 0;
		while(p >= 0) {
			if(state[p] > h) {
				h = state[p];
				++q;
			} else if(p == pc) {
				if(hc > h) {
					h = hc;
					++q;
				}
			} else if(h < 0) {
				++q;
				if(minimum) h = 0;
			}
			p = field.jump(p, d);
		}
		return q;
	}
	
	boolean accept() {
		if(visible == null) return true;
		for (int d = 0; d < 4; d++) {
			for (int i = 0; i < startPoint[d].length; i++) {
				if(!accept(d, i)) return false;
			}
		}
		return true;
	}
	
	boolean accept(int d, int i) {
		int h = visible[d][i];
		if(h <= 0) return true;
		int hc = getCount(d, i);
		return hc < 0 || h == hc;
	}
	
	int getCount(int d, int i) {
		int h = -1;
		int q = 0;
		int p = startPoint[d][i];
		while(p >= 0) {
			if(state[p] > h) {
				q++;
				h = state[p];
			} else if(state[p] < 0) {
				return -1;
			}		
			p = field.jump(p, d);
		}
		return q;
	}
	
	void move() {
		int p = ways[t][way[t]];
		int h = color[t];
		add(p, h);
		++t;
		srch();
		way[t] = -1;		
	}
	
	void add(int p, int h) {
		if(state[p] >= 0) throw new RuntimeException("" + state[p] + ":" + p + " " + h);
		state[p] = h;
		emptyValues--;
		totalUsage[h]++;
		for (int d = 0; d < 4; d++) {
			int q = field.jump(p, d);
			while(q >= 0) {
				restrictions[q][h]++;
				q = field.jump(q, d);
			}
		}
		int ix = field.getX(p);
		int iy = field.getY(p);
		usage[0][iy][h]++;
		usage[1][ix][h]++;
		usage[2][iy][h]++;
		usage[3][ix][h]++;
	}
	
	void back() {
		--t;
		int p = ways[t][way[t]];
		int h = color[t];
		remove(p, h);
	}
	
	void remove(int p, int h) {
		state[p] = -1;
		emptyValues++;
		totalUsage[h]--;
		for (int d = 0; d < 4; d++) {
			int q = field.jump(p, d);
			while(q >= 0) {
				restrictions[q][h]--;
				q = field.jump(q, d);
			}
		}
		int ix = field.getX(p);
		int iy = field.getY(p);
		usage[0][iy][h]--;
		usage[1][ix][h]--;
		usage[2][iy][h]--;
		usage[3][ix][h]--;
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
			if(emptyValues == 0 && accept()) {
				solutionCount++;
				onSolutionFound();
				if(solutionLimit <= solutionCount) return;
			}
			if(wayCount[t] == 0) {
				treeCount++;
				if(treeCountLimit > 0 && treeCount >= treeCountLimit) {
					solutionCount = solutionLimit + 1;
					return;
				}
			}
		}		
	}
	
	void onSolutionFound() {
		if(solutionCount == 1) {
			solution = (int[])state.clone();
//			printSolution();
		}
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}
	
	public int[] getSolution() {
		return solution;
	}
	
	public void printSolution() {
		System.out.println("");
		System.out.print("   ");
		for (int i = 0; i < visible[1].length; i++) {
			System.out.print(" " + getVisibleString(1, i));
		}
		System.out.println("");
		System.out.println("");
		for (int p = 0; p < solution.length; p++) {
			if(field.getX(p) == 0) {
				System.out.print(getVisibleString(0, field.getY(p)) + "  ");
			}
			System.out.print(" " + (solution[p] + 1));
			if(field.isRightBorder(p)) {
				System.out.println("  " + getVisibleString(2, field.getY(p)));
			}
		}
		System.out.println("");
		System.out.print("   ");
		for (int i = 0; i < visible[3].length; i++) {
			System.out.print(" " + getVisibleString(3, i));
		}
		System.out.println("");	
	}
	
	String getVisibleString(int d, int i) {
		if(visible[d][i] <= 0) return "-";
		return "" + visible[d][i];
	}

}
