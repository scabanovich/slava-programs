package forsmarts;

public class BalanceSolver {
	int weightCount = 6;
	int maxDifference = 4;
	int[] sums;
	int[][] usage;
	int tMax = 17;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int treeCount;
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		sums = new int[4];
		usage = new int[4][weightCount + 1];
		wayCount = new int[tMax + 1];
		way = new int[tMax + 1];
		ways = new int[tMax + 1][4];
		t = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t >= tMax) return;
		int v = (t % weightCount) + 1;
		for (int k = 0; k < 4; k++) {
			sums[k] += v;
			if(!isToBreak()) {
				ways[t][wayCount[t]] = k;
				wayCount[t]++;
			}			
			sums[k] -= v;
		}
	}
	
	boolean isToBreak() {
		if(Math.abs(sums[0] + sums[1] - sums[2] - sums[3]) > maxDifference) return true;
		if(Math.abs(sums[0] + sums[2] - sums[1] - sums[3]) > maxDifference) return true;
		return false;
	}
	
	void move() {
		int v = (t % weightCount) + 1;
		int k = ways[t][way[t]];
		sums[k] += v;
		usage[k][v]++;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int v = (t % weightCount) + 1;
		int k = ways[t][way[t]];
		sums[k] -= v;
		usage[k][v]--;
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
			if(wayCount[t] == 0) treeCount++;
			if(isFinished()) {
				printSolution();
			}
		}
	}
	
	boolean isFinished() {
		return sums[0] == sums[2] && sums[1] == sums[3]
		       && usage[0][4] > 0 
		       && usage[0][5] > 0 
		       && usage[1][2] > 0
		       ;
	}
	
	void printSolution() {
		System.out.print("t=" + t + " ");
		for (int i = 0; i < t; i++) {
			System.out.print(ways[i][way[i]]);
		}
		System.out.println(" code=" + getSolutionCode());
		System.out.print(" sums=");
		for (int i = 0; i < sums.length; i++) {
			System.out.print(" " + sums[i]);
		}
		System.out.println("");
	}
	
	String getSolutionCode() {
		StringBuffer sb = new StringBuffer();
		int q = 0;
		for (int i = 0; i < t; i++) {
			if(ways[i][way[i]] != 2) continue;
			int v = (i % weightCount) + 1;
			if(q > 0) sb.append(',');
			sb.append(v);
			q++;
		}
		sb.append("; ");
		q = 0;
		for (int i = 0; i < t; i++) {
			if(ways[i][way[i]] != 3) continue;
			int v = (i % weightCount) + 1;
			if(q > 0) sb.append(',');
			sb.append(v);
			q++;
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		BalanceSolver solver = new BalanceSolver();
		solver.solve();
		System.out.println("TreeCount=" + solver.treeCount);
	}

}
// 1,3,5,1,3; 4,6,3