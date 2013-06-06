package com.slava.cutgame;

public class CutGenerator {
	protected State state;
	protected Cut[] cuts = new Cut[200];
	protected int minDelta;
	protected int cutCount;

	public int getCutCount() {
		return cutCount;
	}
	
	public Cut getCutAt(int i) {
		return cuts[i];
	}

}
