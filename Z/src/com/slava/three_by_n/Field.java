package com.slava.three_by_n;

public class Field {
	int width;
	int height;

	int size;
	int[] x,y;
	int[][] xy;
	int[][] jp;

	public Field() {}
	
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
	}
	
	public int getSize() {
		return size;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getX(int p) {
		return p < 0 || p >= size ? -1 : x[p];
	}
	
	public int getY(int p) {
		return p < 0 || p >= size ? -1 : y[p];
	}
	
	public int getIndex(int ix, int iy) {
		return ix < 0 || ix >= width || iy < 0 || iy >= height ? -1 : xy[ix][iy];
	}
	
	public int jump(int p, int d) {
		return jp[d][p];
	}

}
