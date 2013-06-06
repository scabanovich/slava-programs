package slava.puzzle.sudoku.solver.restriction;

public interface IGeneratedRestriction extends IRestriction {
	public void reset();
	public void setGeneratedSolution(int[] generated);
}
