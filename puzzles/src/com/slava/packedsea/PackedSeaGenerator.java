package com.slava.packedsea;

import com.slava.common.CellSet;
import com.slava.common.RectangularField;

public class PackedSeaGenerator {
	RectangularField field;
	PackedSeaSolver solver = new PackedSeaSolver();
	// [length] -> number of ships of that length
	int[] shipCount = {0,4,3,2,1};

	int excessSingleCount = 0;
	
	//computed automatically
	int[] grossShipCount;
	int grossShipTotalCount;
	int lengthyShipCount;
	
	ShipPosition[] current;
	CellSet state;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] waysP;
	int[][] waysD;
	
	ShipPosition[] solution;
	int packCount;
	
	public PackedSeaGenerator() {}
	
	public void setField(RectangularField f) {
		field = f;
		solver.setField(f);
	}
	
	public void setShipCount(int[] s) {
		shipCount = s;
		solver.setShipCount(s);
	}
	
	public void generate() {
		init();
		anal();
	}
	
	void init() {
		state = new CellSet(field.getSize());
		initGrossShips();
		
		wayCount = new int[lengthyShipCount + 1];
		way = new int[lengthyShipCount + 1];
		waysP = new int[lengthyShipCount + 1][300];
		waysD = new int[lengthyShipCount + 1][300];
		t = 0;
		solution = null;
	}
	
	void initGrossShips() {
		int s = 0;
		for (int i = 1; i < shipCount.length; i++) s += i * shipCount[i];
		int factor = (field.getSize() - excessSingleCount) / s;
		
		if(factor > 3) factor = 1;
		
		grossShipCount = new int[shipCount.length];
		for (int i = 1; i < shipCount.length; i++) {
			grossShipCount[i] = factor * shipCount[i];
		}
		int leftover = field.getSize() - factor * s;
		if(excessSingleCount > leftover) excessSingleCount = leftover;
		grossShipCount[1] += excessSingleCount;
		leftover -= excessSingleCount;
		while(leftover > 0) {
			for (int i = shipCount.length - 1; i >= 1; i--) {
				if(i > leftover) continue;
				grossShipCount[i]++;
				leftover -= i;
			}
		}
		grossShipTotalCount = 0;
		for (int i = 1; i < grossShipCount.length; i++) {
			grossShipTotalCount += grossShipCount[i];
		}
		current = new ShipPosition[grossShipTotalCount];
		int j = 0;
		for (int i = grossShipCount.length - 1; i >= 1; i--) {
			for (int k = 0; k < grossShipCount[i]; k++) {
				current[j] = new ShipPosition(i, -1, -1);
				j++;
			}
		}
		lengthyShipCount = 0;
		for (int i = 2; i < grossShipCount.length; i++) {
			lengthyShipCount += grossShipCount[i];
		}
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == lengthyShipCount) return;
		int size = current[t].getSize();
		for (int p = 0; p < field.getSize(); p++) {
			for (int d = 0; d < 2; d++) {
				if(canAdd(p, d, size)) {
					waysP[t][wayCount[t]] = p;
					waysD[t][wayCount[t]] = d;
					wayCount[t]++;
				}
			}
		}
		randomize();
	}
	
	boolean canAdd(int p, int d, int s) {
		for (int i = 0; i < s; i++) {
			if(p < 0 || state.value(p) > 0) return false;
			p = field.jump(p, d);
		}
		return true;
	}
	
	void randomize() {
		if(wayCount[t] < 2) return;
		int max = 2;
		if(wayCount[t] < max) max = wayCount[t];
		for (int i = 0; i < max; i++) {
			int j = i + (int)((wayCount[t] - i) * Math.random());
			if(j == i) continue;
			int c = waysP[t][i];
			waysP[t][i] = waysP[t][j];
			waysP[t][j] = c;
			c = waysD[t][i];
			waysD[t][i] = waysD[t][j];
			waysD[t][j] = c;
		}		
		wayCount[t] = max;
	}
	
	void move() {
		int s = current[t].getSize();
		int p = waysP[t][way[t]];
		int d = waysD[t][way[t]];
		add(p, d, s);
		++t;
		srch();
		way[t] = -1;
	}
	
	void add(int p, int d, int s) {
		current[t].setPlace(p);
		current[t].setDirection(d);
		for (int i = 0; i < s; i++) {
			state.add(p);
			p = field.jump(p, d);
		}
	}
	
	void back() {
		--t;
		int s = current[t].getSize();
		int p = waysP[t][way[t]];
		int d = waysD[t][way[t]];
		remove(p, d, s);
	}
	
	void remove(int p, int d, int s) {
		current[t].setPlace(-1);
		current[t].setDirection(-1);
		for (int i = 0; i < s; i++) {
			state.remove(p);
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
			if(t == lengthyShipCount) {
				packCount++;
				if(packCount % 10000 == 0) System.out.println(packCount);
				if(onPackFound()) return;
			}
		}
	}
	
	int min = 10000;

	boolean onPackFound() {
		buildSolution();
		solver.setShips(solution);
		solver.solve();
		if(solver.solutionCount > 0 && solver.solutionCount < min) {
			min = solver.solutionCount;
			solver.setSolutionLimit(min);
			System.out.println("--->" + solver.solutionCount);
		}
		if(solver.solutionCount == 1) {
///			printPack(solution);
			printVisualState(getVisualState());
			solver.printSolution();
			return true;
		}
		return false;
	}
	
	void buildSolution() {
		solution = ShipPosition.copy(current);
		int j = lengthyShipCount;
		for (int p = 0; p < field.getSize(); p++) {
			if(state.value(p) <= 0) {
				solution[j].setPlace(p);
				solution[j].setDirection(0);
				j++;
			}
		}
	}
	
	char[] VISUALS = {'>', 'v', '<', '^', '-', '|', 'o'};
	int[] getVisualState() {
		int[] s = new int[field.getSize()];
		for (int i = 0; i < solution.length; i++) {
			ShipPosition pos = solution[i];
			int p = pos.getPlace();
			int d = pos.getDirection();
			int l = pos.getSize();
			if(l == 1) {
				s[p] = 6;
			} else {
				s[p] = (d == 0) ? 2 : 3;
				for (int k = 0; k < l - 2; k++) {
					p = field.jump(p, d);
					s[p] = (d == 0) ? 4 : 5;
				}
				p = field.jump(p, d);
				s[p] = (d == 0) ? 0 : 1;
			}
		}
		return s;		
	}
	
	void printVisualState(int[] s) {
		for (int p = 0; p < s.length; p++) {
			System.out.print(" " + VISUALS[s[p]]);
			if(field.isRightBorder(p)) System.out.println("");
		}
		System.out.println("");
	}
	
	void printPack(ShipPosition[] ps) {
		for (int i = 0; i < ps.length; i++) {
			if(ps[i].getPlace() < 0) break;
			System.out.print(" " + ps[i]);
		}
		System.out.println();
	}

	public static void main(String[] args) {
		RectangularField f = new RectangularField();
		int width = 9;
		f.setSize(width, width);
		PackedSeaGenerator g = new PackedSeaGenerator();
		g.setField(f);
		g.generate();
	}

}

/**
 4:18h 4:44h 4:2h 4:56h 3:11h 3:41h 3:34h 3:60h 3:53h 3:6v 2:25h 2:32h 2:7v 2:0v 2:9v 2:23v 2:28h 2:40v 2:16v 1:1h 1:10h 1:27h 1:30h 1:37h 1:38h 1:39h 1:49h 1:50h 1:51h 1:52h 1:63h
*/