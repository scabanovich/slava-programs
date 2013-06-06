package diogen2006;

public class MixtureSolver {
	MixtureSet initialSet;
	MixtureSet set;
	MixtureDelta[] deltas;
	
	double targetRatio;
	int mixtureLimit;
	
	int moveLimit = 17;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] waysK; //0 - transfer; 1 - add water; 2 - add shit;
	int[][] waysP; //0 - from where;
	int[][] waysQ; //to where
	
	double bestRatio;
	double bestDelta = 0.5;
	
	public MixtureSolver() {}
	
	public void setInitialSet(MixtureSet initialSet) {
		this.initialSet = initialSet;
	}
	
	public void setTargetRation(double d) {
		targetRatio = d;
	}
	
	public void setMixtureLimit(int i) {
		mixtureLimit = i;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		deltas = new MixtureDelta[moveLimit + 1];
		set = initialSet.copy();
		for (int i = 0; i < deltas.length; i++) deltas[i] = new MixtureDelta();
		wayCount = new int[moveLimit + 1];
		way = new int[moveLimit + 1];
		waysK = new int[moveLimit + 1][50];
		waysP = new int[moveLimit + 1][50];
		waysQ = new int[moveLimit + 1][50];
		
		t = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == moveLimit) return;
		if(areAllRatiosOnOneSide()) {
			return;
		}
		MixtureSet s = set;
		for (int p = 0; p < initialSet.mixtures.length; p++) {
			if(s.mixtures[p].amount == 0) continue;
			for (int q = 0; q < initialSet.mixtures.length; q++) {
				if(q == p || s.mixtures[q].amount >= mixtureLimit) continue;
				waysK[t][wayCount[t]] = 0;
				waysP[t][wayCount[t]] = p;
				waysQ[t][wayCount[t]] = q;
				wayCount[t]++;
			}
		}
		if(t < 8) randomize();
	}
	
	void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = 0; i < wayCount[t]; i++) {
			int j = i + (int)((wayCount[t] - i) * Math.random());
			change(waysK[t], i, j);
			change(waysP[t], i, j);
			change(waysQ[t], i, j);
		}
	}
	
	void change(int[] a, int i, int j) {
		int c = a[i];
		a[i] = a[j];
		a[j] = c;
	}
	
	boolean areAllRatiosOnOneSide() {
		MixtureSet s = set;
		boolean less = false;
		boolean more = false;
		for (int i = 0; i < s.mixtures.length; i++) {
			double r = s.mixtures[i].getRatio();
			if(r < targetRatio) less = true; else more = true;
		}
		return !(less && more);
	}
	
	void move() {
		int k = waysK[t][way[t]];
		int p = waysP[t][way[t]];
		int q = waysQ[t][way[t]];
		if(k == 0) {
			set.move(p, q, 1, deltas[t]);
//			System.out.println(sets[t + 1].toString());
//			System.out.println("");
		} else {
			// add water or shit
		}		
		++t;
		srch();
		way[t] = -1;
	}

	void back() {
		--t;
		set.rollback(deltas[t]);
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return; else back();
			}
			way[t]++;
			move();
			if(isBestRatio()) {
				System.out.println(bestDelta);
				printSolution();
			}
		}		
	}
	
/*
	double getDelta() {
		MixtureSet s = set;
		double delta = 100000000d;
		for (int i = 0; i < s.mixtures.length; i++) {
			double r = s.mixtures[i].getRatio();
			double d = Math.abs(r - targetRatio);
			if(d < delta) delta = d;
		}
		return delta;
	}
*/

	boolean isBestRatio() {
		MixtureSet s = set;
		double delta = 100000000d;
		double best = 0;
		for (int i = 0; i < s.mixtures.length; i++) {
			double r = s.mixtures[i].getRatio();
//			System.out.println(r);
			double d = Math.abs(r - targetRatio);
			if(d < delta) {
				delta = d;
				best = r;
			}
		}
		if(delta < bestDelta) {
			bestDelta = delta;
			bestRatio = best;
			return true;
		}
		return false;
	}
	
	void printSolution() {
		for (int i = 0; i < t; i++) {
			int k = waysK[i][way[i]];
			int p = waysP[i][way[i]];
			int q = waysQ[i][way[i]];
			if(k == 0) {
				System.out.print(" " + p + "->" + q + ",");
			} else {
				System.out.print(" " + k + " " + q + ",");
			}
		}
		System.out.println("");
	}

}
