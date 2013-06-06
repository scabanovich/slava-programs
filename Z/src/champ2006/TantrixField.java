package champ2006;

public class TantrixField {
	int width;
	int height;
	int size;
	int[] x,y;
	int[][] xy;
	int[][] jp;
	
	public TantrixField() {}

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
			jp[0][i] = (x[i] == width - 1                      ) ? -1 : i + 1;
			jp[1][i] = (x[i] == width - 1 || y[i] == height - 1) ? -1 : i + 1 + width;
			jp[2][i] = (                     y[i] == height - 1) ? -1 : i + width;
			jp[3][i] = (x[i] == 0                              ) ? -1 : i - 1;
			jp[4][i] = (x[i] == 0         || y[i] == 0) ? -1 : i - 1 - width;
			jp[5][i] = (                     y[i] == 0) ? -1 : i - width;
		}
	}
	
	public int getSize() {
		return size;
	}
	
	public int getIndex(int ix, int iy) {
		return (ix < 0 || ix >= width || iy < 0 || iy >= height) ? -1 : xy[ix][iy];
	}

}
