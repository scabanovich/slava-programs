package slava.puzzle.paths.walker.analysis;

import slava.puzzle.paths.walker.model.WalkerPathsPuzzle;
import com.slava.common.RectangularField;

public class WalkerPathsPuzzleCheck {

	public static String getPuzzleError(WalkerPathsPuzzle puzzle) {
		RectangularField f = puzzle.getModel().getField();
		int[] filter = puzzle.getFilter();
		int[] cs = new int[2];
		for (int p = 0; p < filter.length; p++) {
			if(filter[p] == 1) continue;
			int d = (f.getX(p) + f.getY(p)) % 2;
			cs[d]++;
		}
		if(cs[0] != cs[1]) {
			return "Number of black and white cells must be the same: " + cs[0] + " " + cs[1] + ".";
		}
		for (int p = 0; p < filter.length; p++) {
			if(filter[p] == 1) continue;
			int u = 0;
			for (int d = 0; d < 4; d++) {
				if(puzzle.getParts()[p][d] == 1) u++;
			}
			if(u > 2) {
				return "Forks and crossings are not permitted.";
			}
		}
	
		return null;
	} 
	
	static int generatePlace(RectangularField f, int[] filter, int evenness) {
		while(true) {
			int p = (int)(Math.random() * f.getSize());
			if(filter[p] == 1) continue;
			int op = (f.getX(p) + f.getY(p)) % 2;
			if(evenness >= 0 && op != evenness) continue;
			return p;
		}
	}

}
