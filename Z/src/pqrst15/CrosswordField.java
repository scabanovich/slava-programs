package pqrst15;

public class CrosswordField {
	int width;
	int height;
	int size;
	int[] x,y;
	int[][] xy;
	
	public CrosswordField() {}

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
	}
	// d = 0,1
	public int jump(int p, int d, int dl) {
		return (d == 0) ? jumpX(p, dl) : jumpY(p, dl);
	}
	
	public int jumpX(int p, int dx) {
		int ix = x[p] + dx;
		return (ix < 0 || ix >= width) ? -1 : xy[ix][y[p]];
	}

	public int jumpY(int p, int dy) {
		int iy = y[p] + dy;
		return (iy < 0 || iy >= height) ? -1 : xy[x[p]][iy];
	}

}
