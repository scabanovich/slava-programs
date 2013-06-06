package com.slava.chess;

import com.slava.sudoku.ISudokuField;

public class ChessChameleonSolver {
	ChessFigures figures;
	ISudokuField field;
	int[][] regions;
	int[] figureSet;
	
	int[][] used; // [p][v]
	int[] freeCount; // [p]
	
	int[][] usedRegionColors;
	int[] freeRegionColorCount;
	
	int[] values;
	int vacancies;

	int t;
	int[] wayCount;
	int[] way;
	int[][] waysP;
	int[][] waysC;
	int[] treeEstimate;
	
	boolean randomizing;
	int solutionCount;
	int solutionLimit;
	int treeCount;
	
	int[] solution;
	
	int stopAtVacancies;
	
	public ChessChameleonSolver() {}
	
	public void setFigures(ChessFigures figures) {
		this.figures = figures;
	}
	
	public void setFigureSet(int[] fs) {
		figureSet = fs;
	}
	
	public void setField(ISudokuField field) {
		this.field = field;
		init0();
	}
	
	public void setSolutionLimit(int i) {
		solutionLimit = i;
	}
	
	public void setRandomizing(boolean b) {
		randomizing = b;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init0() {
		regions = field.getRegions();
		vacancies = field.getSize();
		values = new int[vacancies];
		used = new int[field.getSize()][field.getColorCount()];
		freeCount = new int[vacancies];
		usedRegionColors = new int[regions.length][50];
		freeRegionColorCount = new int[regions.length];
		wayCount = new int[field.getSize() + 1];
		way = new int[field.getSize() + 1];
		waysP = new int[field.getSize() + 1][50];
		waysC = new int[field.getSize() + 1][50];
		treeEstimate = new int[field.getSize() + 1];
	}
	
	void init() {
		vacancies = field.getSize();
		for (int i = 0; i < values.length; i++) values[i] = -1;
		
		for (int i = 0; i < field.getSize(); i++) for (int j = 0; j < field.getColorCount(); j++) used[i][j] = 0;
		for (int i = 0; i < freeCount.length; i++) freeCount[i] = field.getColorCount();
		
		for (int i = 0; i < regions.length; i++) for (int j = 0; j < field.getColorCount(); j++) usedRegionColors[i][j] = 0;
		for (int i = 0; i < freeRegionColorCount.length; i++) freeRegionColorCount[i] = field.getColorCount();
		
		t = 0;
		solutionCount = 0;
		treeCount = 0;
		solution = null;
		treeEstimate[0] = 1;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(isFinished()) return;
		doSrch();
		if(t > 0) treeEstimate[t] = treeEstimate[t - 1] * wayCount[t];
		if(randomizing) {
			randomize();
		}
	}
	
	protected void doSrch() {
		if(isNotSolvable()) return;
		int dec = getDeadEndCount();
		if(t < 45 && dec > 8) {
			return;
		}
		int c = t % field.getColorCount();
		int p = t == 0 ? -1 : waysP[t - 1][way[t - 1]];
		if(t == 0) {
			waysC[t][0] = c;
			waysP[t][0] = 1; //initial place
			wayCount[t] = 1;
		} else {
			int figure = figureSet[(t - 1) % figureSet.length];
			int[] moves = figures.getMoves(figure, p);
			for (int i = 0; i < moves.length; i++) {
				int q = moves[i];
				if(canAdd(q, c)) {
					waysC[t][wayCount[t]] = c;
					waysP[t][wayCount[t]] = q;
					wayCount[t]++;
				}
			}
		}
	}
	
	boolean isNotSolvable() {
		for (int p = 0; p < field.getSize(); p++) {
			if(values[p] >= 0) continue;
			if(freeCount[p] == 0) return true;
		}
		for (int r = 0; r < freeRegionColorCount.length; r++) {
			if(freeRegionColorCount[r] == 0) continue;
			if(regions[r].length < field.getColorCount()) continue; //small region
			for (int v = 0; v < field.getColorCount(); v++) {
				if(usedRegionColors[r][v] == 1) continue;
				int wr = 0;
				for (int i = 0; i < regions[r].length; i++) {
					int p = regions[r][i];
					if(values[p] >= 0 || used[p][v] > 0) continue;
					++wr;
				}
				if(wr == 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	int getDeadEndCount() {
		int n = 0;
		for (int p = 0; p < used.length; p++) {
			if(values[p] >= 0) continue;
			int isDeadEnd = 2;
			for (int v = 0; v < used[p].length && isDeadEnd > 0; v++) {
				if(used[p][v] > 0) continue;
				int c = v + 1;
				if(c >= field.getColorCount()) c = c - field.getColorCount();
				int de = 2;
				if(hasAtLeastOneMove(p, c, ChessFigures.QUEEN)
				   || hasAtLeastOneMove(p, c, ChessFigures.NOOK)) {
					de--;
				}
				c = v - 1;
				if(c < 0) c = c + field.getColorCount();
				if(hasAtLeastOneMove(p, c, ChessFigures.QUEEN)
				   || hasAtLeastOneMove(p, c, ChessFigures.NOOK)) {
					de--;
				}
				if(isDeadEnd > de) isDeadEnd = de;
			}
			n += isDeadEnd;
		}
		return n;
	}
	private boolean hasAtLeastOneMove(int p, int c, int figure) {
		int[] moves = figures.getMoves(figure, p);
		for (int i = 0; i < moves.length; i++) {
			int q = moves[i];
			if(canAdd(q, c)) return true;
		}
		return false;
	}
	
	void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = wayCount[t] - 1; i >= 1; i--) {
			int k = (int)((i + 1) * Math.random());
			if(k == i) continue;
			int c = waysC[t][i];
			waysC[t][i] = waysC[t][k];
			waysC[t][k] = c;
			c = waysP[t][i];
			waysP[t][i] = waysP[t][k];
			waysP[t][k] = c;
		}
		if(t < 10 && wayCount[t] > 3) wayCount[t] = 3;
	}
	
	protected void move() {
		int p = waysP[t][way[t]];
		int v = waysC[t][way[t]];
//		System.out.println("t=" + t + " p=" + p + " v=" + v + " wc=" + wayCount[t] + " w=" + way[t]);
		add(p, v);
		++t;
		srch();
		way[t] = -1;
	}
	
	protected void add(int p, int v) {
		vacancies--;
		values[p] = v;
		int[] ri = field.getPlaceToRegions()[p];
		for (int r = 0; r < ri.length; r++) {
			int[] region = regions[ri[r]];
			for (int k = 0; k < region.length; k++) {
				used[region[k]][v]++;
				if(used[region[k]][v] == 1) {
					freeCount[region[k]]--;
				}
			}
			usedRegionColors[ri[r]][v] = 1;
			freeRegionColorCount[ri[r]]--;
		}
	}
	
	protected boolean canAdd(int p, int v) {
		return (values[p] == -1) && (used[p][v] == 0);
	}
	
	protected void back() {
		--t;
		int p = waysP[t][way[t]];
		int v = waysC[t][way[t]];
		remove(p, v);
	}

	protected void remove(int p, int v) {
		vacancies++;
		values[p] = -1;
		int[] ri = field.getPlaceToRegions()[p];
		for (int r = 0; r < ri.length; r++) {
			int[] region = regions[ri[r]];
			for (int k = 0; k < region.length; k++) {
				used[region[k]][v]--;
				if(used[region[k]][v] == 0) {
					freeCount[region[k]]++;
				}
			}
			usedRegionColors[ri[r]][v] = 0;
			freeRegionColorCount[ri[r]]++;
		}
	}

	void anal() {
		srch();
		way[t] = -1;
		int tm = 45;
		int q = 0;
		int tmin = 22;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			++q;
			if(t > tm && !isNotSolvable()) {
				tm = t;
				q = 0;
				System.out.println("-->" + tm);
				if(tm > 49) printSolution(values);
			}
			if(tm > 45 && t < tmin) {
				tmin = t;
//				System.out.println("tmin=" + t);
			}
			
			if(tm < 46 && q >= 150000) return;
			if(tm < 50 && q >= 500000) {
				System.out.println("y tm=" + tm);
				return;
			}
			if((tm < 55 && q >= 10000000)
				|| (tm < 59 && q >= 100000000)) {
				System.out.println("z tm=" + tm);
				return;
			}
			
			if(wayCount[t] == 0) {
				treeCount++;				
			}
			if(stopAtVacancies > 0 && stopAtVacancies == vacancies) {
				solution = (int[])values.clone();
				return;
			}
			if(isFinished()) {
				solutionCount++;
				if(solutionCount == 1) {
					solution = (int[])values.clone();
				}
				if(solutionLimit > 0 && solutionCount >= solutionLimit) return;
				onSolutionFound();
				if(solutionCount % 100000 == 0) {
					double d = getTaskCompleteIndex();
					long estimate = d == 0 ? -1 : (long)(solutionCount / d);
					System.out.println("Solutions=" + solutionCount + " Completed=" + d + " Estimate=" + estimate);
				}
			}
		}
	}
	
	protected void onSolutionFound() {
	}
	
	private double getTaskCompleteIndex() {
		double p = 1d;
		double s = 0d;
		for (int i = 0; i < t; i++) {
			p = p / wayCount[i];
			s += p * way[i];
		}
		return s;
	}
	
	protected boolean isFinished() {
		return vacancies == 0;
	}
	
	public int[] getSolution() {
		return solution;
	}
	
	public void printSolution(int[] solution) {
		System.out.print(solutionToString(solution));
		for (int i = 0; i < t; i++) {
			int p = waysP[i][way[i]];
			char x = (char)(65 + (p % field.getColorCount()));
			int y = p / field.getColorCount() + 1;
			System.out.print(" " + x + y);
		}
		System.out.println("");
	}
	
	public String solutionToString(int[] solution) {
		return field.printSolution(solution);
	}

}
