package slava.puzzle.sudoku.solver.restriction;

public class AdditionalNeighboursRestriction implements IRestriction {
	/**
	 * additionalNeighbours[p] - array of neighbour nodes to node p
	 * that may not have same value as in p
	 * @return
	 */
	int[][] additionalNeighbours;
	
	public AdditionalNeighboursRestriction() {}
	
	public void setAdditionalNeighbours(int[][] ns) {
		additionalNeighbours = ns;
	}

	public void add(int p, int v, IRestrictionListener listener) {
		if(additionalNeighbours != null) {
			int[] ns = additionalNeighbours[p];
			for (int i = 0; i < ns.length; i++) listener.exclude(ns[i], v);
		}
	}

	public void remove(int p, int v, IRestrictionListener listener) {
		if(additionalNeighbours != null) {
			int[] ns = additionalNeighbours[p];
			for (int i = 0; i < ns.length; i++) listener.include(ns[i], v);
		}
	}

	public void init(IRestrictionListener listener) {
		// TODO Auto-generated method stub		
	}

	public void dispose() {}
}
