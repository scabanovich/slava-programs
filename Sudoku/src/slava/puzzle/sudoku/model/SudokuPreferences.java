package slava.puzzle.sudoku.model;

public class SudokuPreferences {
	public static SudokuPreferences instance = new SudokuPreferences();
	
	int generateTreeCountLimit = 1;
	int countSolutionUpTo = 10000;
	
	public int getGenerateTreeCountLimit() {
		return generateTreeCountLimit;
	}

	public void setGenerateTreeCountLimit(int v) {
		generateTreeCountLimit = (v <= 0) ? -1 : v;
	}
	
	public int getCountSolutionUpTo() {
		return countSolutionUpTo;
	}
	
	public void setCountSolutionUpTo(int v) {
		countSolutionUpTo = (v <= 0) ? 1 : v;
	}

}
