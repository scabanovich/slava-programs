package slava.puzzle.paths.nook.model;

public class NookPathsPreferences {
	int solutionLimit = 100;
	int treeLimit = 1;

	public NookPathsPreferences() {}
	
	public int getSolutionLimit() {
		return solutionLimit;
	}
	
	public int getTreeLimit() {
		return treeLimit;
	}
	
	public void setSolutionLimit(int s) {
		solutionLimit = s;
	}
	
	public void setTreeLimit(int s) {
		treeLimit = s;
	}

}
