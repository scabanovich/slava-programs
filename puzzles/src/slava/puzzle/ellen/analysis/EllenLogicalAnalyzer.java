package slava.puzzle.ellen.analysis;

import slava.puzzle.pentaletters.analysis.PentaLettersLogicalAnalyzer;

public class EllenLogicalAnalyzer extends PentaLettersLogicalAnalyzer {

	protected void initChecker() {
		setChecker(new EllenFieldChecker());
		figureSize = 4;
	}

	/*
	 * This method decides if figure can be placed at position p
	 */
	
	protected int[] createPlacement(int f, int p) {
		int q = 0;
		int[] placement = new int[figureSize];
		for (int i = 0; i < figures[f].length; i++) {
			int k = field.jump(p, figures[f][i][0], figures[f][i][1]);
			if(k < 0) return null;
			int v = field.getLetterAt(k);
			if(v == 1) q++;
			placement[i] = k;
		}
		if(q != 2) return null;
		return placement;
	}


}
