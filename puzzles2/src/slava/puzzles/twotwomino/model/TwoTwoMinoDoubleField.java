package slava.puzzles.twotwomino.model;

public class TwoTwoMinoDoubleField extends TwoTwoMinoField {
	TwoTwoMinoField field;
	int[] values;
	
	public TwoTwoMinoDoubleField(TwoTwoMinoField field) {
		this.field = field;
		setSize(2 * field.getWidth(), 2 * field.getHeight());
		values = new int[getSize()];
	}
	
	public TwoTwoMinoField getField() {
		return field;
	}
	
	
	public int convertFromOriginalField(int p, int dx, int dy) {
		int ix = 2 * field.x(p) + dx;
		int iy = 2 * field.y(p) + dy;
		return xy[ix][iy];
	}
	
	public int[] getValues() {
		return values;
	}

}
