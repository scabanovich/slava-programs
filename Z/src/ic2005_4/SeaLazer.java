package ic2005_4;

import com.slava.three_by_n.Field;

public class SeaLazer {
	//obstacle = 1
	// [wall orientation(0 perp.to x)][ray direction(0 - betw 0 and 1)] 
	static int[][] RAY_TRANSFORM = { {1,0,3,2},{3,2,1,0} };
	//obstacle = 0
	static int[][] SHIFT = { {0,2,2,0}, {1,1,3,3} };
	
	static int[] SHIP_COUNT = {0,4,3,2,1};
	
	Field field;

	int[] shipUsage;
	int[] pieces;
	int[] restriction;
	RayState[] rayState;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] waysP;
	int[][] waysL;
	
	public SeaLazer() {}
	
	public void setField(Field f) {
		field = f;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		shipUsage = new int[SHIP_COUNT.length];
		pieces = new int[field.getSize()];
		restriction = new int[field.getSize()];
		restriction[0] = 1;

		int tMax = 200;
		rayState = new RayState[tMax];
		for (int i = 0; i < rayState.length; i++) rayState[i] = new RayState();
		rayState[0].place = 0;
		rayState[0].direction = 0;
		rayState[0].borderOrientation = 0;

		wayCount = new int[tMax];
		way = new int[tMax];
		waysP = new int[tMax][40];
		waysL = new int[tMax][40];

		t = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(isFinished()) return;
			if(getOccupiedInnerPointCount() > 3) return;
		RayState r = rayState[t];
		int d = SHIFT[r.borderOrientation][r.direction];
		int q = field.jump(r.place, d);
		wayCount[t] = 1;
		waysP[t][0] = -1;
		waysL[t][0] = -1;
		if(restriction[q] > 0) return;
		for (int length = 1; length <= 4; length++) {
			if(shipUsage[length] == SHIP_COUNT[length]) continue;
			int pb = q;
			int pd = 1 - r.borderOrientation;
			for (int i = 1; i <= length; i++) {
				if(pb < 0 || restriction[pb] > 0) break;
				if(canAdd(pb, pd, length)) {
					waysP[t][wayCount[t]] = pb;
					waysL[t][wayCount[t]] = length;
					wayCount[t]++;
				}
				pb = field.jump(pb, pd + 2);
			}
		}
		if(t < 20) randomize();
	}
	
	int getOccupiedInnerPointCount() {
		int s = 0;
		for (int ix = 1; ix < field.getWidth() - 1; ix++) {
			for (int iy = 1; iy < field.getWidth() - 1; iy++) {
				if(pieces[field.getIndex(ix, iy)] == 1) ++s;
			}
		}
		return s;
	}
	
	void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = wayCount[t] - 1; i >= 1; i--) {
			int j = (int)(Math.random() * (i + 1));
			if(i == j) continue;
			flip(waysP[t], i, j);
			flip(waysL[t], i, j);
		}
	}
	void flip(int[] array, int i, int j) {
		int c = array[i];
		array[i] = array[j];
		array[j] = c;
	}
	
	boolean canAdd(int p, int d, int length) {
		for (int i = 0; i < length; i++) {
			if(p < 0 || restriction[p] > 0) return false;
			p = field.jump(p, d);
		}
		return true;
	}
	
	void move() {
		RayState r = rayState[t];
		int d = SHIFT[r.borderOrientation][r.direction];
		int q = field.jump(r.place, d);
		if(pieces[q] > 0) {
			rayState[t + 1].place = rayState[t].place;
			rayState[t + 1].direction = RAY_TRANSFORM[r.borderOrientation][r.direction];
		} else if(waysL[t][way[t]] == -1) {
			restriction[q]++;
			rayState[t + 1].place = q;
			rayState[t + 1].direction = rayState[t].direction;
		} else {
			int pb = waysP[t][way[t]];
			int length = waysL[t][way[t]];
			add(pb, 1 - rayState[t].borderOrientation, length);
			rayState[t + 1].place = rayState[t].place;
			rayState[t + 1].direction = RAY_TRANSFORM[r.borderOrientation][r.direction];
		}
		rayState[t + 1].borderOrientation = 1 - rayState[t].borderOrientation;
		++t;
		srch();
		way[t] = -1;
	}
	
	void add(int p, int d, int length) {
		shipUsage[length]++;
		int ix = field.getX(p);
		int iy = field.getY(p);
		int pa = p;
		for (int i = 0; i < length; i++) {
			pieces[pa] = 1;
			pa = field.jump(pa, d); 
		}
		int x1, x2, y1, y2;
		if(d == 0) {
			x1 = ix - 1;
			x2 = ix + length;
			y1 = iy - 1;
			y2 = iy + 1;
		} else {
			y1 = iy - 1;
			y2 = iy + length;
			x1 = ix - 1;
			x2 = ix + 1;
		}
		for (int xc = x1; xc <= x2; xc++) {
			for (int yc = y1; yc <= y2; yc++) {
				int q = field.getIndex(xc, yc);
				if(q >= 0) restriction[q]++;
			}
		}
	}
	
	void back() {
		--t;
		RayState r = rayState[t];
		int d = SHIFT[r.borderOrientation][r.direction];
		int q = field.jump(r.place, d);
		if(pieces[q] > 0 && waysL[t][way[t]] == -1) {
			//do nothing
		} else if(waysL[t][way[t]] == -1) {
			restriction[q]--;
		} else {
			int pb = waysP[t][way[t]];
			int length = waysL[t][way[t]];
			remove(pb, 1 - rayState[t].borderOrientation, length);
		}
	}

	void remove(int p, int d, int length) {
		shipUsage[length]--;
		int ix = field.getX(p);
		int iy = field.getY(p);
		int pa = p;
		for (int i = 0; i < length; i++) {
			pieces[pa] = 0;
			pa = field.jump(pa, d); 
		}
		int x1, x2, y1, y2;
		if(d == 0) {
			x1 = ix - 1;
			x2 = ix + length;
			y1 = iy - 1;
			y2 = iy + 1;
		} else {
			y1 = iy - 1;
			y2 = iy + length;
			x1 = ix - 1;
			x2 = ix + 1;
		}
		for (int xc = x1; xc <= x2; xc++) {
			for (int yc = y1; yc <= y2; yc++) {
				int q = field.getIndex(xc, yc);
				if(q >= 0) restriction[q]--;
			}
		}
	}
	
	void anal() {
		srch();
		way[t] = -1;
		int tm = 100;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
//			printSolution();
			if(isFinished() && t >= tm) {
				tm = t;
				System.out.println("t=" + t);
				printSolution();
			}
		}
	}
	
	boolean isFinished() {
		RayState r = rayState[t];
		int d = SHIFT[r.borderOrientation][r.direction];
		int q = field.jump(r.place, d);
		return q < 0;		
	}
	
	boolean isSolution() {
		for (int i = 0; i < shipUsage.length; i++) {
			if(shipUsage[i] < SHIP_COUNT[i]) return false;
		}
		return true;		
	}
	
	void printSolution() {
		for (int i = 0; i < field.getSize(); i++) {
			System.out.print(" " + pieces[i]);
			if(field.getX(i) == field.getWidth() - 1) System.out.println("");
		}
		System.out.println("");
	}

	public static void main(String[] args) {
		SeaLazer p = new SeaLazer();
		Field f = new Field();
		f.setSize(10, 10);
		p.setField(f);
		p.solve();
	}

}

class RayState {
	int place;
	int direction;
	int borderOrientation;
}

/*
t=151
 0 0 1 1 0 1 0 0 0 0
 0 0 0 0 0 0 0 0 0 0
 1 0 0 0 0 1 0 0 0 0
 1 0 0 0 0 0 0 0 0 0
 0 0 0 0 0 0 0 0 0 1
 1 0 0 0 0 0 0 0 0 1
 1 0 0 0 0 1 0 1 0 1
 1 0 0 0 0 0 0 0 0 1
 0 0 0 0 0 0 0 0 0 0
 0 0 1 1 1 0 1 1 0 0
*/