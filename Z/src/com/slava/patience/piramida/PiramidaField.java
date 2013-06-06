package com.slava.patience.piramida;

public interface PiramidaField {
	//       0           1           2
	//     3    4      5    6      7    8 
	//    9   10  11  12  13  14  15  16  17
	//  18  19  20  21  22  23  24  25  26  27 
	//
	public int piramidSize = 28;
	public int reserveSize = 23;
	
	public int[][] upperNeighbours = {
		{3,4}, {5,6}, {7,8},
		{9,10}, {10,11}, {12,13}, {13,14}, {15,16}, {16,17},
		{18,19}, {19,20},{20,21},{21,22},{22,23},{23,24},{24,25},{25,26},{26,27},
		{},{},{},{},{},{},{},{},{},{},
	};

}
