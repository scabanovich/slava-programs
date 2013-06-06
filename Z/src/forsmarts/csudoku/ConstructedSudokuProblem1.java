package forsmarts.csudoku;

import com.slava.common.RectangularField;

public class ConstructedSudokuProblem1 {
	// +++
	// ++
	static int[][] PA = {
		{0,0},{1,0},{2,0}, {0,1},{1,1}
	};
	//  ++
	// ++++
	//  ++
	static int[][] TT = {
		{1,0},{2,0}, {0,1},{1,1},{2,1},{3,1}, {1,2},{2,2}
	};
	// ++++
	//  +++
	static int[][] PL = {
		{0,0},{1,0},{2,0},{3,0}, {1,1},{2,1},{3,1}
	};
	// +++
	// + +
	static int[][] U = {
		{0,0},{1,0},{2,0}, {0,1},{2,1}
	};
	// +
	// +++
	//   +
	static int[][] Z = {
		{0,0}, {0,1},{1,1},{2,1}, {2,2}
	};
	// +++
	//  ++
	static int[][] PB = {
		{0,0},{1,0},{2,0}, {1,1},{2,1}
	};
	// +++++
	//   +
	static int[][] TL = {
		{0,0},{1,0},{2,0},{3,0},{4,0}, {2,1}
	};
	
	static int E = -1;
	
	Piece[] pieces = new Piece[]{
		new Piece(0, new int[]{E,2,E,E,E}, PA),	
		new Piece(1, new int[]{E,8,E,3,E}, PA),	
		new Piece(2, new int[]{E,6,E,2,E,E,E,E}, TT),	
		new Piece(3, new int[]{E,E,E,5,E,1,8}, PL),	
		new Piece(4, new int[]{E,E,3,E,7,E,4,E}, TT),	
		new Piece(5, new int[]{E,2,E,2,E,E,E,E}, TT),	
		new Piece(6, new int[]{0,E,E,1,E,E,E,5}, TT),	
		new Piece(7, new int[]{E,7,E,E,E}, PA),	
		new Piece(8, new int[]{E,4,E,E,E}, U),	
		new Piece(9, new int[]{E,E,6,1,E}, Z),	
		new Piece(10, new int[]{E,E,3,4,E}, PB),	
		new Piece(11, new int[]{E,2,E,0,E,E}, TL),	
		new Piece(12, new int[]{E,E,4,E,E,E}, TL),	
	};
	
	int[] initialData = {
		E,E,8,E,E,E,E,E,E,	
		E,E,E,E,E,E,E,E,E,	
		E,E,E,E,E,E,E,E,E,	
		E,E,E,E,E,E,E,E,E,	
		E,E,E,E,E,E,E,E,E,	
		E,E,E,E,E,E,4,E,7,	
		E,E,E,E,E,E,E,E,E,	
		E,3,E,E,E,E,E,E,E,	
		E,E,E,E,E,E,E,E,E,	
	};	
	
	public ConstructedSudokuProblem1(int width) {
		RectangularField f = new RectangularField();
		f.setSize(width, width);
		for (int i = 0; i < pieces.length; i++) pieces[i].computePlacements(f);
	}
	
	public Piece[] getPieces() {
		return pieces;
	}
	
	public int[] getInitialData() {
		return initialData;
	}

}

/**

 7 3 9 4 2 5 8 6 1
 6 8 5 7 3 1 2 9 4
 4 2 1 8 6 9 3 7 5
 9 5 8 6 4 3 7 1 2
 3 7 2 1 5 8 9 4 6
 1 6 4 2 9 7 5 3 8
 8 9 3 5 1 4 6 2 7
 5 4 6 9 7 2 1 8 3
 2 1 7 3 8 6 4 5 9
 
 2 4 3 2 5 3 3 9 1
1
 + 3 9 4 + + + 6 +
 + + 5 + + + 2 9 +
 4 + + 8 + + 3 + 5
 + 5 + + + 3 + + +
 + + 2 1 + + + + +
 + 6 + + 9 + 5 3 8
 8 + + + + 4 + + 7
 + 4 + + 7 2 + + +
 + 1 + 3 + + + 5 +

occupation
 c c c g f f f f e
 c c g g g f f f e
 d d g g g k k e e
 d d h g k k k k e
 d h h h l k k j e
 b h h h l l j j j
 b b h m l l j j j
 b b a m m m i j i
 a a a a a m i i i

*/