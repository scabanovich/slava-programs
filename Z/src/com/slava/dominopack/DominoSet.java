package com.slava.dominopack;

public class DominoSet {
	private int dimension; // Default dimension is 7
	private int size;
	private int[][] dominoIndex;
	private int[] lessNumber;
	private int[] largerNumber;

	public DominoSet() {
		setSize(7);
	}
	
	
	public void setSize(int dimension) {
		this.dimension = dimension;
		size = (dimension * (dimension + 1)) / 2;
		dominoIndex = new int[dimension][dimension];
		lessNumber = new int[size];
		largerNumber = new int[size];
		int c = 0;
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j <= i; j++) {
				dominoIndex[i][j] = c;
				dominoIndex[j][i] = c;
				largerNumber[c] = i;
				lessNumber[c] = j;
				++c;
			}
		}
	}

	public int getLessNumber(int index) {
		return lessNumber[index];
	}

	public int getLargerNumber(int index) {
		return largerNumber[index];
	}

	public int getIndex(int a, int b) {
		if(a < 0 || b < 0 || a >= dimension || b >= dimension) {
			return -1;
		}
		return dominoIndex[a][b];
	}

	public int getDimension() {
		return dimension;
	}

	public int getSize() {
		return size;
	}


}
