package ik4;

public class AutumnProduct {
	int[][] firstNumber = new int[][]{
		{1,0,1,1,0,0,0,1,1,1},
		{1,0,1,0,0,0,1,0,1,0},
		{1,0,0,0,0,0,1,0,1,0}
	};
	int[][] secondNumber = new int[][]{
		{1,0,1,0,0,0,1,0,1,0},
		{0,0,0,1,1,1,1,0,1,1},
		{1,0,1,1,0,0,0,1,1,1}
	};
	
	int[][][] partialProducts = new int[][][]{
		{{1,0,0,0,0,0,1,0,1,0},
		 {1,1,0,1,1,0,0,1,1,1},
		 {1,0,0,0,1,0,0,0,1,1},
		 {1,0,0,1,0,1,1,0,1,1}},
		{{0,0,1,1,1,0,0,0,1,1},
		 {1,0,1,1,0,0,0,0,1,1},
		 {1,0,0,0,0,1,1,0,1,1},
		 {1,0,1,0,0,0,0,0,1,0}},
		{{1,0,0,0,0,1,1,0,1,1},
		 {1,0,0,0,0,0,1,0,1,0},
		 {1,0,1,1,0,0,0,0,1,1},
		 {1,1,0,1,1,0,0,1,1,1}}
	};
	int[][] product = new int[][]{
		{1,0,1,1,0,1,1,0,1,1},
		{1,0,0,0,1,0,0,0,1,1},
		{0,0,1,1,0,1,1,0,1,1},
		{1,1,0,1,1,0,0,1,1,1},
		{0,0,1,0,0,0,1,0,1,0},
		{1,1,0,1,1,0,0,1,1,1}
	};
	
	boolean isMatching(int n, int[][] filter) {
		for (int i = 0; i < filter.length; i++) {
			int r = n % 10;
			if(filter[i][r] == 0) return false;
			n = n / 10;
		}
		return true;
	}
	
	public void solve() {
		for (int n1 = 101; n1 < 1000; n1++) {
			if(!isMatching(n1, firstNumber)) continue;
			for (int n2 = 101; n2 < 1000; n2++) {
				if(!isMatching(n2, secondNumber)) continue;
				boolean b = true;
				int q = n2;
				for (int i = 0; b && i < 3; i++) {
					int r = q % 10;
					int pi = r * n1;
					if(!isMatching(pi, partialProducts[i])) b = false;
					q = q / 10;
				}
				if(!b) continue;
				int p = n1 * n2;
				if(!isMatching(p, product)) continue;
				System.out.println("-->");
				System.out.println("n1=" + n1);
				System.out.println("n2=" + n2);
			}
		}
	}

	public static void main(String[] args) {
		new AutumnProduct().solve();
	}
}
