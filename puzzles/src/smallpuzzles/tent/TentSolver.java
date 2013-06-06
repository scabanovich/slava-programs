package smallpuzzles.tent;

import com.slava.common.RectangularField;

public class TentSolver {
	RectangularField field;
	int[] trees;
	int[] vData;
	int[] hData;
	
	int[] treePositions;
	int[] usedTrees;
	
	int unresolvedTreeCount;

	int[] tents;
	int[] vTents;
	int[] hTents;
	int[] restriction;
	
	int t;
	int[] currentTree;
	int[] wayCount;
	int[] way;
	int[][] ways;
	int[] temp;
	
	int solutionCount;
	int[] solution;
	int treeCount;

	public TentSolver() {}
	
	public void setField(RectangularField f) {
		field = f;
	}
	
	public void setData(int[] trees, int[] hData, int[] vData) {
		this.trees = trees;
		this.hData = hData;
		this.vData = vData;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		unresolvedTreeCount = 0;
		for (int p = 0; p < trees.length; p++) {
			if(trees[p] > 0) unresolvedTreeCount++;
		}
		treePositions = new int[unresolvedTreeCount];
		usedTrees = new int[unresolvedTreeCount];
		for (int p = 0, c = 0; p < trees.length; p++) {
			if(trees[p] > 0) {
				treePositions[c] = p;
				c++;
			}
		}
		
		tents = new int[trees.length];
		vTents = new int[vData.length];
		hTents = new int[hData.length];
		restriction = new int[trees.length];
		
		currentTree = new int[unresolvedTreeCount + 1];
		wayCount = new int[unresolvedTreeCount + 1];
		way = new int[unresolvedTreeCount + 1];
		ways = new int[unresolvedTreeCount + 1][4];
		temp = new int[4];
		
		t = 0;
		solutionCount = 0;
		solution = null;
		treeCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(unresolvedTreeCount == 0) return;
		if(!isValidState()) return;
		int wcm = 5;
		int tp = -1;
		for (int i = 0; i < treePositions.length; i++) {
			if(usedTrees[i] > 0) continue;
			int wc = getWayCount(treePositions[i]);
			if(wc < wcm) {
				if(wc == 0) return;
				wcm = wc;
				tp = i;
				for (int k = 0; k < wc; k++) ways[t][k] = temp[k];
			}
		}
		if(wcm < 5) {
			wayCount[t] = wcm;
			currentTree[t] = tp;
		}
	}
	
	int getWayCount(int p) {
		int wc = 0;
		for (int d = 0; d < 4; d++) {
			int q = field.jump(p, d);
			if(q < 0 || restriction[q] > 0 || trees[q] > 0) continue;
			int x = field.getX(q), y = field.getY(q);
			if(hData[y] == hTents[y]) continue;
			if(vData[x] == vTents[x]) continue;
			temp[wc] = q;
			wc++;
		}
		return wc;
	}
	
	boolean isValidState() {
		for (int x = 0; x < vData.length; x++) {
			if(vData[x] < 0) continue;
			if(vData[x] < vTents[x]) return false;
		}
		for (int y = 0; y < hData.length; y++) {
			if(hData[y] < 0) continue;
			if(hData[y] < hTents[y]) return false;
		}
		return true;
	}
	
	void move() {
		int treeIndex = currentTree[t];
		usedTrees[treeIndex]++;
		unresolvedTreeCount--;
		int p = ways[t][way[t]];
		tents[p] = 1;
		vTents[field.getX(p)]++;
		hTents[field.getY(p)]++;
		for (int dx = -1; dx <= 1; dx++) {
			for (int dy = -1; dy <= 1; dy++) {
				int q = field.jumpXY(p, dx, dy);
				if(q >= 0) restriction[q]++;
			}
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int treeIndex = currentTree[t];
		usedTrees[treeIndex]--;
		unresolvedTreeCount++;
		int p = ways[t][way[t]];
		tents[p] = 0;
		vTents[field.getX(p)]--;
		hTents[field.getY(p)]--;
		for (int dx = -1; dx <= 1; dx++) {
			for (int dy = -1; dy <= 1; dy++) {
				int q = field.jumpXY(p, dx, dy);
				if(q >= 0) restriction[q]--;
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
			if(wayCount[t] == 0) {
				treeCount++;
			}
			if(unresolvedTreeCount == 0 && isSolution()) {
				solutionCount++;
				if(solutionCount == 1) {
					solution = (int[])tents.clone();
				}
			}
		}
	}
	
	boolean isSolution() {
		for (int x = 0; x < vData.length; x++) {
			if(vData[x] >= 0 && vData[x] != vTents[x]) return false;
		}
		for (int y = 0; y < hData.length; y++) {
			if(hData[y] >= 0 && hData[y] != hTents[y]) return false;
		}
		return true;
	}
	
	void printSolution() {
		
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}
	
	public int[] getSolutioon() {
		return solution;
	}
	
}
