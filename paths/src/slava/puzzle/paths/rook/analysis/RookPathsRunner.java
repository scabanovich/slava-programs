package slava.puzzle.paths.rook.analysis;

import com.slava.common.RectangularField;

public class RookPathsRunner {

	public static void main(String[] args) {
		RectangularField f = new RectangularField();
		f.setSize(8, 8);
		RookPathsGenerator g = new RookPathsGenerator();
		g.setField(f);
		int[] problem = g.generateSimple();
	}

}
