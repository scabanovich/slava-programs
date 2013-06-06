package pqrst14;

import com.slava.common.RectangularField;

public class NoTouching {
	RectangularField field;
	int size;
	int[] filter;
	
	int[] state;
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	int pieceCount;
	int emptyCellCount;
	
	int pieceCountMinimum = 8;
	
	public void setField(RectangularField field) {
		this.field = field;
		size = field.getSize();
	}
	
	public void setFilter(int[] filter) {
		this.filter = filter;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[size];
		emptyCellCount = size;
		for (int i = 0; i < size; i++) {
			if(filter[i] == 0) {
				state[i] = 100;
				emptyCellCount--;
			}
		}
		place = new int[size];
		wayCount = new int[size + 1];
		way = new int[size + 1];
		ways = new int[size + 1][size];
		next.ws = new int[size];
		
		t = 0;
		pieceCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(isSolution()) return;
		if(pieceCount >= pieceCountMinimum) return;
		getNewPlace();
		if(next.wc == 0) return;
		place[t] = next.p;
		wayCount[t] = next.wc;
		for (int i = 0; i < next.wc; i++) ways[t][i] = next.ws[i];		
	}
	
	NextPlace next = new NextPlace();
	
	void getNewPlace() {
		next.wc = 0;
		next.p = (t == 0) ? 0 : place[t - 1] + 1;
		while(next.p < size && state[next.p] > 0) next.p++;
		if(next.p == size) return;
		if(next.p == 17) {
			int y = 0; y++;
		}
		if(canLeaveEmpty(next.p)) {
			next.ws[next.wc] = 0;
			next.wc++;
		} else {
//			System.out.println(next.p);
//			System.out.println("-->");
		}
		
		int d = getMaxDimension(next.p);
		for (int i = d; i >= 1; i--) { //no single-unit squares
			next.ws[next.wc] = i;
			next.wc++;
		}
	}
	
	class NextPlace {
		int p;
		int[] ws;
		int wc;
	}
	
	int getMaxDimension(int p) {
		if(state[p] != 0) return 0;
		int d = 2;
		while(true) {
			for (int dy = 0; dy < d; dy++) {
				int q = field.jumpXY(p, d - 1, dy);
				if(q < 0 || state[q] > 0) return d - 1;
			}
			for (int dx = 0; dx < d; dx++) {
				int q = field.jumpXY(p, dx, d - 1);
				if(q < 0 || state[q] > 0) return d - 1;
			}
			++d;
		}
	}
	
	boolean canLeaveEmpty(int p) {
		int pb = field.jumpXY(p, -1, -1);
		if(pb >= 0 && state[pb] == 0) return false;
		int pc = field.jumpXY(p, 0, -1);
		if(pc >= 0 && state[pc] == 0 && !hasOneMoreNb(pc, p, dsA)) return false;
		pc = field.jumpXY(p, -1, 0);
		if(pc >= 0 && state[pc] == 0 && !hasOneMoreNb(pc, p, dsB)) return false;
		return true;
	}
	
	int[][] dsA = new int[][]{{1,1}};
	int[][] dsB = new int[][]{{1,1},{0,1},{-1,1}};
	
	boolean hasOneMoreNb(int q, int pRef, int[][] ds) {
		for (int i = 0; i < ds.length; i++) {
			int qq = field.jumpXY(q, ds[i][0], ds[i][1]);
			if(qq >= 0 && state[qq] == 0 && qq != pRef) return true;
		}
		return false;
	}
	
	void move() {
		int d = ways[t][way[t]] - 1;
		int p = place[t];
		if(d >= 0) {
			pieceCount++;
			for (int dx = -1; dx <= d + 1; dx++) {
				for (int dy = -1; dy <= d + 1; dy++) {
					int q = field.jumpXY(p, dx, dy);
					if(q < 0) continue;
					int delta = (dx >= 0 && dx <= d && dy >= 0 && dy <= d) ? 10 : 1;
					if(state[q] == 0) emptyCellCount--;
					state[q] += delta;
				}
			}
		}		
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int d = ways[t][way[t]] - 1;
		int p = place[t];
		if(d >= 0) {
			pieceCount--;
			for (int dx = -1; dx <= d + 1; dx++) {
				for (int dy = -1; dy <= d + 1; dy++) {
					int q = field.jumpXY(p, dx, dy);
					if(q < 0) continue;
					int delta = (dx >= 0 && dx <= d && dy >= 0 && dy <= d) ? 10 : 1;
					state[q] -= delta;
					if(state[q] == 0) emptyCellCount++;
				}
			}
		}		
	}
	
	void anal() {
		srch();
		way[t] = -1;
//		int tm = 20;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
//			if(t > tm) {
//				tm = t;
//				printSolution();
//			}
			if(isSolution() && pieceCount <= pieceCountMinimum) {
				pieceCountMinimum = pieceCount;
				printSolution();
			}
		}
	}
	
	boolean isSolution() {
		return emptyCellCount == 0;
	}	
	
	void printSolution() {
//		System.out.println("Piece Count=" + pieceCountMinimum);
//		for (int i = 0; i < size; i++) {
//			char c = ' ';
//			if(state[i] >= 100) c = ' ';
//			else if(state[i] >= 10) c = 'x';
//			else if(state[i] > 0) c = '*';
//			else c = '+';
//			System.out.print(" " + c);
//			if(field.x(i) == field.getWidth() - 1) System.out.println("");
//		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < t; i++) {
			int p = place[i];
			int d = ways[i][way[i]];
			if(d <= 0) continue;
			int ix = field.getX(p);
			int iy = field.getY(p);
			char cx = (char)(65 + ix);
			String cy = "" + (iy + 1);
			if(sb.length() > 0) sb.append(",");
			sb.append(cx).append(cy).append('-').append(d);
		}			
		System.out.println("" + pieceCount + ":" + sb.toString());
	}	

}

/*
8:B2-5,L2-5,H4-3,C9-3,H9-7,Q10-1,B13-4,Q13-1*/
