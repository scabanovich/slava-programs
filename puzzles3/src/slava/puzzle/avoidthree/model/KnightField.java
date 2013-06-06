package slava.puzzle.avoidthree.model;

import com.slava.common.TwoDimField;

public class KnightField extends TwoDimField {
	
	public static int[][] KNIGHT_ORTS = {
		{2,1}, {1,2}, {-1,2}, {-2,1}, {-2,-1}, {-1,-2}, {1,-2}, {2,-1}
	};
	
	public KnightField() {
		setOrts(KNIGHT_ORTS);
	}


}
