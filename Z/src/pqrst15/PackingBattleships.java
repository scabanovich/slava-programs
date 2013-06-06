package pqrst15;

public class PackingBattleships {
	//0, 1, 2, 3 - direction, 4 - center, -1 - empty or one
	CrosswordField field;
	int[] ship = {4,3,3,2,2,2};
	
	int[] state;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] waysP;
	int[][] waysD;
	
	int solutionCount;
	
	int[][][][] distribution2; //p1,v1,p2,v2
	int[][][][][][] distribution3; //p1,v1,p2,v2,p3,v3
	
	public PackingBattleships() {}
	
	public void setField(CrosswordField field) {
		this.field = field;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.size];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		way = new int[ship.length + 1];
		wayCount = new int[ship.length + 1];
		waysP = new int[ship.length + 1][30];
		waysD = new int[ship.length + 1][30];
		distribution2 = new int[state.length][6][state.length][6];
		distribution3 = new int[state.length][6][state.length][6][state.length][6];
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == ship.length) return;
		int p0 = 0;
		if(t > 0 && ship[t] == ship[t - 1]) {
			p0 = waysP[t - 1][way[t - 1]] + 1;
		}
		for (int p = p0; p < field.size; p++) {
			for (int d = 0; d < 2; d++) {
				if(canAdd(ship[t], p, d)) {
					waysP[t][wayCount[t]] = p;
					waysD[t][wayCount[t]] = d;
					wayCount[t]++;
				}
			}
		}
	}
	
	boolean canAdd(int length, int p, int d) {
		if(state[p] >= 0) return false;
		for (int i = 1; i < length; i++) {
			int q = field.jump(p, d, i);
			if(q < 0 || state[q] >= 0) return false;
		}
		return true;
	}
	
	void move() {
		int p = waysP[t][way[t]];
		int d = waysD[t][way[t]];
		add(ship[t], p, d);
		++t;
		srch();
		way[t] = -1;
	}
	
	void add(int length, int p, int d) {
		state[p] = d;
		for (int i = 1; i < length - 1; i++) {
			int q = field.jump(p, d, i);
			state[q] = 4;
		}
		int q = field.jump(p, d, length - 1);
		state[q] = 2 + d;		
	}
	
	void back() {
		--t;
		int p = waysP[t][way[t]];
		int d = waysD[t][way[t]];
		remove(ship[t], p, d);
	}
	
	void remove(int length, int p, int d) {
		for (int i = 0; i < length; i++) {
			int q = field.jump(p, d, i);
			state[q] = -1;
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
			++way[t];
			move();
			if(t == ship.length) {
				++solutionCount;
				onSolutionFound();
			}
		}
	}
	
	int getState(int p) {
		return state[p] < 0 ? 5 : state[p];
	}
	
	void onSolutionFound() {
		for (int p1 = 0; p1 < field.size; p1++) {
			for (int p2 = 0; p2 < field.size; p2++) {
				if(p1 == p2) continue;
				distribution2[p1][getState(p1)][p2][getState(p2)]++;
			}
		}
		for (int p1 = 0; p1 < field.size; p1++) {
			for (int p2 = 0; p2 < field.size; p2++) {
				if(p1 == p2) continue;
				for (int p3 = 0; p3 < field.size; p3++) {
					if(p3 == p1 || p3 == p2) continue;
					distribution3[p1][getState(p1)][p2][getState(p2)][p3][getState(p3)]++;
				}
			}
		}
	}
	
	public void findSolution() {
		int min = solutionCount;
		for (int p1 = 0; p1 < field.size; p1++) {
			for (int n1 = 0; n1 < 6; n1++) {
				for (int p2 = 0; p2 < field.size; p2++) {
					for (int n2 = 0; n2 < 6; n2++) {
						int dc = distribution2[p1][n1][p2][n2];
						if(dc > 0 && dc < min) {
							min = distribution2[p1][n1][p2][n2];
							if(min == 1) {
								System.out.println("p1=" + p1 + " n1=" + n1 + " p2=" + p2 + " n2=" + n2);
							}
						}
					}
				}
			}
		}
		for (int p1 = 0; p1 < field.size; p1++) {
			for (int n1 = 0; n1 < 6; n1++) {
				for (int p2 = 0; p2 < field.size; p2++) {
					for (int n2 = 0; n2 < 6; n2++) {
						for (int p3 = 0; p3 < field.size; p3++) {
							for (int n3 = 0; n3 < 6; n3++) {
								int dc = distribution3[p1][n1][p2][n2][p3][n3];
								if(dc > 0 && dc <= min) {
									min = dc;
									if(min == 1) {
										System.out.println("p1=" + p1 + " n1=" + n1 + " p2=" + p2 + " n2=" + n2 + " p3=" + p3 + " n3=" + n3);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	void printSolution() {
		for (int i = 0; i < field.size; i++) {
			int v = state[i];
			if(v < 0) v = 5;
			System.out.print(" " + v);
			if(field.x[i] == field.width - 1) System.out.println("");
		}
		System.out.println("");
	}
	
	
	public static void main(String[] args) {
		PackingBattleships p = new PackingBattleships();
		CrosswordField f = new CrosswordField();
		f.setSize(5, 4);
		p.setField(f);
		p.solve();
		System.out.println(p.solutionCount);
		p.findSolution();
	}

}
