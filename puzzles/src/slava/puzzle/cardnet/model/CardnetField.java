package slava.puzzle.cardnet.model;

public class CardnetField {
	int width;
	int height;
	int size;
	
	int[] x,y;
	int[][] xy;
	int[][] jp;
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		size = width * height;
		x = new int[size];
		y = new int[size];
		xy = new int[width][height];
		for (int i = 0; i < size; i++) {
			x[i] = i % width;
			y[i] = i / width;
			xy[x[i]][y[i]] = i;
		}
		jp = new int[8][size];
		for (int i = 0; i < size; i++) {
			jp[0][i] = (x[i] == width - 1) ? -1 : i + 1;
			jp[1][i] = (x[i] == width - 1 || y[i] >= height - 1) ? -1 : i + 1 + width;
			jp[2][i] = (y[i] == height - 1) ? -1 : i + width;
			jp[3][i] = (x[i] == 0 || y[i] == height - 1) ? -1 : i - 1 + width;
			jp[4][i] = (x[i] == 0) ? -1 : i - 1;
			jp[5][i] = (x[i] == 0 || y[i] == 0) ? -1 : i - 1 - width;
			jp[6][i] = (y[i] == 0) ? -1 : i - width;
			jp[7][i] = (x[i] == width - 1 || y[i] == 0) ? -1 : i + 1 - width;
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
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
	
	public int jp(int d, int p) {
		return jp[d][p];
	}
	
	public int getIndex(int h, int v) {
		return xy[v][h];
	}

}
