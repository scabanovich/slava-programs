package slava.puzzles.twotwomino.analysis;

import java.util.*;

import slava.puzzles.twotwomino.model.TwoTwoMinoDoubleField;
import slava.puzzles.twotwomino.model.TwoTwoMinoField;

public class TwoTwoMinoPacking {
	TwoTwoMinoField field;
	TwoTwoMinoDoubleField doubleField;
	int[][][] figures;
	int[] restrictions; // 0 - any; 1 - horizontal(3 or 12); 2 - vertical (5 or 10)
	
	int[][][] placements;
	
	int[] values; 
	int[] states;
	
	int vacancyCount;

	int t;
	int[] wayCount;
	int[][] ways;
	int[] way;
	int[] place;

	boolean isRandomizing;
	int solutionCountLimit = -1;
	int solutionCount;
	int printSolutionLimit = -1;
	protected List solutions = new ArrayList();
	
	public void setField(TwoTwoMinoField field) {
		this.field = field;
		doubleField = new TwoTwoMinoDoubleField(field);
		values = doubleField.getValues();
	}
	
	public void cleanValues() {
		for (int i = 0; i < values.length; i++) values[i] = 0;
	}
	
	public void setFigures(int[][][] figures) {
		this.figures = figures;
	}
	
	public void setRestrictions(int[] restrictions) {
		this.restrictions = restrictions;
	}
	
	public void setRandomizung(boolean b) {
		isRandomizing = b;
	}
	
	public void setSolutionCountLimit(int n) {
		solutionCountLimit = n;
	}
	
	public void setPrintSolutionLimit(int n) {
		printSolutionLimit = n;
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}
	
	public List getSolutions() {
		return solutions;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		states = new int[field.getSize()];
		int size = field.getSize() + 1;
		wayCount = new int[size];
		ways = new int[size][1000];
		way = new int[size];
		place = new int[size];
		t = 0;
		solutionCount = 0;
		solutions.clear();
		vacancyCount = field.getSize();
		
		makePlacements();
	}
	
	void makePlacements() {
		List list = new ArrayList();
		for (int i = 0; i < figures.length; i++) {
			for (int p = 0; p < field.getSize(); p++) {
				int[][] pc = createPlacement(figures[i], p);
				if(pc != null) list.add(pc);
			}
		}
		placements = (int[][][])list.toArray(new int[0][][]);
	}
	
	int[][] createPlacement(int[][] f, int p) {
		int[][] pc = new int[f.length][2];
		for (int i = 0; i < f.length; i++) {
			int dx = f[i][0];
			int dy = f[i][1];
			int s = f[i][2];
			int q = field.jump(p, dx, dy);
			if(q < 0) return null;
			if(restrictions != null) {
				if(restrictions[q] == 0) {
					if(s != 15) return null;
				} else if(restrictions[q] == 1) {
					if(s != 3 && s != 12) return null;
				} else if(restrictions[q] == 2) {
					if(s != 5 && s != 10) return null;
				}
			}
			pc[i][0] = q;
			pc[i][1] = s;			
		}
		return pc;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(vacancyCount == 0) return;
		if(!checkUmbiguity()) return;
		findNextPlace();
		if(place[t] < 0) return;
		createWays();
		if(isRandomizing) randomize();
	}
	
	boolean checkUmbiguity() {
		if(!isRandomizing) return true;
		for (int p1 = 0; p1 < field.getSize(); p1++) {
			if(states[p1] != 15) continue;
			int p2 = field.jump(p1, 0);
			if(p2 < 0 || states[p2] != 15) continue;
			int p3 = field.jump(p2, 0);
			if(p3 < 0 || states[p3] != 15) continue;
			int p1a = doubleField.convertFromOriginalField(p1, 0, 0);
			int p1b = doubleField.convertFromOriginalField(p1, 1, 1);
			if(values[p1a] != values[p1b]) continue;
			int p3a = doubleField.convertFromOriginalField(p3, 0, 0);
			int p3b = doubleField.convertFromOriginalField(p3, 1, 1);
			if(values[p3a] != values[p3b]) continue;
			if(values[p1a] == values[p3a]) continue;
			int p2a = doubleField.convertFromOriginalField(p2, 0, 0);
			if(values[p2a] != values[p1a] && values[p2a] != values[p3a]) continue;
			int p2b = doubleField.convertFromOriginalField(p2, 0, 1);
			if(values[p2b] != values[p1a] && values[p2b] != values[p3a]) continue;
			if(values[p2a] != values[p2b]) return false;
		}
		for (int p1 = 0; p1 < field.getSize(); p1++) {
			if(states[p1] != 15) continue;
			int p2 = field.jump(p1, 1);
			if(p2 < 0 || states[p2] != 15) continue;
			int p3 = field.jump(p2, 1);
			if(p3 < 0 || states[p3] != 15) continue;
			int p1a = doubleField.convertFromOriginalField(p1, 0, 0);
			int p1b = doubleField.convertFromOriginalField(p1, 1, 1);
			if(values[p1a] != values[p1b]) continue;
			int p3a = doubleField.convertFromOriginalField(p3, 0, 0);
			int p3b = doubleField.convertFromOriginalField(p3, 1, 1);
			if(values[p3a] != values[p3b]) continue;
			if(values[p1a] == values[p3a]) continue;
			int p2a = doubleField.convertFromOriginalField(p2, 0, 0);
			if(values[p2a] != values[p1a] && values[p2a] != values[p3a]) continue;
			int p2b = doubleField.convertFromOriginalField(p2, 1, 0);
			if(values[p2b] != values[p1a] && values[p2b] != values[p3a]) continue;
			if(values[p2a] != values[p2b]) return false;
		}
		return true;
	}
	
	void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = wayCount[t] - 1; i >= 1; i--) {
			int j = (int)((i + 1) * Math.random());
			if(i == j) continue;
			int n = ways[t][i];
			ways[t][i] = ways[t][j];
			ways[t][j] = n;
		}
		if(wayCount[t] > 3) wayCount[t] = 3;
	}
	
	int findNextPlace() {
		int[] dn = new int[field.getSize()];
		for (int i = 0; i < placements.length; i++) {
			if(!canAddPlacement(placements[i])) continue;
			for (int j = 0; j < placements[i].length; j++) {
				int p = placements[i][j][0];
				dn[p]++; 
			}
		}
		int m = 100000;
		place[t] = -1;
		for (int i = 0; i < field.getSize(); i++) {
			if(states[i] == 15) continue;
			if(dn[i] >= m) continue;
			m = dn[i];
			if(m == 0) {
				place[t] = -1;
				return 0;
			} 
			place[t] = i;
		}
		return m;
	}
	
	void createWays() {
		int p = place[t];
		if(p < 0) return;
		for (int i = 0; i < placements.length; i++) {
			if(!contains(placements[i], p)) continue;
			if(!canAddPlacement(placements[i])) continue;
			ways[t][wayCount[t]] = i;
			wayCount[t]++;
		}
	}
	
	boolean contains(int[][] pc, int p) {
		for (int i = 0; i < pc.length; i++) {
			if(pc[i][0] == p) return true;
		}
		return false;		
	}
	
	boolean canAddPlacement(int[][] pc) {
		for (int i = 0; i < pc.length; i++) {
			int q = pc[i][0];
			int s = pc[i][1];
			if(q < 0 || (states[q] & s) != 0) return false;
		}
		return true;
	}
	
	void move() {
		int i = ways[t][way[t]];
		int p = place[t];
		addPlacement(placements[i], p);
		++t;
		srch();
		way[t] = -1;
	}
	
	void addPlacement(int[][] pc, int p) {
		for (int i = 0; i < pc.length; i++) {
			int q = pc[i][0];
			int s = pc[i][1];
			states[q] += s;
			if(states[q] == 15) vacancyCount--;
			if((s & 1) > 0) {
				int q1 = doubleField.convertFromOriginalField(q, 0, 0);
				values[q1] = t + 1;
			}
			if((s & 2) > 0) {
				int q1 = doubleField.convertFromOriginalField(q, 1, 0);
				values[q1] = t + 1;
			}
			if((s & 4) > 0) {
				int q1 = doubleField.convertFromOriginalField(q, 0, 1);
				values[q1] = t + 1;
			}
			if((s & 8) > 0) {
				int q1 = doubleField.convertFromOriginalField(q, 1, 1);
				values[q1] = t + 1;
			}
		}
	}
	
	void back() {
		--t;
		int i = ways[t][way[t]];
		int p = place[t];
		removePlacement(placements[i], p);
	}
	
	void removePlacement(int[][] pc, int p) {
		for (int i = 0; i < pc.length; i++) {
			int q = pc[i][0];
			int s = pc[i][1];
			if(states[q] == 15) vacancyCount++; 
			states[q] -= s;
			if((s & 1) > 0) {
				int q1 = doubleField.convertFromOriginalField(q, 0, 0);
				values[q1] = 0;
			}
			if((s & 2) > 0) {
				int q1 = doubleField.convertFromOriginalField(q, 1, 0);
				values[q1] = 0;
			}
			if((s & 4) > 0) {
				int q1 = doubleField.convertFromOriginalField(q, 0, 1);
				values[q1] = 0;
			}
			if((s & 8) > 0) {
				int q1 = doubleField.convertFromOriginalField(q, 1, 1);
				values[q1] = 0;
			}
		}
	}
	
	void anal() {
		int mv = 10;
		srch();
		way[t] = -1;
		int maxt = 0;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(vacancyCount < mv) {
				mv = vacancyCount;
///				System.out.println(vacancyCount);
			}
			if(vacancyCount == 0) {
				solutionCount++;
				if(solutionCount <= solutionCountLimit) {
					solutions.add(values.clone());
					if(solutionCount <= printSolutionLimit) printSolution(doubleField, values);
				}
				if(solutionCount == solutionCountLimit) return;
			}
		}
	}
	
	public void printSolution(TwoTwoMinoDoubleField doubleField, int[] values) {
		for (int i = 0; i < doubleField.getSize(); i++) {
			System.out.print(" " + (char)(96 + values[i]));
			if(doubleField.x(i) == doubleField.getWidth() - 1) System.out.println("");
		}		
	}

}
