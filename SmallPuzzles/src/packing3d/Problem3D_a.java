package packing3d;

public class Problem3D_a extends Problem3D {

	public Problem3D_a() {
		setB();
	}
	
	void setA() {
		xSize = 5;
		ySize = 4;
		zSize = 4;
		figures = new int[][][] {
			{{0,0,0},{0,0,1},{0,1,0},{0,1,1},{1,0,0},{1,0,1},{1,1,0},{1,1,1}},	
			{{0,0,0},{0,0,1},{0,1,0},{0,1,1},{1,0,0},{1,0,1},{2,0,0},{2,0,1}},	
			{{0,0,0},{0,0,1},{0,1,0},{0,1,1},{0,2,1},{0,2,2},{1,2,1},{1,2,2}},	
		};
		usageLimit = new int[]{3,3,4};
	}

	void setB() {
		xSize = 5;
		ySize = 5;
		zSize = 2;
		int[][] figure = {{0,0,0},{1,0,0},{2,0,0},{2,1,0},{3,1,0}};
		figures = new int[][][]{figure};
		int count = xSize * ySize * zSize / figure.length;
		usageLimit = new int[]{count};
	}

}
