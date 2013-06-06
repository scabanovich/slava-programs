package champ2013.regions;

import com.slava.common.RectangularField;

public class RegionsBuilder {
	RectangularField f;
	int[] state;

	public RegionsBuilder() {
		f = new RectangularField();
		f.setSize(5, 5);
		state = new int[f.getSize()];
	}
	
	int SIZE = 16;

	public void run() {
		int[][] statistics = new int[17][17];
		long max = 1;
		for (int i = 0; i < SIZE; i++) max *= 2;
		long z = 0;
		while(z < max) {
			if(getMass() == 7) {
				int[] rs = computeRegions();
				if(rs != null) {
					statistics[rs[0]][rs[1]]++;
					if(rs[0] == 1 && rs[1] > 5) {
						System.out.println(z);
						print();
					}
				}
			}
//			if(z % 10000000 == 0) System.out.println("->" + z);
			next();
			z++;
		}
		for (int i = 1; i < 4; i++) {
			for (int j = 1; j < 16; j++) {
				if(statistics[i][j] > 0) {
					System.out.println(i + " " + j + " " + statistics[i][j]);
				}
			}
		}
	}

	void next() {
		for (int i = 0; i < f.getSize(); i++) {
			if(f.getX(i) == 4) continue;
			if(state[i] == 0) {
				state[i] = 1;
				break;
			} else {
				state[i] = 0;
			}
		}
	}

	int getMass() {
		int c = 0;
		for (int i = 0; i < state.length; i++) {
			if(state[i] == 1) c++;
		}
		return c;
	}

	void print() {
		for (int i = 0; i < state.length; i++) {
			System.out.print(" " + state[i]);
			if(f.isRightBorder(i)) System.out.println("");
		}
		System.out.println("");
	}

	int[] stack = new int[40];

	int[] computeRegions() {
		int[] regions = new int[2];
		int[] visited = new int[state.length];
		for (int i = 0; i < f.getSize(); i++) {
			if(visited[i] > 0) continue;
			int v = state[i];
			visited[i] = 1;
			regions[v]++;
//			if(regions[0] > 2) {
//				return null;
//			}
			int c = 0;
			int m = 1;
			stack[0] = i;
			while(c < m) {
				int p = stack[c];
				for (int d = 0; d < 4; d++) {
					int q = f.jump(p, d);
					if(q >= 0 && state[q] == v && visited[q] == 0) {
						stack[m] = q;
						visited[q] = 1;
						m++;
					}
				}
				c++;
			}			
		}
		return regions;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new RegionsBuilder().run();
	}

}
