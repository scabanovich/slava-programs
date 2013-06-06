package slava.puzzle.sudoku.model;

import java.util.*;
import slava.puzzle.sudoku.solver.AbstractSudokuField;
import slava.puzzle.sudoku.solver.restriction.AdditionalNeighboursRestriction;
import slava.puzzle.sudoku.solver.restriction.DifferenceNotGreaterThanNRestriction;

public class SudokuRunField extends AbstractSudokuField {
	SudokuModel model;
	SudokuDesignField designField;
	SudokuProblemInfo problem;
	
	public SudokuRunField(SudokuModel model) {
		this.model = model;
		this.designField = model.getField();
		this.problem = model.getProblemInfo();
		size = designField.getSize();
		buildRegions();
		buildPlaceToRegions();
		if(problem.isNotTouchingDiagonally()) {
			AdditionalNeighboursRestriction rs = new AdditionalNeighboursRestriction();
			rs.setAdditionalNeighbours(designField.getDiagonalNeighbours());
			addRestriction(rs);
		}
		if(problem.getNeighboursDifferNotMoreThan() > 0) {
			DifferenceNotGreaterThanNRestriction rs = new DifferenceNotGreaterThanNRestriction();
			rs.setNeighboursDifferNotMoreThan(problem.getNeighboursDifferNotMoreThan());
			rs.setData(designField);
			addRestriction(rs);
		}
		if(problem.isUsingDifferenceOneRestriction()) {
			DifferenceNRestriction rs = new DifferenceNRestriction();
			rs.setDiff(problem.getDifferenceValue());
			rs.setData(designField, problem.getDifferenceOneData());
			addRestriction(rs);
		}
		if(problem.isUsingLessOrGreaterRestriction()) {
			LessOrGreaterRestriction rs = new LessOrGreaterRestriction();
			rs.setData(designField, problem.getLessOrGreaterData());
			rs.setProblem(problem);
			addRestriction(rs);
		}
		if(problem.isUsingXVRestriction()) {
			XVRestriction rs = new XVRestriction();
			rs.setData(designField, problem.getXVData());
			rs.setProblem(problem);
			addRestriction(rs);
		}
		if(problem.isUsingPartitioningSumRestriction()) {
			PartitioningSumRestriction rs = new PartitioningSumRestriction();
			rs.setData(designField, problem.getPartitioning(), problem.getSums());
			rs.setProblem(problem);
			addRestriction(rs);
		}
	}

	public int getColorCount() {
		return designField.getWidth();
	}

	public String printSolution(int[] solution) {
		return null;
	}
	
	void buildRegions() {
		List list = new ArrayList();

		int[][] hR = new int[designField.getWidth()][designField.getWidth()];
		for (int i = 0; i < designField.getSize(); i++) {
			int x = designField.x(i);
			int y = designField.y(i);
			hR[y][x] = i;
		}
		for (int i = 0; i < hR.length; i++) list.add(hR[i]);

		int[][] vR = new int[designField.getWidth()][designField.getWidth()];
		for (int i = 0; i < designField.getSize(); i++) {
			int x = designField.x(i);
			int y = designField.y(i);
			vR[x][y] = i;
		}
		for (int i = 0; i < vR.length; i++) list.add(vR[i]);
		
		int[] rs = problem.getRegions();
		int[] sz = new int[rs.length];
		for (int i = 0; i < rs.length; i++) sz[rs[i]]++;
		for (int i = 0; i < sz.length; i++) {
			if(sz[i] > 1 && sz[i] <= getColorCount()) {
				int[] rg = new int[sz[i]];
				int k = 0;
				for (int j = 0; j < rs.length; j++) if(rs[j] == i) {
					rg[k] = j;
					++k;
				}
				list.add(rg);
			}
		}
		
		if(problem.getDiagonalsOption() == SudokuProblemInfo.MAIN_DIAGONALS) {
			int w = designField.getWidth();
			int[] ds = new int[w];
			for (int i = 0; i < ds.length; i++) ds[i] = designField.xy[i][i];
			list.add(ds);
			ds = new int[w];
			for (int i = 0; i < ds.length; i++) ds[i] = designField.xy[i][w - 1 - i];
			list.add(ds);
		} else if(problem.getDiagonalsOption() == SudokuProblemInfo.ALL_DIAGONALS) {
			int w = designField.getWidth();
			for (int ix = 0; ix < w - 1; ix++) {
				int[] ds = new int[w - ix];
				for (int iy = 0; iy < w - ix; iy++) {
					ds[iy] = designField.xy[ix + iy][iy];
				}
				list.add(ds);
			}
			for (int iy = 1; iy < w - 1; iy++) {
				int[] ds = new int[w - iy];
				for (int ix = 0; ix < w - iy; ix++) {
					ds[ix] = designField.xy[ix][ix + iy];
				}
				list.add(ds);
			}
		}

		regions = (int[][])list.toArray(new int[0][]);
	}

}
