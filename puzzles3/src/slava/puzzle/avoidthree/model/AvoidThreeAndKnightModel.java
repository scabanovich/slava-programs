package slava.puzzle.avoidthree.model;

import slava.puzzle.template.model.PuzzleModel;

import com.slava.common.TwoDimField;

public class AvoidThreeAndKnightModel extends PuzzleModel {
	public static int DRAWING_FIELD_MODE = 0;
	public static int SETTING_PUZZLE_MODE = 1;
	
	TwoDimField field;
	KnightField knightField;
	
	AvoidThreeAndKnightPuzzle puzzle;
	int mode = DRAWING_FIELD_MODE;

	public AvoidThreeAndKnightModel() {
		int width = 10;
		int height = 10;
		
		field = new TwoDimField();
		field.setOrts(TwoDimField.DIAGONAL_ORTS);
		field.setSize(width, height);
		
		knightField = new KnightField();
		knightField.setSize(width, height);
		
		puzzle = new AvoidThreeAndKnightPuzzle();
		puzzle.setModel(this);
		puzzle.init();
		
		setLoader(new AvoidThreeAndKnightLoader());

	}
	
	public TwoDimField getField() {
		return field;
	}

	public TwoDimField getKnightField() {
		return knightField;
	}
	
	public AvoidThreeAndKnightPuzzle getPuzzleInfo() {
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
