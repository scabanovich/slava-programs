package forsmarts;

import ik6_1.ChessAndSeaField;

public class ChessAndSeaForsmartsRunner {
	static int[] FORM_1 = {
		0,0,0,0,0,0,0,0,
		0,0,0,0,0,1,0,0,
		0,1,0,0,0,1,0,0,
		0,0,0,0,0,0,0,0,
		0,0,0,0,1,0,0,0,
		0,0,0,0,0,0,0,0,
		0,0,1,0,0,0,0,0,
		0,0,0,0,0,0,0,0,
	};
	static int[] FORM_2 = {
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,1,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		1,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,1,0,0,1,0,
		0,0,0,0,1,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
	};
	
	static void runFirstProblem() {
		ChessAndSeaField f = new ChessAndSeaField();
		f.setSize(8, 8);
		ChessAndSeaForsmartsSolver solver = new ChessAndSeaForsmartsSolver();
		solver.setField(f);
		solver.setChessForm(FORM_1);
		solver.solve();
	}

	static void runSecondProblem() {
		ChessAndSeaField f = new ChessAndSeaField();
		f.setSize(9, 9);
		ChessAndSeaForsmartsSolver solver = new ChessAndSeaForsmartsSolver();
		solver.setField(f);
		solver.setChessForm(FORM_2);
		solver.solve();
	}

	public static void main(String[] args) {
		//warning: hardcode in chechRestrictions method!!! 
//		runFirstProblem();
		runSecondProblem();
	}

}
/*
 
 A B C D E F G H
  
 + x x x + x + x  1
 + + + + + N + +  2
 x R + x + B + +  3
 x + + x + x x +  4
 + + + x Q + + +  5
 + x + x + x + +  6
 + + K + + + + +  7
 x x + x x x + +  8

F1, H1, B6, F6

 A B C D E F G H I
 
 + + x x x + x x +  1
 x + + + N + + + +  2
 + + x + + x + + +  3
 Q + + + + x + + +  4
 + + + + + x + + +  5
 x x + x B x + R x  6
 + + + + K + + + +  7
 + + x x + x x x +  8
 + + + + + + + + +  9
 
 A2,C3,D6,I6
*/