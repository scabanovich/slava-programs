package forsmarts.snake;

public class SnakeByRunner {
	static int a = 10;
	static int b = 11;
	static int c = 12;
	static int d = 13;
	static int e = 14;
	static int[] REGIONS_1 = {
		0,0,0,0,1,1,1,1,1,1,
		0,0,0,0,2,2,3,3,3,4,
		6,6,2,2,2,2,3,4,4,4,
		6,6,7,7,5,3,3,3,5,4,
		6,6,7,7,5,5,5,5,5,4,
		6,6,7,7,9,9,a,e,e,e,
		8,8,9,9,9,9,a,e,d,d,
		8,8,9,b,9,a,a,e,d,d,
		8,8,b,b,b,a,a,e,d,c,
		8,8,b,c,c,c,c,c,c,c,
	};
	static int[] REGIONS_2 = {
		0,0,0,0,3,5,5,5,5,5,
		1,1,0,3,3,6,6,5,e,e,
		1,1,0,3,4,6,6,b,b,e,
		1,1,0,3,4,6,6,b,b,e,
		2,2,2,4,4,6,6,c,b,e,
		2,2,2,7,4,4,6,c,b,e,
		7,7,7,7,7,7,6,c,d,e,
		8,8,9,9,a,a,c,c,d,d,
		8,8,9,9,a,a,a,c,d,d,
		8,8,9,9,a,a,a,a,a,a,
	};
	
	public static void solve(int[] regions, 
			int width, int height,
			int startingRegion, int targetRegion) {
		SnakeField f = new SnakeField();
		f.setSize(width, height);
		SnakeBySolver solver = new SnakeBySolver();
		solver.setField(f);
		solver.setRegions(regions);
		solver.setStartingRegion(startingRegion);
		solver.setTargetRegion(targetRegion);
//		solver.setSnakeLength(45);
		solver.solve();
		System.out.println("solutionCount=" + solver.solutionCount);		
	}

	public static void main(String[] args) {
//		solve(REGIONS_1, 10, 10, 5, 9);
		solve(REGIONS_2, 10, 10, 7, c);
	}
}

/**
//1
 . . g h i j k . . .
 . . f . . . l m n o
 . . e d c . . . . p
 . . . . b . . . r q
 o p q . . . . . s .
 n . r s t . . . t u
 m . . . . . . . . v
 l k j . d c b . . w
 . . i . e . a z y x
 . . h g f . . . . .
solutionCount=1
 1, 3, 4, 6, 41

//2
 . . . c d e f . . .
 . z a b . . g h . .
 . y . . . . . i . .
 . x w v u t . j . .
 . . . . . s . k l m
 e d c . q r . . . n
 f . b . p . . . . o
 g . . . o . . t . p
 h i j . n . . s r q
 . . k l m . . . . .
solutionCount=1
 2, 9, 18, 26

*/