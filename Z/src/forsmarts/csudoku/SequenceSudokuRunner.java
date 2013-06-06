package forsmarts.csudoku;

import com.slava.common.TwoDimField;
import com.slava.sudoku.SudokuField;

/**
 * Starting from any cell write in the grid as many digits as
 * possible to create the solvable Sudoku (it doesn't have to
 * have the unique solution). Digits must be written sequentially:
 * 1-2-3-...-8-9-then 1 again and so on. Each next digit can be
 * placed in any of free neighbouring cells. * 
 * @author glory
 */
public class SequenceSudokuRunner {

	public static void main(String[] args) {
		SudokuField sf = new SudokuField();
		sf.setWidth(9, false);
		
		TwoDimField jf = new TwoDimField();
		jf.setOrts(TwoDimField.DIAGONAL_ORTS);
		jf.setSize(9, 9);
		
		SequenceSudokuSolver solver = new SequenceSudokuSolver();
		solver.setField(sf);
		solver.setJField(jf);
		
		while(true) {
			try {Thread.sleep(50); } catch (Exception e) {}
			solver.solve();
		}
	}

}

/**
Maximum=73
 a b h i y x w v u
 e g c z j m o r t
 f d a k l n p q s
 r q p b c i j m n
 s t o d g h k l o
 - n u e f b a z p
 - j m g - c w y q
 k l i h f d v x r
 - - - - - e u t s

Key=A9-R-DR-DL-UL-D-UR-UR-R-DR-DL-R-UR-D-UR-D-R-U-DR-U-U-L-L-L-L-DL-DL-DR-R-DL-D-R-U-R-U-R-D-R-U-R-D-D-D-D-D-L-L-U-U-DR-U-U-L-L-D-D-D-UL-UL-D-L-UL-DL-R-UR-UL-UR-U-L-L-D-R-DR

Maximum=72
 r s t - - - - n m
 q o n s r q o k l
 p m t v w p h j -
 - l u x z b i g e
 k i - y c a c d f
 j h g f e d b - -
 m n o s t u a z y
 j l p q r e v b x
 k i h g f d c w a

Key=I1-UL-DL-L-U-DL-L-L-L-UL-D-UR-UL-R-R-D-R-R-UL-R-R-DR-DR-UR-U-L-L-U-U-R-UR-D-UL-UL-D-UR-U-R-U-L-DL-DL-U-L-L-DL-D-UR-R-DL-D-UR-DR-U-DL-DR-L-L-L-L-U-DL-U-UR-U-UR-L-DL-U-U-R-R
Maximum=72
 g h i m n o t s a
 f e j l p - p r b
 - d k q r o n q c
 y x c s u - - m d
 z b w v t - i l e
 u a - - - h k j f
 t v - z a b g f g
 r s w x y c d e h
 q p o n m l k j i
Maximum=70
 w y z n m j - - a
 v x a o l k i - b
 t u b r p h - - c
 q s c d q g - - d
 p r l k e f - - e
 o n m j i h - - f
 t u v z a b g f g
 r s w x y c d e h
 q p o n m l k j i
Maximum=69
 a b c d e f g h i
 a z - - k m p q j
 b - y j - l n o k
 - c i x w - - - l
 d - g h v - z - m
 e f - u b a x y n
 q r s t c d v w o
 o p k j i e t u p
 n m l h g f s r q
*/