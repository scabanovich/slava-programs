package slava.puzzle.crossnumber.analysis.logic;

public class RegionFiller {
	static int SET_LIMIT = 1000;
	Region region;
	int setLimit = SET_LIMIT;
	
	int generateSumDistFor = -1;

	int length;
	int[] values;
	int[][] restrictions;
	
	int[] set;
	int[] used;
	int currentSum;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int[][] filter;
	int[][] sumDist; //[sum][value]
	int setCount;
	
	public RegionFiller() {}
	
	public void init(int length, int[] values, int[][] restrictions) {
		this.length = length;
		this.values = values;
		this.restrictions = restrictions;
		wayCount = new int[length + 1];
		way = new int[length + 1];
		ways = new int[length + 1][10];
		set = new int[length];
	}
	
	public void setRegion(Region region) {
		this.region = region;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		filter = new int[length][10];
		if(generateSumDistFor < 0) {
			sumDist = null;
		} else {
			sumDist = new int[46][10];
		}
		used = new int[10];
		setCount = 0;
		currentSum = 0;
		t = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == length) return;
		if(region.sum > 0) {
			if(currentSum > region.sum) return;
			if(getEstimation() < region.sum) return;
		}
		int p = region.points[t];
		if(values[p] > 0) {
			ways[t][wayCount[t]] = values[p];
			wayCount[t]++;
			return;
		}
		for (int v = 1; v < 10; v++) {
			if(used[v] > 0 || restrictions[p][v] > 0) continue;
			if(region.sum > 0 && currentSum + v > region.sum) continue;
			ways[t][wayCount[t]] = v;
			wayCount[t]++;
		}
	}
	
	int getEstimation() {
		int s = currentSum;
		for (int i = t; i < length; i++) {
			int v = getMaxValue(i);
			if(v < 0) return -1;
			s += v;
		}
		return s;
	}
	int getMaxValue(int i) {
		int p = region.points[i];
		if(values[p] > 0) return values[p];
		for (int v = 9; v >= 1; v--) {
			if(used[v] > 0 || restrictions[p][v] > 0) continue;
			return v;
		}
		return -1;
	}
	
	void move() {
		int v = ways[t][way[t]];
		set[t] = v;
		used[v] = 1;
		currentSum += v;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int v = ways[t][way[t]];
		set[t] = 0;
		used[v] = 0;
		currentSum -= v;
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
			if(t == length) {
				onSetFound();
				++setCount;
				if(setCount >= setLimit) return;				
			}
		}
	}
	
	void onSetFound() {
		if(region.sum > 0 && currentSum != region.sum) return;
		for (int i = 0; i < length; i++) {
			filter[i][set[i]] = 1;
		}
		if(generateSumDistFor >= 0) {
			sumDist[currentSum][set[generateSumDistFor]]++;
		}
	}
	
	public int[][] getFilter() {
		return setCount >= setLimit ? null : filter;
	}
	
	public void dumpFilter() {
		for (int i = 0; i < length; i++) {
			System.out.print(region.points[i]);
			for (int j = 0; j < filter[i].length; j++) {
				if(filter[i][j] > 0) System.out.print(" " + j);
			}
			System.out.println("");
		}
	}
	
	public int[][] getSumDistribution() {
		return setCount >= setLimit ? null : sumDist;
	}

}
