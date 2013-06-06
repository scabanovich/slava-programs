package slava.puzzles.seabattlen.analysis;

import slava.puzzles.seabattle.analysis.SeaBattleRandomFiller;
import slava.puzzles.seabattle.model.SeaBattleConstants;
import slava.puzzles.seabattlen.model.SeaBattleNumberedPuzzle;

import com.slava.common.RectangularField;

public class SeaBattleNumberedGenerator {
	SeaBattleRandomFiller filler = new SeaBattleRandomFiller();
	SeaBattleNumberedPuzzle puzzle;
	RectangularField field;

	public void setField(RectangularField field) {
		this.field = field;
		filler.setField(field);
	}

	public SeaBattleNumberedGenerator() {}

	public void setPuzzle(SeaBattleNumberedPuzzle puzzle) {
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
			generateNumbers();
			applyPuzzle();
			if(checkPuzzle()) {
				break;
			}
			attemptCount++;
		}
		System.out.println("Attempts=" + attemptCount);
	}
	
	void generateNumbers() {
		for (int i = 0; i < field.getSize(); i++) {
			puzzle.getNumbers()[i] = (int)(Math.random() * 9) + 1;
		}
	}
	

	void applyPuzzle() {
		int[] solution = filler.getSolution();
		int[] hValues = puzzle.getHValues();
		int[] vValues = puzzle.getVValues();
		for (int ix = 0; ix < field.getWidth(); ix++) {
			vValues[ix] = 0;
			for (int iy = 0; iy < field.getHeight(); iy++) {
				int p = field.getIndex(ix, iy);
				if(solution[p] > 0) vValues[ix] += puzzle.getNumbers()[p];
			}
		}
		for (int iy = 0; iy < field.getHeight(); iy++) {
			hValues[iy] = 0;
			for (int ix = 0; ix < field.getWidth(); ix++) {
				int p = field.getIndex(ix, iy);
				if(solution[p] > 0) hValues[iy] += puzzle.getNumbers()[p];
			}
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
		//TODO
//		SeaBattleFirstSolver solver = new SeaBattleFirstSolver();
//		solver.setField(field);
//		solver.setPuzzle(puzzle);
//		solver.setSolutionLimit(2);
//		solver.solve();
//		if(solver.getSolutionCount() == 0) {
//			System.out.println("Panick: generated puzzle has no solution!");
//		}
//		return solver.getSolutionCount() == 1;
		return true;
	}

}
