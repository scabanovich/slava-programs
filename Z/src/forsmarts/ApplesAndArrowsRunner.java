package forsmarts;

public class ApplesAndArrowsRunner {
	static int[] H_DISTANCE_1 = {-1, 7,-1, 7,-1,-1, 6,-1,-1};
	static int[] V_DISTANCE_1 = {-1,-1,-1,-1,-1,-1,-1,-1,-1};
	static int[] H_DIRECTION_1 = {-1,-1,-1,-1,-1,-1,-1,-1,-1};
	static int[] V_DIRECTION_1 = {-1,-1,-1,-1,-1,-1, 2,-1, 5};

	static int[] H_DISTANCE_2 = { 5,-1,-1, 3,-1, 3,-1,-1,-1};
	static int[] V_DISTANCE_2 = {-1, 8,-1,-1,-1,-1,-1,-1,-1};
	static int[] H_DIRECTION_2 = {-1,-1,-1,-1,-1,-1,-1,-1,-1};
	static int[] V_DIRECTION_2 = {-1,-1,-1,-1,-1,-1,-1,-1,-1};


	public static void main(String[] args) {
		XPlusYField f = new XPlusYField();
		f.setSize(9, 9);
		ArrowsPlacer p = new ArrowsPlacer();
		p.setField(f);
		p.setArrowData(H_DISTANCE_2, V_DISTANCE_2, H_DIRECTION_2, V_DIRECTION_2);
		ApplesAndArrowsSolver s = new ApplesAndArrowsSolver();
		s.setField(f);
		s.setArrowsPlacer(p);
		s.solve();
		System.out.println(s.solutionCount);		
	}

}

/*
 . . A . 2 . . . .
 0 . . . . . . A .
 . . . A . 4 . . .
 . 0 . . . . . . A
 . . . . A . 2 . .
 A . 4 . . . . . .
 . . . . . . A . 5
 . A . 4 . . . . .
 . . . . . A . 4 .

S ,E ,W ,E ,S ,W ,NW ,W ,W

 . A . 1 . . . . .
 . . . . . A . 2 .
 2 . . A . . . . .
 . . . . . 3 . A .
 A . 7 . . . . . .
 . . . . . . 5 . A
 . . A . 2 . . . .
 . . . . . . A . 4
 . 6 . . A . . . .

SE ,S ,S ,SW ,NE ,NW ,S ,W ,N

*/