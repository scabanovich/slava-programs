package com.slava.knight;

public class PairRestriction implements IPairRestriction {
	int[] pairs;

	public void setPairs(int[] pairs) {
		this.pairs = pairs;
	}

	public boolean accept(int[] state, int p, int q) {
		return !isPath(state, p, q);
	}

	public boolean isPath(int[] state, int start, int end) {
		boolean b = true;
		int p = start;
		while(p >= 0) {			
			p = (b) ? pairs[p] : state[p];
			if(p == end) return true;
			b = !b;
		}
		return false;
	}

}
