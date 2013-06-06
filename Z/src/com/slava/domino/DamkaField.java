package com.slava.domino;

public class DamkaField {
	int length;
	int size;
	
	int[] x, y;
	int[][] xy;
	int[][] jp;
	
	public DamkaField(int length) {
		this.length = length;
		init();
	}
	
	void init() {
		size = length * length / 2;
		x = new int[size];
		y = new int[size];
		xy = new int[length][length];
		for (int ix = 0; ix < length; ix++) {
			for (int iy = 0; iy < length; iy++) {
				xy[ix][iy] = -1;
			}
		}
		for (int i = 0; i < size; i++) {
			int i2 = 2 * i;
			y[i] = i2 / length;
			x[i] = i2 % length + (y[i] % 2);
			xy[x[i]][y[i]] = i;
		}
		jp = new int[4][size];
		for (int i = 0; i < size; i++) {
			jp[0][i] = (x[i] == length - 1 || y[i] == length - 1) 
						? -1 : xy[x[i] + 1][y[i] + 1];
			jp[1][i] = (x[i] == 0 || y[i] == length - 1) 
						? -1 : xy[x[i] - 1][y[i] + 1];
			jp[2][i] = (x[i] == 0 || y[i] == 0) 
						? -1 : xy[x[i] - 1][y[i] - 1];
			jp[3][i] = (x[i] == length - 1 || y[i] == 0) 
						? -1 : xy[x[i] + 1][y[i] - 1];
		}
	}
	
	public void print(byte[] state) {
		int fs = length * length;
		for (int i2 = 0; i2 < fs; i2++) {
			int ix = i2 % length;
			int iy = i2 / length;
			int i = xy[ix][iy];
			char c = (i < 0) ? ' ' : state[i] == -1 ? '.' : state[i] == 0 ? 'o': state[i] == 1 ? 'x': state[i] == 2 ? 'O' : 'X';
			System.out.print(" " + c);
			if(ix == length - 1) System.out.println("");
		}
	}

}
