package slava.puzzles.seabattlef.analysis;

import slava.puzzles.seabattle.analysis.SeaBattleRandomFiller;
import slava.puzzles.seabattle.model.SeaBattleConstants;
import slava.puzzles.seabattlef.model.SeaBattleFirstPuzzle;

import com.slava.common.RectangularField;

public class SeaBattleFirstGenerator {
	SeaBattleRandomFiller filler = new SeaBattleRandomFiller();
	SeaBattleFirstPuzzle puzzle;
	RectangularField field;

	public void setField(RectangularField field) {
		this.field = field;
		filler.setField(field);
	}

	public SeaBattleFirstGenerator() {}

	public void setPuzzle(SeaBattleFirstPuzzle puzzle) {
		this.puzzle = puzzle;
		filler.setShips(puzzle.getShips());
	}
	
	public void generate() {
		int attemptCount = 0;
		while(true) {
			filler.setShips(puzzle.getShips());
			filler.solve();
			while(filler.getSolution() == null) {
				filler.solve();
			}
			applyPuzzle();
			if(checkPuzzle()) {
				break;
			}
			attemptCount++;
		}
		System.out.println("Attempts=" + attemptCount);
	}	
	

	void applyPuzzle() {
		int[] solution = filler.getSolution();
		for (int ix = 0; ix < field.getWidth(); ix++) {
			int p = field.getIndex(ix, 0);
			int s = findShip(p, 1, solution);
			puzzle.getBValues(3)[ix] = s;
			p = field.getIndex(ix, field.getHeight() - 1);
			s = findShip(p, 3, solution);
			puzzle.getBValues(1)[ix] = s;			
		}
		for (int iy = 0; iy < field.getWidth(); iy++) {
			int p = field.getIndex(0, iy);
			int s = findShip(p, 0, solution);
			puzzle.getBValues(2)[iy] = s;
			p = field.getIndex(field.getWidth() - 1, iy);
			s = findShip(p, 2, solution);
			puzzle.getBValues(0)[iy] = s;			
		}	
		int[] data = puzzle.getData();
		for (int p = 0; p < data.length; p++) {
			data[p] = SeaBattleConstants.EMPTY;
		}
	}
	
	int findShip(int p, int d, int[] solution) {
		while(p >= 0) {
			if(solution[p] > 0) return solution[p];
			p = field.jump(p, d);
		}
		return -1;
	}
	
	boolean checkPuzzle() {
		for (int d = 0; d < 4; d++) {
			int[] vs = puzzle.getBValues(d);
			for (int i = 0; i < vs.length; i++) {
				if(vs[i] < 0) return false;
			}
		}
		SeaBattleFirstSolver solver = new SeaBattleFirstSolver();
		solver.setField(field);
		solver.setPuzzle(puzzle);
		solver.setSolutionLimit(2);
		solver.solve();
		if(solver.getSolutionCount() == 0) {
			System.out.println("Panick: generated puzzle has no solution!");
		}
		return solver.getSolutionCount() == 1;
	}

}
