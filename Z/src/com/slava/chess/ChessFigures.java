package com.slava.chess;

import ik6_1.ChessAndSeaField;

public class ChessFigures {
	public static int KING = 0;
	public static int QUEEN = 1;
	public static int ROOK = 2;
	public static int BOUND = 3;
	public static int NOOK = 4;
	
	//[figure][cell][index]
	int[][][] moves;
	ChessAndSeaField field = new ChessAndSeaField();
	
	public ChessFigures() {
		this(8, 8);
	}

	public ChessFigures(int width, int height) {
		field.setSize(width, height);
		init();
		//print();
	}
	
	public ChessAndSeaField getField() {
		return field;
	}

	void init() {
		moves = new int[5][field.getSize()][];
		initLongRangeFigureMoves(KING, new int[]{0,1,2,3,4,5,6,7}, false);
		boolean makeLongRange = true;
		initLongRangeFigureMoves(QUEEN, new int[]{0,1,2,3,4,5,6,7}, makeLongRange);
		initLongRangeFigureMoves(ROOK, new int[]{0,1,2,3}, makeLongRange);
		initLongRangeFigureMoves(BOUND, new int[]{4,5,6,7}, makeLongRange);
		initNookMoves();
	}
	
	void initLongRangeFigureMoves(int figure, int[] ds, boolean longRange) {
		for (int p = 0; p < field.getSize(); p++) {
			int k = 0;
			for (int di = 0; di < ds.length; di++) {
				int d = ds[di];
				int q = field.jump(p, d);
				while(q >= 0) {
					k++;
					if(!longRange) break;
					q = field.jump(q, d);
				}
			}
			moves[figure][p] = new int[k];
			k = 0;
			for (int di = 0; di < ds.length; di++) {
				int d = ds[di];
				int q = field.jump(p, d);
				while(q >= 0) {
					moves[figure][p][k] = q;
					k++;
					if(!longRange) break;
					q = field.jump(q, d);
				}
			}
		}
	}
	
	public int[][] NOOK_JUMPS = {
		{1,2},{-1,2},{1,-2},{-1,-2},{2,1},{-2,1},{2,-1},{-2,-1}
	};

	void initNookMoves() {
		for (int p = 0; p < field.getSize(); p++) {
			int k = 0;
			for (int di = 0; di < 8; di++) {
				int[] dd = NOOK_JUMPS[di];
				if(field.jumpXY(p, dd[0], dd[1]) >= 0) k++;
			}
			moves[NOOK][p] = new int[k];
			k = 0;
			for (int di = 0; di < 8; di++) {
				int[] dd = NOOK_JUMPS[di];
				if(field.jumpXY(p, dd[0], dd[1]) >= 0) {
					moves[NOOK][p][k] = field.jumpXY(p, dd[0], dd[1]);
					k++;
				}
			}
		}
	}
	
	public int[] getMoves(int figure, int p) {
		return moves[figure][p];
	}
	
	void print() {
		for (int i = 0; i < moves.length; i++) {
			System.out.println("figure=" + i);
			for (int p = 0; p < field.getSize(); p++) {
				System.out.print(p + ":");
				for (int k = 0; k < moves[i][p].length; k++) {
					System.out.print(" " + moves[i][p][k]);
				}
				System.out.println("");
			}
			System.out.println("");
		}
	}
}
