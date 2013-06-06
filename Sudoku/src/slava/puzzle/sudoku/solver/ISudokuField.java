package slava.puzzle.sudoku.solver;

import slava.puzzle.sudoku.solver.restriction.IRestriction;

public interface ISudokuField {
	public int getSize();
	public int getColorCount();

	public int[][] getRegions();
	public int[][] getPlaceToRegions();

	public IRestriction[] getRestrictions();

	public String printSolution(int[] solution);
	
	/**
	 * Returns symmetric position or -1 if it cannot be provided.
	 * 
	 * @param kind
	 * @param p
	 * @return
	 */
	public int getSymmetry(String kind, int p);
}
