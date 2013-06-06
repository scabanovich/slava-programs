package slava.puzzles.connectfour.solve;

import com.slava.common.RectangularField;

public class Segment {
	RectangularField field;
	int p;
	int d;

	public Segment(RectangularField field, int p, int d) {
		this.field = field;
		this.p = p;
		this.d = d;
	}

}
