package slava.puzzles.seabattle.model;

public class SeaBattlePuzzle extends AbstractSeaBattlePuzzle implements SeaBattleConstants {
	SeaBattleModel model;

	public SeaBattlePuzzle() {
	}
	
	public void setModel(SeaBattleModel model) {
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
	}
	
	public int getTotalShipArea() {
		int c = 0;
		for (int i = 1; i < shipCount.length; i++) {
			c += shipCount[i] * i;
		}
		return c;
	}
	
}
