package slava.puzzle.stars.analysis;

public class StarsRegionField {
	int width;
	int height;
	int size;
	
	int[] x,y;
	int[][] xy;
	int[][] jp;
	
	int[] colors;
	
	int[][] regions;
	int[][] placeToRegions;
	
	public StarsRegionField() {}
	
	public void setSize(int width) {
		this.width = width;
		this.height = width;
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
			jp[1][i] = jump(i,  0,  1);
			jp[2][i] = jump(i, -1,  0);
			jp[3][i] = jump(i,  0, -1);
			jp[4][i] = jump(i,  1,  1);
			jp[5][i] = jump(i, -1,  1);
			jp[6][i] = jump(i, -1, -1);
			jp[7][i] = jump(i,  1, -1);
		}		
	}
	
	public void setRegions(int[] colors) {
		this.colors = colors;
		buildRegions();
		buildPlaceToRegions();
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getX(int p) {
		return p < 0 || p >= size ? -1 : x[p];
	}
	
	public int getY(int p) {
		return p < 0 || p >= size ? -1 : y[p];
	}
	
	public int getPoint(int ix, int iy) {
		if(ix < 0 || ix >= width) return -1;
		if(iy < 0 || iy >= height) return -1;
		return xy[ix][iy];
	}
	
	public int jump(int p, int dx, int dy) {
		return (p < 0 || p >= size) ? -1 : getPoint(x[p] + dx, y[p] + dy);
	}
	public int jump(int p, int d) {
		return (p < 0 || p >= size) ? -1 : jp[d][p];
	}

	void buildRegions() {
		int regionCount = 3 * width;
		regions = new int[regionCount][];
		buildVerticalRegions();
		buildHorizontalRegions();
		buildColorRegions();
	}
	
	void buildVerticalRegions() {
		for (int ix = 0; ix < width; ix++) {
			regions[ix] = new int[width];
			for (int iy = 0; iy < width; iy++) {
				regions[ix][iy] = xy[ix][iy];
			}
		}
	}

	void buildHorizontalRegions() {
		for (int iy = 0; iy < width; iy++) {
			regions[iy + width] = new int[width];
			for (int ix = 0; ix < width; ix++) {
				regions[iy + width][ix] = xy[ix][iy];
			}
		}
	}
	
	void buildColorRegions() {
		int[] h = new int[width];
		for (int p = 0; p < size; p++) {
			h[colors[p]]++;
		}
		for (int c = 0; c < width; c++) regions[c + 2 * width] = new int[h[c]];
		for (int c = 0; c < width; c++) h[c] = 0;
		for (int p = 0; p < size; p++) {
			int c = colors[p];
			regions[c + 2 * width][h[c]] = p;
			h[c]++;
		}		
	}

	void buildPlaceToRegions() {
		placeToRegions = new int[size][];
		for (int p = 0; p < size; p++) {
			int c = 0;
			for (int r = 0; r < regions.length; r++) if(contains(p, r)) ++c;
			placeToRegions[p] = new int[c];
			c = 0;
			for (int r = 0; r < regions.length; r++) if(contains(p, r)) {
				placeToRegions[p][c] = r;
				++c;
			}
		}
	}
	
	boolean contains(int p, int region) {
		for (int i = 0; i < regions[region].length; i++) {
			if(p == regions[region][i]) return true;
		}
		return false;
	}
	
}
