package champukr;

import java.util.*;

public class DominoCubeField {
	int width;
	int volumeSize;
	int surfaceSize;
	int[] x,y,z;
	int[][] jp;
	int[][][] index;
	int[] surfaceIndexToVolumeIndex;
	int[] volumeIndexToSurfaceIndex;
	
	// [equation][surfaceIndex]
	int[][] equations;	
	int[][] surfaceIndexToEquations;
	
	int[] order;

	public void setSize(int width) {
		this.width = width;
		volumeSize = width * width * width;
		surfaceSize = volumeSize - (width - 2) * (width - 2) * (width - 2);
		x = new int[volumeSize];
		y = new int[volumeSize];
		z = new int[volumeSize];
		index = new int[width][width][width];
		for (int i = 0; i < volumeSize; i++) {
			x[i] = (i % width);
			y[i] = (i / width) % width;
			z[i] = (i / width) / width;
			index[x[i]][y[i]][z[i]] = i;
		}
		jp = new int[volumeSize][6];
		for (int i = 0; i < volumeSize; i++) {
			jp[i][0] = (x[i] == width - 1) ? -1 : i + 1;
			jp[i][1] = (y[i] == width - 1) ? -1 : i + width;
			jp[i][2] = (z[i] == width - 1) ? -1 : i + width * width;
			jp[i][3] = (x[i] == 0) ? -1 : i - 1;
			jp[i][4] = (y[i] == 0) ? -1 : i - width;
			jp[i][5] = (z[i] == 0) ? -1 : i - width * width;
		}
		surfaceIndexToVolumeIndex = new int[surfaceSize];
		volumeIndexToSurfaceIndex = new int[volumeSize];
		for (int i = 0; i < volumeSize; i++) volumeIndexToSurfaceIndex[i] = -1;
		int c = 0;
		for (int i = 0; i < volumeSize; i++) {
			if(isOnSurface(i)) {
				surfaceIndexToVolumeIndex[c] = i;
				volumeIndexToSurfaceIndex[i] = c;
				++c;
			}
		}
		buildEquations();
		buildSurfaceIndexToEquationsMaping();
		///printSurfaceIndexToEquations();
		buildOrder();
		printOrder();
	}
	
	public boolean isOnSurface(int vIndex) {
		if(vIndex < 0 || vIndex >= volumeSize) return false;
		return x[vIndex] == 0 || x[vIndex] == width - 1
			|| y[vIndex] == 0 || y[vIndex] == width - 1
			|| z[vIndex] == 0 || z[vIndex] == width - 1;
	}
	
	public int getSurfaceIndex(int ix, int iy, int iz) {
		if(ix < 0 || ix >= width) return -1;
		if(iy < 0 || iy >= width) return -1;
		if(iz < 0 || iz >= width) return -1;
		int v = index[ix][iy][iz];
		return volumeIndexToSurfaceIndex[v];
	}
	
	public int jumpOnSurface(int s, int d) {
		int v = surfaceIndexToVolumeIndex[s];
		int v2 = jp[v][d];
		return v2 < 0 ? -1 : volumeIndexToSurfaceIndex[v2];
	}
	
	static int[][] VECTORS = {
		{1,0,0},{0,1,0},{0,0,1},
		{1,1,0},{1,0,1},{0,1,1},
		{1,-1,0},{1,0,-1},{0,1,-1}	
	};
	
	void buildEquations() {
		List list = new ArrayList();
		for (int i = 0; i < volumeSize; i++) {
			if(!isOnSurface(i)) continue;
			for (int vc  = 0; vc < VECTORS.length; vc++) {
				int ix = x[i];
				int iy = y[i];
				int iz = z[i];
				int dx = VECTORS[vc][0];
				int dy = VECTORS[vc][1];
				int dz = VECTORS[vc][2];
				int[] equation = new int[width];
				boolean ok = true;
				for (int k = 0; k < width && ok; k++) {
					int s = getSurfaceIndex(ix + dx * k, iy + dy * k, iz + dz * k);
					equation[k] = s;
					if(s < 0) ok = false;
				}
				if(ok) list.add(equation);
			}
		}
		equations = (int[][])list.toArray(new int[0][]);
	}
	
	void buildSurfaceIndexToEquationsMaping() {
		surfaceIndexToEquations = new int[surfaceSize][];
		for (int i = 0; i < surfaceSize; i++) {
			int c = 0;
			for (int k = 0; k < equations.length; k++) {
				if(contains(equations[k], i)) ++c;
			}
			surfaceIndexToEquations[i] = new int[c];
			c = 0;
			for (int k = 0; k < equations.length; k++) {
				if(contains(equations[k], i)) {
					surfaceIndexToEquations[i][c] = k;
					++c;
				}
			}
		}
	}
	
	boolean contains(int[] equation, int s) {
		for (int i = 0; i < equation.length; i++) {
			if(equation[i] == s) return true;
		}
		return false;
	}
	
	void printSurfaceIndexToEquations() {
		for (int i = 0; i < surfaceSize; i++) {
			System.out.print(i + ": ");
			for (int j = 0; j < surfaceIndexToEquations[i].length; j++) {
				System.out.print(" " + surfaceIndexToEquations[i][j]);
			}
			System.out.println("");
		}
	}
	
	//building optimal order
	void buildOrder() {
		order = new int[surfaceSize];
		int[] state = new int[surfaceSize];
		order[0] = 0;
		state[0] = 1;
		for (int i = 1; i < surfaceSize; i++) {
			int c = getBestPlace(state);
			state[c] = 1;
			order[i] = c;
		}
	}
	
	int getBestPlace(int[] state) {
		int be = 0;
		int bp = -1;
		for (int i = 0; i < surfaceSize; i++) {
			if(state[i] > 0) continue;
			int e = getEstimate(i, state);
			if(e > be) {
				be = e;
				bp = i;
			}
			
		}
		return bp;
	}
	
	int getEstimate(int s, int[] state) {
		int q = 0;
		int[] eqs = surfaceIndexToEquations[s];
		for (int i = 0; i < eqs.length; i++) {
			int[] equation = equations[eqs[i]];
			int w = 1;
			for (int k = 0; k < equation.length; k++) {
				if(state[equation[k]] == 1) w += 2;
			}
			q += w * w; 
		}
		return q;
	}
	
	void printOrder() {
		for (int i = 0; i < surfaceSize; i++) {
			System.out.print(" " + order[i]);
		}
		System.out.println("");
	}

}
