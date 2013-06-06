package forsmarts;

public class RoundAndRoundRunner {
	static int[] POINTS_1 = {
		0,0,1,1,1,
		0,0,0,0,0,0,0,
		0,1,1,0,1,0,0,
		0,1,0,0,1
	};
	static int[][] LINES_1 = {
		{-1,2,0,1,-1},
		{-1,-1,-1,2,-1},
		{-1,-1,-1,-1,-1},
	};
	static RoundAndRoundLoopFilter FILTER_1 = new RoundAndRoundLoopFilter(POINTS_1, LINES_1);

	static int[] POINTS_2 = {
		1,0,0,0,0,0,0,
		0,0,0,0,1,0,0,0,0,
		0,0,0,0,1,0,1,1,0,0,0,
		0,0,0,0,0,0,1,1,1,0,0,
		0,0,1,0,0,0,0,0,0,
		0,0,1,0,0,0,0,
	};
	static int[][] LINES_2 = {
		{-1, 1, 2, 4, 0, 1,-1},
		{-1, 0, 2, 1, 2,-1,-1},
		{-1,-1, 2,-1,-1,-1,-1},
	};
	static RoundAndRoundLoopFilter FILTER_2 = new RoundAndRoundLoopFilter(POINTS_2, LINES_2);

	static void runFirstProblem() {
		RoundAndRoundField f = new RoundAndRoundField();
		f.setSize(2);
		RoundAndRoundLoopsEnumerator loops = new RoundAndRoundLoopsEnumerator();
		loops.setField(f);
		loops.setMaxCornerCount(7);
		loops.setLoopFilter(FILTER_1);
		loops.solve();
		System.out.println("Loops count=" + loops.loopCount);
		System.out.println("Accepted=" + loops.getLoops().length);
		RoundAndRoundPacker packer = new RoundAndRoundPacker();
		packer.setField(f);
		packer.setFilter(loops.loopFilter);
		packer.setLoops(loops.getLoops());
		packer.solve();
		System.out.println("packs=" + packer.solutionCount);
	}

	static void runSecondProblem() {
		RoundAndRoundField f = new RoundAndRoundField();
		f.setSize(3);
		RoundAndRoundLoopsEnumerator loops = new RoundAndRoundLoopsEnumerator();
		loops.setField(f);
		loops.setMaxCornerCount(7);
		loops.setLoopFilter(FILTER_2);
		loops.solve();
		System.out.println("Loops count=" + loops.loopCount);
		System.out.println("Accepted=" + loops.getLoops().length);
		RoundAndRoundPacker packer = new RoundAndRoundPacker();
		packer.setField(f);
		packer.setFilter(loops.loopFilter);
		packer.setLoops(loops.getLoops());
		packer.solve();
		System.out.println("packs=" + packer.solutionCount);
	}

	public static void main(String[] args) {
		//runFirstProblem();
		runSecondProblem();
	}

/**
1
 2 2 2 2 4
 1 1 3 3 4 3 4
 1 3 1 3 3 4 4
 1 0 1 4 0
 
 
 2,4,6,6,6

1
 6 6 6 6 3 3 3
 6 6 6 6 4 4 3 3 3
 5 5 4 4 4 4 3 2 3 3 3
 5 5 5 2 5 2 2 0 2 0 0
 5 5 5 2 1 2 2 0 0
 5 1 1 1 1 0 1

 0 xxx xxx        6
 1 x xxxxx        6
 2 x xxxx xxx     8
 3 xxx xxx xxxx  10
 4 xx xxxx        6
 5 xx xxxx xxx x 10
 6 xxxx xxxx      8
 
 6,6,6,8,8,10,10

 */
}
