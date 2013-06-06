package com.slava.patience.piramida;

import java.util.HashMap;
import java.util.Map;

public class PiramidaSolver implements PiramidaField {
	static int[] WEIGHTS = {10, 20, 40, 80, 160, 320, 640, 960, 1440, 2160, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100};
	Cards cards;
	int[] pack;

	int[] piramid;
	int[] reserve;
	int[] base;
	
	int[] piramidState;
	int reserveIndex;
	int baseIndex;
	int piramidIndex;
	
	int t;
	int[] weightDeltas;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;
	int weight;
	
	int maxWeight;
	
	Map codes = new HashMap();
	
	public PiramidaSolver() {}
	
	public void setCards(Cards cards) {
		this.cards = cards;
	}
	
	public void setPack(int[] pack) {
		this.pack = pack;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		piramidState = new int[piramidSize];
		piramid = new int[piramidSize];
		for (int i = 0; i < piramidSize; i++) piramid[i] = pack[i];
		reserve = new int[reserveSize];
		for (int i = 0; i < reserveSize; i++) reserve[i] = pack[i + piramidSize];
		reserveIndex = 0;
		base = new int[pack.length];
		base[0] = pack[pack.length - 1];
		baseIndex = 0;
		
		weightDeltas = new int[pack.length + 1];
		way = new int[pack.length + 1];
		wayCount = new int[pack.length + 1];
		ways = new int[pack.length + 1][20];
		weightDeltas[0] = 0;
		
		t = 0;
		weight = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		for (int i = 0; i < 3; i++) if(!isAccessible(i)) return;
		
		Long code = new Long(getCode());
		Integer w = (Integer)codes.get(code);
		if(w != null && w.intValue() >= weight) return;
		codes.put(code, new Integer(weight));
//		if(codes.size() % 100000 == 0) System.out.println("-->" + codes.size() + ":" + code);
		
		for (int p = 0; p < piramidSize; p++) {
			if(piramidState[p] == 1) continue;
			if(isOpen(p) && isMatching(p)) {
				ways[t][wayCount[t]] = p;
				wayCount[t]++;
			}
		}
		if(reserveIndex < reserveSize) {
			ways[t][wayCount[t]] = -1;
			wayCount[t]++;
		}
		if(t < 10) randomize();
	}
	
	void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = wayCount[t] - 1; i >= 1; i--) {
			int j = (int)((i + 1) * Math.random());
			if(i == j) continue;
			int c = ways[t][i];
			ways[t][i] = ways[t][j];
			ways[t][j] = c;			
		}
//		if(wayCount[t] > 4) wayCount[t] = 4;
	}
	
	boolean isOpen(int p) {
		int[] ns = upperNeighbours[p];
		for (int i = 0; i < ns.length; i++) {
			if(piramidState[ns[i]] == 0) return false;
		}
		return true;
	}
	
	boolean isMatching(int p) {
		return cards.areValuesNaighbouring(piramid[p], base[baseIndex]);
	}
	
	boolean isAccessible(int p) {
		if(piramidState[p] == 1) return true;
		int v = piramid[p];
		for (int i = 0; i < piramidState.length; i++) {
			if(piramidState[i] > 0) continue;
			int u = piramid[i];
			if(cards.areValuesNaighbouring(v, u)) return true;
		}
		for (int i = reserveIndex; i < reserve.length; i++) {
			int u = reserve[i];
			if(cards.areValuesNaighbouring(v, u)) return true;
		}
		int u = base[baseIndex];
		if(cards.areValuesNaighbouring(v, u)) return true;
		return false;
	}
	
	void move() {
		int w = ways[t][way[t]];
		if(w < 0) {
			baseIndex++;
			base[baseIndex] = reserve[reserveIndex];
			reserveIndex++;
			weightDeltas[t + 1] = 0;
		} else {
			piramidIndex++;
			piramidState[w] = 1;
			baseIndex++;
			base[baseIndex] = piramid[w];
			weight += WEIGHTS[weightDeltas[t]];
			weightDeltas[t + 1] = weightDeltas[t] + 1;
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int w = ways[t][way[t]];
		if(w < 0) {
			reserveIndex--;
			baseIndex--;
		} else {
			piramidIndex--;
			piramidState[w] = 0;
			baseIndex--;
			weight -= WEIGHTS[weightDeltas[t]];
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
			if(piramidIndex == piramidSize && weight > maxWeight) {
				maxWeight = weight;
				System.out.println("Solution " + weight);
				printSolution();
			}
		}		
	}
	
	void printSolution() {
		for (int i = 0; i < t; i++) {
			int w = ways[i][way[i]];
			if(w < 0) {
				System.out.print(" -");
			} else {
				int c = piramid[w];
				System.out.print(" " + cards.getCardDesignation(c));
			}
		}
		System.out.println("");
	}

	long getCode() {
		long c = 0;
		for (int i = 0; i < piramidSize; i++) {
			c = c * 2 + piramidState[i];
		}
		c = c * reserveSize + reserveIndex;
		c = c * cards.packSize + base[baseIndex];
		c = c * 20 + weightDeltas[t];
		return c;
	}

}
