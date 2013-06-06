package ic5_1;

public class Pentis {
	Figure[] figures;
	int width;
	int height;
	int size;
	int[] x,y;
	int[][] xy;
	int[][] jp;
	
	int[] state;
	int[] profile;
	
	int[] lostCellCount;
	int lostRows;
	int[] usedCellCount;
	int usedRows;
	int maxUsedRows;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] waysF;
	int[][] waysP;
	int solutionCount;

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		size = width * height;
		x = new int[size];
		y = new int[size];
		xy = new int[width][height];
		for (int i = 0; i < size; i++) {
			x[i] = (i % width);
			y[i] = (i / width);
			xy[x[i]][y[i]] = i;
		}
	}
	
	public void setFigures(Figure[] fs) {
		figures = fs;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[size];
		for (int i = 0; i < size; i++) state[i] = -1;
		lostCellCount = new int[height];
		lostRows = 0;
		usedCellCount = new int[height];
		usedRows = 0;
		profile = new int[width];
		wayCount = new int[size];
		way = new int[size];
		waysF = new int[size][100];
		waysP = new int[size][100];
		t = 0;
		maxUsedRows = 13;		
	}
	
	void srch() {
		wayCount[t] = 0;
		if(lostRows > 1) return;
		for (int fi = 0; fi < figures.length; fi++) {
			Figure f = figures[fi];
			if(f.setIndex != (t % 2)) continue;
			for (int ix = -f.minColumn; ix < width - f.maxColumn; ix++) {
				int iy = 0;
				for (int k = 0; k < f.profile.length; k++) {
					int iyk = profile[ix + f.profile[k][0]] - f.profile[k][1];
					if(iyk > iy) {
						iy = iyk;						
					}
				}
				boolean ok = true;
				for (int k = 0; k < f.profile.length && ok; k++) {
					if(iy + f.profile[k][1] + f.profile[k][2] > height) {
						ok = false;
					}
				}
				if(ok) {
					waysF[t][wayCount[t]] = fi;
					waysP[t][wayCount[t]] = xy[ix][iy];
					wayCount[t]++;
				}
			}
		}
	}
	
	void move() {
		int fi = waysF[t][way[t]];
		int p = waysP[t][way[t]];
		placeFigure(figures[fi], p);
		++t;
		srch();
		way[t] = -1;
	}
	
	void placeFigure(Figure f, int p) {
		int ix0 = x[p];
		for (int k = 0; k < f.profile.length; k++) {
			int ix = ix0 + f.profile[k][0];
			int iyMin = y[p] + f.profile[k][1];
			int iyMax = iyMin + f.profile[k][2];
			profile[ix] = iyMax;
			for (int iy = iyMin; iy < iyMax; iy++) {
				state[xy[ix][iy]] = t;
				usedCellCount[iy]++;
				if(usedCellCount[iy] == width) usedRows++;
			}
			int iy = iyMin - 1;
			while(iy >= 0 && state[xy[ix][iy]] == -1) {
				lostCellCount[iy]++;
				if(lostCellCount[iy] == 1) lostRows++;
				--iy;
			}
		}
	}
	
	void back() {
		--t;
		int fi = waysF[t][way[t]];
		int p = waysP[t][way[t]];
		removeFigure(figures[fi], p);
	}
	
	void removeFigure(Figure f, int p) {
		int ix0 = x[p];
		for (int k = 0; k < f.profile.length; k++) {
			int ix = ix0 + f.profile[k][0];
			int iyMin = y[p] + f.profile[k][1];
			int iyMax = iyMin + f.profile[k][2];
			for (int iy = iyMin; iy < iyMax; iy++) {
				state[xy[ix][iy]] = -1;
				if(usedCellCount[iy] == width) usedRows--;
				usedCellCount[iy]--;
			}
			int iy = iyMin - 1;
			while(iy >= 0 && state[xy[ix][iy]] == -1) {
				lostCellCount[iy]--;
				if(lostCellCount[iy] == 0) lostRows--;
				--iy;
			}
			int iyp = iyMin - 1;
			while(iyp >= 0 && state[xy[ix][iyp]] < 0) --iyp;
			profile[ix] = iyp + 1;
		}
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return; else back();
			}
			++way[t];
			move();
			if(maxUsedRows < usedRows) {
				maxUsedRows = usedRows;
				onSolutionFound();
			}
		}
	}
	
	void onSolutionFound() {
		printSolution();
	}
	
	void printSolution() {
		System.out.println("usedRows=" + maxUsedRows);
		for (int i = 0; i < size; i++) {
			char c = (char)(97 + state[i]);
			if(state[i] < 0) c = '*';
			System.out.print(" " + c);
			if(x[i] == width - 1) System.out.println("");
		}
		System.out.println("");
		for (int i = 0; i < t; i++) {
			if(i > 0) System.out.print(",");
			int fi = waysF[i][way[i]];
			int p = waysP[i][way[i]];
			char c = (char)(97 + x[p]);
			System.out.print(c);
			if(figures[fi].index > 0) System.out.print(figures[fi].index);
		}
	}
	
	static Figure[] FIGURES = new Figure[]{
		new Figure(new int[][]{{-1,0,1}, {0,-1,3}, {1,0,1}}, 1, 0),
		new Figure(new int[][]{{-1,0,1}, {0,-2,4}}, 0, 1),
		new Figure(new int[][]{{0,-2,4}, {1,0,1}}, 0, 2),
		new Figure(new int[][]{{-1,0,1}, {0,-1,4}}, 0, 3),
		new Figure(new int[][]{{0,-1,4}, {1,0,1}}, 0, 4),
		new Figure(new int[][]{{-1,0,1}, {0,-1,2}, {1,0,1}, {2,0,1}}, 0, 5),
		new Figure(new int[][]{{-2,0,1}, {-1,0,1}, {0,-1,2}, {1,0,1}}, 0, 6),
		new Figure(new int[][]{{-1,0,1}, {0,0,2}, {1,0,1}, {2,0,1}}, 0, 7),
		new Figure(new int[][]{{-2,0,1}, {-1,0,1}, {0,0,2}, {1,0,1}}, 0, 8)
	};

	public static void main(String[] args) {
		Pentis p = new Pentis();
		p.setSize(8, 16);
		p.setFigures(FIGURES);
		p.solve();
	}
}

class Figure {
	int[][] profile;
	int setIndex;
	int index;
	int minColumn;
	int maxColumn;
	
	public Figure(int[][] profile, int setIndex, int index) {
		this.profile = profile;
		this.setIndex = setIndex;
		this.index = index;
		minColumn = 10;
		maxColumn = -10;
		for (int i= 0; i < profile.length; i++) {
			if(profile[i][0] < minColumn) minColumn = profile[i][0];
			if(profile[i][0] > maxColumn) maxColumn = profile[i][0];
		}
	}

}

//e7,c,f3,d,a2,b,h3,e,g2,c,f3,d,a2,b,h3,e,g2,c,h3,f,a2,d,g6,b