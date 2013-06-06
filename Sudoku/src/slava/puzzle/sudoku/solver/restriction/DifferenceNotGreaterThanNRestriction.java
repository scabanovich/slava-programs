package slava.puzzle.sudoku.solver.restriction;

import slava.puzzle.sudoku.model.SudokuDesignField;
import slava.puzzle.sudoku.solver.ISudokuField;
import slava.puzzle.sudoku.solver.restriction.IRestriction;
import slava.puzzle.sudoku.solver.restriction.IRestrictionListener;

public class DifferenceNotGreaterThanNRestriction implements IRestriction {
	int neighboursDifferNotMoreThan = 3;
	SudokuDesignField field;
	ISudokuField f;
	
	public DifferenceNotGreaterThanNRestriction() {
	}

	public void setNeighboursDifferNotMoreThan(int k) {
		neighboursDifferNotMoreThan = k;
	}
	
	public void setField(ISudokuField f) {
		this.f = f;
		field = new SudokuDesignField();
		field.setSize(f.getColorCount());
	}

	public void setData(SudokuDesignField field) {
		this.field = field;
	}

	public void add(int p, int v, IRestrictionListener listener) {
		for (int d = 0; d < 4; d++) {
			int q = field.jp(p, d);
			if(q < 0) continue;
			for (int vc = 0; vc < field.getWidth(); vc++) {
				if(Math.abs(v - vc) > neighboursDifferNotMoreThan) listener.exclude(q, vc);
			}
		}
	}

	public void remove(int p, int v, IRestrictionListener listener) {
		for (int d = 0; d < 4; d++) {
			int q = field.jp(p, d);
			if(q < 0) continue;
			for (int vc = 0; vc < field.getWidth(); vc++) {
				if(Math.abs(v - vc) > neighboursDifferNotMoreThan) listener.include(q, vc);
			}
		}
	}

	public void init(IRestrictionListener listener) {
		// TODO Auto-generated method stub		
	}


	public void dispose() {}

}
