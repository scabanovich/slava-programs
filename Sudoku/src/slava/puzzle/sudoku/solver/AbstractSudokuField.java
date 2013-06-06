package slava.puzzle.sudoku.solver;

import slava.puzzle.sudoku.solver.restriction.IRestriction;

public abstract class AbstractSudokuField implements ISudokuField, Symmetries {
	protected int size;
	protected int[][] regions;
	protected int[][] placeToRegions;
	
	protected IRestriction[] restrictions = null;
	
	public AbstractSudokuField() {}
	
	public int getSize() {
		return size;
	}

	public int[][] getRegions() {
		return regions;
	}

	public int[][] getPlaceToRegions() {
		return placeToRegions;
	}
	
	public IRestriction[] getRestrictions() {
		return restrictions;
	}
	
	public void addRestriction(IRestriction r) {
		if(restrictions == null) {
			restrictions = new IRestriction[]{r};
		} else {
			IRestriction[] rs = new IRestriction[restrictions.length];
			System.arraycopy(restrictions, 0, rs, 0, restrictions.length);
			rs[restrictions.length] = r;
			restrictions = rs;
		}
	}

	protected void buildPlaceToRegions() {
		placeToRegions = new int[size][];
		for (int p = 0; p < size; p++) {
			int c = 0;
			for (int r = 0; r < regions.length; r++) if(contains(p, r)) ++c;
			placeToRegions[p] = new int[c];
			c = 0;
			for (int r = 0; r < regions.length; r++) if(contains(p, r)) {
				placeToRegions[p][c] = r;
				++c;
			}
		}
	}
	
	protected boolean contains(int p, int region) {
		for (int i = 0; i < regions[region].length; i++) {
			if(p == regions[region][i]) return true;
		}
		return false;
	}
	
	protected void printRegions() {
		for (int i = 0; i < regions.length; i++) {
			for (int j = 0; j < regions[i].length; j++) {
				System.out.print(" " + regions[i][j]);
			}
			System.out.println("");
		}
	}

	public int getVerticalSymmetry(int p) {
		return -1;
	}

	public int getHorizontalSymmetry(int p) {
		return -1;
	}

	public int getDiagonalSymmetry(int p) {
		return -1;
	}

	public int getCentralSymmetry(int p) {
		return -1;
	}

	public int getSymmetry(String kind, int p) {
		if(HORIZONTAL.equals(kind)) {
			return getHorizontalSymmetry(p);
		} else if(VERTICAL.equals(kind)) {
			return getVerticalSymmetry(p);
		} else if(DIAGONAL.equals(kind)) {
			return getDiagonalSymmetry(p);
		} else if(CENTRAL.equals(kind)) {
			return getCentralSymmetry(p);
		} else {
			return -1;
		}
	}

}
