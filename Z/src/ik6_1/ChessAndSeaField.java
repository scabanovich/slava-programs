package ik6_1;

import com.slava.common.TwoDimField;

public class ChessAndSeaField extends TwoDimField {
	public static int[][] DIAGONAL_ORTS_2 = {
		{1,0}, {0,1}, {-1,0}, {0,-1}, {1,1}, {-1,1}, {-1,-1}, {1,-1}
	};

	public ChessAndSeaField() {
		setOrts(DIAGONAL_ORTS_2);
	}


}
