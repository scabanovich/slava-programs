package com.slava.hashi;

import com.slava.common.RectangularField;

public class HashiRandomGenerator {
	RectangularField field;
	int linesAmount;
	
	int[] problem;
	int[] state;
	int[] excluded;
	int addedLines;

	boolean failure = false;
	
	public HashiRandomGenerator() {}

	public void setField(RectangularField f) {
		field = f;
	}
	
	public void setLinesAmount(int s) {
		linesAmount = s;
	}
	
	public void generate() {
		problem = new int[field.getSize()];
		state = new int[field.getSize()];
		excluded = new int[field.getSize()];
		addedLines = 0;
		while(!insertFirst()) {}
		while(addedLines < linesAmount && !failure) {
			insert();
		}
		if(failure) problem = null;
	}
	
	boolean insertFirst() {
		int p = (int)(problem.length * Math.random());
		if(!isNode(field, p)) return false;
		return insert(p);
	}
	
	static double NARROW_BRIDGE_PROBABILITY = 0.65;
	
	boolean insert(int p) {
		if(!isNode(field, p)) return false;
		int d = (int)(4 * Math.random());
		int length = getLength(p, d);
		if(length < 2) return false;
		int l = 2 + (int)((length - 1) * Math.random());
		int q = field.jump(p, d, l);
		if(!isNode(field, q) || excluded[q] == 1) return false;
		int bridgeSize = Math.random() < NARROW_BRIDGE_PROBABILITY ? 1 : 2;
		//discourage massive nodes
		if(!accept(p, bridgeSize) || !accept(q, bridgeSize)) return false;
		add(p, d, q, bridgeSize);
		return true;
	}
	
	boolean accept(int p, int addedBriges) {
		int sum = problem[p] + addedBriges;
		if(sum >= 6 || sum >= 2 * getNeighbourCount(p)) return false;
		if(sum >= 4 && Math.random() >= 0.7) return false;
		return true;
	}
	int getNeighbourCount(int p) {
		int n = 0;
		for (int d = 0; d < 4; d++) {
			if(field.jump(p, d) >= 0) n++;
		}
		return n;
	}
	
	void add(int p, int d, int q, int bridgeSize) {
		problem[p] += bridgeSize;
		problem[q] += bridgeSize;
		int u = field.jump(p, d);
		while(u != q) {
			state[u] = 1;
			u = field.jump(u, d);
		}
		for (int d1 = 0; d1 < 4; d1++) {
			int r = field.jump(p, d1);
			if(r >= 0) excluded[r] = 1;
			r = field.jump(q, d1);
			if(r >= 0) excluded[r] = 1;
		}
		addedLines += bridgeSize;
	}
	
	void insert() {
		int attempt = 0;
		while(!insert(getPlace())) {
			attempt++;
			if(attempt > 500) {
				failure = true;
				return;
			}
		}
	}
	
	int getPlace() {
		while(true) {
			int p = (int)(problem.length * Math.random());
			if(problem[p] > 0 && problem[p] < 5) {
				//discourage massive nodes
				if(problem[p] >= 3 && Math.random() >= 0.6) continue;
				return p;
			}
		}
	}
	
	int getLength(int p, int d) {
		int length = 0;
		int q = field.jump(p, d);
		while(q >= 0 && state[q] == 0) {
			length++;
			if(problem[q] > 0) break;
			q = field.jump(q, d);
		}
		return length;
	}
	
	static boolean isNode(RectangularField field, int p) {
		return true;//(field.getX(p) + field.getY(p)) % 2 == 0;
	}
	
}
