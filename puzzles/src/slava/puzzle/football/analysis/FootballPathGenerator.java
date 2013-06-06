package slava.puzzle.football.analysis;

import slava.puzzle.football.model.FootballField;

public class FootballPathGenerator {
	FootballField field;
	int[] value;
	int size;

	int t;
	int[] place;	
	int[] wayCount;
	int[][] waysP;
	int[][] waysN;
	int[] way;
	
	int[][] exclusions;
	
	int tLimit;
	int[] solution;
	int solutionCount;
	
	public void setField(FootballField field) {
		this.field = field;
		for (int i = 0; i < field.getSize(); i++) {
			field.setValueAt(i, 0);
		}
		value = (int[])field.getValues().clone();
	}
	
	public void setPathLength(int length) {
		tLimit = length;
	}

	public void solve() {
		init();
		anal();
	}
	
	void init() {
		size = field.getSize();
		wayCount = new int[tLimit + 1];
		waysP = new int[tLimit + 1][field.getWidth() + field.getHeight()];
		waysN = new int[tLimit + 1][field.getWidth() + field.getHeight()];
		way = new int[tLimit + 1];
		place = new int[tLimit + 1];
		t = 0;
		place[0] = size - 1;
		exclusions = new int[size][field.getWidth()];
		solution = null;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == tLimit) return;
		int p = place[t];
		if(p == 0) return;
		for (int d = 0; d < 4; d++) {
			int q = 1;
			int pc = field.jump(p, d);
			while(pc >= 0) {
				if(exclusions[pc][q] == 0 && field.getValueAt(pc) == 0 && pc != size - 1 && q <= 6) {
					waysN[t][wayCount[t]] = q;
					waysP[t][wayCount[t]] = pc;
					wayCount[t]++;
				} 
				++q;
				pc = field.jump(pc, d);
			}
		}
		randomize();		
	}
	
	void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = wayCount[t] - 1; i >= 1; i--) {
			int j = (int)(Math.random() * (i + 1));
			if(i == j) continue;
			int k = waysP[t][i];
			waysP[t][i] = waysP[t][j];
			waysP[t][j] = k;
			k = waysN[t][i];
			waysN[t][i] = waysN[t][j];
			waysN[t][j] = k;
		}
	}
	
	void move() {
		int p = waysP[t][way[t]];
		int k = waysN[t][way[t]];
		place[t + 1] = p;
		field.setValueAt(p, k);
		p = place[t];
		for (int d = 0; d < 4; d++) {
			int q = 1;
			int pc = field.jump(p, d);
			while(pc >= 0) {
				exclusions[pc][q]++;
				++q;
				pc = field.jump(pc, d);
			}
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;		
		int p = place[t + 1];
		field.setValueAt(p, 0);
		p = place[t];
		for (int d = 0; d < 4; d++) {
			int q = 1;
			int pc = field.jump(p, d);
			while(pc >= 0) {
				exclusions[pc][q]--;
				++q;
				pc = field.jump(pc, d);
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
			if(t == tLimit && place[t] == 0) {
				solution = (int[])place.clone();
				++solutionCount;
				break;
			}
		}
	}
	
	public int[] getSolution() {
		return solution;
	}
	
}
