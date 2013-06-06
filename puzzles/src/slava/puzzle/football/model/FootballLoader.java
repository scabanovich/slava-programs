package slava.puzzle.football.model;

import slava.puzzle.template.model.PuzzleLoader;

public class FootballLoader extends PuzzleLoader {

	public FootballLoader() {
		init();
	}
	protected void init() {
		setRoot("/data/football");
		initName("football_");
	}

}
