package diogen2006;

import com.slava.common.CellSet;
import com.slava.common.RectangularField;

public class SeaPacker {
	RectangularField field;
	// [length] -> number of ships of that length
	int[] ships = {4,3,3,2,2,2,1,1,1,1};
	
	boolean doNotPutOneNearOneOrTwo = true;
	CellSet filter;
	int packLimit = -1;
	
	CellSet state;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] waysP;
	int[][] waysD;
	
	int packCount;
	
	public SeaPacker() {}
	
	public void setField(RectangularField f) {
		field = f;
	}
	
	public void setFilter(CellSet filter) {
		this.filter = filter;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new CellSet(field.getSize());
		
		wayCount = new int[ships.length + 1];
		way = new int[ships.length + 1];
		waysP = new int[ships.length + 1][300];
		waysD = new int[ships.length + 1][300];
		t = 0;
		packCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == ships.length) return;
		int size = ships[t];
		int p0 = 0;
		if(t > 0 && ships[t - 1] == size) p0 = waysP[t - 1][way[t - 1]] + 1;
		for (int p = p0; p < field.getSize(); p++) {
			for (int d = 0; d < 2; d++) {
				if(canAdd(p, d, size)) {
					waysP[t][wayCount[t]] = p;
					waysD[t][wayCount[t]] = d;
					wayCount[t]++;
				}
			}
		}
//		randomize();
	}
	
	boolean canAdd(int p, int d, int s) {
		if(s == 1 && doNotPutOneNearOneOrTwo) {
			int n = 0;
			for (int d1 = 0; d1 < 4; d1++) {
				int q = field.jump(p, d1);
				if(q <= 0) continue;
				if(state.value(q) == 1 || state.value(q) == 2) {
					return false;
				}
				n++;
			}
			if(n == 0) return false;
		}
		for (int i = 0; i < s; i++) {
			if(p < 0 || state.value(p) > 0) return false;
			if(filter != null && filter.value(p) <= 0) return false;
			p = field.jump(p, d);
		}
		return true;
	}
	
	void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = 0; i < wayCount[t]; i++) {
			int j = i + (int)((wayCount[t] - i) * Math.random());
			if(j == i) continue;
			int c = waysP[t][i];
			waysP[t][i] = waysP[t][j];
			waysP[t][j] = c;
			c = waysD[t][i];
			waysD[t][i] = waysD[t][j];
			waysD[t][j] = c;
		}		
	}
	
	void move() {
		int s = ships[t];
		int p = waysP[t][way[t]];
		int d = waysD[t][way[t]];
		add(p, d, s);
		++t;
		srch();
		way[t] = -1;
	}
	
	void add(int p, int d, int s) {
		for (int i = 0; i < s; i++) {
			state.add(p, ships[t]);
			p = field.jump(p, d);
		}
	}
	
	void back() {
		--t;
		int s = ships[t];
		int p = waysP[t][way[t]];
		int d = waysD[t][way[t]];
		remove(p, d, s);
	}
	
	void remove(int p, int d, int s) {
		for (int i = 0; i < s; i++) {
			state.remove(p, ships[t]);
			p = field.jump(p, d);
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
			way[t]++;
			move();
			if(t == ships.length && isPacking()) {
				packCount++;
				if(packCount % 1000000 == 0) System.out.println(packCount);
				if(onPackFound()) return;
			}
		}
	}
	
	boolean isPacking() {
		return true;
	}
	
	boolean onPackFound() {
		if(packLimit > 0 && packCount > packLimit) return true;
		return false;
	}
	
	void printPack() {
		for (int p = 0; p < field.getSize(); p++) {
			System.out.print(" " + state.value(p));
			if(field.isRightBorder(p)) System.out.println("");
		}
		System.out.println();
	}

	public static void main(String[] args) {
		RectangularField f = new RectangularField();
		f.setSize(6, 4);
		SeaPacker g = new SeaPackerA();
		g.setField(f);
		g.solve();
		System.out.println("-->" + g.packCount);
	}

}

class SeaPackerA extends SeaPacker {
	int min = 20;
	
	int[] temp;
	int[] stack;
	
	void init() {
		super.init();
		temp = new int[field.getSize()];
		stack = new int[field.getSize()];
	}

	boolean isPacking() {
		return isConnected();
	}
	
	boolean onPackFound() {
		SeaPacker b = new SeaPacker();
		b.setField(field);
		b.setFilter(state);
		b.doNotPutOneNearOneOrTwo = false;
		b.ships = new int[]{4,3,3,2,2,2};
		b.packLimit = min;
		b.solve();
		if(b.packCount > 0 && b.packCount < min) {
			min = b.packCount;
			System.out.println("min=" + min);
			if(min == 1) {
				printPack();
				return true;
			}
		}
		return false;
	}
	
	boolean isConnected() {
		int p0 = waysP[0][way[0]];
		temp[p0] = 1;
		int v = 1;
		int c = 0;
		stack[0] = p0;
		while(c < v) {
			int p = stack[c];
			for (int d = 0; d < 4; d++) {
				int q = field.jump(p, d);
				if(q < 0 || temp[q] > 0 || state.value(q) <= 0) continue;
				temp[q] = 1;
				stack[v] = q;
				v++;
			}
			c++;
		}
		int sq = 0;
		for (int i = 0; i < ships.length; i++) sq += ships[i];
		for (int i = 0; i < v; i++) temp[stack[i]] = 0;
		return v == sq;
	}

}

/**
min=1
 2 4 4 4 4 3 0 3 1
 2 0 2 2 0 3 1 3 0
 0 2 2 0 1 3 0 3 1
min=1
 1 4 0 3 3 3 3
 0 4 2 2 0 1 3
 1 4 0 2 2 0 3
 0 4 1 0 2 2 0
min = 2
 4 4 4 4
 0 1 0 1
 3 3 3 0
 1 0 1 3
 0 2 0 3
 2 2 2 3
 2 0 2 0

*/