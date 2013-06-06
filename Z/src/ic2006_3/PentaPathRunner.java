package ic2006_3;

import com.slava.common.RectangularField;

public class PentaPathRunner {

	public static void main(String[] args) {
		RectangularField f = new RectangularField();
		f.setSize(20, 7);
		PentaPathFigures fg = new PentaPathFigures();
		PentaPathFiguresBuilder b = new PentaPathFiguresBuilder();
		b.createPlacements(fg.FIGURES, f);
		PentaPathSolver solver = new PentaPathSolver();
		solver.setField(f);
		solver.setPlacementsForPoints(b.placementsForPoint);
		while(true) {
			solver.solve();
		}
	}

}

/**
Turns=16
Path
 ` ` ` ` ` ` ` ` ` ` ` e f g h i j k ` `
 ` z a b c d ` ` ` ` ` d ` ` ` ` ` l m `
 ` y p o n m l k j i ` c ` ` ` ` ` ` n `
 ` x q ` ` ` ` ` ` h a b ` ` w v u t o `
 ` w r ` ` ` ` ` ` g ` ` ` ` x ` ` s p `
 ` v s ` ` ` ` ` ` f ` ` ` ` y ` ` r q `
 ` u t ` ` ` ` ` ` e d c b a z ` ` ` ` `
Penta
 Y - L L L L - - X - - + N + + + + V V V
 Y + + + + L - X X X - + N N - F - + + V
 Y Y + T + + + + X + - + - N - F F F + V
 Y + + T T T - - - + W + - N + + F + + -
 - + + T - - - Z Z + W W - - + - - + U U
 - + + - - - - - Z + - W W - + P P + + U
 - + I I I I I - Z Z + + + + P P P - U U

Turns=17
Path
 ` m l k j i h g f e d ` ` m n ` ` ` ` `
 ` n ` ` ` ` ` ` ` ` c ` ` l o ` w x ` `
 ` o ` ` ` ` ` ` ` ` b i j k p ` v y ` `
 ` p q r s t u v ` ` a h ` ` q ` u z ` `
 ` ` ` ` ` ` ` w ` ` ` g ` ` r s t a ` `
 ` ` ` ` ` ` y x ` ` ` f ` ` ` ` ` b ` `
 ` ` ` ` ` ` z a b c d e ` ` ` ` ` c d `
Penta
 - + + X + + + + Y + + P P + + F F - T -
 - + X X X - Y Y Y Y + P P + F F + + T -
 - + - X - - - - - - + + P + + F + T T T
 - N + + + + L + - W W + - - + - + + - -
 N N - L L L L + W W - + Z Z + + V + U U
 N - - - - - + + W - - + Z - - - V + - U
 N - I I I I I + + + + Z Z - V V V + U U

Turns=20
Path
 0 0 0 0 0 0 0 0 0 0 1 1 1 0 0 0 0 1 1 0
 1 1 1 1 1 0 0 0 0 1 1 0 1 1 1 1 1 1 1 0
 1 0 0 0 1 1 1 1 1 1 1 0 0 0 1 1 0 0 1 0
 1 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 1 0
 1 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0
 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 1 1 1 1 0
 0 0 0 1 1 1 1 1 1 1 1 1 1 1 1 1 0 0 0 0
Penta
 0 0 0 1 1 1 0 1 1 1 0 0 1 1 1 1 1 0 1 1
 0 1 0 0 0 1 0 1 0 1 0 0 0 0 0 0 0 0 0 1
 0 1 1 1 0 1 0 0 0 0 0 1 1 1 0 1 1 1 0 1
 0 0 0 1 0 0 0 0 0 0 1 1 0 0 0 0 1 0 0 1
 0 1 0 0 0 1 1 0 1 0 0 0 0 1 1 0 1 0 0 0
 1 1 1 0 1 1 0 0 1 1 1 0 0 1 1 0 0 0 1 0
 0 1 0 0 1 0 0 0 0 1 0 0 0 0 1 0 1 1 1 1

*/
