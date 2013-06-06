package champukr2013;

import champukr2013.DoublePentaminoFigures.Variant;

import com.slava.common.RectangularField;

public class DoublePentaminoPacking {
	Variant[][] variants;
	RectangularField f = new RectangularField();
	int[] form = {
		1,1,1,1,1,1,1,1,1,1,0,0,0,
		1,1,1,1,1,1,1,1,1,1,1,0,0,
		1,1,1,1,1,1,1,1,1,1,1,1,0,
		1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,
		0,1,1,1,1,1,1,1,1,1,1,1,0,
		0,0,1,1,1,1,1,1,1,1,1,0,0,
		0,0,0,1,1,1,1,1,1,1,0,0,0,
	};
	
	int[][] removed = {{7,8}};

	int[] state;
	int[] usage;
	int empty = 0;
	int used = 0;

	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] waysF, waysI;

	int treeCount;
	int solutionCount;

	public DoublePentaminoPacking() {
		f.setSize(13, 13);
		DoublePentaminoFigures fs = new DoublePentaminoFigures();
		fs.run();
		variants = fs.variants;
	}

	public void solve() {
		init();
		anal();
	}

	void init() {
		state = new int [f.getSize()];
		usage = new int[variants.length];
		
		place = new int[50];
		wayCount = new int[50];
		way = new int[50];
		waysF = new int[50][50];
		waysI = new int[50][50];
		
		t = 0;
		solutionCount = 0;
		treeCount = 0;
	}

	void srch() {
		wayCount[t] = 0;
		if(used == variants.length || !isAccepted()) return;
		place[t] = nextPlace();
		if(empty < 8) {
			waysF[t][wayCount[t]] = -1;
			wayCount[t]++;
		}
		for (int fg = 0; fg < variants.length; fg++) {
			for (int i = 0; i < variants[fg].length; i++) {
				Variant v = variants[fg][i];
				if(usage[v.index] > 0) continue;
				if(canPlace(v)) {
					waysF[t][wayCount[t]] = fg;
					waysI[t][wayCount[t]] = i;
					wayCount[t]++;
				}
			}
		}
	}

	boolean isAccepted() {
		for (int i = 0; i < removed.length; i++) {
			int ix = removed[i][0], iy = removed[i][1];
			int s00 = state[f.getIndex(ix, iy)];
			if(s00 == 0 || s00 == 20) continue;
			if(s00 != state[f.getIndex(ix + 1, iy)]) continue;
			if(s00 != state[f.getIndex(ix, iy + 1)]) continue;
			if(s00 != state[f.getIndex(ix + 1, iy + 1)]) continue;
			return false;
		}
		return true;
	}

	boolean canPlace(Variant v) {
		for (int k = 0; k < v.nodes.length; k++) {
			int dx = v.nodes[k][0], dy = v.nodes[k][1];
			int q = f.jumpXY(place[t], dx, dy);
			if(q < 0 || state[q] > 0 || form[q] == 0) return false;
		}
		return true;
	}

	int nextPlace() {
		int p = t == 0 ? 0 : place[t - 1] + 1;
		while(form[p] == 0 || state[p] > 0) {
			p++;
		}
		return p;
	}

	void move() {
		int p = place[t];
		int fg = waysF[t][way[t]];
		int i = waysI[t][way[t]];
		if(fg < 0) {
			state[p] = 20;
			empty++;
		} else {
			used++;
			Variant v = variants[fg][i];
			usage[v.index] = 1;
			for (int k = 0; k < v.nodes.length; k++) {
				int dx = v.nodes[k][0], dy = v.nodes[k][1];
				int q = f.jumpXY(p, dx, dy);
				state[q] = v.index + 1;
			}
		}
		++t;
		srch();
		way[t] = -1;
	}

	void back() {
		t--;
		int p = place[t];
		int fg = waysF[t][way[t]];
		int i = waysI[t][way[t]];
		if(fg < 0) {
			state[p] = 0;
			empty--;
		} else {
			used--;
			Variant v = variants[fg][i];
			usage[v.index] = 0;
			for (int k = 0; k < v.nodes.length; k++) {
				int dx = v.nodes[k][0], dy = v.nodes[k][1];
				int q = f.jumpXY(p, dx, dy);
				state[q] = 0;
			}
		}
	}

	void anal() {
		srch();
		way[t] = -1;
		int tm = 0;
		while(true) {
			while(way[t] >= wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(wayCount[t] == 0) {
				treeCount++;
//				if(treeCount >= 7000000) return;
//				if(solutionCount == 0 && treeCount % 10000000 == 0) printSolution();
			}
			if(t > tm) {
				System.out.println(t);
				tm = t;
			}
			if(isSolution()) {
				solutionCount++;
				printSolution();
//				return;
			}
		}
	}

	public boolean isSolution() {
		return used == variants.length && isAccepted();
	}

	void printSolution() {
		for (int i = 0; i < state.length; i++) {
			System.out.print(" " + (char)(96 + state[i]));
			if(f.isRightBorder(i)) System.out.println("");
		}
		System.out.println("");
	}
}

/**

 i i i i a a h h ` ` ` ` `
 i i i i a a h h h ` ` ` `
 i i i i a a h h h t ` ` `
 f f f f a a h h h g g ` `
 f f f f a a t k k g g g `
 f f j j a a k k k g g g g
 f f j j j j k k k k g g g
 e e j j j j t k k k l l t
 e e e e j j d d d l l l l
 e e e e d d d d d l l l l
 e e c c d d d d b b l l `
 c c c c c b b b b b ` ` `
 c c c c c b b b b b ` ` `


*/