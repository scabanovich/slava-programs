package packing3d;

public class CubeField {
	int xSize;
	int ySize;
	int zSize;
	int size;
	int[] x,y,z;
	int[][][] xyz;
	int[][] jp;
	int[][] rotation;
	
	public CubeField() {}
	
	public void setSize(int xSize, int ySize, int zSize) {
		this.xSize = xSize;
		this.ySize = ySize;
		this.zSize = zSize;
		size = xSize * ySize * zSize;
		x = new int[size];
		y = new int[size];
		z = new int[size];
		xyz = new int[xSize][ySize][zSize];
		for (int i = 0; i < size; i++) {
			x[i] = (i % xSize);
			y[i] = (i / xSize) % ySize;
			z[i] = (i / xSize) / ySize;
			xyz[x[i]][y[i]][z[i]] = i;
		}
		jp = new int[6][size];
		for (int i = 0; i < size; i++) {
			jp[0][i] = (x[i] == xSize - 1) ? -1 : i + 1;
			jp[1][i] = (y[i] == ySize - 1) ? -1 : i + xSize;
			jp[2][i] = (z[i] == zSize - 1) ? -1 : i + xSize * ySize;
			jp[3][i] = (x[i] == 0) ? -1 : i - 1;
			jp[4][i] = (y[i] == 0) ? -1 : i - xSize;
			jp[5][i] = (z[i] == 0) ? -1 : i - xSize * ySize;
		}
		rotation = new int[3][size];
		for (int i = 0; i < size; i++) {
			int ix = x[i], iy = y[i], iz = z[i];
			rotation[0][i] = xyz(ix, ySize - iz - 1, iy);
			rotation[1][i] = xyz(xSize - iz - 1, iy, ix);
			rotation[2][i] = xyz(xSize - iy - 1, ix, iz);
		}
	}
	
	public int jump(int p, int d) {
		return p < 0 || p >= size ? -1 : jp[d][p];
	}
	
	public int xyz(int ix, int iy, int iz) {
		return (ix < 0 || ix >= xSize || iy < 0 || iy >= ySize || iz < 0 || iz >= zSize) ? -1 : xyz[ix][iy][iz];
	}

}
