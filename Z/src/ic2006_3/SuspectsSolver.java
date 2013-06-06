package ic2006_3;

import com.slava.common.RectangularField;

public class SuspectsSolver {
	static int POLICEMAN = 0;
	static int SUSPECT = 1;

	RectangularField field;
	
	int[] form;
	int personCount;
	int maxPolicemenCount;
	int maxSuspectCount;
	int[] places;
	int[][] distances;

	int[] state;
	int policemenCount;
	int suspectCount;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;
	
	public SuspectsSolver() {}
	
	public void setField(RectangularField f) {
		field = f;
	}
	
	public void setForm(int[] f) {
		form = f;
		personCount = 0;
		for (int i = 0; i < form.length; i++) {
			if(form[i] == 1) {
				personCount++;
			}
		}
		maxPolicemenCount = personCount / 2;
		maxSuspectCount = personCount - maxPolicemenCount;
		
		places = new int[personCount];
		int c = 0;
		for (int i = 0; i < form.length; i++) {
			if(form[i] == 1) {
				places[c] = i;
				c++;
			}
		}
		
		distances = new int[personCount][personCount];
		for (int i = 0; i < personCount; i++) {
			int p = places[i];
			int ix = field.getX(p);
			int iy = field.getY(p);
			for (int j = 0; j < personCount; j++) {
				int q = places[j];
				int jx = field.getX(q);
				int jy = field.getY(q);
				distances[i][j] = (ix - jx) * (ix - jx) + (iy - jy) * (iy - jy);
			}
		}
	}

	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		for (int p = 0; p < state.length; p++) state[p] = -1;
		wayCount = new int[500];
		way = new int[500];
		ways = new int[500][16];
		t = 0;
		solutionCount = 0;
		policemenCount = 0;
		suspectCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == personCount) return;
		if(policemenCount < maxPolicemenCount) {
			ways[t][wayCount[t]] = POLICEMAN;
			wayCount[t]++;
		}
		if(suspectCount < maxSuspectCount) {
			ways[t][wayCount[t]] = SUSPECT;
			wayCount[t]++;
		}
	}
	
	void move() {
		int i = ways[t][way[t]];
		int p = places[t];
		state[p] = i;
		if(i == POLICEMAN) {
			policemenCount++;
		} else {
			suspectCount++;
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int i = ways[t][way[t]];
		int p = places[t];
		state[p] = -1;
		if(i == POLICEMAN) {
			policemenCount--;
		} else {
			suspectCount--;
		}
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) {
					return;
				}
				back();
			}
			way[t]++;
			move();
			if(t == personCount && checkSuspects()) {
				solutionCount++;
				printSolution();
			}
		}
	}
	
	boolean checkSuspects() {
		for (int i = 0; i < personCount; i++) {
			int p = places[i];
			if(state[p] != SUSPECT) continue;
			int n = 0;
			for (int j = 0; j < personCount; j++) {
				int q = places[j];
				if(state[q] != POLICEMAN) continue;
				if(distances[i][j] <= 9) ++n;
			}
			if(n != 2) return false;
		}
		return true;
	}
	
	void printSolution() {
		System.out.println("Solution " + solutionCount);
		for (int p = 0; p < field.getSize(); p++) {
			String ch = (state[p] >= 0) ? "" + state[p] : "-";
			System.out.print(" " + ch);
			if(field.isRightBorder(p)) System.out.println("");
		}
		System.out.println("");
	}
}
