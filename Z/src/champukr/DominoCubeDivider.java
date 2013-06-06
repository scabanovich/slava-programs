package champukr;

public class DominoCubeDivider {
	DominoCubeField field;
	DominoSet dominoes;
	int[] numbers;

	int[] state;
	int[] usedDominoes;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] waysA;
	int[][] waysB;
	
	int treeCount;
	int solutionCount;
	int[] solution;
	
	int[][] distribution;
	
	public DominoCubeDivider() {}

	public void setField(DominoCubeField f) {
		field = f;
	}
	
	public void setDominoes(DominoSet dominoes) {
		this.dominoes = dominoes;
	}
	
	public void setNumbers(int[] numbers) {
		this.numbers = (int[])numbers.clone();
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.surfaceSize];
		for (int i = 0; i < state.length; i++) state[i] = -1;
		usedDominoes = new int[dominoes.size];
		wayCount = new int[dominoes.size + 1];
		way = new int[dominoes.size + 1];
		waysA = new int[dominoes.size + 1][50];
		waysB = new int[dominoes.size + 1][50];
		t = 0;
		treeCount = 0;
		solutionCount = 0;
		solution = null;
		distribution = new int[field.surfaceSize][6];
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == dominoes.size) return;
		int wcb = 100;
		int[] ds = getDistribution();
		int n = getNarrowestDominoIndex(ds);
		if(ds[n] == 0) return;
		if(ds[n] < wcb) {
			wcb = ds[n];
			getWayByDominoe(n);
			for (int k = 0; k < wcb; k++) {
				waysA[t][k] = tempA[k];
				waysB[t][k] = tempB[k];
			}
		}
		if(wcb < 2) {
			wayCount[t] = wcb;
			return;
		}
			if(true) {
				wayCount[t] = wcb;
				return;
			}
					
		for (int p = 0; p < field.surfaceSize; p++) {
			if(state[p] >= 0) continue;
			int wc = getWayCountByPlace(p);
			if(wc < wcb) {
				if(wc == 0) return;
				wcb = wc;
				for (int k = 0; k < wc; k++) {
					waysA[t][k] = tempA[k];
					waysB[t][k] = tempB[k];
				}
			}
		}
		if(wcb < 100) wayCount[t] = wcb;
	}
	
	int[] tempA = new int[50];
	int[] tempB = new int[50];
	
	int getWayCountByPlace(int p) {
		int wc = 0;
		for (int d = 0; d < 6; d++) {
			int q = field.jumpOnSurface(p, d);
			if(q < 0 || state[q] >= 0) continue;
			int domino = dominoes.dominoIndex[numbers[p]][numbers[q]];
			if(usedDominoes[domino] > 0) continue;
			tempA[wc] = p;
			tempB[wc] = q;
			wc++;
		}
		return wc;
	}
	
	int getWayByDominoe(int n) {
		int wc = 0;
		int a = dominoes.lessNumber[n];
		int b = dominoes.largerNumber[n];
		for (int p = 0; p < field.surfaceSize; p++) {
			if(p < 0 || state[p] >= 0) continue;
			if(numbers[p] != a && numbers[p] != b) continue;
			for (int d = 0; d < 3; d++) {
				int q = field.jumpOnSurface(p, d);
				if(q < 0 || state[q] >= 0) continue;
				if(dominoes.dominoIndex[numbers[p]][numbers[q]] == n)  {
					tempA[wc] = p;
					tempB[wc] = q;
					wc++;
				}
			}
		}		
		return wc;
	}
	
	int[] getDistribution() {
		int[] ds = new int[dominoes.size];
		for (int p = 0; p < field.surfaceSize; p++) {
			if(p < 0 || state[p] >= 0) continue;
			for (int d = 0; d < 3; d++) {
				int q = field.jumpOnSurface(p, d);
				if(q < 0 || state[q] >= 0) continue;
				int domino = dominoes.dominoIndex[numbers[p]][numbers[q]];
				if(usedDominoes[domino] > 0) continue;
				ds[domino]++;
			}
		}		
		return ds;
	}
	
	int getNarrowestDominoIndex(int[] ds) {
		int wc = 100;
		int bi = -1;
		for (int k = 0; k < dominoes.size; k++) {
			if(usedDominoes[k] > 0) continue;
			if(ds[k] < wc) {
				wc = ds[k];
				bi = k;
			}
		}
		return bi;
	}
	
	void move() {
		int pa = waysA[t][way[t]];
		int pb = waysB[t][way[t]];
		state[pa] = t + 1;
		state[pb] = t + 1;
		int d = dominoes.dominoIndex[numbers[pa]][numbers[pb]];
		usedDominoes[d]++;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int pa = waysA[t][way[t]];
		int pb = waysB[t][way[t]];
		state[pa] = -1;
		state[pb] = -1;
		int d = dominoes.dominoIndex[numbers[pa]][numbers[pb]];
		usedDominoes[d]--;
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
			if(t == dominoes.size) {
				solutionCount++;
				onSolutionFound();
			}
		}
	}
	
	void onSolutionFound() {
		if(solutionCount == 1) {
			solution = (int[])state.clone();
		}
		addToDistribution();
	}
	
	void addToDistribution() {
		for (int i = 0; i < state.length; i++) {
			for (int d = 0; d < 6; d++) {
				int j = field.jumpOnSurface(i, d);
				if(j < 0 || state[j] != state[i]) continue;
				distribution[i][d]++;
			}
		}
	}
	
	public int getFixedDominoCount() {
		int c = 0;
		for (int i = 0; i < state.length; i++) {
			int q = 0;
			for (int d = 0; d < 6; d++) {
				if(distribution[i][d] > 0) q++;
			}
			if(q == 1) ++c;
		}
		c = c / 2;
		return c;
	}

}
