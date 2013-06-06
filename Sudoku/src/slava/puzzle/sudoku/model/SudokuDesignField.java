package slava.puzzle.sudoku.model;

public class SudokuDesignField {
	int width;
	int size;
	
	int[] x,y;
	int[][] xy;
	int[][] jp;
	
	int[][] diagonalNeighbours;
	
	public void setSize(int width) {
		this.width = width;
		size = width * width;
		x = new int[size];
		y = new int[size];
		xy = new int[width][width];
		for (int i = 0; i < size; i++) {
			x[i] = i % width;
			y[i] = i / width;
			xy[x[i]][y[i]] = i;
		}
		jp = new int[8][size];
		for (int i = 0; i < size; i++) {
			jp[0][i] = (x[i] == width - 1) ? -1 : i + 1;
			jp[1][i] = (y[i] == width - 1) ? -1 : i + width;
			jp[2][i] = (x[i] == 0        ) ? -1 : i - 1;
			jp[3][i] = (y[i] == 0        ) ? -1 : i - width;
			jp[4][i] = (x[i] == width - 1 || y[i] >= width - 1) ? -1 : i + 1 + width;
			jp[5][i] = (x[i] == 0         || y[i] == width - 1) ? -1 : i - 1 + width;
			jp[6][i] = (x[i] == 0         || y[i] == 0        ) ? -1 : i - 1 - width;
			jp[7][i] = (x[i] == width - 1 || y[i] == 0        ) ? -1 : i + 1 - width;
		}
		buildDiagonalNeighbours();
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return width;
	}
	
	public int getSize() {
		return size;
	}
	
	public int x(int p) {
		return p < 0 || p >= size ? -1 : x[p];
	}

	public int y(int p) {
		return p < 0 || p >= size ? -1 : y[p];
	}
	
	public int jp(int p, int d) {
		return jp[d][p];
	}
	
	public int getIndex(int h, int v) {
		return xy[v][h];
	}
	
	void buildDiagonalNeighbours() {
		diagonalNeighbours = new int[size][];
		for (int p = 0; p < size; p++) {
			int k = 0;
			for (int d = 4; d < 8; d++) {
				int q = jp(p, d);
				if(q >= 0) ++k;
			}
			diagonalNeighbours[p] = new int[k];
			k = 0;
			for (int d = 4; d < 8; d++) {
				int q = jp(p, d);
				if(q >= 0) {
					diagonalNeighbours[p][k] = q;
					++k;
				}
			}
		}
	}

	public int[][] getDiagonalNeighbours() {
		return diagonalNeighbours;
	}

}
