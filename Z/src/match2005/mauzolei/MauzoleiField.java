package match2005.mauzolei;

public class MauzoleiField {
	int xSize;
	int ySize;
	int zSize;
	int size;
	
	int[] filter;
	
	int[] x,y,z;
	int[][][] xyz;
	
	int[] zReflection;
	int[] zRotation;
	int[][] xRotations;
	
	public MauzoleiField() {}
	
	public void setSize(int xSize, int ySize, int zSize, int[] filter) {
		this.xSize = xSize;
		this.ySize = ySize;
		this.zSize = zSize;
		size = xSize * ySize * zSize;
		this.filter = filter;
		x = new int[size];
		y = new int[size];
		z = new int[size];
		xyz = new int[xSize][ySize][zSize];
		for (int i = 0; i < size; i++) {
			x[i] = (i % xSize);
			y[i] = (i / xSize) % ySize;
			z[i] = i / xSize / ySize;
			xyz[x[i]][y[i]][z[i]] = i;
		}
		zReflection = new int[size];
		zRotation = new int[size];
		xRotations = new int[size][ySize];
		for (int i = 0; i < size; i++) {
			zReflection[i] = position(y[i], x[i], z[i]);
			zRotation[i] = position(ySize - y[i] - 1, x[i], z[i]);
			for (int j = 0; j < ySize; j++) {
				xRotations[i][j] = position(x[i], ySize - 1 - z[i] - j, y[i] - 2);
			}
		}
	}
	
	public int position(int ix, int iy, int iz) {
		return (ix < 0 || ix >= xSize || iy < 0 || iy >= ySize || iz < 0 || iz >= zSize || filter[xyz[ix][iy][iz]] == 0) ? -1 : xyz[ix][iy][iz];
	}
	
	public int jump(int p, int dx, int dy, int dz) {
		return position(x[p] + dx, y[p] + dy, z[p] + dz);
	}

	public int jump(int p, int d) {
		if(d == 0) return jump(p, 1, 0, 0);
		if(d == 1) return jump(p, 0, 1, 0);
		if(d == 2) return jump(p, 0, 0, 1);
		if(d == 3) return jump(p, -1, 0, 0);
		if(d == 4) return jump(p, 0, -1, 0);
		if(d == 5) return jump(p, 0, 0, -1);
		return -1;
	}

}
