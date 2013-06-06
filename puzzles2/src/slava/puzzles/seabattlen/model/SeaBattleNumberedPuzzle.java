package slava.puzzles.seabattlen.model;

import slava.puzzles.seabattle.model.AbstractSeaBattlePuzzle;
import slava.puzzles.seabattle.model.SeaBattleConstants;

public class SeaBattleNumberedPuzzle extends AbstractSeaBattlePuzzle implements SeaBattleConstants {
	SeaBattleNumberedModel model;
	int[] numbers;
	
	public SeaBattleNumberedPuzzle() {
	}
	
	public void setModel(SeaBattleNumberedModel model) {
		this.model = model;
	}
	
	public void init() {
		field = model.getField();
		size = field.getSize();
		data = new int[size];
		for (int i = 0; i < size; i++) data[i] = EMPTY;
		hValues = new int[field.getHeight()];
		for (int i = 0; i < hValues.length; i++) hValues[i] = -1;
		vValues = new int[field.getWidth()];
		for (int i = 0; i < vValues.length; i++) vValues[i] = -1;
		numbers = new int[field.getSize()];
		for (int i = 0; i < numbers.length; i++) numbers[i] = -1;
	}
	
	public int[] getNumbers() {
		return numbers;
	}
	
}
