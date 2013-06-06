package slava.puzzles.twotwomino.model;

public class ExtendedFigureTestField {
	int width;
	int height;
	int size;
	int[] x,y;
	int[][] xy;
	int[][] jp;
	
	int[] field;
	
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
		field = new int[size];		
	}
	
	public boolean checkExtendedFigure(int[][] f) {
		for (int i = 0; i < size; i++) field[i] = 0;
		for (int i = 0; i < f.length; i++) {
			int xi = 2 * f[i][0] + 4;
			int yi = 2 * f[i][1];
			int s = f[i][2];
			if((s & 1) != 0) field[xy[xi][yi]] = 1;
			if((s & 2) != 0) field[xy[xi + 1][yi]] = 1;
			if((s & 4) != 0) field[xy[xi][yi + 1]] = 1;
			if((s & 8) != 0) field[xy[xi + 1][yi + 1]] = 1;
		}
		return checkConnectedness();
	}
	
	int[] is = new int[100];
	
	private boolean checkConnectedness() {
		int volume = 0;
		int vb = -1;
		for (int i = 0; i < size; i++) if(field[i] == 1) {
			if(vb < 0) vb = i;
			volume++;
		}
		int v = 1;
		int c = 0;
		is[0] = vb;
		field[vb] = 2;
		while(c < v) {
			int p = is[c];
			for (int d = 0; d < 4; d++) {
				int q = jp[d][p];
				if(q < 0 || field[q] != 1) continue;
				is[v] = q;
				++v;
				field[q] = 2;
			}
			++c;
		}
		return (v == volume);
	}
	

}
