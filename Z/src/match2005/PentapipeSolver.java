package match2005;

public class PentapipeSolver {
	PentapipeField field;
	PentapipeFigure[] figures;
	
	int[] state;
	int[] restriction;
	int[] figureUsage;
	int freeFigureCount;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] waysP;
	int[][] waysF;

	public PentapipeSolver() {}
	
	public void setFiled(PentapipeField f) {
		field = f;
	}
	
	public void setFigures(PentapipeFigure[] figures) {
		this.figures = figures;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		restriction = new int[field.getSize()];
		figureUsage = new int[12];
		wayCount = new int[30];
		way = new int[30];
		waysP = new int[30][200];
		waysF = new int[30][200];
		t = 0;
		freeFigureCount = 12;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(freeFigureCount == 0) return;
		if(getFreeArea() < freeFigureCount * 5) return;
		if(t > 0 && freeFigureCount > estimate()) return;
		if(t == 0) {
			PentapipeFigure f = figures[0];
			for (int p = 3; p < field.getSize(); p++) {
				if(canPut(f, p)) {
					waysP[t][wayCount[t]] = p;
					waysF[t][wayCount[t]] = 0;
					wayCount[t]++;
				}
			}
		} else {
			int q = getPoint();
			if(q < 0) return;
			for (int fi = 0; fi < figures.length; fi++) {
				PentapipeFigure f = figures[fi];
				if(figureUsage[f.index] > 0) continue;
				for (int p = 0; p < field.getSize(); p++) {
					if(!canPut(f, p)) continue;
					if(!includes(f, p, q)) continue;
					waysP[t][wayCount[t]] = p;
					waysF[t][wayCount[t]] = fi;
					wayCount[t]++;
				}
			}
			waysP[t][wayCount[t]] = q;
			waysF[t][wayCount[t]] = -1;
			wayCount[t]++;
		}
	}
	
	int getFreeArea() {
		int s = 0;
		for (int p = 0; p < field.getSize(); p++) {
			if(state[p] < 0 && restriction[p] == 0) s++;
		}
		return s;
	}
	
	boolean canPut(PentapipeFigure f, int p) {
		for (int i = 0; i < f.points.length; i++) {
			int dx = f.points[i][0];
			int dy = f.points[i][1];
			int q = field.jumpXY(p, dx, dy);
			if(q < 0 || state[q] >= 0 || restriction[q] > 0) return false;
		}
		for (int i = 0; i < f.shades.length; i++) {
			int dx = f.shades[i][0];
			int dy = f.shades[i][1];
			int q = field.jumpXY(p, dx, dy);
			if(q >= 0 && state[q] >= 0) return false;
		}
		return true;
	}
	
	int[] estimateField;
	int[] stack;
	
	int estimate() {
		if(stack == null) {
			stack = new int[field.getSize()];
			estimateField = new int[field.getSize()];
		} else {
			for (int i = 0; i < field.getSize(); i++) estimateField[i] = 0;
		}
		int fgs = 0;
		for (int p = 0; p < field.getSize(); p++) {
			if(state[p] >= 0 || restriction[p] > 0 || estimateField[p] > 0) continue;
			int v = 1;
			int c = 0;
			boolean connected = false;
			stack[0] = p;
			estimateField[p] = 1;
			while(c < v) {
				int q = stack[c];
				for (int d = 0; d < 4; d++) {
					int n = field.jump(q, d);
					if(n < 0 || estimateField[n] > 0) continue;
					if(state[n] >= 0) {
						connected = true;
					} else if(restriction[n] == 0) {
						stack[v] = n;
						estimateField[n] = 1;
						++v;
					}
				}
				++c;
			}
			if(connected) fgs += (v / 5);
		}
		return fgs;
	}
	
	int getPoint() {
		for (int p = 0; p < field.getSize(); p++) {
			if(state[p] < 0) continue;
			for (int d = 0; d < 4; d++) {
				int q = field.jump(p, d);
				if(q >= 0 && state[q] < 0 && restriction[q] == 0) return q;
			}
		}
		return -1;
	}
	
	boolean includes(PentapipeFigure f, int p, int q) {
		for (int i = 0; i < f.points.length; i++) {
			int dx = f.points[i][0];
			int dy = f.points[i][1];
			if(q == field.jumpXY(p, dx, dy)) return true;
		}
		return false;
	}
	
	void move() {
		int i = waysF[t][way[t]]; 
		int p = waysP[t][way[t]];
		if(i < 0) {
			restriction[p]++;
		} else {
			PentapipeFigure f = figures[i];
			put(f, p);
		}	
		++t;
		srch();
		way[t] = -1;
	}
	
	void put(PentapipeFigure f, int p) {
		freeFigureCount--;
		figureUsage[f.index]++;
		for (int i = 0; i < f.points.length; i++) {
			int dx = f.points[i][0];
			int dy = f.points[i][1];
			int q = field.jumpXY(p, dx, dy);
			state[q] = f.index;
			restriction[q]++;
		}
		for (int i = 0; i < f.shades.length; i++) {
			int dx = f.shades[i][0];
			int dy = f.shades[i][1];
			int q = field.jumpXY(p, dx, dy);
			if(q < 0) continue;
			restriction[q]++;
		}
	}
	
	void back() {
		--t;
		int i = waysF[t][way[t]]; 
		int p = waysP[t][way[t]];
		if(i < 0) {
			restriction[p]--;
		} else {
			PentapipeFigure f = figures[i];
			remove(f, p);
		}	
	}
	
	void remove(PentapipeFigure f, int p) {
		freeFigureCount++;
		figureUsage[f.index]--;
		for (int i = 0; i < f.points.length; i++) {
			int dx = f.points[i][0];
			int dy = f.points[i][1];
			int q = field.jumpXY(p, dx, dy);
			state[q] = -1;
			restriction[q]--;
		}
		for (int i = 0; i < f.shades.length; i++) {
			int dx = f.shades[i][0];
			int dy = f.shades[i][1];
			int q = field.jumpXY(p, dx, dy);
			if(q < 0) continue;
			restriction[q]--;
		}
	}
	
	void anal() {
		srch();
		way[t] = -1;
		int fm = 12;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(freeFigureCount < fm) {
				fm = freeFigureCount;
				System.out.println(fm);
			}
			if(freeFigureCount == 0) {
				printSolution();
			}
		}
	}
	
	void printSolution() {
		System.out.println("Solution");
		for (int i = 0; i < field.getSize(); i++) {
			char c = state[i] < 0 ? '+' : PentapipeFigures.letters[state[i]];
			System.out.print(" " + c);
			if(field.getX(i) == field.getWidth() - 1) System.out.println("");
		}
	}
	
}
