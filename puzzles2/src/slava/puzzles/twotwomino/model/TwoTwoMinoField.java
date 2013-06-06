package slava.puzzles.twotwomino.model;

public class TwoTwoMinoField {
	int width;
	int height;
	int size;
	int[] x;
	int[] y;
	int[][] xy;
	int[][] jp;

	public void setSize(int width, int height) {
		if(this.width == width && this.height == height) return;
		this.width = width;
		this.height = height;
		size = width * height;
		x = new int[size];
		y = new int[size];
		xy = new int[width][height];
		for (int i = 0; i < size; i++) {
			x[i] = (i % width);
			y[i] = (i / width);
			xy[x[i]][y[i]] = i;
		}
		jp = new int[4][size];
		for (int i = 0; i < size; i++) {
			jp[0][i] = (x[i] == width - 1) ? -1 : i + 1;
			jp[1][i] = (y[i] == height - 1) ? -1 : i + width;
			jp[2][i] = (x[i] == 0) ? -1 : i - 1;
			jp[3][i] = (y[i] == 0) ? -1 : i - width;
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
	
	public int jump(int p, int dx, int dy) {
		int ix = x[p] + dx;
		int iy = y[p] + dy;
		return (ix < 0 || ix >= width || iy < 0 || iy >= height) ? -1 : xy[ix][iy];
	}
	
	public int jump(int p, int direction) {
		return jp[direction][p];
	}
	
	public int x(int i) {
		return (i < 0 || i >= size) ? -1 : x[i];
	}

	public int y(int i) {
		return (i < 0 || i >= size) ? -1 : y[i];
	}
	
}
