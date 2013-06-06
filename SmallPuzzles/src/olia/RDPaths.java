package olia;

public class RDPaths {
	int width;
	int height;
	int size;
	
	int[] form;
	
	int[] pathCount;
	
	public RDPaths() {
	}
	
	public void setSize(int width, int height) {
		this.width = width + 1;
		this.height = height + 1;
		size = this.width * this.height;
		form = new int[size];
		pathCount = new int[size];
	}
	
	int index(int x, int y) {
		return x < 0 || x >= width || y < 0 || y >= height ? -1 : y * width + x;
	}
	
	public int compute() {
		pathCount[0] = 1;
		for (int s = 1; s <= width + height - 2; s++) {
			for (int ix = 0; ix <= s; ix++) {
				if(ix >= width) continue;
				int iy = s - ix;
				if(iy < 0 || iy >= height) continue;
				int p = index(ix, iy);
				int q = index(ix, iy - 1);
				pathCount[p] = q < 0 || form[q] == 1 ? 0 : pathCount[q];
				q = index(ix - 1, iy);
				pathCount[p] += q < 0 || form[q] == 1 ? 0 : pathCount[q];
			}
		}
		return pathCount[size - 1];
	}
	
	void printTable() {
		for (int i = 0; i < size; i++) {
			String s = "" + pathCount[i];
			while(s.length() < 4) s = " " + s;
			System.out.print(" " + s);
			int x = i % width;
			if(x == width - 1) System.out.println("");
		}
		System.out.println("");
	}
	
	void findOnePoint(int result) {
		for (int p = 1; p < size - 1; p++) {
			form[p] = 1;
			if(compute() == result) {
				int x = p % width;
				int y = p / width;
				System.out.println("x=" + x + " y=" + y);
			}
			form[p] = 0;
		}
	}

	void findTwoPoints(int result) {
		int min = 10000;
		for (int p1 = 1; p1 < size - 1; p1++) {
			form[p1] = 1;
			for (int p2 = p1 + 1; p2 < size - 1; p2++) {
				form[p2] = 1;
				int k = compute();
				int delta = Math.abs(2005 - k);
				if(delta < min) {
					min = delta;
					System.out.println(k);
				}
				if(k == result) {
					int x1 = p1 % width;
					int y1 = p1 / width;
					int x2 = p2 % width;
					int y2 = p2 / width;
					System.out.println("x1=" + x1 + " y1=" + y1 + "x2=" + x2 + " y2=" + y2);
				}
				form[p2] = 0;
			}
			form[p1] = 0;
		}
	}

	void findNPoints(int result, int n) {
		int min = 10000;
		int[] points = new int[n];
		int t = 0;
		points[0] = 0;
		while(true) {
			while(t == n || points[t] >= size - 2) {
				if(t == 0) return;
				t--;
			}
			points[t]++;
			++t;
			if(t == n) {
				for (int i = 0; i < size; i++) form[i] = 0;
				for (int i = 0; i < n; i++) form[points[i]] = 1;
				int k = compute();
				int delta = Math.abs(2005 - k);
				if(delta < min) {
					min = delta;
					System.out.println(k);
				}
				if(delta == 0) {
					printArray(points);
					printTable();
				} 
			} else {
				points[t] = points[t - 1];
			}
		}
	}
	
	void printArray(int[] points) {
		for (int i = 0; i < points.length; i++) {
			int x = points[i] % width;
			int y = points[i] / width;
			System.out.print(" " + x + ":" + y);
		}
		System.out.println("");
	}

	public static void main(String[] args) {
//		for (int i = 6; i < 25; i++) {
//			System.out.println("i=" + i);
			RDPaths p = new RDPaths();
			p.setSize(7,7);
			p.findNPoints(2005, 3);
//		}
	}

}
