package slava.puzzle.paths.walker.analysis;

import java.util.ArrayList;

import com.slava.common.RectangularField;

public class WalkerPathsSolver {
	static int[][] VARIANTS = {
		{0}, {1}, {2}, {3}, {0,1}, {0,2}, {0,3}, {1,2}, {1,3}, {2,3}
	};
	static int[] REVERSE = {2,3,0,1};
	static int[] NEXT = {1,2,3,0};
	RectangularField field;
	int[] filter;
	int[][] parts;
	// 0 - no data; 1 - straight; 2 - turn
	int[] turns;
	int filteredSize;
	boolean isRandomizung;
	int solutionLimit;
	
	int[][] state;
	int[] usage;
	int unresolved;
	
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	int[] temp;
	
	int solutionCount;
	int[][] solution;
	int rememberedSolutionLimit = 1000;
	ArrayList solutions = new ArrayList();
	int treeCount;
	int treeCountLimit = -1;
	
	public WalkerPathsSolver() {}
	
	public void setField(RectangularField f) {
		field = f;
	}
	
	public void setFilter(int[] filter) {
		this.filter = filter;
	}
	
	public void setParts(int[][] parts) {
		this.parts = parts;
	}
	
	public void setTurns(int[] turns) {
		this.turns = turns;
	}
	
	public void setRandomizing(boolean b) {
		isRandomizung = b;
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
	
	void init() {
		state = new int[field.getSize()][4];
		usage = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) {
			usage[i] = 0;
		}
		filteredSize = 0;
		unresolved = 0;
		for (int p = 0; p < state.length; p++) {
			if(filter[p] == 0) {
				filteredSize++;
			}
			for (int d = 0; d < 4; d++) {
				state[p][d] = -1;
				int q = field.jump(p, d);
				if(q < 0 || filter[p] == 1 || filter[q] == 1) {
					state[p][d] = 0;
				}
			}
		}
		if(parts != null) for (int p = 0; p < parts.length; p++) {
			for (int d = 0; d < 2; d++) {
				int q = field.jump(p, d);
				if(q < 0) continue;
				if(parts[p][d] < 0) continue;
				int v = parts[p][d];
				state[p][d] = v;
				state[q][REVERSE[d]] = v;
				if(v == 1) {
					usage[p]++;
					usage[q]++;
				}
			}
		}
		for (int p = 0; p < state.length; p++) {
			if(usage[p] > 2) throw new RuntimeException("Path cannot cross itself: " + p);
			if(filter[p] == 0 && usage[p] < 2) unresolved++;
		}
		wayCount = new int[field.getSize() + 1];
		place = new int[field.getSize() + 1];
		way = new int[field.getSize() + 1];
		ways = new int[field.getSize() + 1][field.getWidth() + field.getHeight()];
		temp = new int[field.getWidth() + field.getHeight() - 2];
		
		t = 0;
		solutionCount = 0;
		solution = null;
		solutions.clear();
		treeCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(unresolved == 0) return;
		if(!WalkerContinuityCheck.check(field, filter, state, usage)) return;
		
		int wm = 1000;
		for (int p = 0; p < state.length; p++) {
			if(usage[p] == 2 || filter[p] == 1) continue;
			int wc = getWayCount(p);
			if(wc == 0) return;
			if(wc < wm) {
				wm = wc;
				place[t] = p;
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
	
	int getWayCount(int p) {
		int wc = 0;
		for (int k = 0; k < VARIANTS.length; k++) {
			int[] ds = VARIANTS[k];
			if(usage[p] + ds.length != 2) continue;
			boolean c = true;
			for (int i = 0; i < ds.length; i++) {
				int d = ds[i];
				int q = field.jump(p, d);
				if(q < 0 || filter[q] == 1 || state[p][d] >= 0 || usage[q] == 2) {
					c = false;
				}
			}
			if(!c) continue;
			add(p, k);
			int length = WalkerContinuityCheck.getCircleLength(field, filter, state, usage, p);
			boolean ok = length < 0 || length == filteredSize;
			if(ok) ok = WalkerTurnsCheck.check(field, filter, turns, state, usage);
			remove(p, k);
			if(ok) {
				temp[wc] = k;
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
		int[] ds = VARIANTS[v];
		for (int i = 0; i < ds.length; i++) {
			int d = ds[i];
			int q = field.jump(p, d);
			state[p][d] = 1;
			state[q][REVERSE[d]] = 1;
			usage[p]++;
			usage[q]++;
			if(usage[p] == 2) unresolved--;
			if(usage[q] == 2) unresolved--;
		}
	}	

	void back() {
		--t;
		int p = place[t];
		int v = ways[t][way[t]];
		remove(p, v);
	}
	
	void remove(int p, int v) {
		int[] ds = VARIANTS[v];
		for (int i = 0; i < ds.length; i++) {
			int d = ds[i];
			int q = field.jump(p, d);
			state[p][d] = -1;
			state[q][REVERSE[d]] = -1;
			if(usage[p] == 2) unresolved++;
			if(usage[q] == 2) unresolved++;
			usage[p]--;
			usage[q]--;
		}
	}
	
	void anal() {
//		print(state);
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
			if(wayCount[t] == 0) {
				treeCount++;
				if(treeCountLimit > 0 && treeCount > treeCountLimit) {
					if(solutionCount > 0) {
						if(solutionLimit <= 0) {
							solutionCount = 1000;
						} else {
							solutionCount = solutionLimit + 1;
						}						
					}
					return;
				}
			}
			if(t > tm) {
				tm = t;
//				System.out.println(t + " " + place[t - 1]);
			}
			if(wayCount[t] == 3) {
				print(state);
				System.out.println(place[t]);
				return;
			}

			if(unresolved == 0) {
				solutionCount++;
				if(!checkSolution()) {
					print(state);
					throw new RuntimeException("wrong path");
				}
				if(solutionCount == 1) {
					int[][] ss = new int[state.length][4];
					for (int i = 0; i < ss.length; i++) {
						for (int d = 0; d < 4; d++) ss[i][d] = state[i][d];
					}
					solution = ss;
				}
				if(solutionLimit > 0 && solutionLimit < solutionCount) {
					return;
				} else if(solutionCount <= rememberedSolutionLimit){
					int[][] ss = new int[state.length][4];
					for (int i = 0; i < ss.length; i++) {
						for (int d = 0; d < 4; d++) ss[i][d] = state[i][d];
					}
					solutions.add(ss);
				}
			}
		}
	}
	
	public void print(int[][] solution) {
		for (int p = 0; p < solution.length; p++) {
			String s = "";
			if(filter[p] == 1) {
				s = "+";
			} else {
				int k = 0;
				int v = 1;
				for (int d = 0; d < 4; d++) {
					if(solution[p][d] == 1) k += v;
					v = v * 2;
				}
				s += k;
			}
			if(s.length() < 2) s = " " + s;
			System.out.print(" " + s);
			if(field.isRightBorder(p)) System.out.println("");
		}
		System.out.println("");
	}
	
	boolean checkSolution() {
		//TODO
		return true;
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}
	
	public int[][] getSolution() {
		return solution;
	}
	
	public ArrayList getSolutions() {
		return solutions;
	}
	
	public int getTreeCount() {
		return treeCount;
	}

}
