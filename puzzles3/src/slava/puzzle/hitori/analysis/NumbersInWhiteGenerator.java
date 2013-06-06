package slava.puzzle.hitori.analysis;

import com.slava.common.RectangularField;

public class NumbersInWhiteGenerator {
	RectangularField field;
	int[] form;
	int colorCount;
	
	int[] state;
	int[][] usage; //[p][c]
	
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	int[] temp;
	
	int emptyCells;
	
	public NumbersInWhiteGenerator() {}
	
	public void setField(RectangularField f) {
		field = f;
	}
	
	public void setForm(int[] form) {
		this.form = form;
	}
	
	public void setColorCount(int c) {
		colorCount = c;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		emptyCells = 0;
		for (int i = 0; i < state.length; i++) {
			if(form[i] < 1) emptyCells++;
		}
		usage = new int[field.getSize()][colorCount];
		
		wayCount = new int[emptyCells + 1];
		way = new int[emptyCells + 1];
		ways = new int[emptyCells + 1][colorCount];
		place = new int[emptyCells + 1];
		temp = new int[colorCount];
		
		t = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(emptyCells == 0) return;
		int wcb = 100;
		for (int p = 0; p < state.length; p++) {
			if(form[p] == 1 || state[p] >= 0) continue;
			int wc = getWayCount(p);
			if(wc == 0) return;
			if(wc < wcb) {
				wcb = wc;
				for (int i = 0; i < wc; i++) ways[t][i] = temp[i];
				place[t] = p;
			}
		}
		if(wcb < 100) {
			wayCount[t] = wcb;
		}
		randomize();
	}
	
	void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = 0; i < wayCount[t]; i++) {
			int j = i + (int)(Math.random() * (wayCount[t] - i));
			if(j == i) continue;
			int c = ways[t][i];
			ways[t][i] = ways[t][j];
			ways[t][j] = c;
		}
	}
	
	int getWayCount(int p) {
		int wc = 0;
		for (int c = 0; c < colorCount; c++) {
			if(usage[p][c] == 0) {
				temp[wc] = c;
				wc++;
			}
		}		
		return wc;		
	}
	
	void move() {
		int p = place[t];
		int w = ways[t][way[t]];
		state[p] = w;
		emptyCells--;
		for (int d = 0; d < 4; d++) {
			int q = field.jump(p, d);
			while(q >= 0) {
				usage[q][w]++;
				q = field.jump(q, d);
			}
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = place[t];
		int w = ways[t][way[t]];
		state[p] = -1;
		emptyCells++;
		for (int d = 0; d < 4; d++) {
			int q = field.jump(p, d);
			while(q >= 0) {
				usage[q][w]--;
				q = field.jump(q, d);
			}
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
			if(emptyCells == 0) return;			
		}
		
	}
	
	public int[] getNumbers() {
		int[] numbers = (int[])state.clone();
		for (int i = 0; i < state.length; i++) numbers[i]++;
		return numbers;		
	}

}
