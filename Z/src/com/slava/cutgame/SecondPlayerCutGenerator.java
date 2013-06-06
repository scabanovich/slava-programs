package com.slava.cutgame;

public class SecondPlayerCutGenerator extends CutGenerator {
	int bestDelta;
	
	public SecondPlayerCutGenerator(State state, int minDelta) {
		this.state = state;
		this.minDelta = minDelta;
		generate();
	}
	
	public void generate() {
		int[] vs = state.getValues();
		cutCount = 0;
		cuts[cutCount] = new Cut();
		cuts[cutCount].execute(state, 0, 0);
		bestDelta = cuts[cutCount].getCut().getDelta();
		++cutCount;
		if(bestDelta <= minDelta) return;
		for (int i = 0; i < vs.length; i++) {
			if(i > 0 && vs[i] == vs[i - 1]) continue;
			int l = vs[i] / 2;
			for (int p = 1; p <= l; p++) {
				cuts[cutCount] = new Cut();
				cuts[cutCount].execute(state, i, p);
				int delta = cuts[cutCount].getCut().getDelta();
				++cutCount;
				if(delta < bestDelta) {
					bestDelta = delta;
					if(bestDelta <= minDelta) return;
				}
			}
		}
	}
	
	public int getBestDelta() {
		return bestDelta;
	}
	
}
