package com.slava.sudoku;

public interface ISudokuField {
	public int getSize();
	public int getColorCount();
	public int[][] getRegions();
	public int[][] getPlaceToRegions();
	public String printSolution(int[] solution);
}
