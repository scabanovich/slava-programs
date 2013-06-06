package com.slava.hashi;

import com.slava.common.RectangularField;

public class HashiSolver {
	RectangularField field;

	int[] problem;
	int solutionLimit;
	
	
	int[] bridgesToDo;
	int[] state;
	int unresolvedCount;

	int t = 0;
	int[] wayCount;
	int[] way;
	int[][] waysP;
	int[][][] waysD;

	int[][] tempWaysD;


	int solutionCount;
	int[][] solution;
	
	int treeCount;

	public HashiSolver() {}

	public void setField(RectangularField f) {
		field = f;
	}

	public void setProblem(int[] problem) {
		this.problem = problem;
	}
	
	public void setSoltionLimit(int s) {
		solutionLimit = s;
	}
	
	public void solve() {
		init();
		anal();		
	}

	void init() {
		state = new int[field.getSize()];
		bridgesToDo = (int[])problem.clone();
		unresolvedCount = 0;
		for (int i = 0; i  < problem.length; i++) {
			unresolvedCount += bridgesToDo[i];
		}
		
		wayCount = new int[field.getSize() + 1];
		way = new int[field.getSize() + 1];
		waysP = new int[field.getSize() + 1][100];
		waysD = new int[field.getSize() + 1][100][4];
		
		tempWaysD = new int[100][4];
		
		t = 0;
		solutionCount = 0;
		treeCount = 0;
	}

	void srch() {
		wayCount[t] = 0;
		if(unresolvedCount == 0) return;
		
		int mwc = 1000;
		for (int p = 0; p < state.length; p++) {
			if(bridgesToDo[p] == 0) continue;
			int m = getMaximum(p);
			if(m < bridgesToDo[p]) return;
			int wc = getWayCount(p);
			if(wc == 0) return;
			if(wc >= mwc) continue;
			mwc = wc;
			for (int i = 0; i < wc; i++) {
				waysP[t][i] = p;
				for (int d = 0; d < 4; d++) waysD[t][i][d] = tempWaysD[i][d]; 
			}
		}
		if(mwc < 1000) {
			wayCount[t] = mwc;
		}		
	}
	
	int getMaximum(int p) {
		int c = 0;
		for (int d = 0; d < 4; d++) {
			int q = field.jump(p, d);
			while(q >= 0 && this.problem[q] == 0) {
				if(state[q] > 0) {
					q = -1;
					break;
				}
				q = field.jump(q, d);
			}
			if(q >= 0) c += bridgesToDo[q];
		}		
		return c;		
	}
	
	static int BRIDGE_LIMIT = 2;
	int getWayCount(int p) {
		int w = 0;
		int[] dm = getAvailable(p);
		for (int k0 = 0; k0 <= dm[0] && k0 <= BRIDGE_LIMIT; k0++) {
			for (int k1 = 0; k1 <= dm[1] && k1 <= BRIDGE_LIMIT && k0 + k1 <= bridgesToDo[p]; k1++) {
				for (int k2 = 0; k2 <= dm[2] && k2 <= BRIDGE_LIMIT && k0 + k1 + k2 <= bridgesToDo[p]; k2++) {
					int k3 = bridgesToDo[p] - k0 - k1 - k2;
					if(k3 < 0 || k3 > dm[3] || k3 > BRIDGE_LIMIT) continue;
					tempWaysD[w][0] = k0;
					tempWaysD[w][1] = k1;
					tempWaysD[w][2] = k2;
					tempWaysD[w][3] = k3;
					w++;
				}
			}
		}

		return w;
	}
	
	int[] getAvailable(int p) {
		int[] c = new int[4];
		for (int d = 0; d < 4; d++) {
			int q = field.jump(p, d);
			while(q >= 0 && this.problem[q] == 0) {
				if(state[q] > 0) {
					q = -1;
					break;
				}
				q = field.jump(q, d);
			}
			if(q >= 0) c[d] = bridgesToDo[q];
		}		
		return c;		
	}	

	void move() {
		int p = waysP[t][way[t]];
		int[] dn = waysD[t][way[t]];
		for (int d = 0; d < 4; d++) {
			if(dn[d] == 0) continue;
			unresolvedCount -= 2 * dn[d];
			bridgesToDo[p] -= dn[d];
			int q = field.jump(p, d);
			while(q >= 0 && problem[q] == 0) {
				state[q] = 1;
				q = field.jump(q, d);
			}
			bridgesToDo[q] -= dn[d];
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = waysP[t][way[t]];
		int[] dn = waysD[t][way[t]];
		for (int d = 0; d < 4; d++) {
			if(dn[d] == 0) continue;
			unresolvedCount += 2 * dn[d];
			bridgesToDo[p] += dn[d];
			int q = field.jump(p, d);
			while(q >= 0 && problem[q] == 0) {
				state[q] = 0;
				q = field.jump(q, d);
			}
			bridgesToDo[q] += dn[d];
		}
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
			if(unresolvedCount == 0 && isConnectedSolution()) {
				solutionCount++;
				if(solutionCount == 1) {
					solution = new int[state.length][4];
					for (int i = 0; i < t; i++) {
						int p = waysP[i][way[i]];
						int[] dn = waysD[i][way[i]];
						for (int d = 0; d < 4; d++) {
							solution[p][d] = dn[d];
						}
					}
				}
				if(solutionLimit > 0 && solutionCount >= solutionLimit) return;
			}
		}
	}
	
	boolean isConnectedSolution() {
		int[][] matrix = new int[problem.length][];
		for (int i = 0; i < t; i++) {
			int p = waysP[i][way[i]];
			int[] dn = waysD[i][way[i]];
			for (int d = 0; d < 4; d++) {
				if(dn[d] == 0) continue;
				int q = field.jump(p, d);
				while(problem[q] == 0) q = field.jump(q, d);
				addConnectionToMatrix(matrix, p, q);
				addConnectionToMatrix(matrix, q, p);
			}
		}
		int size = 0;
		for (int p = 0; p < problem.length; p++) if(problem[p] > 0) ++size;
		int p = 0;
		while(problem[p] == 0) ++p;
		int[] visited = new int[problem.length];
		int[] stack = new int[size];
		int c = 0;
		int v = 1;
		stack[0] = p;
		visited[p] = 1;
		while(c < v) {
			p = stack[c];
			int[] a = matrix[p];
			for (int i = 0; i < a.length; i++) {
				int q = matrix[p][i];
				if(visited[q] > 0) continue;
				visited[q] = 1;
				stack[v] = q;
				v++;
			}
			c++;
		}
		return v == size;
	}
	void addConnectionToMatrix(int[][] matrix, int p, int q) {
		int[] a = matrix[p];
		if(a == null) a = new int[0];
		int[] b = new int[a.length + 1];
		System.arraycopy(a, 0, b, 0, a.length);
		b[a.length] = q;
		matrix[p] = b;
		
	}
	
	static String[][] MOVE_DESIGNATION = {
		{"", "e", "E"}, {"", "s", "S"}, {"", "w", "W"}, {"", "n", "N"}
	};

	void printSolution() {
		System.out.println("Solution:");
		if(solution == null) {
			System.out.println("Not found");
			return;
		}
		for (int i = 0; i < state.length; i++) {
			String s = " ";
			for (int d = 0; d < 4; d++) s += MOVE_DESIGNATION[d][solution[i][d]];
			if(s.equals(" ")) {
				if(problem[i] > 0){
					s = "o";
				} else if(HashiRandomGenerator.isNode(field, i)) {
					s = "+";
				} else {
					s = ".";
				}
			}
			while(s.length() < 5) s = " " + s;
			System.out.print(s);
			if(field.isRightBorder(i)) System.out.println();
		}
	}

}
