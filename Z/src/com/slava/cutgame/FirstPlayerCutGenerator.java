package com.slava.cutgame;

public class FirstPlayerCutGenerator extends CutGenerator {
	
	public FirstPlayerCutGenerator(State state, int minDelta) {
		this.state = state;
		this.minDelta = minDelta;
		generate();
	}

	public void generate() {
		int[] vs = state.getValues();
		cutCount = 0;
		if(state.getDelta() <= minDelta) return;
		Cut cut = new Cut();
		cut.execute(state, 0, 0);
		if(accept(cut)) {
			cuts[cutCount] = cut;
			++cutCount;
		}
		for (int i = 0; i < vs.length; i++) {
			if(i > 0 && vs[i] == vs[i - 1]) continue;
			int l = vs[i] / 2;
			for (int p = 1; p <= l; p++) {
				cut = new Cut();
				cut.execute(state, i, p);
				if(accept(cut)) {
					cuts[cutCount] = cut;
					++cutCount;
				}
			}
		}
	}
	
	private boolean accept(Cut cut) {
		SecondPlayerCutGenerator cut2 = new SecondPlayerCutGenerator(cut.getCut(), minDelta);
		return cut2.getBestDelta() > minDelta;
	}

}
