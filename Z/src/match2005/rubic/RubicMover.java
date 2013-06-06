package match2005.rubic;

public class RubicMover implements RubicConstants {
	RubicState state;
	RubicRotation[] rotations = ROTATIONS;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int timeLimit;
	int solutionCount;
	
	public RubicMover() {}
	
	public void setState(RubicState state) {
		this.state = state;
	}
	
	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}
	
	public void setRotations(RubicRotation[] rotations) {
		this.rotations = rotations;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		wayCount = new int[22];
		way = new int[22];
		ways = new int[22][ROTATIONS.length];
		t = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(!canContinue()) return;
		RubicRotation r2 = (t == 0) ? null : rotations[ways[t - 1][way[t - 1]]];
		RubicRotation r3 = (t < 2) ? null : rotations[ways[t - 2][way[t - 2]]];
		for (int i = 0; i < rotations.length; i++) {
			if(t > 0) {
				if(!accept(rotations[i], r2, r3)) continue;
			}
			ways[t][wayCount[t]] = i;
			wayCount[t]++;
		}
	}
	
	boolean accept(RubicRotation r1, RubicRotation r2, RubicRotation r3) {
		if(r2 == null) return true;
		if(r1.axe != r2.axe) return true;
		if(r1.range <= r2.range) return false;
		if(r3 != null && r3.axe == r2.axe) return false;
		return true;
	}
	
	void move() {
		int r = ways[t][way[t]];
		rotations[r].execute(state);
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int r = REVERSE_ROTATION[ways[t][way[t]]];
		rotations[r].execute(state);
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
			if(isSolution()) {
				solutionCount++;
				onSolutionFound();
			}
		}
	}
	
	protected boolean canContinue() {
		return t < timeLimit;
	}
	
	protected boolean isSolution() {
		return t == timeLimit;
	}
	
	protected void onSolutionFound() {
	}
	
}
