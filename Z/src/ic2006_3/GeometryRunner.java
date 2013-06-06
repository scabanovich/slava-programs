package ic2006_3;

import com.slava.common.RectangularField;

public class GeometryRunner {
	static int[] POINTS = {
		0,0,0,0,0,1,0,1,0,0,0,
		0,0,1,1,1,0,0,1,0,0,0,
		0,0,1,0,0,0,0,0,1,0,0,
		0,1,0,0,1,1,1,0,0,1,0,
		1,1,0,1,0,0,0,0,0,0,1,
		1,0,0,1,0,1,1,0,0,0,1,
		0,0,1,1,1,0,1,1,1,0,0,
		1,1,0,1,0,0,1,1,0,0,0,
		0,0,0,0,1,0,1,1,1,0,0,
		0,0,0,0,0,1,1,0,0,0,0,
		0,1,1,1,0,1,0,1,0,0,0
	};

	public static void main(String[] args) {
		RectangularField f = new RectangularField();
		f.setSize(11, 11);
		GeometryFiguresBuilder b = new GeometryFiguresBuilder();
		b.setField(f);
		b.setPoints(POINTS);
		b.build();
		GeometrySolver solver = new GeometrySolver();
		solver.setField(f);
		solver.setPoints(POINTS);
		solver.setFigures(b.figuresForPoint);
		solver.solve();
		System.out.println("solutionCount=" + solver.solutionCount);

	}
}

/**
Solution
 + + + + + c + a + + +
 + + a k b + + l + + +
 + + j + + + + + h + +
 + k + + a k b + + h +
 j b + d + + + + + + l
 e + + k + h e + + + c
 + + g b i + h g i + +
 e f + f + + e j + + +
 + + + + i + l d i + +
 + + + + + j c + + + +
 + f g f + d + g + + +

key=e8-6,k6-20,h3-8,k7-12,

e8-6,h3-8,k7-12,k6-20

solutionCount=1

*/