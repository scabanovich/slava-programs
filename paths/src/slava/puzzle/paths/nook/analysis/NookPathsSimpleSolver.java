package slava.puzzle.paths.nook.analysis;

import java.util.ArrayList;

import com.slava.common.RectangularField;

public class NookPathsSimpleSolver {
	RectangularField field;
	NookField nook;
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
	int rememberedSolutionLimit = 1000;
	ArrayList solutions = new ArrayList();
	int treeCount;
	
	public NookPathsSimpleSolver() {}
	
	public void setField(RectangularField f) {
		field = f;
		nook = new NookField();
		nook.setSize(f.getWidth(), f.getHeight());
	}
	
	public void setFilter(int[] filter) {
		nook.setFilter(filter);
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
			if(nook.filter != null && nook.filter[i] == 1) continue;
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
		solutions.clear();
		treeCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(unused == 0) return;
		if(!NookContinuityCheck.check(nook, state, usage)) return;
		int[] uniqueWay = NookContinuityCheck.getUniqueMove(nook, state, usage);
		if(uniqueWay != null) {
			if(uniqueWay[1] < 0) return;
			value[t] = uniqueWay[1];
			ways[t][0] = uniqueWay[0];
			wayCount[t] = 1;
			return;
		}
		
		int wm = 1000;
		for (int v = 0; v < nook.getFilteredSize(); v++) {
			if(usage[v] >= 0) continue;
			int pa = v == 0 ? -1 : usage[v - 1];
			int pb = v == nook.getFilteredSize() - 1 ? -1 : usage[v + 1];
			if(pa < 0 && pb < 0) continue;
			int wc = 0;
			if(pa >= 0) {
				int delta = 1;
				int vd = v + 1;
				while(delta < 6 && vd < nook.getFilteredSize() && usage[vd] < 0) {
					++delta;
					++vd;
				}
				if(vd == nook.getFilteredSize() || delta == 6) {
					delta = 0;
				} else {
					pb = usage[vd];
				}
				wc = getWayCount(pa, pb, delta);
			} else {
				int delta = 1;
				int vd = v - 1;
				while(delta < 6 && vd >= 0 && usage[vd] < 0) {
					++delta;
					--vd;
				}
				if(vd == -1 || delta == 6) {
					delta = 0;
				} else {
					pa = usage[vd];
				}
				wc = getWayCount(pb, pa, delta);
			}
			
			
//			wc = pa < 0 ? getWayCount(pb, pa, 1) : getWayCount(pa, pb, 1);
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
	
	int getWayCount(int pa, int pb, int delta) {
		int wc = 0;
		for (int d = 0; d < 8; d++) {
			int q = nook.jump(pa, d);
			if(q < 0 || state[q] >= 0) continue;
			if(pb >= 0 && delta > 0 && nook.getDistance(q, pb) > delta) continue;
			temp[wc] = q;
			wc++;
		}
		return wc;
	}
	
	void move() {
		int p = ways[t][way[t]];
		int v = value[t];
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
		int tm = 0;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(wayCount[t] == 0) treeCount++;
			if(t > tm) {
				tm = t;
//				System.out.println(t);
			}
			if(unused == 0) {
				solutionCount++;
				if(!checkSolution()) {
					print(state);
					throw new RuntimeException("wrong path");
				}
				if(solutionCount == 1) {
					solution = (int[])state.clone();
				}
				if(solutionLimit > 0 && solutionLimit < solutionCount) {
					return;
				} else if(solutionCount <= rememberedSolutionLimit){
					int[] ss = (int[])state.clone();
					for (int i = 0; i < ss.length; i++) ss[i] = ss[i] + 1;
					solutions.add(ss);
				}
			}
		}
	}
	
	public void print(int[] solution) {
		for (int p = 0; p < solution.length; p++) {
			String s = (nook.filter != null && nook.filter[p] == 1) ? "*"
					: (solution[p] < 0) ? " -" : "" + (solution[p] + 1);
			if(s.length() < 2) s = " " + s;
			System.out.print(" " + s);
			if(field.isRightBorder(p)) System.out.println("");
		}
		System.out.println("");
	}
	
	boolean checkSolution() {
		for (int v = 0; v < nook.getFilteredSize() - 1; v++) {
			int p1 = usage[v], p2 = usage[v + 1];
			if(nook.getDistance(p1, p2) != 1) return false;
		}
		return true;
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}
	
	public int[] getSolution() {
		return solution;
	}
	
	public ArrayList getSolutions() {
		return solutions;
	}
	
	public int getTreeCount() {
		return treeCount;
	}

}
