package slava.puzzle.stars.model;

public class StarsSetsField {
	int width;
	int height;
	int size;
	
	int[] x,y;
	int[][] xy;
	int[][] jp;
	
	int[] sets;
	int[] setVolumes;
	
	public void setSize(int width) {
		if(this.width == width) return;
		this.width = width;
		this.height = width;
		size = width * height;
		x = new int[size];
		y = new int[size];
		xy = new int[width][height];
		for (int i = 0; i < size; i++) {
			x[i] = i % width;
			y[i] = i / width;
			xy[x[i]][y[i]] = i;
		}
		jp = new int[4][size];
		for (int i = 0; i < size; i++) {
			jp[0][i] = (x[i] == width - 1) ? -1 : i + 1;
			jp[1][i] = (y[i] == height - 1) ? -1 : i + width;
			jp[2][i] = (x[i] == 0) ? -1 : i - 1;
			jp[3][i] = (y[i] == 0) ? -1 : i - width;
		}
		sets = new int[size];
		setVolumes = new int[width];
		setVolumes[0] = size;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getSize() {
		return size;
	}
	
	public int x(int p) {
		return p < 0 || p >= size ? -1 : x[p];
	}

	public int y(int p) {
		return p < 0 || p >= size ? -1 : y[p];
	}
	
	public int jump(int p, int d) {
		return jp[d][p];
	}
	
	public int getIndex(int h, int v) {
		return xy[v][h];
	}
	
	public int getSetAt(int p) {
		return p < 0 || p >= size ? -1 : sets[p];
	}
	
	public void setSetAt(int p, int c) {
		if(p < 0 || p >= size) return;
		if(c < 0 || c >= width) return;
		if(sets[p] == c) return;
		setVolumes[sets[p]]--;
		sets[p] = c;
		setVolumes[sets[p]]++;
	}
	
	public int getSetVolume(int c) {
		return c < 0 || c >= width ? 0 : setVolumes[c];
	}
	
	public int getNewSetIndex() {
		for (int c = 0; c < width; c++) {
			if(setVolumes[c] == 0) return c;
		}
		return -1;
	}
	
	public int[] getSets() {
		return sets;
	}


}
