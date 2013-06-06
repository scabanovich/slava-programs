package com.slava.nurikabe;

import com.slava.common.RectangularField;

public class NurikabeSolver {
	static int WATER = 0;
	static int LAND = 1;
	RectangularField field;

	int[] problem;

	int solutionLimit;

	int[] state;
	int unresolvedCount;
	int[] islandHomes;
	// how many cells must be added to complete i-th island
	int[] cellsToAdd;
	
	int t = 0;
	int[] wayCount;
	int[] way;
	int[][] waysP;
	int[][] waysV;
	
	int solutionCount;
	int[] solution;
	
	int treeCount;

	public NurikabeSolver() {}

	public void setField(RectangularField f) {
		field = f;
	}

	public void setProblem(int[] problem) {
		this.problem = problem;
	}
	
	public void setSoltionLimit(int s) {
		solutionLimit = s;
	}
	
	public void solve() {
		init();
		anal();		
	}
	
	void init() {
		state = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		
		unresolvedCount = state.length;
		initialResolve();
		
		wayCount = new int[field.getSize() + 1];
		way = new int[field.getSize() + 1];
		waysP = new int[field.getSize() + 1][2];
		waysV = new int[field.getSize() + 1][2];
		
		t = 0;
		solutionCount = 0;
		treeCount = 0;
	}
	
	void initialResolve() {
		int index = 0;
		// island markers
		for (int p = 0; p < problem.length; p++) {
			if(problem[p] > 0) {
				index++;
				state[p] = index;
				unresolvedCount--;
			}
		}
		islandHomes = new int[index + 1];
		cellsToAdd = new int[index + 1];
		for (int p = 0; p < problem.length; p++) {
			if(state[p] > 0) {
				islandHomes[state[p]] = p;
				cellsToAdd[state[p]] = problem[p] - 1;
			}
		}

		for (int p = 0; p < problem.length; p++) {
			// islands of area 1.
			if(problem[p] == 1) {
				for (int d = 0; d < 4;  d++) {
					int q = field.jump(p, d);
					if(q >= 0 && state[q] < 0) addToWater(q);
				}
			}
			// places touching two or more islands
			if(problem[p] == 0 && state[p] < 0) {
				if(isPlaceTouchingMoreThanOneIsland(p)) {
					addToWater(p);
				}
			}
		}
		
		int[] shapes = buildShapes();
		for (int p = 0; p < shapes.length; p++) {
			if(shapes[p] < 0 && state[p] < 0) {
				addToWater(p);
			}
		}
	}
	
	void addToWater(int p) {
		state[p] = WATER;
		unresolvedCount--;
	}
	
	void addToIsland(int p, int index) {
		state[p] = index;
		unresolvedCount--;
		cellsToAdd[index]--;
	}
	
	void removeFromWater(int p) {
		state[p] = -1;
		unresolvedCount++;
	}
	
	void removeFromIsland(int p) {
		cellsToAdd[state[p]]++;
		state[p] = -1;
		unresolvedCount++;
	}
	
	boolean isPlaceTouchingMoreThanOneIsland(int p) {
		int island = -1;
		for (int d = 0; d < 4;  d++) {
			int q = field.jump(p, d);
			if(q < 0 || state[q] <= WATER) continue;
			if(island < 0) {
				island = state[q];
			} else {
				if(island != state[q]) return true;
			}
		}
		return false;
	}
	
	int[] buildShapes() {
		int[] result = new int[state.length];
		for (int p = 0; p < state.length; p++) {
			if(state[p] <= WATER) {
				result[p] = state[p];
			} else {
				result[p] = cellsToAdd[state[p]] + 1; //imitate problem
			}
		}
		boolean changed = true;
		while(changed) {
			changed = false;
			for (int p = 0; p < state.length; p++) {
				if(state[p] >= WATER) continue;
				int max = 0;
				for (int d = 0; d < 4; d++) {
					int q = field.jump(p, d);
					if(q >= 0 && result[q] > max) max = result[q];
				}
				if(max > 1 && max - 1 > result[p]) {
					result[p] = max - 1;
					changed = true;
				}
			}
		}
		return result;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(unresolvedCount == 0) return;
		if(!checkWaterPools() || !checkContinuousnessOfWater()) return;
		for (int p = 0; p < state.length; p++) {
			if(state[p] < 0 && isPlaceTouchingMoreThanOneIsland(p)) {
				waysP[t][0] = p;
				waysV[t][0] = WATER;
				wayCount[t] = 1;
				return;
			}
		}
		int requiredWater = findRequiredWaterNearCompletedIsland();
		if(requiredWater >= 0) {
			waysP[t][0] = requiredWater;
			waysV[t][0] = WATER;
			wayCount[t] = 1;
			return;
		}
		int[] shapes = buildShapes();
		for (int p = 0; p < shapes.length; p++) {
			if(shapes[p] < 0 && state[p] < 0) {
				waysP[t][0] = p;
				waysV[t][0] = WATER;
				wayCount[t] = 1;
				return;
			}
		}
		int[] p = getPlaceToGrowIsland();
		if(p == null) return;
		waysP[t][0] = p[0];
		waysV[t][0] = p[1];
		wayCount[t] = 1;
		if(p[2] == 1) return;
		waysP[t][1] = p[0];
		waysV[t][1] = WATER;
		wayCount[t] = 2;
	}
	
	boolean checkWaterPools() {
		for (int p = 0; p < state.length; p++) {
			if(state[p] != WATER) continue;
			int q = field.jump(p, 0);
			if(q < 0 || state[q] != WATER) continue;
			q = field.jump(p, 1);
			if(q < 0 || state[q] != WATER) continue;
			q = field.jump(q, 0);
			if(q < 0 || state[q] != WATER) continue;
			return false;
		}
		return true;
	}
	
	boolean checkContinuousnessOfWater() {
		int[] s = new int[field.getSize()];
		int[] vector = new int[field.getSize()];
		int partCount = 0;
		for (int i = 0; i < s.length; i++) {
			if(state[i] != WATER || s[i] == 1) continue;
			partCount++;
			if(partCount > 1) return false;
			int v = 1;
			int c = 0;
			vector[0] = i;
			s[i] = 1;
			while(c < v) {
				int p = vector[c];
				for (int d = 0; d < 4; d++) {
					int q = field.jump(p, d);
					if(q < 0 || s[q] > 0 || state[q] > WATER) continue;
					s[q] = 1;
					vector[v] = q;
					v++;
				}
				c++;
			}
		}
		return true;
	}
	
	int findRequiredWaterNearCompletedIsland() {
		for (int p = 0; p < state.length; p++) {
			if(state[p] >= WATER) continue;
			for (int d = 0; d < 4; d++) {
				int q = field.jump(p, d);
				if(q >= 0 && state[q] > WATER && cellsToAdd[state[q]] == 0) {
					return p;
				}
			}
		}
		return -1;
	}
	
	int[] getPlaceToGrowIsland() {
		int[] vents = new int[islandHomes.length];
		int[] ventCounts = new int[islandHomes.length];
		for (int p = 0; p < state.length; p++) {
			if(state[p] >= 0) continue;
			for (int d = 0; d < 4; d++) {
				int q = field.jump(p, d);
				if(q < 0 || state[q] <= WATER) continue;
				int v = state[q];
				if(cellsToAdd[v] == 0) continue;
				ventCounts[v]++;
				vents[v] = p;
			}
		}
		int estimate = 1000;
		int[] place = null;
		for (int i = 1; i < islandHomes.length; i++) {
			if(ventCounts[i] == 0) {
				if(cellsToAdd[i] > 0) return null;
			} else if(ventCounts[i] == 1) {
				estimate = 0;
				place = new int[]{vents[i], i, ventCounts[i]};
			} else {
				int est = cellsToAdd[i] + ventCounts[i];
				if(est < estimate) {
					estimate = est;
					place = new int[]{vents[i], i, ventCounts[i]};
				}
			}
		}
		
		return place;
	}
	
	void move() {
		int p = waysP[t][way[t]];
		int v = waysV[t][way[t]];
		if(v == WATER) {
			addToWater(p);
		} else {
			addToIsland(p, v);
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = waysP[t][way[t]];
		int v = waysV[t][way[t]];
		if(v == WATER) {
			removeFromWater(p);
		} else {
			removeFromIsland(p);
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
			if(unresolvedCount == 0 && checkWaterPools() && checkContinuousnessOfWater()) {
				solutionCount++;
				if(solutionCount == 1) {
					solution = (int[])state.clone();
				}
				if(solutionLimit > 0 && solutionCount >= solutionLimit) return;
			}
		}
	}

	void printSolution() {
		System.out.println("Solution:");
		if(solution == null) {
			System.out.println("Not found");
			return;
		}
		for (int i = 0; i < state.length; i++) {
			String s = solution[i] < 0 ? "?" : solution[i] == 0 ? "+" : "" + (char)(96 + solution[i]);
			System.out.print(" " + s);
			if(field.isRightBorder(i)) System.out.println("");
		}
	}
}
