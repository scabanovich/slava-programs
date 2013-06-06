package com.slava.nurikabe;

import com.slava.common.RectangularField;

public class NurikabeGenerator {
	
	static int WATER = 0;
	static int LAND = 1;
	RectangularField field;

	int waterAmount;
	int smallIslandLimit = 10;
	int biggestIslandArea = 20;

	int[] state;
	
	int[] islands;
	int islandsCount;
	int[] islandAreas;
	
	//resulting nurikabe problem
	int[] problem;
	
	public NurikabeGenerator() {}
	
	public void setField(RectangularField f) {
		field = f;
	}
	
	public void setWaterAmount(int c) {
		waterAmount = c;
	}
	
	public void setSmallIslandLimit(int c) {
		smallIslandLimit = c;
	}
	
	public void setBiggestIslandArea(int c) {
		biggestIslandArea = c;
	}

	public void generate() {
		NurikabeSolver solver = new NurikabeSolver();
		solver.setSoltionLimit(10);
		while(true) {
			init();
			int flipCount = 0;
			while(flipCount < 1000000) {
				if(flip()) {
					flipCount++;
					int max = buildIslands();
					if(flipCount > 500 && max <= biggestIslandArea && getSmallIslandCount() <= smallIslandLimit) {
//						System.out.println("Flip count=" + flipCount);
						break;
					}
				}
			}
			createProblem();
			solver.setField(field);
			solver.setProblem(problem);
			solver.solve();
			if(solver.solutionCount == 1) break;
			System.out.println("-->" + solver.solutionCount);
		}
		printState();
		printProblem();
		solver.printSolution();
		System.out.println("Tree count=" + solver.treeCount);
	}
	
	void init() {
		state = new int[field.getSize()];
		for (int i = 0; i < state.length; i++) state[i] = LAND;
		int w = 0;
		for (int p = 0; p < state.length && w < waterAmount; p++) {
			state[p] = WATER;
			++w;
			int p1 = field.jump(p, 2);
			if(p1 < 0) continue;
			int p2 = field.jump(p, 3);
			if(p2 < 0) continue;
			int p3 = field.jump(p1, 3);
			if(state[p1] == WATER && state[p2] == WATER && state[p3] == WATER) {
				state[p] = LAND;
				w--;
			}
		}
	}
	
	boolean checkContinuousnessOfWater() {
		int[] s = new int[field.getSize()];
		int[] vector = new int[field.getSize()];
		for (int i = 0; i < s.length; i++) {
			if(state[i] != WATER) continue;
			int v = 1;
			int c = 0;
			vector[0] = i;
			s[i] = 1;
			while(c < v) {
				int p = vector[c];
				for (int d = 0; d < 4; d++) {
					int q = field.jump(p, d);
					if(q < 0 || s[q] > 0 || state[q] != WATER) continue;
					s[q] = 1;
					vector[v] = q;
					v++;
				}
				c++;
			}
			return (v == waterAmount);
		}
		return true;
	}
	
	boolean checkPools() {
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
	
	// returns size of the biggest island
	int buildIslands() {
		islands = new int[field.getSize()];
		for (int i = 0; i < islands.length; i++) islands[i] = -1;
		islandAreas = new int[field.getSize() / 3];
		islandsCount = 0;
		int max = 0;
		int[] vector = new int[field.getSize()];
		for (int i = 0; i < islands.length; i++) {
			if(state[i] != LAND) continue;
			if(islands[i] >= 0) continue;
			int v = 1;
			int c = 0;
			vector[0] = i;
			islands[i] = islandsCount;
			while(c < v) {
				int p = vector[c];
				for (int d = 0; d < 4; d++) {
					int q = field.jump(p, d);
					if(q < 0 || islands[q] >= 0 || state[q] != LAND) continue;
					islands[q] = islandsCount;
					vector[v] = q;
					v++;
				}
				c++;
			}
			islandAreas[islandsCount] = v;
			islandsCount++;
			if(v > max) max = v;
		}
		return max;
	}
	
	int getSmallIslandCount() {
		int c = 0;
		for (int i = 0; i < islandsCount; i++) {
			if(islandAreas[i] == 1) c++;
		}
		return c;
	}
	
	boolean flip() {
		int pW = getRandomWater();
		int pL = getRandomLand();
		state[pW] = LAND;
		state[pL] = WATER;
		if(!checkPools() || !checkContinuousnessOfWater()) {
			state[pW] = WATER;
			state[pL] = LAND;
			return false;
		} else {
			return true;
		}		
	}
	
	int getRandomWater() {
		while(true) {
			int p = (int)(Math.random() * state.length);
			if(state[p] == WATER) return p;
		}
	}
	
	int getRandomLand() {
		while(true) {
			int p = (int)(Math.random() * state.length);
			if(state[p] == LAND) return p;
		}
	}
	
	void createProblem() {
		problem = new int[field.getSize()];
		for (int i = 0; i < islandsCount; i++) {
			int p = -1;
			while(p < 0  || islands[p] != i) {
				p = (int)(Math.random() * field.getSize());
			}
			problem[p] = islandAreas[islands[p]];
		}
	}
	
	void printState() {
		for (int i = 0; i < state.length; i++) {
			System.out.print(" " + state[i]);
			if(field.isRightBorder(i)) System.out.println("");
		}
		System.out.println("Island sizes");
		for (int i = 0; i < islandsCount; i++) {
			System.out.print(" " + islandAreas[i]);
		}
		System.out.println("");
	}
	
	void printProblem() {
		System.out.println("Problem");
		for (int i = 0; i < state.length; i++) {
			System.out.print(" " + problem[i]);
			if(field.isRightBorder(i)) System.out.println("");
		}
		System.out.println("");
	}
	
	public static void main(String[] args) {
		NurikabeGenerator g = new NurikabeGenerator();
		RectangularField f = new RectangularField();
		f.setSize(8, 8);
		g.setField(f);

		g.setWaterAmount(39);
		g.setSmallIslandLimit(2);
		g.setBiggestIslandArea(5);
		
		long b = System.currentTimeMillis();
		g.generate();
		b = System.currentTimeMillis() - b;
		System.out.println("Done in " + b);
	
	}

}

/*
 10*10 water=65 small <= 3 biggest <= 4
Problem
 0 0 1 0 0 0 0 2 0 0
 3 0 0 0 2 0 0 0 0 0
 0 0 0 0 0 0 0 0 3 0
 0 0 0 0 0 0 4 0 0 0
 0 0 0 3 0 0 0 0 0 0
 0 0 0 0 0 0 0 0 0 0
 0 3 0 2 0 0 0 2 0 2
 0 0 0 0 0 0 0 0 0 0
 1 0 0 0 3 0 2 0 0 0
 0 0 0 0 0 0 0 0 2 0

Solution:
 + + a + + + + b b +
 c + + + d d + + + +
 c c + g + + + e e +
 + + + g + f f + e +
 + h + g + f + + + +
 + h + + + f + j + k
 + h + i i + + j + k
 + + + + + + n + + +
 l + m m m + n + o +
 + + + + + + + + o +

*/
