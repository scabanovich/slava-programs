package forsmarts;

public class RoundAndRoundPacker {
	RoundAndRoundField field;
	RoundAndRoundLoop[] loops;
	RoundAndRoundLoopFilter filter;
	
	int[] stateT;
	int[] stateL;
	int[][] currentLines;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;
	
	public RoundAndRoundPacker() {}
	
	public void setField(RoundAndRoundField f) {
		field = f;
	}
	
	public void setLoops(RoundAndRoundLoop[] loops) {
		this.loops = loops;
	}
	
	public void setFilter(RoundAndRoundLoopFilter f) {
		filter = f;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		stateT = new int[field.getTriangleCount()];
		stateL = new int[field.getTriangleCount()];
		for (int tr = 0; tr < stateT.length; tr++) stateT[tr] = -1;
		for (int tr = 0; tr < stateL.length; tr++) stateL[tr] = -1;
		currentLines = new int[3][field.getWidth()];
		wayCount = new int[field.getTriangleCount()];
		way = new int[field.getTriangleCount()];
		ways = new int[field.getTriangleCount()][5000];
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(!checkLinesC()) return;
		if(!checkState()) return;
			//if(t > 4) return;
		int tr = getNarrowestPlace();
			//getFreeTriangle();
		if(tr < 0) return;
		for (int i = 0; i < loops.length; i++) {
			if(accept(i, tr)) {
				ways[t][wayCount[t]] = i;
				wayCount[t]++;
			}
		}
		if(t < 2)
		System.out.println(t + " " + wayCount[t]);
	}
	
	int getNarrowestPlace() {
		int[] ds = new int[stateT.length];
		for (int i = 0; i < loops.length; i++) {
			if(!accept(i)) continue;
			RoundAndRoundLoop loop = loops[i];
			for (int j = 0; j < loop.triangleState.length; j++) {
				if(loop.triangleState[j] > 0) ds[j]++;
			}
		}
		int trn = -1;
		int q = loops.length;
		for (int tr = 0; tr < stateT.length; tr++) {
			if(stateT[tr] >= 0) continue;
			if(ds[tr] >= q) continue;
			q = ds[tr];
			trn = tr;
		}
		
		return trn;
	}
	
	boolean checkLinesC() {
		for (int d = 0; d < 3; d++) {
			for (int i = 0; i < currentLines[d].length; i++) {
				int ls = filter.lineRestrictions[d][i];
				if(ls < 0) continue;
				if(currentLines[d][i] > 2 * ls) return false;
			}
		}
		return true;
	}
	
	boolean checkLinesF() {
		for (int d = 0; d < 3; d++) {
			for (int i = 0; i < currentLines[d].length; i++) {
				int ls = filter.lineRestrictions[d][i];
				if(ls < 0) continue;
				if(currentLines[d][i] != 2 * ls) return false;
			}
		}
		return true;
	}
	
	boolean accept(int loopIndex) {
		RoundAndRoundLoop loop = loops[loopIndex];
		for (int i = 0; i < loop.triangleState.length; i++) {
			if(loop.triangleState[i] > 0) {
				if(stateT[i] >= 0) return false;
			}
		}
		return true;
	}
	boolean accept(int loopIndex, int tr) {
		RoundAndRoundLoop loop = loops[loopIndex];
		boolean cs = false;
		for (int i = 0; i < loop.triangleState.length; i++) {
			if(loop.triangleState[i] > 0) {
				if(i == tr) cs = true;
				if(stateT[i] >= 0) return false;
			}
		}
		return cs;
	}
	
	int getFreeTriangle() {
		for (int tr = 0; tr < stateT.length; tr++) {
			if(stateT[tr] < 0) return tr;
		}
		return -1;
	}
	
	void move() {
		int loopIndex = ways[t][way[t]];
		RoundAndRoundLoop loop = loops[loopIndex];
		for (int i = 0; i < loop.triangleState.length; i++) {
			if(loop.triangleState[i] > 0) {
				stateL[i] = loopIndex;
				stateT[i] = t;
			}
		}
		for (int d = 0; d < 3; d++) {
			for (int i = 0; i < currentLines[d].length; i++) {
				currentLines[d][i] += loop.lines[d][i];
			}
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int loopIndex = ways[t][way[t]];
		RoundAndRoundLoop loop = loops[loopIndex];
		for (int i = 0; i < loop.triangleState.length; i++) {
			if(loop.triangleState[i] > 0) {
				stateL[i] = -1;
				stateT[i] = -1;
			}
		}		
		for (int d = 0; d < 3; d++) {
			for (int i = 0; i < currentLines[d].length; i++) {
				currentLines[d][i] -= loop.lines[d][i];
			}
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
			if(getFreeTriangle() < 0 && checkLinesF() && checkSolution()) {
				solutionCount++;
				System.out.println(solutionCount);
				printSolution();
			}
		}
	}

	boolean checkState() {
		int[][] matrix = new int[t][t];
		int[] corners = new int[t];
		int[] points = new int[t];
		int[] unfinished = new int[t];
		for (int tr = 0; tr < stateT.length; tr++) {
			int k = stateT[tr];
			if(k < 0) continue;
			corners[k] = loops[stateL[tr]].cornerCount;
			points[k] = loops[stateL[tr]].blackPointCount;
			for (int d = 0; d < 3; d++) {
				int trn = field.jumpTriangle(tr, d);
				if(trn < 0 || stateT[trn] == k) continue;
				int n = stateT[trn];
				if(n < 0) {
					unfinished[k] = 1;
					continue;
				}
				matrix[k][n] = 1;
				matrix[n][k] = 1;
			}
		}
		for (int i = 0; i < corners.length; i++) {
			int ps = 0;
			int ps2 = 0;
			for (int j = 0; j < matrix[i].length; j++) {
				if(matrix[i][j] == 1) ps += points[j];
				else ps2 += points[j];
			}
			if(unfinished[i] == 0) {
				if(ps != corners[i]) return false;
			} else {
				if(filter.blackPointsCount - ps2 < corners[i]) return false;
				if(ps > corners[i]) return false;
			}
		}
		return true;
	}

	boolean checkSolution() {
		int[][] matrix = new int[t][t];
		int[] corners = new int[t];
		int[] points = new int[t];
		for (int tr = 0; tr < stateT.length; tr++) {
			int k = stateT[tr];
			corners[k] = loops[stateL[tr]].cornerCount;
			points[k] = loops[stateL[tr]].blackPointCount;
			for (int d = 0; d < 3; d++) {
				int trn = field.jumpTriangle(tr, d);
				if(trn < 0 || stateT[trn] == k) continue;
				int n = stateT[trn];
				matrix[k][n] = 1;
				matrix[n][k] = 1;
			}
		}
		for (int i = 0; i < corners.length; i++) {
			int ps = 0;
			for (int j = 0; j < matrix[i].length; j++) {
				if(matrix[i][j] == 1) ps += points[j];
			}
			if(ps != corners[i]) return false;
		}
		return true;
	}
	
	void printSolution() {
		for (int tr = 0; tr < stateT.length; tr++) {
			int p = field.triangleP[tr];
			System.out.print(" " + stateT[tr]);
			if(tr + 1 < stateT.length) {
				int tr1 = tr + 1;
				int p1 = field.triangleP[tr1];
				if(field.getX(p1) < field.getX(p)) System.out.println("");
			}
		}
		System.out.println("");
	}


}
