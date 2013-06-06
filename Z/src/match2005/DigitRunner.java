package match2005;

import com.slava.common.RectangularField;

public class DigitRunner {

	static void set16x8(DigitSolver solver, RectangularField f) {
		f.setSize(16, 10);
		solver.setField(f);
		solver.setRestriction(new int[]{
			1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
			0,1,1,1,1,1,1,1,0,0,0,0,0,0,0,1,
			0,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
		});
		solver.setPoints(f.getIndex(0,1), f.getIndex(15,2));
	}
	
	static void set16x16D(DigitSolver solver, RectangularField f) {
		f.setSize(16, 16);
		solver.setField(f);
		solver.setRestriction(new int[]{
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,
			0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,
			0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,
			0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,
			0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,
			0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,
			0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,
			0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,
			0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,
			0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,
			0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,
			0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,
			0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,
			0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,
			0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,
			0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,
		});
		solver.setPoints(f.getIndex(13,0), f.getIndex(2,15));
	}

	static void set16x16(DigitSolver solver, RectangularField f) {
		f.setSize(16, 16);
		solver.setField(f);
		solver.setPoints(f.getIndex(15,15), f.getIndex(15,15));
	}

	public static void main(String[] args) {
		DigitFigures figures = new DigitFigures();
		figures.init();
		
		DigitSolver solver = new DigitSolver();
		RectangularField f = new RectangularField();
		set16x16(solver, f);
		solver.setFigures(figures.figures);
		while(true) {
			solver.solve();
		}
	}

}

/*
Weight=57
 . . u u t . . . . . . . . . . +
 v u u + t d d c c + + . . . a a
 v u + + t t d e c c + + a a a +
 v p p p + t s e e c + b b g g g
 p p q q q s s + e b b b g g h +
 o + r r s s m + e + f f f h h h
 o + r n n m m k + f f j + + + h
 o o n n l l l k k + f j j i i h
 + o n + + k k k + j j j + i i i
Weight=56
 + u u u u u u u u u u u u u u u
 a + + b b b o o o + p p p + + u
 a a b b c + n o o p p q q q u +
 + a a c c c n + + + p r r u u u
 e + e e d c n n n l l r s + + u
 f e e + d c + n m m l l s s s t
 f f e g d h m m m + j l + s t t
 + f f g + h h i i i j k s t t +
 + + + g h h + h i i j k k + + +
Weight=104
 + v v u u u u t t + s + + r r q
 + v v v u u + t t + s s r r q q
 + w + + + u + t s s s s r q q q
 + w w w k k k l l l l m + + + q
 + w w k k + + l l + + m m + + p
 + w x j j + i + l h h g m + p p
 + x x j j + i i h h g g m p p p
 + x x j i i i i h g g g n + + p
 + y y + + + + + + + + g n n n o
 y y y d e e e e f f f f n n o o
 + + y d e e + c f f + b n o o +
 z z y d d e + c c f + b b + + +
 z z z + d c c c c b b b b a a +
 a + + + c c c d d d d e + f a a
 a a b b b c c d d + e e + f f a
 + a a b b + + + d + e e f f f f
*/
