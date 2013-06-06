package ik4;

public class AlphabetField {
	int width;
	int height;
	int layers;
	
	int size;
	int[] x,y,z;
	int[][][] xyz;
	int[] cx, cy;
	
	int[][] jp;
	
	int[] mainGrid;
	
	public void setSize(int width, int height, int layers) {
		this.width = width;
		this.height = height;
		this.layers = layers;
		size = width * height * layers;
		x = new int[size];
		y = new int[size];
		z = new int[size];
		xyz = new int[width][height][layers];
		mainGrid = new int[size];
		cx = new int[size];
		cy = new int[size];
		for (int i = 0; i < size; i++) {
			x[i] = (i % width);
			y[i] = (i / width) % height;
			z[i] = i / width / height;
			xyz[x[i]][y[i]][z[i]] = i;
			mainGrid[i] = ((x[i] % 2 == 0) && (y[i] % 2 == 0)) ? 1 : 0;
			cx[i] = (x[i] % 2 == 0) ? -1 : x[i] / 2;
			cy[i] = (y[i] % 2 == 0) ? -1 : y[i] / 2;
		}
	}
	
	public int jump(int p, int dx, int dy) {
		return xyz(x[p] + dx, y[p] + dy, z[p]); 
	}
	
	public int xyz(int ix, int iy, int iz) {
		return (ix < 0 || ix >= width || iy < 0 || iy >= height || iz >= layers) ? -1 : xyz[ix][iy][iz]; 
	}
	
	public boolean isMainGridPoint(int p) {
		return mainGrid[p] == 1;
	}
	
	public int getColumn(int p) {
		return cx[p];
	}
	
	public int getRow(int p) {
		return cy[p];
	}	

}
