package com.slava.sudoku;

public class SudokuField extends AbstractSudokuField implements ISudokuField {
	int width;	
	int[] x,y;
	int[][] xy;
	
	public SudokuField() {}
	
	public void setWidth(int width, boolean diagonals) {
		this.width = width;
		size = width * width;
		x = new int[size];
		y = new int[size];
		xy = new int[width][width];
		for (int i = 0; i < size; i++) {
			x[i] = (i % width);
			y[i] = (i / width);
			xy[x[i]][y[i]] = i;
		}
		buildRegions(diagonals);
		buildPlaceToRegions();
	}
	
	void buildRegions(boolean diagonals) {
		boolean diagonals2 = false; //Portugalov's isosudoku diagonals
		int regionCount = (diagonals) ? 
			((diagonals2) ? 3 * width + (2 * width - 7) : 3 * width + 2) : 3 * width; 
		regions = new int[regionCount][width];
		buildVerticalRegions();
		buildHorizontalRegions();
		buildSquareRegions();
		if(diagonals) {
			if(diagonals2) buildDiagonalRegions2();
			else buildDiagonalRegions();
		}
	}
	
	void buildVerticalRegions() {
		for (int ix = 0; ix < width; ix++) {
			for (int iy = 0; iy < width; iy++) {
				regions[ix][iy] = xy[ix][iy];
			}
		}
	}

	void buildHorizontalRegions() {
		for (int iy = 0; iy < width; iy++) {
			for (int ix = 0; ix < width; ix++) {
				regions[iy + width][ix] = xy[ix][iy];
			}
		}
	}
	
	void buildDiagonalRegions() {
		for (int i = 0; i < width; i++) {
			regions[regions.length - 2][i] = xy[i][i];
			regions[regions.length - 1][i] = xy[i][width - 1 - i];
		}
	}
	
	void buildDiagonalRegions2() {
		int k = regions.length - (width - 3) - (width - 4);
		for (int i = 0; i < width - 3; i++) {
			regions[k] = new int[width - i];
			for (int j = 0; j < regions[k].length; j++) regions[k][j] = xy[i + j][j];
			++k;
		}
		for (int i = 1; i < width - 3; i++) {
			regions[k] = new int[width - i];
			for (int j = 0; j < regions[k].length; j++) regions[k][j] = xy[j][i + j];
			++k;
		}
	}
	
	//width = 6 or 8 or 9 only
	void buildSquareRegions() {
		int[] region = 
			width == 9 ? new int[]{0,1,2,9,10,11,18,19,20} :
			width == 8 ? new int[]{0,1,2,3,8,9,10,11} :
			width == 6 ? new int[]{0,1,2,6,7,8} :
			null;
		int[] dx = 
			width == 9 ? new int[]{0,3,6,0,3,6,0,3,6} : 
			width == 8 ? new int[]{0,4,0,4,0,4,0,4} :
			width == 6 ? new int[]{0,3,0,3,0,3} :
			null;
		int[] dy = 
			width == 9 ? new int[]{0,0,0,3,3,3,6,6,6} :
			width == 8 ? new int[]{0,0,2,2,4,4,6,6} :
			width == 6 ? new int[]{0,0,2,2,4,4} :
			null;
		if(region == null) return;
		for (int i = 0; i < width; i++) {
			for (int k = 0; k < region.length; k++) {
				int p = xy[x[region[k]] + dx[i]][y[region[k]] + dy[i]];
				regions[i + width + width][k] = p;
			}
		}
	}
	
	public int getColorCount() {
		return width;
	}

	public String printSolution(int[] solution) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < size; i++) {
			char c = (solution[i] < 0) ? '+' : ("" + (solution[i] + 1)).charAt(0);
			sb.append(" " + c);
			if(x[i] == width - 1) sb.append("\n");
		}
		return sb.toString();
	}

}
