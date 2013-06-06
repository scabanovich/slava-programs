package slava.puzzles.pusher.solve;

import com.slava.common.RectangularField;

public class PusherField {
	RectangularField field;
	int[] walls;
	
	public PusherField() {}

	public void setField(RectangularField f) {
		field = f;
		walls = new int[f.getSize()];
	}

	public void addWall(int i) {
		walls[i] = 1;
	}

	public void removeWall(int i) {
		walls[i] = 0;
	}

	public RectangularField getField() {
		return field;
	}

	public boolean isWall(int i) {
		return walls[i] == 1;
	}

}
