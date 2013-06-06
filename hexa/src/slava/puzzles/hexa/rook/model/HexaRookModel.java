package slava.puzzles.hexa.rook.model;

import java.util.ArrayList;
import slava.puzzle.template.model.PuzzleModel;
import slava.puzzles.hexa.common.HexaField;

public class HexaRookModel extends PuzzleModel {
	HexaField field;
	HexaRookPuzzle puzzle;
	protected ArrayList solutions = new ArrayList();
	protected int currentSolution = -1;

	public HexaRookModel() {
		field = new HexaField();
		field.setSideLength(5);
		puzzle = new HexaRookPuzzle();
		puzzle.setModel(this);
		setLoader(new HexaRookLoader());
	}
	
	public HexaField getField() {
		return field;
	}
	
	public HexaRookPuzzle getPuzzleInfo() {
		return puzzle;
	}

	public boolean isShowingSolution() {
		return currentSolution >= 0;
	}
	
	public void setSolutions(ArrayList list) {
		solutions.clear();
		solutions.addAll(list);
		if(solutions.size() == 0) {
			currentSolution = -1;
		} else {
			currentSolution = 0;
		}		
	}
	
	public int[] getSelectedSolution() {
		return currentSolution < 0 ? null : (int[])solutions.get((currentSolution));
	}
	
	public void nextSolution() {
		if(currentSolution < 0) return;
		currentSolution++;
		if(currentSolution == solutions.size()) currentSolution = 0;
	}
	
	public void prevSolution() {
		if(currentSolution < 0) return;
		currentSolution--;
		if(currentSolution < 0) currentSolution = solutions.size() - 1;
	}
	
	public void clearSolutions() {
		setSolutionInfo(null);
		solutions.clear();
		currentSolution = -1;
	}
	
	public void changeFieldSize(int sideLength) {
		clearSolutions();
		field.setSideLength(sideLength);
		puzzle.init();
	}
	
	public void clean() {
		clearSolutions();
		for (int p = 0; p < field.getSize(); p++) {
			puzzle.getPositions()[p] = HexaRookConstants.NOT_POSITION;
		}
	}

}
