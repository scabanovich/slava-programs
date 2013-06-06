package slava.puzzle.football.model;

import slava.puzzle.template.model.PuzzleModel;

public class FootballModel extends PuzzleModel {
	protected FootballField field;
	protected int[] state;
	protected int generatedPathLength = 9;
	
	public FootballModel() {
		init();
	}
	
	protected void init() {
		field = new FootballField();
		field.setSize(8, 5);
		setLoader(new FootballLoader());
	}
	
	public FootballField getField() {
		return field;
	}
	
	public void setState(int[] state) {
		this.state = state;
	}
	
	public int getStateAt(int p) {
		return state == null ? 0 : state[p]; 
	}
	
	/*
	 * Property used by generator
	 */
	
	public void setGeneratedPathLength(int generatedPathLength) {
		this.generatedPathLength = generatedPathLength;
	}
	
	public int getGeneratedPathLength() {
		return generatedPathLength;
	}
	
}
