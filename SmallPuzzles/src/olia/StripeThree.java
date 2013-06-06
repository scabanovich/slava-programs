package olia;

public class StripeThree {
	int length;
	int maxX;
	int maxY;
	
	int width = 3;
	int size;
	
	int[] x,y;
	int[][] xy;
	int[][] jp;
	
	int[][] nv;
	
	int[][] prohibit;
	
	int[] state;
	
	int order1;
	int order2;
	
	public void setLength(int length) {
		this.length = length;
		maxX = length - 1;
		maxY = width - 1;
		size = width * length;
		x = new int[size];
		y = new int[size];
		xy = new int[length][width];
		for (int i = 0; i < size; i++) {
			x[i] = (i % length);
			y[i] = (i / length);
			xy[x[i]][y[i]] = i;
		}
		jp = new int[4][size];
		for (int i = 0; i < size; i++) {
			jp[0][i] = (x[i] >= length - 1) ? -1 : i + 1; 
			jp[1][i] = (y[i] >= width - 1) ? -1 : i + length; 
			jp[2][i] = (x[i] == 0) ? -1 : i - 1; 
			jp[3][i] = (y[i] == 0) ? -1 : i - length; 
		}
		
		state = new int[size];
		for (int i = 0; i < size; i++) {
			state[i] = maxX - x[i];
		}
		nv = new int[length][length];
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length;j++) {
				nv[i][j] = (i == j + 1 || j == i + 1) ? 1 : 0; 
			}
		}
		prohibit = new int[length][size];
		order1 = 0;
		order2 = 0;
		for (int i = 0; i < size; i++) {
			prohibit[0][i] = (y[i] < 2 && x[i] > 0 && x[i] < maxX) ? 1 : 0;
			prohibit[maxX][i] = (y[i] > 0 && x[i] > 0 && x[i] < maxX) ? 1 : 0;
		}
	}
	
	boolean move() {
		int p = (int)(Math.random() * size);
		int d = (int)(Math.random() * 4);
		int q = jp[d][p];
		if(q < 0 || nv[state[p]][state[q]] == 0) return false;
		if(q < p) {
			int c = p;
			p = q;
			q = c;
		}

		if(state[p] == 0 && x[p] == 0 && x[q] > 0) return false;
		if(state[q] == maxX && x[q] == maxX && x[p] < maxX) return false;
		
		if(x[p] == 0 && y[p] == maxY) {
			if(state[p] < state[q]) return false;
		}
		if(x[q] == 0 && y[q] == maxY) {
			if(state[q] < state[p]) return false;
		}
		if(state[xy[0][maxY]] < 4 && p == xy[maxX][0]) {
			if(state[p] > state[q]) return false;
		}
		if(state[xy[0][maxY]] < 4 && q == xy[maxX][0]) {
			if(state[q] > state[p]) return false;
		}
		if(order1 == 0 || order2 == 0) {
			if(state[p] == 0 && x[p] == maxX && y[p] != 0) {
				return false;
			}
			if(state[q] == 0 && x[q] == maxX && y[q] != 0) {
				return false;
			}
			if(state[p] == maxX && x[p] == 0 && y[p] != maxY) {
				return false;
			}
			if(state[q] == maxX && x[q] == 0 && y[q] != maxY) {
				return false;
			}
		}
		
		if(order1 > 0 && order1 > 0) {
			if(x[p] == 0 && y[p] == 1) {
				if(state[p] < state[q]) return false;
			}
			if(x[q] == 0 && y[q] == 1) {
				if(state[q] < state[p]) return false;
			}
			if(state[xy[0][1]] < 4 && p == xy[maxX][1]) {
				if(state[p] > state[q]) return false;
			}
			if(state[xy[0][1]] < 4 && q == xy[maxX][1]) {
				if(state[q] > state[p]) return false;
			}
		}
		
		int c = state[p];
		state[p] = state[q];
		state[q] = c;
		if(state[p] == 0 && x[p] == 0 && x[q] > 0) {
			++order1;
		} else if(state[q] == 0 && x[q] == 0 && x[p] > 0) {
			++order1;
		}
		if(state[p] == maxX && x[p] == maxX && x[q] < maxX) {
			++order2;
		} else if(state[q] == maxX && x[q] == maxX && x[p] < maxX) {
			++order2;
		}
		
		return true;
	}
	
	public void run() {
		int count1 = 0;
		int count2 = 0;
		while(order1 + order2 < 4) {
			if(move()) {
				++count1;
				if(count1 == 10000) {
					++count2;
					count1 = 0;
					if(count2 % 100 == 0) {
						System.out.println((count2 / 100));
						printState();
					}
				}
			}
		}
		printState();
	}
	
	void printState() {
		for (int i = 0; i < size; i++) {
			System.out.print(" " + state[i]);
			if(x[i] == maxX) System.out.println("");
		}		
	}

	public static void main(String[] args) {
		StripeThree s = new StripeThree();
		s.setLength(10);
		s.run();
	}
}
