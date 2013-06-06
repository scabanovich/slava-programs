package forsmarts.snake;

public class SnakeBySolver extends AbstractSnakeSolver {
	//data section
	int[] regions;
	int startingRegion;
	int targetRegion;

	//state section
	int[] regionUsage;

	public SnakeBySolver() {}
	
	public void setRegions(int[] rs) {
		regions = rs;
	}
	
	public void setStartingRegion(int s) {
		startingRegion = s;
	}
	
	public void setTargetRegion(int s) {
		targetRegion = s;
	}
	
	protected void init() {
		int maxRegion = 0;
		for (int p = 0; p < regions.length; p++) {
			if(regions[p] > maxRegion) maxRegion = regions[p];
		}
		regionUsage = new int[maxRegion + 1];
		setSnakeLength(3 * maxRegion + 3);
		super.init();
	}

	protected void srchInitial() {
		for (int p = 0; p < regions.length; p++) {
			if(regions[p] == startingRegion) addWay(p);
		}		
	}

	protected boolean canMove(int q) {
		return regionUsage[regions[q]] < 3;
	}

	protected boolean isOk() {
		for (int i = 0; i < regionUsage.length; i++) {
			if(regionUsage[i] > 3) return false;
		}
		return true;
	}

	protected boolean isSolution() {
		for (int i = 0; i < regionUsage.length; i++) {
			if(regionUsage[i] != 3) return false;
		}
		return regions[place[t]] == targetRegion;
	}

	protected void onBack(int p) {
		regionUsage[regions[p]]--;		
	}

	protected void onMove(int p) {
		regionUsage[regions[p]]++;		
	}

}
