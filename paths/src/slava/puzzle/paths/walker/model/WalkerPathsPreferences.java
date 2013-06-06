package slava.puzzle.paths.walker.model;

public class WalkerPathsPreferences {
	public static String STANDARD_KIND = "standard";
	public static String MASUI_KIND = "masui";
	String puzzleKind = "standard";
	int solutionLimit = 100;
	int treeLimit = 5;

	public WalkerPathsPreferences() {}

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
	
	public String getPuzzleKind() {
		return puzzleKind;
	}
	
	public void setPuzzleKind(String s) {
		puzzleKind = s;
	}
	
	public boolean isMasui() {
		return MASUI_KIND.equals(puzzleKind);
	}

}
