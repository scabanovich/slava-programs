package match2005;

public class PushField {
	int width;
	int height;
	int size;
	int[] x,y;
	int[][] xy;
	int[][] jp;
	
	int[] constraint;
	int[] values;
	
	public PushField() {		
	}
	
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
		jp = new int[4][size]; 
		for (int i = 0; i < size; i++) {
			jp[0][i] = (x[i] == width - 1) ? -1 : i + 1;
			jp[1][i] = (y[i] == height - 1) ? -1 : i + width;
			jp[2][i] = (x[i] == 0) ? -1 : i - 1;
			jp[3][i] = (y[i] == 0) ? -1 : i - width;
		}
		values = new int[size];
		for (int i = 0; i < size; i++) values[i] = -1;
	}
	
	public void setConstraint(int[] constraint) {
		this.constraint = constraint;
	}
	
	public boolean canAddFigure(int p, int[][] figure, int v) {
		for (int i = 0; i < figure.length; i++) {
			int q = jump(p, figure[i][0], figure[i][1]);
			if(q < 0 || constraint[q] != 1 || (values[q] >= 0 && values[q] != v)) return false;
		}
		return true;
	}

	public void moveFigure(int pb, int pe, int[][] figure, int v) {
		removeFigure(pb, figure);
		addFigure(pe, figure, v);
	}
	
	public void addFigure(int p, int[][] figure, int v) {
		for (int i = 0; i < figure.length; i++) {
			int q = jump(p, figure[i][0], figure[i][1]);
			values[q] = v;
		}
	}

	public void removeFigure(int p, int[][] figure) {
		for (int i = 0; i < figure.length; i++) {
			int q = jump(p, figure[i][0], figure[i][1]);
			values[q] = -1;
		}
	}
	
	public int getSize() {
		return size;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int x(int p) {
		return p < 0 || p >= size ? -1 : x[p];
	}
	
	public int y(int p) {
		return p < 0 || p >= size ? -1 : y[p];
	}
	
	public int getIndex(int ix, int iy) {
		if(ix < 0 || ix >= width) return -1;
		if(iy < 0 || iy >= height) return -1;
		return xy[ix][iy];
	}
	
	public int jump(int p, int dx, int dy) {
		int ix = x[p] + dx;
		int iy = y[p] + dy;
		return (ix < 0 || ix >= width || iy < 0 || iy >= height) ? -1 : xy[ix][iy];
	}

}
