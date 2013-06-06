package ic2005_1;

public class Labirinth {
	int width;
	int height;
	int size;
	int[] x,y;
	int[][] jp;
	int[] vRestrictions;
	
	int[] state;
	int[] vState;
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int pathLength;
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		size = width * height;
		x = new int[size];
		y = new int[size];
		for (int i = 0; i < size; i++) {
			x[i] = (i % width);
			y[i] = (i / width);
		}
		jp = new int[4][size];
		for (int i = 0; i < size; i++) {
			jp[0][i] = (x[i] == width - 1) ? -1 : i + 1; 
			jp[1][i] = (y[i] == height - 1) ? -1 : i + width; 
			jp[2][i] = (x[i] == 0) ? -1 : i - 1; 
			jp[3][i] = (x[i] == 0) ? -1 : i - width; 
		}
	}
	
	public void setWalls(int[] walls) {
		if(walls.length != size) throw new RuntimeException("Wrong walls");
		for (int i = 0; i < size; i++) {
			int i0 = jp[0][i];
			if(i0 >= 0 && (walls[i] & 1) != 0) {
				jp[0][i] = -1;
				jp[2][i0] = -1;
			}
			int i1 = jp[1][i];
			if(i1 >= 0 && (walls[i] & 2) != 0) {
				jp[1][i] = -1;
				jp[3][i1] = -1;
			}
		}
	}
	
	public void setVRestrictions(int[] v) {
		vRestrictions = v;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		wayCount = new int[size];
		way = new int[size];
		ways = new int[size][height];
		state = new int[size];
		for (int i = 0; i < size; i++) state[i] = -1;
		vState = new int[width];
		t = 0;
		pathLength =66;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == 0) {
			for (int iy = 0; iy < height; iy++) {
				int q = width * iy;
				ways[t][wayCount[t]] = q;
				wayCount[t]++;
			}
		} else {
			int p = ways[t - 1][way[t - 1]];
			for (int d = 0; d < 4; d++) {
				int q = jp[d][p];
				if(q < 0 || state[q] >= 0 || vState[x[q]] >= vRestrictions[x[q]]) continue;
				ways[t][wayCount[t]] = q;
				wayCount[t]++;
			}
		}
	}
	
	void move() {
		int p = ways[t][way[t]];
		state[p] = t;
		vState[x[p]]++;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = ways[t][way[t]];
		state[p] = -1;
		vState[x[p]]--;
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] >= wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			++way[t];
			move();
			int p = ways[t - 1][way[t - 1]];
			if(x[p] == width - 1) {
				if(pathLength < t) {
					pathLength = t;
					System.out.println("pathLength=" + pathLength);
					printSolution();
				}
			}
		}
	}
	void printSolution() {
		System.out.println("");
		for (int i = 0; i < size; i++) {
			char c = '+';
			if(state[i] >= 0) {
				int k = state[i] % 26;
				c = (char)(97 + k);
			}
			System.out.print(" " + c);
			if(x[i] == width - 1) System.out.println("");
		}
		System.out.println("");
		System.out.print(t + ": ");
		for (int ix = 0; ix < width; ix++) {
			if(ix > 0) System.out.print(", ");
			for (int iy = 0; iy < height; iy++) {
				int i = iy * width + ix;
				if(state[i] >= 0) {
					char c = (char)(97 + iy);
					System.out.print(c);					
				}
			}
		}
	}
	
	
	static int[] WALLS = new int[]{
		0,1,0,2,2,2,2,0,2,1,0,2,0,2,2,0,0,  //a
		0,2,2,1,0,0,0,1,0,1,2,2,2,2,0,2,0,  //b
		0,2,0,1,1,1,1,0,3,2,0,0,2,0,2,0,0,  //c
		0,0,3,2,0,0,0,2,2,1,1,2,1,2,0,2,0,  //d
		0,2,0,2,3,1,2,0,1,2,2,2,3,0,2,0,0,  //e
		0,0,0,0,1,2,0,3,2,1,0,2,0,1,0,0,0,  //f
		0,3,1,1,0,0,0,2,0,3,2,0,3,2,2,0,0,  //g
		0,0,2,0,1,1,2,0,1,0,0,3,0,0,1,2,0,  //h
		0,2,0,3,0,2,2,1,0,3,0,2,1,1,0,0,0,  //i
		0,1,2,0,2,1,0,2,0,2,0,0,2,1,1,0,0,  //j
		0,2,1,0,1,1,2,2,2,2,1,0,2,0,2,0,0,  //k
		0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0   //l
	};
	
	static int[] V_RESTRICTIONS = new int[]{
		1,2,3,4,5,6,7,8,9,8,7,6,5,4,3,2,1
	};
	
	public static void main(String[] args) {
		Labirinth p = new Labirinth();
		p.setSize(17, 12);
		p.setWalls(WALLS);
		p.setVRestrictions(V_RESTRICTIONS);
		p.solve();
	}

}
/*
 + + + + + + + + + + + + + + + + +
 + + + + + r s t w x + + + + + + +
 + + + + + q + u v y z a + + + + +
 + + + + o p k j i h + b c + + + +
 + + l m n + l m n g f e d + + + +
 + + k j i + + + o p + + + + + + +
 + + + + h u t s r q + + + + + + +
 a b + f g v + + + h i + s t + + +
 + c d e + w x y f g j + r u z a +
 + + + + + + a z e + k p q v y b c
 + + + + + + b c d + l o + w x + +
 + + + + + + + + + + m n + + + + +

*/