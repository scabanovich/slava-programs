package com.slava.packedsea;

import com.slava.common.CellSet;
import com.slava.common.RectangularField;
import com.slava.common.TwoDimField;

public class PackedSeaSolver {
	RectangularField field;
	TwoDimField octalfield;
	int[] shipCount = {0,4,3,2,1};
	ShipPosition[] ships;
	int shipTotal;
	
	int solutionLimit;
	
	CellSet state;
	CellSet restriction;
	
	int t;
	int[] shipSize; // t-array, size of ship to be handled at t
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;
	int[] solution;

	public PackedSeaSolver() {}

	public void setField(RectangularField f) {
		field = f;
		octalfield = new TwoDimField();
		octalfield.setOrts(TwoDimField.DIAGONAL_ORTS);
		octalfield.setSize(f.getWidth(), f.getHeight());		
	}
	
	public void setShipCount(int[] s) {
		shipCount = s;
	}
	
	public void setShips(ShipPosition[] ships) {
		this.ships = ships;
	}
	
	public void setSolutionLimit(int s) {
		solutionLimit = s;
	}

	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new CellSet(field.getSize());
		restriction = new CellSet(field.getSize());
		
		shipTotal = 0;
		for (int i = 0; i < shipCount.length; i++) shipTotal += shipCount[i];
		shipSize = new int [shipTotal];
		int j = 0;
		for (int i = shipCount.length - 1; i >= 1; i--) {
			for (int k = 0; k < shipCount[i]; k++) {
				shipSize[j] = i;
				j++;
			}
		}
		
		wayCount = new int[shipTotal + 1];
		way = new int[shipTotal + 1];
		ways = new int[shipTotal + 1][100];
		
		t = 0;
		solutionCount = 0;
		solution = null;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == shipTotal) return;
		int size = shipSize[t];
		int im = 0;
		if(t > 0 && shipSize[t - 1] == size) im = ways[t - 1][way[t - 1]];
		for (int i = im; i < ships.length; i++) {
			if(ships[i].getSize() != size) continue;
			if(canAdd(ships[i].getPlace(), ships[i].getDirection(), ships[i].getSize())) {
				ways[t][wayCount[t]] = i;
				wayCount[t]++;
			}
		}
	}
	
	boolean canAdd(int p, int d, int s) {
		for (int i = 0; i < s; i++) {
			if(p < 0 || state.value(p) > 0 || restriction.value(p) > 0) return false;
			p = field.jump(p, d);
		}
		return true;
	}
	
	void move() {
		int i = ways[t][way[t]];
		add(ships[i].getPlace(), ships[i].getDirection(), ships[i].getSize());
		++t;
		srch();
		way[t] = -1;
	}
	
	void add(int p, int d, int s) {
		for (int i = 0; i < s; i++) {
			state.add(p);
			for (int d2 = 0; d2 < 8; d2++) {
				int q = octalfield.jump(p, d2);
				if(q >= 0) restriction.add(q);
			}
			p = field.jump(p, d);
		}
	}
	
	void back() {
		--t;
		int i = ways[t][way[t]];
		remove(ships[i].getPlace(), ships[i].getDirection(), ships[i].getSize());
	}
	
	void remove(int p, int d, int s) {
		for (int i = 0; i < s; i++) {
			state.remove(p);
			for (int d2 = 0; d2 < 8; d2++) {
				int q = octalfield.jump(p, d2);
				if(q >= 0) restriction.remove(q);
			}
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
			if(t == shipTotal) {
				solutionCount++;
				if(solutionLimit > 0 && solutionCount > solutionLimit) return;
				if(solutionCount == 1) {
					solution = new int[field.getSize()];
					for (int p = 0; p < solution.length; p++) {
						solution[p] = state.value(p);
					}
				}
			}
		}
	}
	
	void printSolution() {
		for (int p = 0; p < solution.length; p++) {
			char ch = solution[p] <= 0 ? '+' : 'x';
			System.out.print(" " + ch);
			if(field.isRightBorder(p)) System.out.println("");
		}
		System.out.println("");
	}

}
