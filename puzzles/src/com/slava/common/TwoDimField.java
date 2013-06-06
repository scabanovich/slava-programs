package com.slava.common;

public class TwoDimField {
	protected int width;
	protected int height;
	protected int size;
	protected int[] x,y;
	protected int[][] xy;
	protected int[][] jp;
	
	int[][] orts;
	
	public static int[][] RECTANGULAR_ORTS = {
		{1,0}, {0,1}, {-1,0}, {0,-1}
	};
	
	public static int[][] DIAGONAL_ORTS = {
		{1,0}, {1,1}, {0,1}, {-1,1}, {-1,0}, {-1,-1}, {0,-1}, {1,-1}
	};
	
	public static int[][] TRIANGULAR_ORTS = {
		{1,0}, {1,1}, {0,1}, {-1,0}, {-1,-1}, {0,-1}
	};
	
	public TwoDimField() {}
	
	public void setOrts(int[][] orts) {
		this.orts = orts;
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		size = width * height;
		x = new int[size];
		y = new int[size];
		xy = new int[width][height];
		for (int p = 0; p < size; p++) {
			x[p] = (p % width);
			y[p] = (p / width);
			xy[x[p]][y[p]] = p;
		}
		jp = new int[orts.length][size];
		for (int p = 0; p < size; p++) {
			for (int d = 0; d < orts.length; d++) {
				jp[d][p] = jump(p, d, 1);
			}
		}		
	}

	public int getSize() {
		return size;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getIndex(int ix, int iy) {
		return (ix < 0 || ix >= width || iy < 0 || iy >= height) ? -1 : xy[ix][iy];
	}
	
	public int getX(int p) {
		return x[p];
	}
	
	public int getY(int p) {
		return y[p];
	}
	
	public int jump(int p, int d) {
		return (p < 0 || p >= size) ? -1 : jp[d][p];
	}
	
	public int jump(int p, int d, int r) {
		if(p < 0 || p >= size) return -1;
		int ix = x[p] + orts[d][0] * r;
		int iy = y[p] + orts[d][1] * r;
		return getIndex(ix, iy);
	}
	
	public int jumpXY(int p, int dx, int dy) {
		return (p < 0 || p >= size) ? -1 : getIndex(x[p] + dx, y[p] + dy);
	}
	
	public boolean isRightBorder(int p) {
		return x[p] == width - 1;
	}
	
}
