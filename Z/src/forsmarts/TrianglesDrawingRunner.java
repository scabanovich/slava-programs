package forsmarts;

import com.slava.common.RectangularField;

public class TrianglesDrawingRunner {
	static int[] POINTS_1 = {
		0,1,0,1,0,0,1,0,
		1,0,1,0,1,1,1,0,
		1,1,0,0,1,0,0,1,
		0,0,0,1,0,0,0,0,
		0,1,0,0,0,1,1,0,
		1,1,1,1,0,0,1,0,
		1,0,0,0,0,0,0,0,
		0,0,0,1,1,0,0,0,
	};
	
	static int[] POINTS_2 = {
		1,0,1,0,1,0,0,1,
		1,0,0,1,0,0,1,1,
		0,0,0,0,0,1,0,1,
		1,0,1,0,0,0,1,0,
		1,0,0,1,1,0,0,1,
		0,1,0,0,0,1,0,1,
		0,0,0,0,1,0,1,0,
		0,0,1,0,0,0,0,1,
	};
	
	public static void run() {
		RectangularField f = new RectangularField();
		f.setSize(8, 8);
		TrianglesDrawingSolver solver = new TrianglesDrawingSolver();
		solver.setField(f);
		solver.setPoints(POINTS_1);
		solver.solve();
		System.out.println("SolutionCount=" + solver.solutionCount);
	}
	
	public static void test() {
		RectangularField f = new RectangularField();
		f.setSize(8, 8);
//		int[] t = {9, 4, 46};
//		for (int p = 0; p < f.getSize(); p++) {
//			boolean b = TrianglesBuilder.isInside(f, t, p);
//			System.out.print(" " + (b ? 1 : 0));
//			if(f.isRightBorder(p)) System.out.println("");
//		}
	
//		TrianglesBuilder.Triangle[] ts = TrianglesBuilder.createTriangles(f, POINTS_1);
//		System.out.println("Found " + ts.length + " triangles");
//		for (int i = 0; i < ts.length; i++) {
//			int[] t = ts[i].points;
//			System.out.println(" (" + t[0] + "," + t[1] + "," + t[2] + ") " + ts[i].doubledSquare);
//		}
//		
//		System.out.println("");
		
		boolean b = TrianglesBuilder.intersect(f, 0, 63, 1, 18);
		System.out.println(b);
		
	}
	
	public static void main(String[] args) {
		//test();
		run();
	}

}

/*

a) G1 B1 A3 B6 D4 D6 E2 D1
b) H1 G4 A5 A2 B6 E1 D2 A1

*/