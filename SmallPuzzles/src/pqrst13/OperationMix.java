package pqrst13;

public class OperationMix {
	
	int[] used = new int[16];
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int[] numbers = new int[16];
	
	double[] values = new double[8];
	
	double best = 200;
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		wayCount = new int[17];
		way = new int[17];
		ways = new int[17][17];
		used = new int[16];
		t = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == 16) return;
		if(getScore() < best - 0.000000001) return;
		for (int i = 0; i < 16; i++) {
			if(used[i] == 1) continue;
			ways[t][wayCount[t]] = i;
			wayCount[t]++;
		}
	}
	
	void move() {
		int i = ways[t][way[t]];
		numbers[t] = i + 1;
		used[i] = 1;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int i = ways[t][way[t]];
		used[i] = 0;		
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			++way[t];
			move();
			if(t == 16) {
				double s = getScore();
				if(s > best - 0.000000001) {
					best = s;
					System.out.println("Score = " + best);
					for (int i = 0; i < 16; i++) System.out.print(" " + numbers[i]);
					System.out.println("");
					for (int i = 0; i < 8; i++) {
						System.out.println(values[i]);
					}
				}
			}
		}
	}
	
	double getScore() {
		if(t == 4) {
			values[0] = 1d * numbers[0] * numbers[1] / numbers[2] - numbers[3];
			return getScore(0);
		} else if(t == 7) {
			values[1] = (1d * numbers[0] - numbers[4]) * numbers[5] + numbers[6];
			return getScore(1);
		} else if(t == 10) {
			values[2] = (1d * numbers[4] - numbers[7] + numbers[8]) * numbers[9];
			return getScore(2);
		} else if(t == 12) {
			values[3] = (1d * numbers[1] + numbers[7]) / numbers[10] * numbers[11];
			return getScore(3);
		} else if(t == 14) {
			values[4] = (1d * numbers[5] + numbers[10] - numbers[12]) / numbers[13];
			return getScore(4);
		} else if(t == 15) {
			values[5] = (1d * numbers[2] * numbers[8] - numbers[12]) / numbers[14];
			return getScore(5);
		} else if(t == 16) {
			values[6] = 1d * numbers[6] / numbers[11] * numbers[14] + numbers[15];
			values[7] = 1d * numbers[3] / numbers[9] + numbers[13] - numbers[15]; 
			return getScore(7);
		}
		return best;
	}
	
	double getScore(int n) {
		double min = 10000000;
		double max = -1;
		for (int i = 0; i <= n; i++) {
			if(values[i] <= 0) return -1d;
			if(values[i] > max) max = values[i];
			if(values[i] < min) min = values[i];
		}
		double score = 50 + 200 * min / max;
		return score;
	}
	

	public static void main(String[] args) {
		OperationMix p = new OperationMix();
		p.solve();
	}

}

/*
Score = 222.72727272727272
 11 15 7 14 10 8 3 12 13 1 16 6 4 2 9 5
 
223:
 11,15, 7,14,
 10,12,13, 1,
  8,16, 4, 2,
  3, 6, 9, 5
*/