package com.slava.sudoku;

public class ToroidalSudokuballRunner {

	static int[] PROBLEM = {
	     0, 8,0, 0, 9,0, 0, 0,0, 
	      0, 0, 0,0, 0, 0,3, 8, 0, 
	      0,    0,0,    6,0,    0,
	     6, 0,0, 2, 0,0, 0, 0,0,
            0,0,    0,0,    7,0,
	      0, 0, 4,0, 3, 0,0, 0, 0,
	      0,    0,0,    0,0,    0,
	     0, 6,0, 0, 0,0, 0, 0,0,
	        0,5,    0,0,    0,0,    
	      0, 0, 0,0, 0, 5,0, 0, 0,
	      1,    0,0,    0,8,    0,
	        0,0,    2,0,    4,0
	};

	public static void solve() {
		ToroidalSudokuballField field = new ToroidalSudokuballField();
		SudokuRunner.solveVisual(field, PROBLEM);
	}

	public static void main(String[] args) {
		solve();
	}

}

/*
 3  8 2  5  9 4  1  6 7
  5  9  6 1  7  2 3  8  4
  4     1 3     6 5     2
 6  3 7  2  8 5  4  9 1
    5 8     1 9     7 3
  9  1  4 6  3  7 2  5  8
  2     7 4     8 6     9
 7  6 3  8  5 2  9  1 4
    2 5     4 1     3 6
  8  4  3 9  6  5 7  2  1
  1     9 7     3 8     5
    7 6     2 8     4 9

 3 8 2 5 5 9 4 1 1 6 7 3
 5 9 9 6 1 7 7 2 3 8 8 4
 4 9 9 1 3 7 7 6 5 8 8 2
 6 3 7 2 2 8 5 4 4 9 1 6
 6 5 8 2 2 1 9 4 4 7 3 6
 9 1 1 4 6 3 3 7 2 5 5 8
 2 1 1 7 4 3 3 8 6 5 5 9
 7 6 3 8 8 5 2 9 9 1 4 7
 7 2 5 8 8 4 1 9 9 3 6 7
 8 4 4 3 9 6 6 5 7 2 2 1
 1 4 4 9 7 6 6 3 8 2 2 5
 3 7 6 5 5 2 8 1 1 4 9 3



1 9 7 8 2 5 4 6

*/
