package slava.puzzle.paths.rook.analysis;

import com.slava.common.RectangularField;

public class RookPathsSimpleSolver {
	RectangularField field;
	int[] data;
	boolean isRandomizung;
	int solutionLimit;
	
	int[] state;
	int[] usage;
	int unused;
	
	int t;
	int[] value;
	int[] wayCount;
	int[] way;
	int[][] ways;
	int[] temp;
	
	int solutionCount;
	int[] solution;
	
	public RookPathsSimpleSolver() {}
	
	public void setField(RectangularField f) {
		field = f;
	}
	
	public void setData(int[] data) {
		this.data = data;
	}
	
	public void setRandomizing(boolean b) {
		isRandomizung = b;
	}
	
	public void setSolutionLimit(int s) {
		solutionLimit = s;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		usage = new int[field.getSize()];
		unused = 0;
		for (int i = 0; i < state.length; i++) {
			usage[i] = -1;
		}
		for (int i = 0; i < state.length; i++) {
			state[i] = data == null || data[i] < 0 ? -1 : data[i];
			if(data[i] >= 0) {
				usage[data[i]] = i;
			} else {
				unused++;
			}
		}
		wayCount = new int[unused + 100];
		value = new int[unused + 100];
		way = new int[unused + 100];
		ways = new int[unused + 100][field.getWidth() + field.getHeight()];
		temp = new int[field.getWidth() + field.getHeight() - 2];
		
		t = 0;
		solutionCount = 0;
		solution = null;		
	}
	
	void srch() {
		wayCount[t] = 0;
		if(unused == 0) return;
		int wm = 1000;
		for (int v = 0; v < field.getSize(); v++) {
			if(usage[v] >= 0) continue;
			int pa = v == 0 ? -1 : usage[v - 1];
			int pb = v == usage.length - 1 ? -1 : usage[v + 1];
			if(pa < 0 && pb < 0) continue;
			int wc = pa < 0 ? getWayCount(pb, pa) : getWayCount(pa, pb);
			if(wc == 0) return;
			if(wc < wm) {
				wm = wc;
				value[t] = v;
				for (int i = 0; i < wc; i++) ways[t][i] = temp[i];
			}
		}		
		if(wm < 1000) {
			wayCount[t] = wm;
			if(isRandomizung) randomize();
		}
		
	}
	
	void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = 0; i < wayCount[t]; i++) {
			int j = i + (int)((wayCount[t] - i) * Math.random());
			if(i == j) continue;
			int c = ways[t][i];
			ways[t][i] = ways[t][j];
			ways[t][j] = c;
		}		
	}
	
	int getWayCount(int pa, int pb) {
		int wc = 0;
		int ixb = pb < 0 ? -1 : field.getX(pb);
		int iyb = pb < 0 ? -1 : field.getY(pb);
		int ix = field.getX(pa);
		for (int iy = 0; iy < field.getHeight(); iy++) {
			if(pb >= 0 && ix != ixb && iy != iyb) continue;
			int p = field.getIndex(ix, iy);
			if(state[p] >= 0) continue;
			temp[wc] = p;
			wc++;
		}
		int iy = field.getY(pa);
		for (ix = 0; ix < field.getWidth(); ix++) {
			if(pb >= 0 && ix != ixb && iy != iyb) continue;
			int p = field.getIndex(ix, iy);
			if(state[p] >= 0) continue;
			temp[wc] = p;
			wc++;
		}
		return wc;
	}
	
	void move() {
		int p = ways[t][way[t]];
		int v = value[t];
		if(usage[v] >= 0) throw new RuntimeException("v=" + v + " p=" + p);
		if(state[p] >= 0) throw new RuntimeException("p=" + p + " v=" + v);
		usage[v] = p;
		state[p] = v;
		unused--;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = ways[t][way[t]];
		int v = value[t];
		usage[v] = -1;
		state[p] = -1;
		unused++;
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
			if(unused == 0) {
				solutionCount++;
				if(solutionCount == 1) {
					solution = (int[])state.clone();
				}
				if(solutionLimit > 0 && solutionLimit < solutionCount) return;
			}
		}
	}
	
	public void print(int[] solution) {
		for (int p = 0; p < solution.length; p++) {
			String s = (solution[p] < 0) ? " -" : "" + solution[p];
			if(s.length() < 2) s = " " + s;
			System.out.print(" " + s);
			if(field.isRightBorder(p)) System.out.println("");
		}
		System.out.println("");
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}
	
	public int[] getSolution() {
		return solution;
	}

}
