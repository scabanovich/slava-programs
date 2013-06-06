package balance;

import java.util.ArrayList;

public class BalanceSolver {
	Weight root;
	
	int numberCount;
	int[] numberUsage;
	int emptyLeaves;
	
	Weight[] leaves;
	
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	int[] temp;
	
	int solutionCount;	
	
	public BalanceSolver() {}
	
	public void setRoot(Weight root) {
		this.root = root;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		numberCount = root.getLeafCount();
		numberUsage = new int[numberCount + 1];

		ArrayList list = new ArrayList();
		root.collectLeaves(list);
		leaves = (Weight[])list.toArray(new Weight[0]);
		emptyLeaves = 0;
		for (int i = 0; i < leaves.length; i++) if(leaves[i].mass < 0) emptyLeaves++;
		
		place = new int[numberCount + 1];
		wayCount = new int[numberCount + 1];
		way = new int[numberCount + 1];
		ways = new int[numberCount + 1][numberCount];
		temp = new int[numberCount];
		
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(emptyLeaves == 0) return;
		int wcb = 100;
		for (int p = 0; p < leaves.length; p++) {
			if(leaves[p].mass > 0) continue;
			int wc = getWeightCount(p);
			if(wc == 0) return;
			if(wc < wcb) {
				wcb = wc;
				place[t] = p;
				for (int i = 0; i < wc; i++) ways[t][i] = temp[i];
			}
		}
		if(wcb < 100) {
			wayCount[t] = wcb;
		}
	}
	
	int getWeightCount(int p) {
		int c = 0;
		for (int m = 1; m <= numberCount; m++) {
			if(numberUsage[m] > 0) continue;
			leaves[p].setMass(m);
			boolean b = leaves[p].checkBalance();
			leaves[p].setMass(-1);
			if(b) {
				temp[c] = m;
				c++;
			}
		}		
		return c;
	}
	
	void move() {
		int p = place[t];
		int m = ways[t][way[t]];
		leaves[p].setMass(m);
		numberUsage[m]++;
		emptyLeaves--;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = place[t];
		int m = ways[t][way[t]];
		leaves[p].setMass(-1);
		numberUsage[m]--;
		emptyLeaves++;
	}
	
	void anal() {
		srch();
		way[t] = -1;
		int tm = 0;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t > tm) {
				tm = t;
//				System.out.println(t + ":" + emptyLeaves);
			}
			if(emptyLeaves < 1) {
				solutionCount++;
				if(solutionCount == 1) {
					System.out.println("Solution:");
					System.out.println(root.toString());
				}
			}
		}
		
	}
	
}
