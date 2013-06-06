package slava.puzzle.paths.nook.analysis;

import com.slava.common.RectangularField;

public class NookPathsRunner {

	public static void main(String[] args) {
		RectangularField f = new RectangularField();
		f.setSize(8, 8);
		NookPathsGenerator g = new NookPathsGenerator();
		g.setField(f);
		int[] filter = new int[f.getSize()];
		filter[7] = 1; filter[6] = 1;
//		filter[15] = 1; filter[14] = 1;
		g.setFilter(filter);
//		int[] path = g.generatePath();
		g.generateSimple();
	}

}
