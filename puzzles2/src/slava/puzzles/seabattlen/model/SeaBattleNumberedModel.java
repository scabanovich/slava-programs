package slava.puzzles.seabattlen.model;

import slava.puzzles.seabattle.model.AbstractSeaBattleModel;

public class SeaBattleNumberedModel extends AbstractSeaBattleModel {
	SeaBattleNumberedPuzzle puzzle;
//	SeaBattlePreferences preferences = new SeaBattlePreferences();
	
	public SeaBattleNumberedModel() {
		super();
		puzzle = new SeaBattleNumberedPuzzle();
		puzzle.setModel(this);
		puzzle.init();
		setLoader(new SeaBattleNumberedLoader());
	}
	
	public SeaBattleNumberedPuzzle getPuzzleInfo() {
		return puzzle;
	}
	
//	public SeaBattlePreferences getPreferences() {
//		return preferences;
//	}
	
	public void changeFieldSize(int width, int height) {
		super.changeFieldSize(width, height);
		puzzle.init();
	}

}
