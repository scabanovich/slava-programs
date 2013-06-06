package smallpuzzles.matrix;

import java.util.Random;

public class MatrixTransposition {
	int width;
	int height;
	int[] matrix;
	
	int operations;

	public MatrixTransposition(int width, int height, int[] matrix) {
		this.width = width;
		this.height = height;
		this.matrix = matrix;
	}
	
	boolean isNotTransferredYet(int p) {
		int q = getTransposedIndex(p);
		while(q != p) {
			if(q < p) return false;
			q = getTransposedIndex(q);
		}
		return true;		
	}
	
	public void transpose() {
		operations = 0;
		int size = width * height;
		for (int p = 0; p < size; p++) {
			if(!isNotTransferredYet(p)) continue;
			int c = matrix[p];
			int q = getTransposedIndex(p);
			while(q != p) {
				int c1 = matrix[q];
				matrix[q] = c;
				c = c1;
				q = getTransposedIndex(q);
			}
			matrix[p] = c;
		}
	}
	
	public void study() {
		int[] visited = new int[width * height];
		int g = 0;
		for (int p = 0; p < visited.length; p++) {
			if(visited[p] > 0) continue;
			g++;
			int q = p;
			int v = 0;
			do {
				visited[q] = g;
				v++;
				q = getTransposedIndex(q);
			} while(q != p);
			if(g < 20) {
				System.out.print(" "+ p + ":" + v + ",");
			}
		}
		if(g >= 20) {
			System.out.print(" ..." + " totally " + g + " cycles");
		}
		System.out.println("");
//		for (int p = 0; p < visited.length; p++) {
//			String s = "" + visited[p];
//			while(s.length() < 3) s = " " + s;
//			System.out.print(" " + s);
//			if((p % width) == width - 1) {
//				System.out.println("");
//			}
//		}
	}
	
	void printMatrixA() {
		for (int p = 0; p < matrix.length; p++) {
			String s = "" + matrix[p];
			while(s.length() < 4) s = " " + s;
			System.out.print(" " + s);
			if((p % width) == width - 1) System.out.println("");
		}
		System.out.println("");
	}
	
	void printMatrixB() {
		for (int p = 0; p < matrix.length; p++) {
			String s = "" + matrix[p];
			while(s.length() < 4) s = " " + s;
			System.out.print(" " + s);
			if((p % height) == height - 1) System.out.println("");
		}
		System.out.println("");
	}
	
	int getTransposedIndex(int p) {
		operations++;
		int px = p % width;
		int py = p / width;
		return px * height + py;
	}
	
	static int[] getRandowMatrix(int w, int h) {
		int[] m = new int[w * h];
		Random r = new Random();
		for (int p = 0; p < m.length; p++) {
			m[p] = r.nextInt(100);
		}
		return m;
	}
	
	static void findMostExpensiveMatrix(int maxDimension) {
		double maxCost = 0d;
		int mw = 0;
		int mh = 0;
		for (int w = 401; w <= maxDimension; w++) {
			for (int h = 1; h < w; h++) {
				int[] m = getRandowMatrix(w, h);
				MatrixTransposition processor = new MatrixTransposition(w, h, m);
				processor.transpose();
				double cost = 1d * processor.operations / w / h;
				if(cost > maxCost) {
					maxCost = cost;
					mw = w;
					mh = h;
					System.out.println("" + mw + "x" + mh + " " + cost);
				}				
			}
		}
	}
	
	static void test() {
		int w = 21;
		int h = 23;
		int[] m = getRandowMatrix(w, h);
		MatrixTransposition processor = new MatrixTransposition(w, h, m);
		processor.study();
//		processor.printMatrixA();
		processor.transpose();
//		processor.printMatrixB();
		double operationsPerCell = 1d * processor.operations / w / h;
		System.out.println("Operations (per cell): " + operationsPerCell);
	}
	
	static double ss(int a, int b) {
		long w = 1;
		for (int i = 0; i < b; i++) w *= 2;
		double s = 1d * a;
		int ds = 0;
		long d = 1;
		long dw = w - d;
		int i = 0;
		while(true) {
			i++;
			d = d * (a + 1 - i) / i;
			if(d == 0) return 0d;
			System.out.println("->" + i + " " + d + " " + dw);
			if(d >= dw) {
				ds += dw * i;
				break;
			} else {
				ds += d * i;
				dw -= d;
			}
		}
		s -= 1d * ds / w;
		double v = 1d * a + (a - b) / 2d;
		double rate = s / v;
		return rate;
	}

	public static void main(String[] args) {
		double best = 0.6d;
		for (int a = 50; a < 51; a++) {
			for (int b = 24; b < 25; b++) {
				double q = ss(a, b);
				if(q > best) {
					best = q;
					System.out.println("a=" + a + " b=" + b + " q=" + q);
				}
				
			}
		}
//		findMostExpensiveMatrix(500);
	}

}

// 80x75 13.495166666666668
// 199x126 18.102616255882587
// 339x286 18.16454194772779
// 366x78 19.510403530895335
// 440x406 20.363087774294673


