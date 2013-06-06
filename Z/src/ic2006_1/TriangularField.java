package ic2006_1;

import java.util.Arrays;

public class TriangularField {
	int length;
	int size;
	int[] x,y,z,h;
	int[][][] index;
	
	int[][] jp;

	int[] rotation;
	int[] reflection;
	int[] xshift;
	int[] yshift;
	
	public TriangularField() {}
	
	public void setSize(int length) {
		this.length = length;
		size = length * length;
		x = new int[size];
		y = new int[size];
		z = new int[size];
		h = new int[size];
		index = new int[length][length][length];
		for (int ix = 0; ix < length; ix++) {
			for (int iy = 0; iy < length; iy++) {
				for (int iz = 0; iz < length; iz++) {
					index[ix][iy][iz] = -1;
				}
			}
		}
		int q = 0;
		for (int ix = 0; ix < length; ix++) {
			for (int iy = 0; iy < length; iy++) {
				int iz = length - 1 - ix - iy;
				int ih = 0;
				if(iz >= 0) {
					x[q] = ix;
					y[q] = iy;
					z[q] = iz;
					h[q] = ih;
					index[x[q]][y[q]][z[q]] = q;
					++q;
				}
				iz = length - 2 - ix - iy;
				ih = 1;
				if(iz >= 0) {
					x[q] = ix;
					y[q] = iy;
					z[q] = iz;
					h[q] = ih;
					index[x[q]][y[q]][z[q]] = q;
					++q;
				}
			}
		}
		jp = new int[size][3];
		for (int i = 0; i < size; i++) {
			if(h[i] == 1) {
				jp[i][0] = (x[i] + 1 >= length) ? -1 : index[x[i] + 1][y[i]][z[i]];
				jp[i][1] = (y[i] + 1 >= length) ? -1 : index[x[i]][y[i] + 1][z[i]];
				jp[i][2] = (z[i] + 1 >= length) ? -1 : index[x[i]][y[i]][z[i] + 1];
			} else {
				jp[i][0] = (x[i] - 1 < 0) ? -1 : index[x[i] - 1][y[i]][z[i]];
				jp[i][1] = (y[i] - 1 < 0) ? -1 : index[x[i]][y[i] - 1][z[i]];
				jp[i][2] = (z[i] - 1 < 0) ? -1 : index[x[i]][y[i]][z[i] - 1];
			}			
		}
		reflection = new int[size];
		rotation = new int[size];
		xshift = new int[size];
		yshift = new int[size];
		for (int i = 0; i < size; i++) {
			reflection[i] = index[x[i]][z[i]][y[i]];
			rotation[i] = index[z[i]][x[i]][y[i]];
			xshift[i] = (y[i] + 1 >= length || z[i] == 0) ? -1 : index[x[i]][y[i] + 1][z[i] - 1];
			yshift[i] = (x[i] + 1 >= length || z[i] == 0) ? -1 : index[x[i] + 1][y[i]][z[i] - 1];
		}
		
//		for (int i = 0; i < size; i++) {
//			System.out.print(i + ": " + x[i] + "," + y[i] + "," + z[i] + " ns=");
//			for (int d = 0; d < 3; d++) System.out.print(" " + jp[i][d]);
//			System.out.print(" xshift=" + xshift[i]);
//			System.out.println("");
//		}
	}
	
	public int getLength() {
		return length;
	}
	
	public int getSize() {
		return size;
	}
	
	public int[] rotate(int[] pc) {
		return transform(pc, rotation);
	}
	
	public int[] reflect(int[] pc) {
		return transform(pc, reflection);
	}
	
	public int[] shiftAlongX(int[] pc) {
		return transform(pc, xshift);
	}
	
	public int[] shiftAlongY(int[] pc) {
		return transform(pc, yshift);
	}
	
	int[] transform(int[] pc, int[] tr) {
		int[] rs = new int[pc.length];
		for (int i = 0; i < pc.length; i++) {
			rs[i] = tr[pc[i]];
			if(rs[i] < 0) return null;
		}
		Arrays.sort(rs);
		return rs;
	}

}
