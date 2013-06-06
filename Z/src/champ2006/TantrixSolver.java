package champ2006;

public class TantrixSolver implements TantrixFigures {
	public interface PackingListener {
		public void packingFound(TantrixSolver solver);
	}
	TantrixField field;
	int[] form;
	
	int[][] state;
	int[] figureUsage;
	
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] waysF;
	int[][] waysD;
	
	PackingListener listener;
	
	public TantrixSolver() {}
	
	public void setField(TantrixField field) {
		this.field = field;
	}
	
	public void setForm(int[] form) {
		this.form = form;
	}
	
	public void setListener(PackingListener listener) {
		this.listener = listener;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()][6];
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < 6; j++) state[i][j] = -1;
		}
		figureUsage = new int[FIGURES.length];
		place = new int[FIGURES.length + 1];
		wayCount = new int[FIGURES.length + 1];
		way = new int[FIGURES.length + 1];
		waysF = new int[FIGURES.length + 1][FIGURES.length * 6];
		waysD = new int[FIGURES.length + 1][FIGURES.length * 6];
		t = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		place[t] = getNextPlace();
		if(place[t] < 0) return;
		for (int f = 0; f < FIGURES.length; f++) {
			if(figureUsage[f] > 0) continue;
			int dm = getDMax(f);
			for (int d = 0; d < dm; d++) {
				if(canAdd(f, place[t], d)) {
					waysF[t][wayCount[t]] = f;
					waysD[t][wayCount[t]] = d;
					wayCount[t]++;
				}
			}
		}
		if(t == 0) randomize();
	}
	
	void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = 0; i < wayCount[t]; i++) {
			int j = i + (int)((wayCount[t] - i) * Math.random());
			int c = waysD[t][i]; waysD[t][i] = waysD[t][j]; waysD[t][j] = c;
			c = waysF[t][i]; waysF[t][i] = waysF[t][j]; waysF[t][j] = c;
		}
	}
	
	int getDMax(int f) {
		int[] fg = FIGURES[f];
		for (int i = 0; i < 3; i++) {
			if(fg[i] != fg[i + 3]) return 6;
		}
		return 3;
	}
	
	int getNextPlace() {
		int p = (t == 0) ? 0 : place[t - 1] + 1;
		while(p < field.getSize() && form[p] == 0) ++p;
		return p >= field.getSize() ? -1 : p;
	}
	
	boolean canAdd(int f, int p, int r) {
		int[] fg = FIGURES[f];
		for (int d = 0; d < 6; d++) {
			int dr = REVERSE[d];
			int v = fg[ROTATIONS[r][d]];
			int q = field.jp[d][p];
			if(q < 0 || form[q] == 0) {
				//do not move black out of field
				//if(v == 0) return false;
			}
			if(q < 0 || state[q][dr] < 0 || state[q][dr] == v) continue;
			return false;
		}		
		return true;
	}
	
	void move() {
		int p = place[t];
		int f = waysF[t][way[t]];
		int d = waysD[t][way[t]];
		add(f, p, d);
		++t;
		srch();
		way[t] = -1;
	}
	
	void add(int f, int p, int r) {
		int[] fg = FIGURES[f];
		for (int d = 0; d < 6; d++) {
			int v = fg[ROTATIONS[r][d]];
			state[p][d] = v;
		}
		figureUsage[f]++;
	}
	
	void back() {
		--t;
		int p = place[t];
		int f = waysF[t][way[t]];
		int d = waysD[t][way[t]];
		remove(f, p, d);
	}
	
	void remove(int f, int p, int r) {
		for (int d = 0; d < 6; d++) {
			state[p][d] = -1;
		}
		figureUsage[f]--;
	}
	
	void anal() {
		srch();
		way[t] = -1;
//		int path = 0;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t == FIGURES.length) {
				listener.packingFound(this);
			}
		}
	}
	
	void printSolution() {
		for (int i = 0; i < t; i++) {
			int f = waysF[i][way[i]];
			int d = waysD[i][way[i]];
			System.out.print(" (" + f + "," + d + ")");
		}
		System.out.println("");
	}
	
	int getLongestPath() {
		int[] temp = new int[state.length];
		int path = 0;
		for (int p = 0; p < state.length; p++) {
			if(form[p] == 0 || temp[p] > 0) continue;
			int cp = 1;
			temp[p] = 1;
			for (int d = 0; d < 6; d++) {
				if(state[p][d] == 0) cp += getRay(p, d, temp);
			}
			if(cp > path) path = cp;
		}
		return path;
	}
	
	int getRay(int p, int d, int[] temp) {
		int c = 0;
		int q = p;
		while(true) {
			q = field.jp[d][q];
			if(q < 0 || form[q] == 0 || q == p || temp[q] > 0) break;
			temp[q]++;
			++c;
			d = getOutDirection(q, REVERSE[d]);			
		}
		return c;
	}
	
	int getOutDirection(int p, int d) {
		for (int d1 = 0; d1 < 6; d1++) {
			if(d1 == d) continue;
			if(state[p][d] == state[p][d1]) return d1;
		}
		return -1;
	}
	
	int getProduct() {
		int product = 1;
		for (int c = 0; c < 3; c++) {
			int[] temp = new int[state.length];
			for (int p = 0; p < state.length; p++) {
				if(form[p] == 0 || temp[p] > 0) continue;
				int cp = 1;
				temp[p] = 1;
				for (int d = 0; d < 6; d++) {
					if(state[p][d] == c) cp += getRay(p, d, temp);
				}
				product *= cp;
			}
		}
		return product;
	}

}
