package slava.puzzle.sudoku.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import slava.puzzle.sudoku.solver.restriction.IRestriction;
import slava.puzzle.sudoku.solver.restriction.IRestrictionListener;

public class PartitioningSumRestriction implements IRestriction {
	SudokuProblemInfo problem;
	SudokuDesignField field;
	int colorCount;
	int[] partitioning; //[p] --> index
	int[] sums;  //[index] --> sum

	Set[] points;

	public PartitioningSumRestriction() {}

	public void setData(SudokuDesignField field, int[] partitioning, int[] sums) {
		this.field = field;
		colorCount = field.getWidth();
		this.partitioning = partitioning;
		this.sums = (int[])sums.clone();
		points = new Set[sums.length];
		for (int i = 0; i < points.length; i++) points[i] =  new HashSet();
		for (int p = 0; p < partitioning.length; p++) {
			points[partitioning[p]].add(new Integer(p));
		}
	}

	public void setProblem(SudokuProblemInfo problem) {
		this.problem = problem;
	}

	boolean inTheSameRegion(Integer p1, Integer p2) {
		return inTheSameRegion(p1.intValue(), p2.intValue());
	}
	
	boolean inTheSameRegion(int p1, int p2) {
		if(problem.getRegionAt(p1) == problem.getRegionAt(p2)) return true;
		if(field.x(p1) == field.x(p2)) return true;
		if(field.y(p1) == field.y(p2)) return true;
		return false;
	}

	public void init(IRestrictionListener listener) {
		for (int index = 0; index < sums.length; index++) {
			int s = sums[index];
			if(points[index].size() == 2) {
				Integer[] qs = (Integer[])points[index].toArray(new Integer[2]);
				boolean same = inTheSameRegion(qs[0], qs[1]);
				for (int vc = 0; vc < s - colorCount - 1 && vc < colorCount; vc++) {
					listener.exclude(qs[0].intValue(), vc);
					listener.exclude(qs[1].intValue(), vc);
				}			
				for (int vc = s - 1; vc < colorCount; vc++) {
					if(vc < 0) continue;
					listener.exclude(qs[0].intValue(), vc);
					listener.exclude(qs[1].intValue(), vc);
				}			
				if(same && (s % 2 == 0) && s >= 2 && s <= 2 * colorCount ) {
					int middle = (s - 2) / 2;
					listener.exclude(qs[0].intValue(), middle);
					listener.exclude(qs[1].intValue(), middle);
				}
			}
		}
		
	}

	public void add(int p, int v, IRestrictionListener listener) {
		int index = partitioning[p];
		Set ps = points[index];
		ps.remove(new Integer(p));
		sums[index] -= (v + 1);
		int s = sums[index];
		if(ps.size() == 1) {
			int q = ((Integer)ps.iterator().next()).intValue();
			for (int vc = 0; vc < field.getWidth(); vc++) {
				if(vc != s - 1) listener.exclude(q, vc);
			}				
		} else if(ps.size() == 2) {
			Integer[] qs = (Integer[])ps.toArray(new Integer[2]);
			boolean same = inTheSameRegion(qs[0], qs[1]);
			for (int vc = 0; vc < s - colorCount - 1 && vc < colorCount; vc++) {
				listener.exclude(qs[0].intValue(), vc);
				listener.exclude(qs[1].intValue(), vc);
			}			
			for (int vc = s - 1; vc < colorCount; vc++) {
				if(vc < 0) continue;
				listener.exclude(qs[0].intValue(), vc);
				listener.exclude(qs[1].intValue(), vc);
			}			
			if(same && (s % 2 == 0) && s >= 2 && s <= 2 * colorCount ) {
				int middle = (s - 2) / 2;
				listener.exclude(qs[0].intValue(), middle);
				listener.exclude(qs[1].intValue(), middle);
			}
		}
	}

	public void remove(int p, int v, IRestrictionListener listener) {
		int index = partitioning[p];
		Set ps = points[index];
		int s = sums[index];
		if(ps.size() == 1) {
			int q = ((Integer)ps.iterator().next()).intValue();
			for (int vc = 0; vc < field.getWidth(); vc++) {
				if(vc != s - 1) listener.include(q, vc);
			}				
		} else if(ps.size() == 2) {
			Integer[] qs = (Integer[])ps.toArray(new Integer[2]);
			boolean same = inTheSameRegion(qs[0], qs[1]);
			for (int vc = 0; vc < s - colorCount - 1 && vc < colorCount; vc++) {
				listener.include(qs[0].intValue(), vc);
				listener.include(qs[1].intValue(), vc);
			}			
			for (int vc = s - 1; vc < colorCount; vc++) {
				if(vc < 0) continue;
				listener.include(qs[0].intValue(), vc);
				listener.include(qs[1].intValue(), vc);
			}			
			if(same && (s % 2 == 0) && s >= 2 && s <= 2 * colorCount ) {
				int middle = (s - 2) / 2;
				listener.include(qs[0].intValue(), middle);
				listener.include(qs[1].intValue(), middle);
			}
		}
		sums[index] += (v + 1);
		ps.add(new Integer(p));
	}

	public void dispose() {
		
	}

}
