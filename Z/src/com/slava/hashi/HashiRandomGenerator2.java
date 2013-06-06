package com.slava.hashi;

import com.slava.common.RectangularField;

public class HashiRandomGenerator2 {
	RectangularField field;
	int linesAmount;
	
	int[] problem;
	int[] excluded;
	
	public HashiRandomGenerator2() {}

	public void setField(RectangularField f) {
		field = f;
	}
	
	public void setLinesAmount(int s) {
		linesAmount = s;
	}
	
	public void generate() {
		problem = new int[field.getSize()];
		excluded = new int[field.getSize()];
		fill();
		putNumbers();
		reduceBigNumbers();
		provideEvenness();
	}
	
	private void fill() {
		int c = 0;
		while(true) {
			int p = (int)(problem.length * Math.random());
			while(problem[p] > 0 || excluded[p] > 0) {
				c++;
				if(c == 200) return;
				p = (int)(problem.length * Math.random());
			}
			c = 0;
			problem[p] = 1;
			for (int d = 0; d < 4; d++) {
				int q = field.jump(p, d);
				if(q >= 0) excluded[q] = 1;
			}
		}
	}
	
	private void putNumbers() {
		for (int p = 0; p < problem.length; p++) {
			if(problem[p] == 0) continue;
			int nc = getNeighborCount(p);
			if(nc == 0) problem[p] = 0;
			int max = nc * 2;
			if(max > 5) max = 5;
			max--;
			problem[p] = 1 + (int)(Math.random() * max);
			if(problem[p] > 5) problem[p] = 5;
			if(problem[p] == 1 && Math.random() > 0.2) problem[p]++;
		}
	}
	
	private void reduceBigNumbers() {
		int changes = 1;
		while(changes > 0) {
			changes = 0;
			for (int p = 0; p < problem.length; p++) {
				if(problem[p] == 0) continue;
				int k = 0;
				for (int d = 0; d < 4; d++) {
					int q = getNeighbor(p, d);
					if(q < 0) continue;
					int delta = problem[q];
					if(delta > 2) delta = 2;
					k += delta;
				}
				if(problem[p] > k) {
					changes++;
					problem[p] = k;
				}
				if(problem[p] == k && k > 0) {
					if(Math.random() < 0.7) {
						changes++;
						problem[p]--;
					}
				}
			}
		}

	}
	
	private void provideEvenness() {
		int c = 0;
		for (int p = 0; p < problem.length; p++) {
			c += problem[p];
		}
		if(c % 2 == 0) return;
		int p = -1;
		while(p < 0 || problem[p] < 2) p = (int)(problem.length * Math.random());
		problem[p]--;
	}
	
	private int getNeighbor(int p, int d) {
		int q = field.jump(p, d);
		while(q >= 0 && problem[q] == 0) q = field.jump(q, d);
		return q;
	}
	private int getNeighborCount(int p) {
		int c = 0;
		for (int d = 0; d < 4; d++) {
			int q = getNeighbor(p, d);
			if(q >= 0) c++;
		}
		return c;
	}
}
