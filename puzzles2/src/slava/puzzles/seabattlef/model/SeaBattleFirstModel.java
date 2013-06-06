package slava.puzzles.seabattlef.model;

import slava.puzzles.seabattle.model.AbstractSeaBattleModel;

public class SeaBattleFirstModel extends AbstractSeaBattleModel {
	SeaBattleFirstPuzzle puzzle;
//	SeaBattlePreferences preferences = new SeaBattlePreferences();
	
	public SeaBattleFirstModel() {
		super();
		puzzle = new SeaBattleFirstPuzzle();
		puzzle.setModel(this);
		puzzle.init();
		setLoader(new SeaBattleFirstLoader());
	}
	
	public SeaBattleFirstPuzzle getPuzzleInfo() {
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
