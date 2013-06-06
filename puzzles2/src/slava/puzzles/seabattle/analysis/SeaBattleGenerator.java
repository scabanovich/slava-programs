package slava.puzzles.seabattle.analysis;

import slava.puzzles.seabattle.model.SeaBattleConstants;
import slava.puzzles.seabattle.model.SeaBattlePuzzle;

import com.slava.common.RectangularField;

public class SeaBattleGenerator implements SeaBattleConstants {
	SeaBattleRandomFiller filler = new SeaBattleRandomFiller();
	SeaBattlePuzzle puzzle;
	RectangularField field;
	
	int mode = GENERSTE_BY_REDUCING;	
	
	public SeaBattleGenerator() {}

	public void setField(RectangularField field) {
		this.field = field;
		filler.setField(field);
	}

	public void setPuzzle(SeaBattlePuzzle puzzle) {
		this.puzzle = puzzle;
		filler.setShips(puzzle.getShips());
	}
	
	public void setMode(int s) {
		mode = s;
	}

	public void generate() {
		if(mode == GENERSTE_BY_REDUCING) {
			generateByReducing();
		} else if(mode == GENERSTE_BY_NAROWING_WITH_ALL) {
			generateByNarrowing(false);
		} else if(mode == GENERSTE_BY_NAROWING_WITH_WATER_ONLY) {
			generateByNarrowing(true);
		}
	}

	public void generateByReducing() {
		while(filler.getSolution() == null) {
			filler.solve();
		}
		applyPuzzle();
		reduce();
	}
	
	void applyPuzzle() {
		int[] hValues = puzzle.getHValues();
		for (int i = 0; i < hValues.length; i++) hValues[i] = filler.hUsage[i];
		int[] vValues = puzzle.getVValues();
		for (int i = 0; i < vValues.length; i++) vValues[i] = filler.vUsage[i];
		int[] solution = filler.getSolution();
		int[] data = puzzle.getData();
		for (int p = 0; p < data.length; p++) {
			if(solution[p] > 0) {
				data[p] = SeaBattleConstants.EMPTY;
			} else {
				data[p] = SeaBattleConstants.WATER;
			}
		}
	}
	
	void reduce() {
		int[] order = new int[field.getSize()];
		for (int i = 0; i < order.length; i++) order[i] = i;
		for (int i = 0; i < order.length; i++) {
			int j = (int)(Math.random() * (order.length - i)) + i;
			int c = order[i]; order[i] = order[j]; order[j] = c;
		}
		int[] data = puzzle.getData();
		for (int i = 0; i < order.length; i++) {
			int p = order[i];
			if(data[p] != SeaBattleConstants.WATER) continue;
			data[p] = SeaBattleConstants.EMPTY;
			SeaBattleSolver solver = new SeaBattleSolver();
			solver.setField(field);
			solver.setPuzzle(puzzle);
			solver.setSolutionLimit(2);
			solver.solve();
			if(solver.getSolutionCount() != 1) {
				data[p] = SeaBattleConstants.WATER;
			}
			System.out.println("-->" + solver.getSolutionCount());
		}
	}
	
	/**
	 * Generatte random position, start with only side numbers,
	 * and add data to narrowest places.
	 *
	 */
	
	public void generateByNarrowing(boolean waterOnly) {
		while(filler.getSolution() == null) {
			filler.solve();
		}
		applyPuzzle2();
		reduce2(waterOnly);
	}

	void applyPuzzle2() {
		int[] hValues = puzzle.getHValues();
		for (int i = 0; i < hValues.length; i++) hValues[i] = filler.hUsage[i];
		int[] vValues = puzzle.getVValues();
		for (int i = 0; i < vValues.length; i++) vValues[i] = filler.vUsage[i];
		int[] data = puzzle.getData();
		for (int p = 0; p < data.length; p++) {
			data[p] = SeaBattleConstants.EMPTY;
		}
	}
	
	void reduce2(boolean waterOnly) {
		int[] data = puzzle.getData();
		while(true) {
			SeaBattleSolver solver = new SeaBattleSolver();
			solver.setField(field);
			solver.setPuzzle(puzzle);
			solver.setSolutionLimit(10000);
			solver.solve();
			if(solver.getSolutionCount() < 2) {
				return;
			}
			int[] narrowest = waterOnly ? solver.getNarrowestDataWaterOnly()
					: solver.getNarrowestData();
			int p = narrowest[0];
			int v = narrowest[1];
			int c = narrowest[2];
			data[p] = v;
			System.out.println("-->" + solver.getSolutionCount() + ": " + p + " " + v + " " + c);
		}
	}
	
}
