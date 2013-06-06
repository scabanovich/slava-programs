package slava.puzzle.pentaletters.analysis;

import java.util.*;

public class PentaLettersLogicalAnalyzer extends AbstractPentaLettersAnalyzer {
	boolean logicallySolvable = false;
	boolean unsolvable = false;

	List figurePlacements = new ArrayList();
	// Number of figures involving cell i.
	int[] cellDistribution;
	int[][] pairDistribution;
	// Equals 1 if unique placement is attached to the cell.
	int[] settled;
	int[][] settledPairs;
	int groupAssigned = 0;
	int groupSettled = 0;
	
	StringBuffer solution = new StringBuffer();	

	public boolean isLogicallySolvable() {
		return logicallySolvable;
	}
	
	public void solve() {
		check();
		init();
		anal();
	}
	
	protected void init() {
		figureCount = field.getSize() / figureSize;
		field.resetGroups();
		groups = (int[])field.getGroups().clone();
		groupAssigned = 0;
		groupSettled = 0;
		logicallySolvable = false;
		unsolvable = false;
		solutions.clear();
		solution.setLength(0);
		
		cellDistribution = new int[field.getSize()];
		pairDistribution = new int[field.getSize()][field.getSize()];
		settled = new int[field.getSize()];
		settledPairs = new int[field.getSize()][field.getSize()];
		for (int i = 0; i < settled.length; i++) {
			cellDistribution[i] = 0;
			for (int j = 0; j < settled.length; j++) {
				pairDistribution[i][j] = 0;
				settledPairs[i][j] = 0;
				if(i == j) settledPairs[i][j] = 1;
			} 
			settled[i] = 0;
		}
		initFigurePlacements();
	}
	
	void initFigurePlacements() {
		figurePlacements.clear();
		for (int p = 0; p < field.getSize(); p++) {
			for (int f = 0; f < figures.length; f++) {
				int[] placement = createPlacement(f, p);
				if(placement == null) continue;
				figurePlacements.add(placement);
				for (int i = 0; i < placement.length; i++) {
					cellDistribution[placement[i]]++;
					for (int j = 0; j < placement.length; j++) {
						if(i != j) pairDistribution[placement[i]][placement[j]]++;
					}
				} 
			}
		}
	}
	
	void reduceFigurePlacements() {
		Iterator it = figurePlacements.iterator();
		while(it.hasNext()) {
			int[] placement = (int[])it.next();
			if(isAcceptablePlacement(placement)) continue;
			it.remove();
			removePlacement(placement);
		}
	}
	
	int getNarrowCell() {
		int p = -1;
		int q = 1000;
		for (int i = 0; i < cellDistribution.length; i++) {
			if(settled[i] == 1) continue;
			if(cellDistribution[i] >= q) continue;
			q = cellDistribution[i];
			p = i;
		}
		return p;
	}
	
	boolean isAcceptablePlacement(int[] placement) {
		int g = -1;
		for (int i = 0; i < placement.length; i++) {
			int gi = groups[placement[i]];
			if(gi == -1) continue;
			if(gi != g/* && g >= 0*/) return false;
			g = gi;
		}
		return true;
	}
	
	/*
	 * This method decides if figure can be placed at position p
	 */
	
	protected int[] createPlacement(int f, int p) {
		int[] temp = new int[figureSize];
		for (int i = 0; i < temp.length; i++) temp[i] = 0;
		int[] placement = new int[figureSize];
		for (int i = 0; i < figures[f].length; i++) {
			int k = field.jump(p, figures[f][i][0], figures[f][i][1]);
			if(k < 0) return null;
			int v = field.getLetterAt(k);
			if(temp[v] > 0) return null;
			temp[v]++;
			placement[i] = k;
		}
		return placement;
	}

	protected void anal() {
		while(true) {
			reduceUniqueGroups();
			if(groupSettled == figureCount) {
				logicallySolvable = true;
				solutions.add(groups.clone());
				return;
			}
			int p = getNarrowCell();
			int q = cellDistribution[p];
			if(q == 0) {
				solution.append("Unsolvable " + present(p)).append('\n');
				unsolvable = true;
				return;
			}
			if(!reduceSeparatePairs() && !reducePair()) {
				solutions.add(groups.clone());
				return;
			} 
		}
	}
	
	boolean reduceUniqueGroups() {
		int p = getNarrowCell();
		if(p < 0) return true;
		int q = cellDistribution[p];
		if(q > 1) return false;
		while(q == 1) {
			int[] placement = findPlacement(p);
			int g = getGroup(placement);
			if(g < 0) {
				g = groupAssigned;
				groupAssigned++;
			}
			solution.append("Unique " + present(p)).append('\n');
			setGroup(placement, g);
			setSettled(placement);
			reduceFigurePlacements();
			p = getNarrowCell();
			if(p < 0) return true;
			q = cellDistribution[p];			
		}
		return true;
	}
	
	boolean reducePair() {
		for (int i = 0; i < settled.length; i++) {
			if(settled[i] == 1) continue;
			for (int j = 0; j < settled.length; j++) {
				if(settled[j] == 1) continue;
				if(settledPairs[i][j] == 1) continue;
				if(cellDistribution[i] > pairDistribution[i][j]) continue;
				if(groups[i] == groups[j] && groups[i] >= 0) continue;
				settledPairs[i][j] = 1;
				settledPairs[j][i] = 1;
				if(joinPair(i, j) > 0) {
					solution.append("Join " + present(i) + " and " + present(j)).append('\n');
					reduceFigurePlacements();
					return true;
				}
			}
		}
		return false;
	}
	
	int joinPair(int p1, int p2) {
		int q = 0;
		Iterator it = figurePlacements.iterator();
		while(it.hasNext()) {
			int[] placement = (int[])it.next();
			if(!containsOneOf(placement, p1, p2)) continue;
			it.remove();
			removePlacement(placement);
			q++;
		}
		return q;
	}
	
	boolean contains(int[] placement, int p) {
		for (int i = 0; i < placement.length; i++) {
			if(placement[i] == p) return true;
		}
		return false;
	}

	int getContainedCount(int[] placement, int p1, int p2) {
		int q = 0;
		for (int i = 0; i < placement.length; i++) {
			if(placement[i] == p1 || placement[i] == p2) ++q;
		}
		return q;
	}
	
	boolean containsOneOf(int[] placement, int p1, int p2) {
		return getContainedCount(placement, p1, p2) == 1;
	}
	
	int[] findPlacement(int p) {
		Iterator it = figurePlacements.iterator();
		while(it.hasNext()) {
			int[] placement = (int[])it.next();
			for (int i = 0; i < placement.length; i++) {
				if(placement[i] == p) return placement;
			}
		}
		return null;
	}

	boolean reduceSeparatePairs() {
		for (int p1 = 0; p1 < settled.length; p1++) {
			if(settled[p1] == 1) continue;
			for (int d = 0; d < 2; d++) {
				int p2 = field.jump(p1, d);
				if(p2 < 0 || settled[p2] == 1) continue;
				if(settledPairs[p1][p2] == 1) continue;
				if(pairDistribution[p1][p2] == 0) continue;
				for (int p0 = 0; p0 < settled.length; p0++) {
					if(settled[p0] == 1) continue;
					if(cellDistribution[p0] != pairDistribution[p0][p1] + pairDistribution[p0][p2]) continue;
					if(!isSeparatingPair(p0, p1, p2)) continue;
					settledPairs[p1][p2] = 1;
					settledPairs[p2][p1] = 1;
					solution.append("Separate " + present(p1) + " and " + present(p2) + " for " + present(p0)).append('\n');
					separatePair(p1, p2);
					return true;			
				}
			}
		}
		return false;
	}
	
	boolean isSeparatingPair(int p0, int p1, int p2) {
		Iterator it = figurePlacements.iterator();
		while(it.hasNext()) {
			int[] placement = (int[])it.next();
			for (int i = 0; i < placement.length; i++) {
				if(!contains(placement, p0)) continue;
				if(!containsOneOf(placement, p1, p2)) return false;
			}
		}
		return true;
	}
	
	int separatePair(int p1, int p2) {
		int q = 0;
		Iterator it = figurePlacements.iterator();
		while(it.hasNext()) {
			int[] placement = (int[])it.next();
			if(getContainedCount(placement, p1, p2) < 2) continue;
			it.remove();
			removePlacement(placement);
			q++;
		}
		return q;
	}
	
	int getGroup(int[] placement) {
		for (int i = 0; i < placement.length; i++) {
			int g = groups[placement[i]];
			if(g != -1) return g;
		}
		return -1;
	}
	
	void setGroup(int[] placement, int g) {
		for (int i = 0; i < placement.length; i++) {
			groups[placement[i]] = g;
		}
	}
	
	void setSettled(int[] placement) {
		for (int i = 0; i < placement.length; i++) settled[placement[i]] = 1;
		groupSettled++;
	}
	
	void removePlacement(int[] placement) {
		for (int i = 0; i < placement.length; i++) {
			cellDistribution[placement[i]]--;
			for (int j = 0; j < placement.length; j++) {
				if(i != j) pairDistribution[placement[i]][placement[j]]--;
			}
		}
	}
	
	String present(int i) {
		return field.x(i) + ":" + field.y(i);
	}
	
	public String getSolution() {
		return solution.toString();
	}

}
