package slava.puzzle.paths.walker.model;

public class WalkerPathsPuzzle {
	WalkerPathsModel model;
	int[] filter; // 0 - use that cell, 1 - rule that cell out
	int[][] parts; // [p][d] -1 - not assigned; 0 - wall; 1 - path segment
	int[] turns; // 0 - no data; 1 - straight; 2 - turn

	public WalkerPathsPuzzle() {}
	
	public void setModel(WalkerPathsModel model) {
		this.model = model;
	}

	public WalkerPathsModel getModel() {
		return model;
	}

	public int[][] getParts() {
		return parts;
	}
	
	public int[] getFilter() {
		return filter;
	}
	
	public int[] getTurns() {
		return turns;
	}
	
	public void init() {
		parts = new int[model.getField().getSize()][4];
		for (int p = 0; p < parts.length; p++) {
			for (int d = 0; d < 4; d++) {
				parts[p][d] = -1;
			}
		}
		filter = new int[model.getField().getSize()];
		turns = new int[model.getField().getSize()];
	}

}
