package com.slava.sudoku;

public class ToroidalSudokuballField extends AbstractSudokuField {
	
/*
     0    1  2    3    4  5    6    7  8 
       9   10   11 12   13   14 15   16   17 
      18        19 20        21 22        23
    24   25 26   27   28 29   30   31 32
         33 34        35 36        37 38
      39   40   41 42   43   44 45   46   47
      48        49 50        51 52        53
    54   55 56   57   58 59   60   61 62
         63 64        65 66        67 68    
      69   70   71 72   73   74 75   76   77
      78        79 80        81 82        83
         84 85        86 87        88 89
         
*/

	static int L = 100;
	static int M = 101;
	static int N = 103;
	static int[] spaces = {
	     1, 0,1, 1, 0,1, 1, 0,M, 
	      1, 1, 0,1, 1, 0,1, 1, M, 
	      4,    0,4,    0,4,    L,
	     1, 0,1, 1, 0,1, 1, 0,N,
            0,4,    0,4,    0,M,
	      1, 1, 0,1, 1, 0,1, 1, M,
	      4,    0,4,    0,4,    L,
	     1, 0,1, 1, 0,1, 1, 0,N,
	        0,4,    0,4,    0,M,    
	      1, 1, 0,1, 1, 0,1, 1, M,
	      4,    0,4,    0,4,    N,
	        0,4,    0,4,    0,L
	};
	
	public ToroidalSudokuballField() {
		init();
	}
	
	void init() {
		size = 90;
		regions = new int[][]{
			//horizontal
			{ 0, 1, 2, 3, 4, 5, 6, 7, 8},	
			{ 9,10,11,12,13,14,15,16,17},	
			{18,10,19,20,13,21,22,16,23},	
			{24,25,26,27,28,29,30,31,32},	
			{24,33,34,27,35,36,30,37,38},	
			{39,40,41,42,43,44,45,46,47},	
			{48,40,49,50,43,51,52,46,53},	
			{54,55,56,57,58,59,60,61,62},	
			{54,63,64,57,65,66,60,67,68},	
			{69,70,71,72,73,74,75,76,77},	
			{78,70,79,80,73,81,82,76,83},	
			{ 0,84,85, 3,86,87, 6,88,89},	

			{ 0, 3, 6,24,27,30,54,57,60},	
			{10,13,16,40,43,46,70,73,76},	

			//vertical
			{ 0, 9,18,24,39,48,54,69,78},	
			{ 1,10,25,33,40,55,63,70,84},	
			{ 2,10,26,34,40,56,64,70,85},	
			{ 3,11,19,27,41,49,57,71,79},	
			{ 3,12,20,27,42,50,57,72,80},	
			{ 4,13,28,35,43,58,65,73,86},	
			{ 5,13,29,36,43,59,66,73,87},	
			{ 6,14,21,30,44,51,60,74,81},	
			{ 6,15,22,30,45,52,60,75,82},	
			{ 7,16,31,37,46,61,67,76,88},	
			{ 8,16,32,38,46,62,68,76,89},	
			{ 0,17,23,24,47,53,54,77,83},	

			//diagonal
			{ 1, 2, 9,10,11,18,19,25,26},
			{ 4, 5,12,13,14,20,21,28,29},
			{ 7, 8,15,16,17,22,23,31,32},
			{33,34,39,40,41,48,49,55,56},
			{35,36,42,43,44,50,51,58,59},
			{37,38,45,46,47,52,53,61,62},
			{63,64,69,70,71,78,79,84,85},
			{65,66,72,73,74,80,81,86,87},
			{67,68,75,76,77,82,83,88,89},
		};
		
		buildPlaceToRegions();
	}

	public int getColorCount() {
		return 9;
	}

	public String printSolution(int[] solution) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < solution.length; i++) {
			sb.append(" ").append(solution[i] + 1);
			int s = spaces[i];
			if(s >= 100) {
				sb.append("\n");
				s -= 100;
			}
			while(s > 0) {
				sb.append(' ');
				s--;
			}
		}
		return sb.toString();
	}

}
