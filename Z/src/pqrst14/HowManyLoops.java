package pqrst14;

import com.slava.common.*;

public class HowManyLoops {
	RectangularField field;
	int size;

	int[] state;
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;

	public void setField(RectangularField field) {
		this.field = field;
		size = field.getSize();
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[size + 1];
		place = new int[size + 2];
		wayCount = new int[size + 2];
		way = new int[size + 2];
		ways = new int[size + 2][size];
		
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(isSolution() || t == size + 1) return;
		if(t == 0) {
			for (int i = 0; i < size; i++) {
				ways[t][i] = i;
			}
			wayCount[t] = size;
		} else {
			int p = place[t - 1];
			int dm = (t == 1) ? 1 : 4;
			for (int d = 0; d < dm; d++) {
				int q = field.jump(p, d);
				if(q < 0) continue;
				if(q < place[0]) continue;
				if(state[q] == 0 || (t >= 3 && q == place[0])) {
					ways[t][wayCount[t]] = q;
					wayCount[t]++;
				}
			}
		}
	}
	
	void move() {
		int p = ways[t][way[t]];
		place[t] = p;
		if(state[p] == 0) state[p] = t + 1;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = place[t];
		if(state[p] == t + 1) state[p] = 0;
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
			if(isSolution()) {
				++solutionCount;
				printSolution();
			}
		}
	}
	
	boolean isSolution() {
		return t > 2 && place[t - 1] == place[0];
	}	
	
	void printSolution() {
		System.out.println("solution");
		for (int i = 0; i < size; i++) {
			char c = state[i] == 0 ? '.' : (char)(96 + state[i]);
			System.out.print(" " + c);
			if(field.getX(i) == field.getWidth() - 1) System.out.println("");
		}
	}	

	public static void main(String[] args) {
		RectangularField field = new RectangularField();
		field.setSize(4, 7);
		HowManyLoops p = new HowManyLoops();
		p.setField(field);
		p.solve();
		System.out.println("Solution count=" + p.solutionCount);
	}

}

//1049
