package slava.puzzle.sudoku.solver.restriction;

public interface IRestriction {
	public void init(IRestrictionListener listener);
	public void add(int p, int v, IRestrictionListener listener);
	public void remove(int p, int v, IRestrictionListener listener);
	public void dispose();
}
