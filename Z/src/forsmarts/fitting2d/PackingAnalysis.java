package forsmarts.fitting2d;

public class PackingAnalysis extends AbstractPackingAnalysis {	
	protected int[] figure;	
	protected int[][] ways;
	
	public PackingAnalysis() {}
	
	protected void init() {
		figure = new int[placements.length];
		wayCount = new int[200];
		way = new int[200];
		ways = new int[200][1000];
		figure = new int[tMax + 1];
		super.init();
	}
	
	protected void srch() {
		wayCount[t] = 0;
		if(t >= tMax) return;
		if(isIllegalState()) return;
		if(t == 0) {
			figure[t] = 0;
		} else {
			figure[t] = figure[t - 1];
			while(usage[figure[t]] == usageLimits[figure[t]]) figure[t]++;
		}
		int imin = 0;
		if(t > 0 && figure[t] == figure[t - 1]) imin = ways[t - 1][way[t - 1]] + 1;
		for (int i = imin; i < placements[figure[t]].length; i++) {
			if(canPlace(i)) {
				ways[t][wayCount[t]] = i;
				wayCount[t]++;
			}
		}
		filterWays();
	}
	
	//Override to create a random subset of variants when their total is too greate.
	protected void  filterWays() {
		
	}
	
	/**
	 * Override if need to check matching additional conditions.
	 * @return
	 */	
	protected boolean isIllegalState() {
		return false;
	}
	
	protected boolean canPlace(int pi) {
		int index = placements[figure[t]][pi].getIndex();
		if(usage[index] == usageLimits[index]) return false;
		int[] ps = placements[figure[t]][pi].getPoints();
		for (int i = 0; i < ps.length; i++) {
			if(fieldRestriction != null && fieldRestriction[ps[i]] > 0) return false;
			if(state[ps[i]] >= 0 || (shapeMode != NO_SHAPE && shape[ps[i]] > 0)) return false;
		}
		return true;
	}
	
	protected void move() {
		int pi = ways[t][way[t]];
		int[] ps = placements[figure[t]][pi].getPoints();
		for (int i = 0; i < ps.length; i++) {
			int p = ps[i];
			state[p] = figure[t];
			shape[p]++;
			for (int d = 0; d < 4; d++) {
				int q = field.jump(p, d);
				if(q >= 0) shape[q]++;
			}
			if(shapeMode == HVD_SHAPE) {
				for (int d = 1; d < 8; d += 2) {
					int q = octafield.jump(p, d);
					if(q >= 0) shape[q]++;
				}
			}
		}
		usage[figure[t]]++;
		++t;
		srch();
		way[t] = -1;
	}
	
	protected void back() {
		--t;
		int pi = ways[t][way[t]];
		int[] ps = placements[figure[t]][pi].getPoints();
		for (int i = 0; i < ps.length; i++) {
			int p = ps[i];
			state[p] = -1;
			shape[p]--;
			for (int d = 0; d < 4; d++) {
				int q = field.jump(p, d);
				if(q >= 0) shape[q]--;
			}
			if(shapeMode == HVD_SHAPE) {
				for (int d = 1; d < 8; d += 2) {
					int q = octafield.jump(p, d);
					if(q >= 0) shape[q]--;
				}
			}
		}
		usage[figure[t]]--;
	}
	
}
