package slava.puzzles.seabattle.analysis;

import com.slava.common.RectangularField;

public class SeaBattleRandomFiller {
	RectangularField field;
	RectangularField octafield;
//	SeaBattlePuzzle puzzle;

	int[] state;
	int[] restrictions;
	int[] ships;
	int[] hUsage;
	int[] vUsage;
	

	int t;
	int[] wayCount;
	int[] way;
	int[][] waysP;
	int[][] waysD;
	int[][] waysS;
	
	int[] solution;
	
	public SeaBattleRandomFiller() {}

	public void setField(RectangularField field) {
		this.field = field;
		octafield = new RectangularField();
		octafield.setOrts(RectangularField.DIAGONAL_ORTS);
		octafield.setSize(field.getWidth(), field.getHeight());
	}
	
	public void setShips(int[] ships) {
		this.ships = (int[])ships.clone();
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.getSize()];
		restrictions = new int[field.getSize()];
		
		hUsage = new int[field.getHeight()];
		vUsage = new int[field.getWidth()];
		
		
		wayCount = new int[field.getSize() + 1];
		way = new int[22];
		waysP = new int[22][300];
		waysD = new int[22][300];
		waysS = new int[22][300];
		
		t = 0;
		solution = null;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(isCompleted()) return;
		int s = getShip();
		if(s >= 0) {
			getWaysForShip(s);
			randomize();
		}
	}
	
	void addWay(int p, int d, int s) {
		waysP[t][wayCount[t]] = p;
		waysD[t][wayCount[t]] = d;
		waysS[t][wayCount[t]] = s;
		wayCount[t]++;
	}
	
	int getShip() {
		for (int i = ships.length - 1; i >= 1; i--) {
			if(ships[i] > 0) return i;
		}
		return -1;
	}
	
	void getWaysForShip(int s) {
		for (int p = 0; p < field.getSize(); p++) {
			int dm = s > 1 ? 2 : 1;
			for (int d = 0; d < dm; d++) {
				if(canAdd(p, d, s)) addWay(p, d, s);
			}			
		}
	}
	
	void randomize() {
		if(wayCount[t] < 2) return;
		int wc = 4;
		if(wc > wayCount[t]) wc = wayCount[t];
		for (int i = 0; i < wc; i++) {
			int j = (int)(Math.random() * (wayCount[t] - i)) + i;
			if(j == i) continue;
			int p = waysP[t][i]; waysP[t][i] = waysP[t][j]; waysP[t][j] = p;
			int d = waysD[t][i]; waysD[t][i] = waysD[t][j]; waysD[t][j] = d;
			int s = waysS[t][i]; waysS[t][i] = waysS[t][j]; waysS[t][j] = s;
		}
		wayCount[t] = wc;
	}
	
	boolean canAdd(int p, int d, int s) {
		if(restrictions[p] > 0) return false;
		if(ships[s] == 0) return false;
		if(s == 1 && d != 0) return false;
		for (int i = 0; i < s; i++) {
			if(p < 0 || restrictions[p] > 0 || state[p] > 0) return false;
			p = field.jump(p, d);
		}		
		return true;
	}
	
	void move() {
		int p = waysP[t][way[t]];
		int d = waysD[t][way[t]];
		int s = waysS[t][way[t]];
		if(s == 0) {
			restrictions[p]++;
		} else {
			int q = p;
			ships[s]--;
			for (int i = 0; i < s; i++) {
				state[q] = s;
				hUsage[field.getY(q)]++;
				vUsage[field.getX(q)]++;
				for (int d2 = 0; d2 < 8; d2++) {
					int q2 = octafield.jump(q, d2);
					if(q2 >= 0) restrictions[q2]++;
				}
				q = field.jump(q, d);
			}
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = waysP[t][way[t]];
		int d = waysD[t][way[t]];
		int s = waysS[t][way[t]];
		if(s == 0) {
			restrictions[p]--;
		} else {
			int q = p;
			ships[s]++;
			for (int i = 0; i < s; i++) {
				state[q] = 0;
				hUsage[field.getY(q)]--;
				vUsage[field.getX(q)]--;
				for (int d2 = 0; d2 < 8; d2++) {
					int q2 = octafield.jump(q, d2);
					if(q2 >= 0) restrictions[q2]--;
				}
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
			if(isCompleted()) {
				solution = (int[])state.clone();
				return;
			}
		}		
	}
	
	boolean isCompleted() {
		for (int i = 1; i < ships.length; i++) {
			if(ships[i] > 0) return false;
		}
		return true;
	}
	
	int getShipCount() {
		int c = 0;
		for (int i = 1; i < ships.length; i++) {
			c += ships[i];
		}
		return c;
	}
	
	public int[] getSolution() {
		return solution;
	}
	
}
