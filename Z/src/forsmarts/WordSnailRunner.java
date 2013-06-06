package forsmarts;

import com.slava.common.RectangularField;

/**
 * Season 8. Issue 87 (28). Problem 17.
 * Word snail (6+6)
 * Write all the given words into each grid, following the spiral 
 * (all words are used in each grid). Words must be separated 
 * by at least one empty cell. Letters cannot appear more than 
 * once in any row or column.
 * 
 * @author glory
 *
 */
public class WordSnailRunner {
	
	public static void main(String[] args) {
		RectangularField f = new RectangularField();
		f.setSize(9, 9);
		
		WordSnailSolver solver = new WordSnailSolver();
		solver.setField(f);
		solver.setWords(WORDS);
		solver.setSolutionRestriction(new SolutionRestrictionImpl(76, 'R', solver.words));
		
		solver.solve();
		System.out.println("Solution Count=" + solver.solutionCount + " Tree Count=" + solver.treeCount);
		
	}
	
	static String[] WORDS = {
		"BELARUS", "EXAMPLE", "EXPERIMENT", "FORSMARTS", "PENTAMINO", "SPORT"
	};
	
	static class SolutionRestrictionImpl implements WordSnailSolver.SolutionRestriction {
		int p;
		int ci;
		
		SolutionRestrictionImpl(int p, char ch, WordsCache words) {
			this.p = p;
			ci = words.getIndex(ch);
		}

		public boolean accept(WordSnailSolver solver) {
			if(solver.margin[p] > 0) return false;
			if(solver.state[p] >= 0 && solver.state[p] != ci) return false;
			if(solver.state[p] < 0 && !hasMoreWords(solver)) return false;
			return true;
		}
		boolean hasMoreWords(WordSnailSolver solver) {
			for (int i = 0; i < solver.wordUsage.length; i++) {
				if(solver.wordUsage[i] == 0) return true;
			}
			return false;
		}
		
	}

}

/*
 
a)
 + + + P E N T A M
 + + + E X A M P I
 + S + + + + + L N
 + U T + S P + E O
 + R N + + O E + +
 S A E + T R X + +
 T L M I R E P + +
 R E B + + + + + +
 A M S R O F + + +

Key: AEM--P-PM


b)
 + + + F O R S M A
 P L E + + + + S R
 M O + + + E X P T
 A N + + + + P O S
 X I + + + + E R +
 E M + + + + R T +
 + A T N E M I + +
 + T N E P + + + +
 + + S U R A L E B

Key: -TT---XSA

*/