package match2005;

public class RubicCaos {
	int[][][] rotations = new int[][][] {
		{{0,2,7,5},{1,4,6,3},{23,34,42,15},{20,36,44,12},{18,39,47,10}},
		{{0,5,7,2},{1,3,6,4},{23,15,42,34},{20,12,44,36},{18,10,47,39}},
		{{0,7},{2,5},{1,6},{4,3},{23,42},{34,15},{20,44},{36,12},{18,47},{39,10}},
		
		{{8,10,15,13},{9,12,14,11},{0,47,29,21},{3,46,27,22},{5,45,24,23}},
		{{8,13,15,10},{9,11,14,12},{0,21,29,47},{3,22,27,46},{5,23,24,45}},
		{{8,15},{10,13},{9,14},{11,12},{0,29},{47,21},{3,27},{46,22},{5,24},{45,23}},
		
		{{16,18,23,21},{17,20,22,19},{0,8,26,34},{1,9,25,33},{2,10,24,32}},
		{{16,21,23,18},{17,19,22,20},{0,34,26,8},{1,33,25,9},{2,32,24,10}},
		{{16,23},{18,21},{17,22},{20,19},{0,26},{8,34},{1,25},{9,33},{2,24},{10,32}},
		
		{{24,26,31,29},{25,28,30,27},{21,32,40,13},{19,35,43,11},{16,37,45,8}},
		{{24,29,31,26},{25,27,30,28},{21,13,40,32},{19,11,43,35},{16,8,45,37}},
		{{24,31},{26,29},{25,30},{28,27},{21,40},{13,32},{19,43},{11,35},{16,45},{8,37}},
		
		{{32,34,39,37},{33,36,38,35},{2,42,31,16},{4,41,28,17},{7,40,26,18}},
		{{32,37,39,34},{33,35,38,36},{2,16,31,42},{4,17,28,41},{7,18,26,40}},
		{{32,39},{37,34},{33,38},{35,36},{2,31},{16,42},{4,28},{17,41},{7,26},{18,40}},
		
		{{40,42,47,45},{41,44,46,43},{37,7,15,29},{38,6,14,30},{39,5,13,31}},
		{{40,45,47,42},{41,43,46,44},{37,29,15,7},{38,30,14,6},{39,31,13,5}},
		{{40,47},{45,42},{41,46},{43,44},{37,15},{29,7},{38,14},{30,6},{39,13},{31,5}},

	};
	
	int[][] pairsF = {
		{0,1},{0,3},{2,1},{2,4},{7,4},{7,6},{5,3},{5,6},{1,3},{1,4},{6,3},{6,4}
	};
	int[][] pairsG = {
		{1,18},{1,23},{3,10},{3,15},{6,42},{6,47},{4,34},{4,39},
		{9,21},{9,23},{11,24},{11,29},{14,45},{14,47},{12,0},{12,5},
		{17,32},{17,34},{20,0},{20,2},{22,8},{22,10},{19,24},{19,26},
		{25,16},{25,21},{27,8},{27,13},{28,32},{28,37},{30,40},{30,45},
		{33,16},{33,18},{35,26},{35,31},{36,2},{36,7},{38,40},{38,42},
		{41,37},{41,39},{43,29},{43,31},{44,5},{44,7},{46,13},{46,15}
	};
	
	int[] direction = {
		0,0,0, 1,1,1, 2,2,2, 3,3,3, 4,4,4, 5,5,5,
	};
	int[] axe = {
		0,0,0, 1,1,1, 2,2,2, 0,0,0, 1,1,1, 2,2,2,	
	};
	
	int[] rev = {
		1,0,2, 4,3,5, 7,6,8, 10,9,11, 13,12,14, 16,15,17	
	};
	
	int[] faces;
	int[] state;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;

	int tMax = 15;
	int tRandom = tMax - 12;
	int bestEstimate = 60;
	int bestQ = 1000;
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		faces = new int[48];
		for (int i = 0; i < faces.length; i++) faces[i] = i / 8;
		state = new int[48];
		for (int i = 0; i < faces.length; i++) state[i] = faces[i];
		wayCount = new int[40];
		way = new int[40];
		ways = new int[40][30];
		t = 0;		
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == tMax) return;
		if(t == 0) {
			wayCount[t] = 2;
			ways[t][0] = 0;
			ways[t][1] = 2;
			return;
		} else if(t == 1) {
			wayCount[t] = 3;
			for (int i = 0; i < 3; i++)	ways[t][i] = 3 + i;
			return;
		}

		int q = estimate2();
		if(q > 10 && q < 20 && t + (q / 4) > tMax) return;
		if(t + (q / 5) > tMax) return;

		int w1 = ways[t - 1][way[t - 1]];
		for (int w = 0; w < rotations.length; w++) {
			if(direction[w] == direction[w1]) continue;
			if(t > 1 && axe[w] == axe[w1] 
			   && direction[w] == direction[ways[t - 2][way[t - 2]]]) continue;
			ways[t][wayCount[t]] = w;
			wayCount[t]++;
		}
		randomize();
	}
	
	void randomize() {
		if(wayCount[t] < 3) return;
		if(t < tRandom) {
			int a = ways[t][(int)(wayCount[t] * Math.random())];
			int b = a;
			while(b == a) b = ways[t][(int)(wayCount[t] * Math.random())];
			ways[t][0] = a;
			ways[t][1] = b;
			wayCount[t] = 2;
		}
	}
	
	void move() {
		int w = ways[t][way[t]];
		rotate(rotations[w]);
		++t;
		srch();
		way[t] = -1;
	}
	
	void rotate(int[][] rotation) {
		for (int i = 0; i < rotation.length; i++) rotate(rotation[i]);
	}
	
	void rotate(int[] rotation) {
		int q = state[rotation[0]];
		for (int i = 1; i < rotation.length; i++) {
			state[rotation[i - 1]] = state[rotation[i]];
		}
		state[rotation[rotation.length - 1]] = q;
		
	}
	
	void back() {
		--t;
		int w = ways[t][way[t]];
		rotate(rotations[rev[w]]);
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while (way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t == tMax) {
				int e = estimate();
				if(e < bestEstimate) {
					bestEstimate = e;
					System.out.println(e);
				}
				if(e == 0) {
					int q = estimate2();
					if(q < bestQ) {
						bestQ = q;
						System.out.println("Q=" + q);
						if(bestQ == 0) {
							printSolution();
						}
					}
				}
			}
		}
	}
	
	int[][] distribution = new int[6][6];
	
	int estimate() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) distribution[i][j] = 0;
		}
		for (int i = 0; i < 6; i++) distribution[i][i] = 2;
		int r = 0;
		for (int i = 0; i < 48; i++) {
			int f = faces[i];
			int s = state[i];
			distribution[f][s]++;
			if(distribution[f][s] > 2) {
				++r;
				if(r > bestEstimate) return r;
			}
		}
		return r;
	}
	
	int estimate2() {
		int q = 0;
		for (int i = 0; i < pairsG.length; i++) {
			if(state[pairsG[i][0]] == state[pairsG[i][1]]) ++q;
		}
		for (int i = 0; i < pairsF.length; i++) {
			int p1 = pairsF[i][0];
			int p2 = pairsF[i][1];
			for (int k = 0; k < 6; k++) {
				if(state[p1] == state[p2]) ++q;
				p1 += 8;
				p2 += 8;
			}
		}		
		return q;
	}
	
	void printSolution() {
		for (int i = 0; i < t; i++) {
			int w = ways[i][way[i]];
			System.out.println(" " + w);
		}
	}
	
	public static void main(String[] args) {
		RubicCaos p = new RubicCaos();
		p.solve();
//		for (int i = 0; i < 48; i++) System.out.print(p.state[i] + " ");
//		System.out.println();
	}

}
