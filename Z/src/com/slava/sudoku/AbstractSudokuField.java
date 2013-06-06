package com.slava.sudoku;

public abstract class AbstractSudokuField implements ISudokuField {
	protected int size;
	protected int[][] regions;
	protected int[][] placeToRegions;
	
	public AbstractSudokuField() {}
	
	public int getSize() {
		return size;
	}

	public int[][] getRegions() {
		return regions;
	}

	public int[][] getPlaceToRegions() {
		return placeToRegions;
	}

	protected void buildPlaceToRegions() {
		placeToRegions = new int[size][];
		for (int p = 0; p < size; p++) {
			int c = 0;
			for (int r = 0; r < regions.length; r++) if(contains(p, r)) ++c;
			placeToRegions[p] = new int[c];
			c = 0;
			for (int r = 0; r < regions.length; r++) if(contains(p, r)) {
				placeToRegions[p][c] = r;
				++c;
			}
		}
	}
	
	protected boolean contains(int p, int region) {
		for (int i = 0; i < regions[region].length; i++) {
			if(p == regions[region][i]) return true;
		}
		return false;
	}
	
	protected void printRegions() {
		for (int i = 0; i < regions.length; i++) {
			for (int j = 0; j < regions[i].length; j++) {
				System.out.print(" " + regions[i][j]);
			}
			System.out.println("");
		}
	}

}
