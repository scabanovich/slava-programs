package slava.puzzles.seabattlef.model;

import slava.puzzles.seabattle.model.AbstractSeaBattlePuzzle;

public class SeaBattleFirstPuzzle extends AbstractSeaBattlePuzzle implements SeaBattleFirstConstants {
	SeaBattleFirstModel model;
	int[][] bValues;
	
	public SeaBattleFirstPuzzle() {
	}
	
	public void setModel(SeaBattleFirstModel model) {
		this.model = model;
	}
	
	public void init() {
		field = model.getField();
		size = field.getSize();
		data = new int[size];
		for (int i = 0; i < size; i++) data[i] = EMPTY;
		bValues = new int[4][];
		bValues[0] = new int[field.getHeight()];
		bValues[1] = new int[field.getWidth()];
		bValues[2] = new int[field.getHeight()];
		bValues[3] = new int[field.getWidth()];
		for (int d = 0; d < 4; d++) {
			for (int i = 0; i < bValues[d].length; i++)	bValues[d][i] = -1;
		}
	}
	
	public int[] getData() {
		return data;
	}
	
	public int[] getBValues(int d) {
		return bValues[d];
	}
	
	public int[][] getBValues() {
		return bValues;
	}
	
	public int getTotalShipArea() {
		int c = 0;
		for (int i = 1; i < shipCount.length; i++) {
			c += shipCount[i] * i;
		}
		return c;
	}
	
}
