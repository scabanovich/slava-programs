package com.slava.stars;

import java.util.ArrayList;

public class StarsSolver {
	StarsRegionField field;
	
	int[] values;
	int[] restrictions;
	int[] regionStarCount;
	int starsAvailable;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] waysP;
	int[][] waysC;

	int solutionCount;
	int treeCount;
	ArrayList solutions;
	
	int solutionLimit = 0;
	int memoredSolutionLimit = 30;

	public void setField(StarsRegionField field) {
		this.field = field;
		init0();
	}
	
	void init0() {
		values = new int[field.size];
		restrictions = new int[field.size];
		regionStarCount = new int[field.regions.length];
		wayCount = new int[field.size + 1];
		way = new int[field.size + 1];
		waysP = new int[field.size + 1][field.width];
		waysC = new int[field.size + 1][field.width];
		solutions = new ArrayList();
	}
	
	public void setSolutionLimit(int s) {
		solutionLimit = s;
	}

	public void solve() {
		init();
		anal();
	}
	
	void init() {
		starsAvailable = field.width * 2;
		for (int i = 0; i < field.size; i++) {
			values[i] = 0;
			restrictions[i] = 0;
		}
		for (int i = 0; i < field.regions.length; i++) {
			regionStarCount[i] = 0;
		}
		t = 0;
		solutionCount = 0;
		treeCount = 0;
		solutions.clear();
	}
	
	boolean canAddStar(int p) {
		return (restrictions[p] == 0);
	}
	
	void addStar(int p) {
		starsAvailable--;
		values[p] = 1;
		restrictions[p]++;
		for (int d = 0; d < 8; d++) {
			int q = field.jump(p, d);
			if(q >= 0) restrictions[q]++;
		}
		int[] ri = field.placeToRegions[p];
		for (int r = 0; r < ri.length; r++) {
			int regionIndex = ri[r];
			regionStarCount[regionIndex]++;
			if(regionStarCount[regionIndex] == 2) {
				int[] region = field.regions[ri[r]];
				for (int k = 0; k < region.length; k++) {
					restrictions[region[k]]++;
				}
			}
		}
	}
	
	void removeStar(int p) {
		starsAvailable++;
		values[p] = 0;
		restrictions[p]--;
		for (int d = 0; d < 8; d++) {
			int q = field.jump(p, d);
			if(q >= 0) restrictions[q]--;
		}
		int[] ri = field.placeToRegions[p];
		for (int r = 0; r < ri.length; r++) {
			int regionIndex = ri[r];
			if(regionStarCount[regionIndex] == 2) {
				int[] region = field.regions[ri[r]];
				for (int k = 0; k < region.length; k++) {
					restrictions[region[k]]--;
				}
			}
			regionStarCount[regionIndex]--;
		}
	}
	
	void srch() {
		wayCount[t] = 0;
		if(isFinished()) return;
		doSrch();
	}
	
	void doSrch() {
		if(!canFillAnyRegion()) return;
		for (int p = 0; p < field.size; p++) {
			if(restrictions[p] != 0) continue;
			restrictions[p]++;
			boolean b = true;
			int[] ri = field.placeToRegions[p];
			for (int r = 0; r < ri.length && b; r++) {
				b = canFillRegion(ri[r]);
			}
			restrictions[p]--;
			if(!b) {
				waysP[t][0] = p;
				waysC[t][0] = 1;
				wayCount[t] = 1;
				return;
			}
		}
		for (int p = 0; p < field.size; p++) {
			if(restrictions[p] != 0) continue;
			addStar(p);
			boolean b = canFillAnyRegion();
			removeStar(p);
			if(!b) {
				waysP[t][0] = p;
				waysC[t][0] = 0;
				wayCount[t] = 1;
				return;
			}
		}
		int p = getMostNarrowPlace();
		if(p < 0) return;
		for (int c = 0; c < 2; c++) {
			waysP[t][wayCount[t]] = p;
			waysC[t][wayCount[t]] = c;
			wayCount[t]++;
		}
	}
	
	boolean canFillAnyRegion() {
		for (int r = 0; r < regionStarCount.length; r++) {
			if(!canFillRegion(r)) return false;
		}
		return true;
	}
	
	boolean canFillRegion(int r) {
		if(regionStarCount[r] == 2) return true;
		int[] region = field.regions[r];
		if(regionStarCount[r] == 1) {
			for (int k = 0; k < region.length; k++) {
				if(restrictions[region[k]] == 0) return true;
			}
		}
		for (int k = 0; k < region.length; k++) {
			if(restrictions[region[k]] > 0) continue;
			addStar(region[k]);
			boolean ok = false;
			for (int k2 = k + 1; k2 < region.length && !ok; k2++) {
				ok = restrictions[region[k2]] == 0;
			}			
			removeStar(region[k]);
			if(ok) return true;
		}
		return false;
	}
	
	int getMostNarrowPlace() {
		int p = -1;
		int sm = 100;
		for (int r = 0; r < regionStarCount.length; r++) {
			if(regionStarCount[r] == 2) continue;
			int s = 0;
			int pa = -1;
			int dm = -1;
			int[] region = field.regions[r];
			for (int k = 0; k < region.length; k++) {
				if(restrictions[region[k]] != 0) continue;
				++s;
				int dq = 0;
				for (int d = 0; d < 8; d++) {
					int q = field.jump(region[k], d);
					if(q >= 0 && restrictions[q] == 0) ++dq;
				}
				if(dq > dm) {
					dm = dq;
					pa = region[k];
				}
			}
			if(regionStarCount[r] == 1) s *= 2;
			if(s < sm) {
				sm = s;
				p = pa;
			}
		}
		return p;
	}
	
	void move() {
		int p = waysP[t][way[t]];
		int v = waysC[t][way[t]];
		if(v == 1) {
			addStar(p);
		} else {
			restrictions[p]++;
		}		
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = waysP[t][way[t]];
		int v = waysC[t][way[t]];
		if(v == 1) {
			removeStar(p);
		} else {
			restrictions[p]--;
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
			if(wayCount[t] == 0) {
				treeCount++;				
			}
			if(isFinished()) {
				solutionCount++;
				int[] solution = (int[])values.clone();
				if(solutionCount <= memoredSolutionLimit) {
					solutions.add(solution);
				}
				if(solutionLimit > 0 && solutionCount >= solutionLimit) return;
//				if(solutionCount == 1) printSolution(solution);
			}
		}
	}
	
	protected boolean isFinished() {
		return starsAvailable == 0;
	}
	
	public int[][] getSolution() {
		return (int[][])solutions.toArray(new int[0][]);
	}
	
	public void printSolution(int[] solution) {
		System.out.print(solutionToString(solution));
	}
	
	public String solutionToString(int[] solution) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < field.size; i++) {
			char c = (solution[i] == 0) ? '+' : '*';
			sb.append(" " + c);
			if(field.x[i] == field.width - 1) sb.append("\n");
		}
		return sb.toString();
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}
	
	public int getTreeSize() {
		return treeCount;
	}

}
