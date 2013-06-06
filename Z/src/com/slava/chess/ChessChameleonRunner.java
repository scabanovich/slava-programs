package com.slava.chess;

import com.slava.sudoku.SudokuField;

public class ChessChameleonRunner {

	public static void main(String[] args) {
	  while(true) {
		ChessFigures cf = new ChessFigures();
		ChessChameleonSolver solver = new ChessChameleonSolver();
		solver.setFigures(cf);
		solver.setFigureSet(new int[]{3,1,0,2,4});
		SudokuField field = new SudokuField();
		field.setWidth(8, false);
		solver.setField(field);
		solver.setRandomizing(true);
		solver.solve();
	  }
	}

}

/*
58: BQKRN, G8, C4, B4, C3, F3, G5, F4, A4, B3, B1, 
D2, A5, H5, H4, H1, G3, H2, G2, G1, D1, B2, A3, E7, D6, D5, 
E3, A7, F2, E1, C1, D3, H7, E4, F5, C5, B7, A8, D8, C8, C2, 
A1, H8, H3, G4, D4, E2, B5, B8, C7, D7, F8, H6, C6, B6, G6, 
E5, F6, A6

-->57 QKRNB
 8 1 5 3 4 + 2 6
 + 4 7 2 5 3 1 8
 5 8 3 6 1 4 7 2
 7 2 1 4 8 6 3 5
 3 6 2 8 7 1 5 4
 1 5 4 7 + 8 6 3
 2 3 8 1 6 + + 7
 4 7 6 5 + 2 + 1
 C4 B4 C3 F3 G5 F4 A4 B3 B1 D2 A5 H5 H4 H1 G3 H2 G2 G1 D1 B2 A3 E7 D6 D5 E3 A7 F2 E1 C1 D3 H7 E4 F5 C5 B7 A8 D8 C8 C2 A1 H8 H3 G4 D4 E2 B5 B8 C7 D7 F8 H6 C6 B6 G6 E5 F6 A6
-->57 QKRNB
 3 5 7 1 8 6 4 2
 4 2 6 8 1 5 7 3
 + + 5 4 7 2 3 1
 2 7 1 3 5 4 8 6
 7 3 4 6 2 1 5 8
 5 1 8 2 + 3 6 7
 8 6 2 5 + 7 1 4
 1 4 3 + 6 8 + +
 C4 C7 C8 B8 A6 F1 C1 D2 D1 B2 A1 A2 B1 B7 A5 E1 E2 F3 F6 H7 E4 G6 H6 H5 G7 E5 D4 D3 C3 D5 G2 G4 H3 H1 G3 F4 G5 H4 B4 C6 A8 A4 B5 C5 D7 E8 F7 F8 F5 D6 H2 G1 F2 C2 E3 A7 B6
-->56
 3 4 6 8 7 5 2 +
 2 + 7 1 6 8 4 3
 5 + 3 7 4 1 8 6
 4 8 1 6 3 7 5 2
 1 7 5 + 2 6 3 8
 6 3 8 2 5 4 1 7
 + 6 2 5 1 + + 4
 7 1 4 3 8 2 + 5
 C4 H4 G5 E3 F1 H3 H6 H5 G6 E5 H2 H7 H8 D4 B5 E8 B8 C7 C3 A4 D7 B7 A8 C6 E7 F8 D8 C8 C5 A6 D3 D1 D2 D6 E4 G2 G4 F5 C2 B4 A5 A2 A1 B1 A3 C1 E1 F2 F3 G1 B6 F6 E6 E2 F4 G3
-->56 
 7 5 8 4 6 + 2 3
 6 1 3 2 7 5 4 8
 4 3 + + 5 2 1 7
 5 7 2 1 4 3 8 6
 8 6 1 3 2 4 + 5
 + 4 7 + 8 6 3 1
 1 2 5 7 3 8 6 4
 + 8 + 6 1 7 5 2
 D4 E5 D5 B6 E3 E1 E2 G4 H6 D2 C2 D1 H5 G7 F8 F7 E8 H8 G6 H7 C7 D8 D7 B8 G3 F3 F4 E4 F2 H4 H3 H2 B2 C4 B3 A3 A4 A2 B4 A5 A7 B7 E7 F5 B1 B5 C6 E6 C5 G1 H1 G2 G8 F6 A1 C1
-->56 RKQNB
 7 8 2 5 4 6 1 3
 1 6 4 3 8 + 5 2
 5 3 6 8 1 + 2 7
 4 2 1 7 5 3 + 8
 3 7 + 4 2 1 + 6
 2 1 8 6 7 5 3 4
 8 4 3 + 6 2 7 5
 6 5 7 + + 8 4 1
 C4 B4 B3 G8 F6 C3 H3 H4 H8 F7 G6 H6 H7 H5 G7 F8 F5 E5 C7 D5 E4 E7 E6 E2 G1 H2 D2 C2 D1 B2 D4 D3 E3 G3 H1 B7 B8 A8 C8 A7 B6 A6 A5 E1 G2 F1 A1 B1 A2 C1 F4 A4 A3 D6 B5 C6
-->56
 1 3 8 6 5 4 2 7
 2 7 5 4 1 3 6 8
 3 8 2 + + + + 4
 4 6 1 7 8 5 + 2
 7 4 6 1 + 8 5 3
 8 5 3 2 + 1 7 6
 6 1 4 3 7 2 8 5
 5 2 7 + 3 6 4 1
 C4 H4 H5 H3 G5 H6 H1 H2 E2 G1 F2 F1 E1 D1 B2 C1 A1 A2 B1 D2 F4 F8 E7 E4 F6 C3 A3 A4 C2 B4 A5 A6 B7 B8 C6 B5 B6 C5 D4 B3 D5 D6 D7 C7 A8 G2 G6 G7 H8 F7 E8 G8 H7 A7 C8 F5
-->56
 8 7 3 6 + 1 + 4
 2 4 5 1 + 3 + 7
 4 6 + 8 2 5 3 +
 5 + 1 2 7 4 6 8
 7 1 4 3 8 6 5 2
 6 2 8 5 + 7 1 3
 3 5 2 7 1 8 4 6
 1 8 6 4 3 2 7 5
 C4 D4 D5 C5 A4 D1 B1 A1 A8 C7 G3 G7 H8 H7 F6 H4 E7 F8 F2 H1 F3 F5 E4 E5 G6 H5 E8 D8 D6 C8 D7 D3 D2 A2 C1 B2 B7 A6 A5 C6 B5 B6 A7 A3 C2 B3 G8 F7 F1 E3 H6 F4 G5 G4 H2 B8
-->55 QNBRK
 2 7 5 4 + 1 3 6
 1 + 8 3 4 2 7 5
 + + 6 2 3 5 8 1
 + 8 + 1 2 7 6 4
 3 5 + 7 6 8 4 2
 6 + 4 8 5 3 1 7
 4 3 7 5 1 6 2 8
 + 1 2 6 7 4 5 3
 D4 E4 ...
-->53 QNBRK
 5 6 4 2 1 8 3 7
 8 1 + + 4 2 5 6
 6 + 2 5 3 + 1 4
 3 4 7 1 5 6 8 2
 2 3 5 + 7 4 6 1
 1 7 6 4 8 3 2 5
 + + 1 3 + + 7 8
 7 5 8 + 2 + 4 3
 D4 D1 E3 C1 A1 B1 B6 C8 A6 G6 F6 F5 H6 G5 G7 H7 C7 E8 D7 D6 C5 A3 C4 E6 E1 F2 G1 H3 G2 H2 H1 F1 G3 H4 H8 G8 B8 C6 A8 A2 B2 C3 B5 E2 E4 F4 E5 G4 H5 A5 A4 B4 D3
 
*/