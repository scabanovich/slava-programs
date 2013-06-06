package com.slava.snake;

public class Snake {
	int n;
	int width;
	int length;
	int size;
	int[] field;
	int snakeLength;
	
	int[] usedLength;
	
	int[] x,y;
	int[][] xy;
	int[][] jp;
	
	int variantCount;
	int[][] variants;
	int[] variantLength;
	
	int tMax;
	int t;
	int[] way;
	int[] wayCount;
	int[][] ways;
	
	int solutionCount;
	
	public void setN(int n) {
		this.n = n;
		width = n + 1;
		length = n;
		size = width * length;
		x = new int[size];
		y = new int[size];
		xy = new int[width][length];
		for (int i = 0; i < size; i++) {
			x[i] = (i % width);
			y[i] = (i / width);
			xy[x[i]][y[i]] = i;
		}
		jp = new int[4][size];
		for (int i = 0; i < size; i++) {
			jp[0][i] = (x[i] == width - 1) ? -1 : i + 1;
			jp[1][i] = (y[i] == length - 1) ? -1 : i + width;
			jp[2][i] = (x[i] == 0) ? -1 : i - 1;
			jp[3][i] = (y[i] == 0) ? -1 : i - width;
		}

		field = new int[size];
		for (int ix = 0; ix < width; ix++) {
			field[xy[ix][0]] = 1;
		}
		field[xy[0][1]] = 1;
		field[xy[width - 1][1]] = 1;

		variantCount = 1;
		for (int i = 0; i < width; i++) variantCount = variantCount * 2;
		variants = new int[variantCount][width];
		variantLength = new int[variantCount];
		for (int i = 0; i < variantCount; i++) {
			int j = i;
			variantLength[i] = 0;
			for (int k = 0; k < width; k++) {
				variants[i][k] = (j % 2);
				variantLength[i] += variants[i][k];
				j = j / 2;
			}
		}
		
		snakeLength = 0;
		for (int k = 2; k <= width; k++) snakeLength += k;
	}
	
	public void solve() {
		if(snakeLength % 2 == 1) {
			System.out.println("snakeLength=" + snakeLength);
			return;
		}
		init();
		anal();
	}
	
	void init() {
		tMax = length - 2;
		wayCount = new int[tMax + 1];
		way = new int[tMax + 1];
		ways = new int[tMax + 1][variantCount];

		usedLength = new int[n + 2];
		usedLength[0] = 1;
		usedLength[1] = 1;
		usedLength[2] = 1;
		usedLength[n + 1] = 1;
		
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == tMax) return;
		int iy = t + 2;
		for (int v = 0; v < variantCount; v++) {
			if(usedLength[variantLength[v]] != 0) continue;
			putLine(iy, v);
			if(accept(iy)) {
				ways[t][wayCount[t]] = v;
				wayCount[t]++;
			}
			clearLine(iy);
		}
		randomize();
	}
	
	int wayCountLimit = 8;	
	int mwc = 0;

	void randomize() {
		if(t > 7) {
			if(wayCount[t] > mwc) {
				mwc = wayCount[t];
				System.out.println("mwc=" + mwc);
			}
			return;
		}
		int wc = wayCount[t];
		if(wc > wayCountLimit) wc = wayCountLimit;
		for (int i = 0; i < wc; i++) {
			int j = i + (int)(Math.random() * (wayCount[t] - i));
			if(i == j) continue;
			int c = ways[t][i];
			ways[t][i] = ways[t][j];
			ways[t][j] = c;
		}
		wayCount[t] = wc;
	}
	
	boolean accept(int iy) {
		int tails = 0;
		for (int ix = 0; ix < width; ix++) {
			int i = xy[ix][iy];
			if(field[i] == 1) {
				int n = getNeighbours(i);
				if(n > 2 || n == 0) return false;
				if(n == 1) tails++;
			}
			int i1 = xy[ix][iy - 1];
			if(field[i1] == 1) {
				int n = getNeighbours(i1);
				if(n != 2) return false;
			}
		}
		if(iy < length - 1 && tails < 2) return false;
		if(iy == length - 1 && tails != 0) return false;
		//  0 1
		//  1 0
		for (int ix = 0; ix < width - 1; ix++) {
			int i01 = xy[ix][iy];
			int i00 = xy[ix][iy - 1];
			int i11 = xy[ix + 1][iy];
			int i10 = xy[ix + 1][iy - 1];
			if(field[i00] == field[i11] && field[i01] == field[i10]) {
				if(field[i00] != field[i10]) return false;
				if(field[i00] == 1) return false;
			}
		}
		//    0 0
		//  0 1 1 0
		for (int ix = 1; ix < width - 2; ix++) {
			if(field[xy[ix - 1][iy]] != 0) continue;
			if(field[xy[ix][iy]] != 1) continue;
			if(field[xy[ix + 1][iy]] != 1) continue;
			if(field[xy[ix + 2][iy]] != 0) continue;
			if(field[xy[ix][iy - 1]] != 0) continue;
			if(field[xy[ix + 1][iy - 1]] != 0) continue;
			return false;
		}
		return true;
	}
	
	int getNeighbours(int i) {
		int q = 0;
		for (int d = 0; d < 4; d++) {
			int j = jp[d][i];
			if(j >= 0 && field[j] == 1) ++q;
		}
		return q;
	}
	
	void move() {
		int v = ways[t][way[t]];
		putLine(t + 2, v);
		usedLength[variantLength[v]]++;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int v = ways[t][way[t]];
		clearLine(t + 2);
		usedLength[variantLength[v]]--;
	}
	
	void putLine(int iy, int v) {
		for (int ix = 0; ix < width; ix++) {
			int i = xy[ix][iy];
			field[i] = variants[v][ix];
		}
	}

	void clearLine(int iy) {
		for (int ix = 0; ix < width; ix++) {
			int i = xy[ix][iy];
			field[i] = 0;
		}
	}
	

	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t == tMax && isConnected()) {
				printSolution();
				solutionCount++;
			}
			
		}
	}
	
	boolean isConnected() {
		int pp = 0;
		int pc = 1;
		int v = 2;
		while(true) {
			int pn = -1;
			for (int d = 0; d < 4; d++) {
				int i = jp[d][pc];
				if(i < 0 || i == pp || field[i] != 1) continue;
				pn = i;
				break;
			}
			if(pn < 0) return false;
			if(pn == 0) break;
			pp = pc;
			pc = pn;
			++v;
		}
		return v == snakeLength;
	}
	
	void printSolution() {
		System.out.println();
		for (int i = 0; i < size; i++) {
			System.out.print(" " + field[i]);
			if(x[i] == width - 1) System.out.println();
		}
	}
	
	public static void main(String[] args) {
		Snake snake = new Snake();
		snake.setN(13);
		snake.solve();
		System.out.println("solutionCount=" + snake.solutionCount);
	}

}

/**
 1 1 1 1 1 1 1 1 1 1 1 1 1
 1 0 0 0 0 0 0 0 0 0 0 0 1
 1 1 1 1 0 1 1 1 1 1 1 0 1
 0 0 0 1 0 1 0 0 0 0 1 1 1
 0 1 1 1 0 1 1 1 1 0 0 0 0
 1 1 0 0 0 0 0 0 1 0 1 1 1
 1 0 0 1 1 1 1 0 1 0 1 0 1
 1 1 1 1 0 0 1 0 1 1 1 0 1
 0 0 0 0 0 1 1 0 0 0 0 0 1
 1 1 1 1 1 1 0 0 1 1 1 0 1
 1 0 0 0 0 0 0 0 1 0 1 0 1
 1 1 1 1 1 1 1 1 1 0 1 1 1
 */