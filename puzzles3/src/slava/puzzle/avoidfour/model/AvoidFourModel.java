package slava.puzzle.avoidfour.model;

import slava.puzzle.template.model.*;

public class AvoidFourModel extends PuzzleModel {
	public static int DRAWING_FIELD_MODE = 0;
	public static int SETTING_PUZZLE_MODE = 1;
	AvoidFourField field;
	AvoidFourPuzzle puzzle;
	int mode = DRAWING_FIELD_MODE;
	
	public AvoidFourModel() {
		field = new AvoidFourField();
		field.setSize(12, 12);
		puzzle = new AvoidFourPuzzle();
		puzzle.setModel(this);
		puzzle.init();
		setLoader(new AvoidFourLoader());
	}
	
	public AvoidFourField getField() {
		return field;
	}
	
	public AvoidFourPuzzle getPuzzleInfo() {
		return puzzle;
	}
	
	public int getMode() {
		return mode;
	}
	
	public boolean isSettingPuzzleMode() {
		return mode == SETTING_PUZZLE_MODE;
	}
	
	public boolean isDrawingFieldMode() {
		return mode == DRAWING_FIELD_MODE;
	}
	
	public void setSettingPuzzleMode() {
		mode = SETTING_PUZZLE_MODE;
	}
	
	public void setDrawingFieldMode() {
		if(mode == DRAWING_FIELD_MODE) return;
		mode = DRAWING_FIELD_MODE;
		puzzle.clearPuzzle();
	}

}
