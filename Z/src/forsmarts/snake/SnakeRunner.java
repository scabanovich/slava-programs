package forsmarts.snake;

public class SnakeRunner {
	static int S = -1; //stone
	static int B = 1;  //beginning
	static int F = 2;
	
	static int[] DATA = {
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,S,0,
		0,0,0,0,B,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		0,0,0,F,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
	};
	
	static int[] H_DATA = {
		-1,5,-1, 3, 6, 4,-1,  5,-1
	};

	static int[] V_DATA = {
		-1, 7,-1, 6,  3, 3,  6,-1,-1
	};

	public static void main(String[] args) {
		SnakeField f = new SnakeField();
		f.setSize(9, 9);
		SnakeSolver solver = new SnakeSolver();
		solver.setField(f);
		solver.setData(DATA, H_DATA, V_DATA, 38);
		solver.solve();
		System.out.println("Solution count = " + solver.getSolutionCount());
	}

}
