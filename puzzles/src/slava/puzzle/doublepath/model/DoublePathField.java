package slava.puzzle.doublepath.model;

public class DoublePathField {
	int width;
	int height;
	int size;
	int[] x, y;
	int[][] xy;
	int[][] jp;
	
	int[] states; // 1 - black, 3 - black end, 2 - white, 4 - white end
	
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
		states = new int[size];
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
	
	public int getState(int i) {
		return states[i];
	}
	
	public boolean isWhite(int i) {
		return states[i] == 2 || states[i] == 4;
	}
	
	public boolean isBlack(int i) {
		return states[i] == 1 || states[i] == 3;
	}
	
	public void setState(int i, int v) {
		states[i] = v;
	}
	
	public int x(int i) {
		return x[i];
	}
	
	public int y(int i) {
		return y[i];
	}
	
	public int jump(int point, int d) {
		return jp[d][point];
	}
	
	public boolean areSeparatedByPoint(int p1, int p2) {
		if(p1 == p2) return false;
		int dx = x[p2] - x[p1];
		int dy = y[p2] - y[p1];
		for (int i = 2; i < width; i++) {
			if(i > Math.abs(dx) && i > Math.abs(dy)) break;
			while(dx % i == 0 && dy % i == 0) {
				dx = dx / i;
				dy = dy / i;
			}
		}
		int p = xy[x[p1] + dx][y[p1] + dy];
		while(p != p2) {
			if(getState(p) != 0) return true;
			p = xy[x[p] + dx][y[p] + dy];
		}
		return false;
	}
	
	public int[] getStateCopy() {
		return (int[])states.clone();
	}
	
	public void setStates(int[] states) {
		for (int i = 0; i < size; i++) this.states[i] = states[i];
	}

}
