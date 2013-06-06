package slava.puzzle.sudoku.model;

import slava.puzzle.template.model.PuzzleModel;

public class SudokuModel extends PuzzleModel {
	SudokuDesignField field;
	SudokuProblemInfo problem;
	SudokuModelListener listener;
	
	public SudokuModel() {
		field = new SudokuDesignField();
		problem = new SudokuProblemInfo();
		setSize(9);
		setLoader(new SudokuLoader());
	}
	
	public void setSize(int width) {
		field.setSize(width);
		problem.setSize(field.getSize());
		setSolutionInfo(null);
		if(listener != null) listener.fieldResized();
	}
	
	public SudokuDesignField getField() {
		return field;
	}
	
	public SudokuProblemInfo getProblemInfo() {
		return problem;
	}
	
	public void addListener(SudokuModelListener listener) {
		this.listener = listener;
	}

}
