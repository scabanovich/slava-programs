package slava.puzzle.ellen.analysis;

import slava.puzzle.pentaletters.analysis.PentaLettersAnalyzer;

public class EllenAnalyzer extends PentaLettersAnalyzer {
	

	protected void initChecker() {
		setChecker(new EllenFieldChecker());
		figureSize = 4;
	}

	protected boolean checkFigure(int f, int p) {
		int q = 0;
		for (int i = 0; i < figures[f].length; i++) {
			int k = field.jump(p, figures[f][i][0], figures[f][i][1]);
			if(k < 0 || groups[k] >= 0) return false;
			int v = field.getLetterAt(k);
			if(v == 1) q++;
		}
		if(!ignoreLetters && q != 2) return false;
		return true;
	}

}
