package slava.crossword.runtime.filter;

import slava.crossword.runtime.ISolutionEstimate;
import slava.crossword.runtime.IWordFilter;

public class WordFilter implements IWordFilter, ISolutionEstimate {
	
	int[] weights = new int[]{
		2,0,0,0,0,2,0,0,0,2,0,0,0,3,4,
		2,3,0,0,3,0,0,0,0,5,0,0,0,0,0,
		0,0,0
	};
	
	int MAX_ZERO_LETTERS = 1;
	
	public boolean accept(byte[] w) {
		int k = 0;
		for (int i = 0; i < w.length; i++) {
			int c = (int)w[i];
			if(c < 0 || c >= weights.length) return false;
			if(weights[c] == 0) ++k;
		}
		return k <= MAX_ZERO_LETTERS;
	}

	public int estimate(byte[] values) {
		int k = 0;
		for (int i = 0; i < values.length; i++) {
			int c = (int)values[i];
			if(c < 0 || c >= weights.length) continue;
			k += weights[c];
		}
		return k;
	}

}
