package match2005;

public class PentapipeRunner {

	public static void main(String[] args) {
		PentapipeFigures figures = new PentapipeFigures();
		figures.init();
		
		PentapipeSolver solver = new PentapipeSolver();
		PentapipeField f = new PentapipeField();
		f.setSize(12, 9);
		System.out.println("width=" + f.getWidth() + " height=" + f.getHeight());
		solver.setFiled(f);
		solver.setFigures(figures.figures);
		solver.solve();

	}

}
/*
 18 * 6 
 p p p i i i i i y y y y t t t w w +
 p p + + + + + + + y + + + t + + w w
 + + + s s s z z + + + + + t + + + w
 u u s s + + + z + + x + + f + + + v
 u + + + + l + z z x x x f f + + + v
 u u l l l l + + + + x + + f f v v v
 11 * 10 
 b b b b b c c + + i i
 + + + + + + c c + i i
 f f f e e + + c + + i
 + f + + e + + d + + h
 + f + + e e d d d h h
 + g + + + + + d + + h
 g g g k k + + j + + h
 g + + + k + j j + + l
 m + m + k + j + + + l
 m m m + k + j + l l l
 
 21 * 5 no solution
 17 * 6 no solution
 15 * 7, 7 * 15 no solution
 11 * 9 no solution
 10 * 10 no solution
 8 * 13 no solution
*/