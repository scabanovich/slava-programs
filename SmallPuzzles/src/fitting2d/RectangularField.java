package fitting2d;

public class RectangularField {
	int xSize;
	int ySize;
	int size;
	int[] x,y;
	int[][] xy;
	int[][] jp;
	int[] rotation;
	int[] reflection;
	
	public RectangularField() {}
	
	public void setSize(int xSize, int ySize) {
		this.xSize = xSize;
		this.ySize = ySize;
		size = xSize * ySize;
		x = new int[size];
		y = new int[size];
		xy = new int[xSize][ySize];
		for (int i = 0; i < size; i++) {
			x[i] = (i % xSize);
			y[i] = (i / xSize) % ySize;
			xy[x[i]][y[i]] = i;
		}
		jp = new int[6][size];
		for (int i = 0; i < size; i++) {
			jp[0][i] = (x[i] == xSize - 1) ? -1 : i + 1;
			jp[1][i] = (y[i] == ySize - 1) ? -1 : i + xSize;
			jp[2][i] = (x[i] == 0) ? -1 : i - 1;
			jp[3][i] = (y[i] == 0) ? -1 : i - xSize;
		}
		rotation = new int[size];
		for (int i = 0; i < size; i++) {
			int ix = x[i], iy = y[i];
			rotation[i] = xy(xSize - iy - 1, ix);
		}
		reflection = new int[size];
		for (int i = 0; i < size; i++) {
			int ix = x[i], iy = y[i];
			reflection[i] = xy(ix, ySize - iy - 1);
		}
	}
	
	public int jump(int p, int d) {
		return p < 0 || p >= size ? -1 : jp[d][p];
	}
	
	public int xy(int ix, int iy) {
		return (ix < 0 || ix >= xSize || iy < 0 || iy >= ySize) ? -1 : xy[ix][iy];
	}

}
