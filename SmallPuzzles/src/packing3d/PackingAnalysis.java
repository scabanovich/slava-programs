package packing3d;

public class PackingAnalysis {
	CubeField cubeField;
	int size;
	Placement[] placements;
	int[] usageLimits;
	
	int[] state;	
	int[] usage;
	int filledCount;

	int[] place;	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;
	
	public void setPlacements(Placement[] placements) {
		this.placements = placements;		
	}
	
	public void setUsageLimits(int[] usageLimits) {
		this.usageLimits = usageLimits;
	}
	
	public void setCubeField(CubeField cubeField) {
		this.cubeField = cubeField;
		this.size = cubeField.size;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[size];
		for (int i = 0; i < size; i++) state[i] = -1;
		distribution = new int[size];
		usage = new int[usageLimits.length];
		wayCount = new int[200];
		way = new int[200];
		ways = new int[200][1000];
		t = 0;
		filledCount = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(filledCount == size) return;
		createDistribution();
		int p = getNarrowestPlace();
		if(p < 0 || distribution[p] == 0) return;
		for (int i = 0; i < placements.length; i++) {
			if(canPlace(i) && placements[i].contains(p)) {
				ways[t][wayCount[t]] = i;
				wayCount[t]++;
			}
		}
	}
	
	int getNarrowestPlace() {
		int m = 1000;
		int p = -1;
		for (int i = 0; i < size && m > 0; i++) {
			if(state[i] >= 0) continue;
			if(distribution[i] < m) {
				m = distribution[i];
				p = i;
			}
		}
		return p;
	}
	
	void createDistribution() {
		for (int i = 0; i < size; i++) distribution[i] = 0;
		for (int i = 0; i < placements.length; i++) {
			if(canPlace(i)) {
				int[] ps = placements[i].getPoints();
				for (int j = 0; j < ps.length; j++) distribution[ps[j]]++;				
			}
		}
	}
	
	int[] distribution;
	
	boolean canPlace(int pi) {
		int index = placements[pi].getIndex();
		if(usage[index] == usageLimits[index]) return false;
		int[] ps = placements[pi].getPoints();
		for (int i = 0; i < ps.length; i++) {
			if(state[ps[i]] >= 0) return false;
		}
		return true;
	}
	
	void move() {
		int pi = ways[t][way[t]];
		int[] ps = placements[pi].getPoints();
		filledCount += ps.length;
		for (int i = 0; i < ps.length; i++) {
			state[ps[i]] = t;
		}
		int index = placements[pi].getIndex();
		usage[index]++;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int pi = ways[t][way[t]];
		int[] ps = placements[pi].getPoints();
		filledCount -= ps.length;
		for (int i = 0; i < ps.length; i++) {
			state[ps[i]] = -1;
		}
		int index = placements[pi].getIndex();
		usage[index]--;
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
			if(filledCount == size) {
				++solutionCount;
				printSolution();
			}
		}		
	}
	
	void printSolution() {
		System.out.println("Solution " + solutionCount);
		for (int i = 0; i < size; i++) {
			System.out.print(" " + state[i]);
			if(cubeField.x[i] == cubeField.xSize - 1) {
				System.out.println("");
				if(cubeField.y[i] == cubeField.ySize - 1) {
					System.out.println("");
				} 
			} 
		}
	}

}
