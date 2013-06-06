package olia.triangles;

public class TrianglesField {
	protected int width;
	protected int height;
	protected int size;
	protected int[] x,y;
	protected int[][] xy;
	protected int[][] jp;
	
	public void setSize(int width, int height) {
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
		jp = new int[6][size];
		for (int i = 0; i < size; i++) {
			jp[0][i] = (x[i] == width - 1) ? -1 : i + 1;
			jp[1][i] = (x[i] == width - 1 || y[i] == height - 1) ? -1 : i + 1 + width;
			jp[2][i] = (y[i] == height - 1) ? -1 : i + width;
			jp[3][i] = (x[i] == 0) ? -1 : i - 1;
			jp[4][i] = (x[i] == 0 || y[i] == 0) ? -1 : i - 1 - width;
			jp[5][i] = (y[i] == 0) ? -1 : i - width;
		}
	}
	
	public int getSize() {
		return size;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int x(int p) {
		return x[p];
	}

	public int y(int p) {
		return y[p];
	}
	
	public int jump(int d, int p) {
		return jp[d][p];
	}
	
	public int getIndex(int ix, int iy) {
		return (ix < 0 || ix >= width || iy < 0 || iy >= height) ? -1 : xy[ix][iy];
	}
	
	public int[] rotate1(int dx, int dy) {
		return new int[]{dx - dy, dx};
	}

	public int[] rotate5(int dx, int dy) {
		return new int[]{dy, dy - dx};
	}
	
	public int getThirdVertexA(int p1, int p2) {
		int dx = x[p2] - x[p1];
		int dy = y[p2] - y[p1];
		int[] r = rotate1(dx, dy);
		int ix = x[p1] + r[0];
		int iy = y[p1] + r[1];
		return getIndex(ix, iy);
	}

	public int getThirdVertexB(int p1, int p2) {
		int dx = x[p2] - x[p1];
		int dy = y[p2] - y[p1];
		int[] r = rotate5(dx, dy);
		int ix = x[p1] + r[0];
		int iy = y[p1] + r[1];
		return getIndex(ix, iy);
	}


}
