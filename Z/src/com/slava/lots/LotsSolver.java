package com.slava.lots;

import com.slava.common.RectangularField;

public class LotsSolver {
	RectangularField field;
	
	int minArea;
	int maxArea;
	
	int[] problem;
	int solutionLimit;
	int treeCountLimit;

	int[] state;
	int unresolvedCount;

	int t = 0;
	int[] wayCount;
	int[] way;
	int[] place;
	int[][] waysLx;
	int[][] waysLy;

	int[] solution;
	int solutionCount;
	
	int treeCount;

	public LotsSolver() {}

	public void setField(RectangularField f) {
		field = f;
	}
	
	public void setMinArea(int s) {
		minArea = s;
	}
	
	public void setMaxArea(int s) {
		maxArea = s;
	}
	
	public void setProblem(int[] s) {
		problem = s;
	}

	public void setSoltionLimit(int s) {
		solutionLimit = s;
	}
	
	public void setTreeCountLimit(int s) {
		treeCountLimit = s;
	}
	
	public void solve() {
		init();
		anal();		
	}

	void init() {
		state = new int[field.getSize()];
		for (int p = 0; p < state.length; p++) state[p] = -1;
		unresolvedCount = field.getSize();
		
		wayCount = new int[field.getSize() + 1];
		way = new int[field.getSize() + 1];
		place = new int[field.getSize() + 1];
		waysLx = new int[field.getSize() + 1][100];
		waysLy = new int[field.getSize() + 1][100];
		
		t = 0;
		solutionCount = 0;
		treeCount = 0;
		solution = null;
	}

	void srch() {
		wayCount[t] = 0;
		if(unresolvedCount == 0) return;
		int p = getNextPlace();
		place[t] = p;
		if(p < 0) return;
		for (int ix = 0; ix < field.getWidth() - field.getX(p); ix++) {
			for (int iy = 0; iy < field.getHeight() - field.getY(p); iy++) {
				int lx = ix + 1;
				int ly = iy + 1;
				int s = lx * ly;
				if(s > maxArea || s < minArea) continue;
				if(!canAdd(p, lx, ly)) break;
				if(!checkProblem(p, lx, ly))  continue;
				waysLx[t][wayCount[t]] = lx;
				waysLy[t][wayCount[t]] = ly;
				wayCount[t]++;
			}
		}
		randomize();
	}
	
	boolean canAdd(int p, int lx, int ly) {
		for (int ix = 0; ix < lx; ix++) {
			for (int iy = 0; iy < ly; iy++) {
				int q = field.getIndex(field.getX(p) + ix, field.getY(p) + iy);
				if(state[q] >= 0) return false;
			}
		}
		return true;
	}
	
	boolean checkProblem(int p, int lx, int ly) {
		if(problem == null) return true;
		int s = lx * ly;
		boolean ok = false;
		for (int ix = 0; ix < lx; ix++) {
			for (int iy = 0; iy < ly; iy++) {
				int q = field.getIndex(field.getX(p) + ix, field.getY(p) + iy);
				if(problem[q] > 0) {
					if(ok) return false;
					if(s != problem[q]) return false; else ok = true;
				}
			}
		}
		return ok;
	}
	
	void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = wayCount[t] - 1; i > 0; i--) {
			int j = (int)((i + 1) * Math.random());
			if(i != j) {
				int k = waysLx[t][i];
				waysLx[t][i] = waysLx[t][j];
				waysLx[t][j] = k;
				k = waysLy[t][i];
				waysLy[t][i] = waysLy[t][j];
				waysLy[t][j] = k;
			}
		}
	}
	
	int getNextPlace() {
		int p0 = 0;
		if(t > 0) p0 = place[t - 1] + 1;
		for (int p = p0; p < state.length; p++) {
			if(state[p] < 0) return p;
		}
		return -1;
	}
	
	void move() {
		int p = place[t];
		int lx = waysLx[t][way[t]];
		int ly = waysLy[t][way[t]];
		unresolvedCount -= lx * ly;
		for (int ix = 0; ix < lx; ix++) {
			for (int iy = 0; iy < ly; iy++) {
				int q = field.getIndex(field.getX(p) + ix, field.getY(p) + iy);
				state[q] = t;
			}
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = place[t];
		int lx = waysLx[t][way[t]];
		int ly = waysLy[t][way[t]];
		unresolvedCount += lx * ly;
		for (int ix = 0; ix < lx; ix++) {
			for (int iy = 0; iy < ly; iy++) {
				int q = field.getIndex(field.getX(p) + ix, field.getY(p) + iy);
				state[q] = -1;
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
			if(wayCount[t] == 0) treeCount++;
			if(unresolvedCount == 0) {
				solutionCount++;
				if(solutionCount == 1) {
					solution = (int[])state.clone();
//					printSolution(state);
				}
				if(solutionLimit > 0 && solutionCount >= solutionLimit) return;
			}
			if(treeCountLimit > 0 && treeCount > treeCountLimit) {
				solutionCount = solutionLimit + 1;
				return;
			}
		}
	}
	
	public void printSolution(int[] state) {
		for (int p = 0; p < state.length; p++) {
			String s = " " + (char)(97 + (state[p] % 26));
			System.out.print(s);
			if(field.isRightBorder(p)) System.out.println("");
		}
		System.out.println("");
	}
	
	public int[] getSolution() {
		return solution;
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}
	
}
