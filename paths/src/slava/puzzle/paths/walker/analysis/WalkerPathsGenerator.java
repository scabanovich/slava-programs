package slava.puzzle.paths.walker.analysis;

import slava.puzzle.paths.walker.model.WalkerPathsPreferences;
import slava.puzzle.paths.walker.model.WalkerPathsPuzzle;
import com.slava.common.RectangularField;

public class WalkerPathsGenerator {
	protected RectangularField field;
	protected WalkerPathsSolver solver = new WalkerPathsSolver();
	protected WalkerPathsPuzzle puzzle;
	protected WalkerPathsPreferences preferences;

	public WalkerPathsGenerator() {
		solver.setRandomizing(true);
		solver.setSolutionLimit(100);
	}
	
	public void setField(RectangularField f) {
		field = f;
		solver.setField(f);
	}
	
	public void setTreeLimit(int s) {
		solver.setTreeCountLimit(s);
	}
	
	public void setPuzzle(WalkerPathsPuzzle puzzle) {
		this.puzzle = puzzle;
	}
	
	public void setPreferences(WalkerPathsPreferences p) {
		preferences = p;
	}
	
	public void generate() {
		int[] distr = new int[2];
		int attemptCount = 0;
		int scm = solver.solutionLimit + 1;
		while(true) {
			attemptCount++;
			int sc = attempt();
			if(sc == 0) {
				distr[0]++;
			} else if(sc >= solver.solutionLimit) {
				distr[1]++;
			} else if(sc < scm) {
				scm = sc;
				System.out.println(scm);
			}
			if(attemptCount % 10000 == 0) {
				System.out.println(attemptCount + " " + distr[0] + " " + distr[1] + " " + scm);
			}
			if(sc == 1) return;
		}
	}
	
	protected void cleanPuzzle() {
		int[] filter = puzzle.getFilter();
		for (int i = 0; i < filter.length; i++) {
			filter[i] = 0;
		}
		int[][] parts = puzzle.getParts();
		for (int p = 0; p < parts.length; p++) {
			for (int d = 0; d < 4; d++) {
				parts[p][d] = -1;
			}
		}
		int[] turns = puzzle.getTurns();
		for (int p = 0; p < turns.length; p++) {
			turns[p] = 0;
		}
	}
	
	int attempt() {
		cleanPuzzle();
		int[] filter = puzzle.getFilter();
		int[] ds = new int[2];
		for (int i = 0; i < field.getSize(); i++) {
			int s = (field.getX(i) + field.getY(i)) % 2;
			ds[s]++;
		}
		
		//Take from preferences
		int filtered = field.getSize() / 10;
		if(field.getWidth() == 10) filtered = 12;
		if((filtered + field.getSize()) % 2 == 1) filtered++;
		
		for (int k = 0; k < filtered; k++) {
			if(ds[0] > ds[1]) {
				int p = WalkerPathsPuzzleCheck.generatePlace(field, filter, 0);
				filter[p] = 1;
				ds[0]--;
			} else {
				int p = WalkerPathsPuzzleCheck.generatePlace(field, filter, 1);
				filter[p] = 1;
				ds[1]--;
			}
		}
		solver.setField(field);
		solver.setFilter(filter);
		solver.setParts(puzzle.getParts());
		solver.solve();
		return solver.getSolutionCount();
	}
	
}
