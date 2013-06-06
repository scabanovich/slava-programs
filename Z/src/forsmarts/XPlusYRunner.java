package forsmarts;

public class XPlusYRunner {
	static int Y = 2;
	static int C = 1;
	static int[] INITIAL_STATE = {
		0,0,0,0,0,0,0,0,0,0,
		0,0,C,0,0,0,0,0,Y,0,
		0,0,0,0,0,0,0,0,0,0,
		0,0,C,0,0,0,0,0,0,0,
		0,0,0,0,C,0,0,0,0,C,
		0,C,0,0,0,0,0,0,0,0,
		0,0,0,0,0,C,0,0,0,0,
		C,0,0,0,0,0,0,C,0,0,
		0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,Y,0,0,0,0,0,
	};
	
	static int N = -1;
	static int[] NEIGHBORS = {
		N,N,N,N,N,N,N,N,N,N,
		N,N,N,N,N,N,N,N,N,N,
		N,N,N,N,N,N,N,N,N,N,
		N,N,4,N,N,N,N,N,N,N,
		N,N,N,N,N,N,N,N,N,3,
		N,N,N,N,N,N,N,N,N,N,
		N,N,N,N,N,4,N,N,N,N,
		N,N,N,N,N,N,N,2,N,N,
		N,N,N,N,N,N,N,N,N,N,
		N,N,N,N,N,N,N,N,N,N,
	};

	public static void main(String[] args) {
		XPlusYField f = new XPlusYField();
		f.setSize(10, 10);
		XPlusYCircles c = new XPlusYCircles();
		c.setField(f);
		c.setCircleLimit(21);
		c.setInitialState(INITIAL_STATE);
		c.setNeighbours(NEIGHBORS);
		c.solve();
		System.out.println(c.solutionCount);
	}

}
/*
18, 20, 5
 . . . . . y . . . .
 x . + . . . . . y .
 . . . . + . x . . .
 + . x . . . . . . .
 . . . . x . . y . +
 . x . . . . . . . .
 . . . + . x . . . y
 y . . . . . . + . .
 . . y . . . . . . .
 . . . . y . + . x .
*/