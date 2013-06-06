package forsmarts.snake;

import com.slava.common.TwoDimField;

public class SnakeField extends TwoDimField {
	static int[][] DIAGONAL_2_ORTS = {
		{1,0}, {0,1}, {-1,0}, {0,-1}, {1,1}, {-1,1}, {-1,-1}, {1,-1}
	};

	public SnakeField() {
		setOrts(DIAGONAL_2_ORTS);
	}

}
