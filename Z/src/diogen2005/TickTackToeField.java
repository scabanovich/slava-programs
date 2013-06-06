package diogen2005;

public class TickTackToeField {
	int width;
	int height;
	int size;
	
	int[] x,y;
	int[][] xy;
	
	int[][] jp;

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
		jp = new int[8][size];
		for (int i = 0; i < size; i++) {
			jp[0][i] = jump(i,  1,  0);
			jp[1][i] = jump(i,  1,  1);
			jp[2][i] = jump(i,  0,  1);
			jp[3][i] = jump(i, -1,  1);
			jp[4][i] = jump(i, -1,  0);
			jp[5][i] = jump(i, -1, -1);
			jp[6][i] = jump(i,  0, -1);
			jp[7][i] = jump(i,  1, -1);
		}
	}
	
	public int jump(int p, int d) {
		return jp[d][p];
	}
	
	public int jump(int p, int dx, int dy) {
		int ix = x[p] + dx;
		int iy = y[p] + dy;
		return (ix < 0 || ix >= width || iy < 0 || iy >= height) ? -1 : xy[ix][iy];
	}

}
