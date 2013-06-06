package champ2005;

public class CubeField {
	int width;
	int size;
	int[] x,y,z;
	int[][][] xyz;
	int[][] jp;
	
	int[][] proections;
	
	public CubeField() {}
	
	public void setSize(int width) {
		this.width = width;
		size = width * width * width;
		x = new int[size];
		y = new int[size];
		z = new int[size];
		xyz = new int[width][width][width];
		for (int i = 0; i < size; i++) {
			x[i] = (i % width);
			y[i] = (i / width) % width;
			z[i] = i / width / width;
			xyz[x[i]][y[i]][z[i]] = i;
		}
		jp = new int[6][size];
		for (int i = 0; i < size; i++) {
			jp[0][i] = (x[i] == width - 1) ? -1 : i + 1;
			jp[1][i] = (y[i] == width - 1) ? -1 : i + width;
			jp[2][i] = (z[i] == width - 1) ? -1 : i + width * width;
			jp[3][i] = (x[i] == 0) ? -1 : i - 1;
			jp[4][i] = (y[i] == 0) ? -1 : i - width;
			jp[5][i] = (z[i] == 0) ? -1 : i - width * width;
		}
		proections = new int[3][size];
		for (int i = 0; i < size; i++) {
			proections[0][i] = xyz[0][y[i]][z[i]];
			proections[1][i] = xyz[x[i]][0][z[i]];
			proections[2][i] = xyz[x[i]][y[i]][0];
		}
	}
	
	


}
