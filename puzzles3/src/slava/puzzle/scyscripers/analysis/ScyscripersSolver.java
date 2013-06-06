package slava.puzzle.scyscripers.analysis;

import com.slava.common.RectangularField;

public class ScyscripersSolver {
	RectangularField field;
	int[][] startPoint; // [d][i] -> [p - first visible cell]
	int[][] visible; //[d][i] -> [number of visible houses]
	
	int[] weights = {1,3,5,10,20,50,200,1000};
	int[] pweights;
	
	int solutionLimit;
	boolean isRandomizing = false;
	int treeCountLimit = -1;
	
	int[] state;
	int[][][] usage; //[d][i][h]
	int[][] restrictions;
	int emptyValues;
	
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	int[] temp;
	
	int treeCount;
	int solutionCount;
	int[] solution;
	
	public ScyscripersSolver() {
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
	
	public void setRandomizing(boolean b) {
		isRandomizing = b;
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
		
		emptyValues = 0;
		for (int i = 0; i < state.length; i++) {
			if(state[i] < 0) emptyValues++;
		}
		
		applyVisibles0();
		initWeights();
		
		place = new int[field.getSize() + 1];
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
	
	void initWeights() {
		if(visible == null) return;
		pweights = new int[state.length];
		for (int p = 0; p < pweights.length; p++) {
			int ix = field.getX(p);
			int iy = field.getY(p);
			if(visible == null) {
				pweights[p] = 1;
			} else {
				pweights[p] = weights[visible[0][iy]] *
					weights[visible[1][ix]] *
					weights[visible[2][iy]] *
					weights[visible[3][ix]];
			}
		}
	}

	void srch() {
		wayCount[t] = 0;
		if(emptyValues == 0) return;
		if(!accept()) return;
		place[t] = -1;
		int wcm = 100;
		int ww = Integer.MAX_VALUE;
		for (int p = 0; p < state.length; p++) {
			if(state[p] >= 0) continue;
			int wc = getWayCount(p);
			if(wc == 0) return;
			if(wc < wcm || (visible != null && wc == wcm && pweights[p] < ww)) {
				if(wc == wcm) {
					ww = pweights[p];
				} else {
					if(visible != null) ww = pweights[p];
				}
				wcm = wc;
				for (int i = 0; i < wc; i++) ways[t][i] = temp[i];
				place[t] = p;
			}
		}
		if(wcm == 100) return;
		wayCount[t] = wcm;
		if(isRandomizing) randomize();
	}
	
	int getWayCount(int p) {
		int wc = 0;
		for (int i = 0; i < field.getWidth(); i++) {
			if(restrictions[p][i] == 0) {
				temp[wc] = i;
				wc++;
			}
		}
		return wc;
	}
	
	void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = 0; i < wayCount[t]; i++) {
			int j = i + (int)((wayCount[t] - i) * Math.random());
			if(j == i) continue;
			int c = ways[t][i];
			ways[t][i] = ways[t][j];
			ways[t][j] = c;
		}		
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
	
	boolean xxx = false;
	
	boolean accept(int d, int i) {
		int h = visible[d][i];
		if(h <= 0) return true;
		return h <= getMaxCount(d, i) && h >= getMinCount(d, i);
	}
	
	int getMaxCount(int d, int i) {
		int h = -1;
		int q = 0;
		int p = startPoint[d][i];
		int hcc = -1;
		while(p >= 0) {
			if(state[p] > h) {
				q++;
				h = state[p];
			} else if(state[p] < 0) {
				int hc = getSmallest(d, i, hcc);
				if(hc >= 0) {
					if(xxx) throw new RuntimeException("Fuck");
					hcc = hc;
				}
				if(hc > h) {
					h = hc;
					q++;
				}
			}		
			p = field.jump(p, d);
		}
		return q;
	}
	
	int getSmallest(int d, int i, int h) {
		for (int k = h + 1; k < usage[d][i].length; k++) {
			if(usage[d][i][k] == 0) return k;
		}
		return -1;
	}
	
	int getMinCount(int d, int i) {
		int h = -1;
		int q = 0;
		int p = startPoint[d][i];
		boolean f = false;
		while(p >= 0) {
			if(state[p] > h) {
				q++;
				h = state[p];
			} else if(state[p] < 0 && !f) {
				int hc = getLargest(d, i);
				if(hc > h) {
					if(xxx) throw new RuntimeException("Fuck");
					h = hc;
					q++;
				}
				f = true;
			}		
			p = field.jump(p, d);
		}
		return q;
	}
	
	int getLargest(int d, int i) {
		for (int k = usage[d][i].length - 1; k >= 0; k--) {
			if(usage[d][i][k] == 0) return k;
		}
		return -1;
	}

	void move() {
		int p = place[t];
		int h = ways[t][way[t]];
		add(p, h);
		++t;
		srch();
		way[t] = -1;		
	}
	
	void add(int p, int h) {
		state[p] = h;
		emptyValues--;
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
		int p = place[t];
		int h = ways[t][way[t]];
		remove(p, h);
	}
	
	void remove(int p, int h) {
		state[p] = -1;
		emptyValues++;
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
			if(emptyValues == 0 && accept2()) {
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
	
	boolean accept2() {
		xxx = true;
		boolean b = accept();
		xxx = false;
		return b;
	}
	
	void onSolutionFound() {
		if(solutionCount == 1) solution = (int[])state.clone();
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
			System.out.print(" " + visible[1][i]);
		}
		System.out.println("");
		System.out.println("");
		for (int p = 0; p < solution.length; p++) {
			if(field.getX(p) == 0) {
				System.out.print(visible[0][field.getY(p)] + "  ");
			}
			System.out.print(" " + (solution[p] + 1));
			if(field.isRightBorder(p)) {
				System.out.println("  " + visible[2][field.getY(p)]);
			}
		}
		System.out.println("");
		System.out.print("   ");
		for (int i = 0; i < visible[3].length; i++) {
			System.out.print(" " + visible[3][i]);
		}
		System.out.println("");
	
	}

}
