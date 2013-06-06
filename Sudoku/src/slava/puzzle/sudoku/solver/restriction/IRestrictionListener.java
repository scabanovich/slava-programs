package slava.puzzle.sudoku.solver.restriction;

public interface IRestrictionListener {
	public void exclude(int p, int v);
	public void include(int p, int v);
}
