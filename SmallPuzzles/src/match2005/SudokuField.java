package match2005;

public class SudokuField {
	int width;
	int size;

	int[] x,y;
	int[][] xy;

	int[][] regions;
	int[][] placeToRegions;

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
		int regionCount = (diagonals) ? 3 * width + 2 : 3 * width;
		regions = new int[regionCount][width];
		buildVerticalRegions();
		buildHorizontalRegions();
		buildSquareRegions();
		if (diagonals)
			buildDiagonalRegions();
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

//width = 9 only
	void buildSquareRegions() {
		int[] region = { 0, 1, 2, 9, 10, 11, 18, 19, 20 };
		int[] dx = { 0, 3, 6, 0, 3, 6, 0, 3, 6 };
		int[] dy = { 0, 0, 0, 3, 3, 3, 6, 6, 6 };
		for (int i = 0; i < 9; i++) {
			for (int k = 0; k < region.length; k++) {
				int p = xy[x[region[k]] + dx[i]][y[region[k]] + dy[i]];
				regions[i + width + width][k] = p;
			}
		}
	}

	void buildPlaceToRegions() {
		placeToRegions = new int[size][];
		for (int p = 0; p < size; p++) {
			int c = 0;
			for (int r = 0; r < regions.length; r++)
				if (contains(p, r))
					++c;
			placeToRegions[p] = new int[c];
			c = 0;
			for (int r = 0; r < regions.length; r++)
				if (contains(p, r)) {
					placeToRegions[p][c] = r;
					++c;
				}
		}
	}

	boolean contains(int p, int region) {
		for (int i = 0; i < regions[region].length; i++) {
			if (p == regions[region][i])
				return true;
		}
		return false;
	}

	void printRegions() {
		for (int i = 0; i < regions.length; i++) {
			for (int j = 0; j < regions[i].length; j++) {
				System.out.print(" " + regions[i][j]);
			}
			System.out.println("");
		}
	}

}
