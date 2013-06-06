package slava.puzzles.seabattle.model;

public class SeaBattleModel extends AbstractSeaBattleModel {
	SeaBattlePuzzle puzzle;
	SeaBattlePreferences preferences = new SeaBattlePreferences();
	
	public SeaBattleModel() {
		super();
		puzzle = new SeaBattlePuzzle();
		puzzle.setModel(this);
		puzzle.init();
		setLoader(new SeaBattleLoader());
	}
	
	public SeaBattlePuzzle getPuzzleInfo() {
		return puzzle;
	}
	
	public SeaBattlePreferences getPreferences() {
		return preferences;
	}
	
	public void changeFieldSize(int width, int height) {
		super.changeFieldSize(width, height);
		puzzle.init();
	}

}
