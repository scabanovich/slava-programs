package forsmarts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreeOField extends ThreeVisionField {
	static int[][] FIGURE = {
		{0,0}, {1,0}, {2,0}, {0,1}, {3,1}, {0,2}, {4,2}, {1,3}, {4,3}, {2,4}, {3,4}, {4,4}
	};
	
	public int[][] placementsByCell;

	public void makePlacements(int[] form, int[] dots) {
		this.form = form;
		this.dots = dots;
		List list = new ArrayList();
		for (int p = 0; p < size; p++) {
			if(form[p] == 0) continue;
			int[] placement = createPlacement(p);
			if(placement == null) continue;
			if(!contains(list, placement)) {
				list.add(placement);
			}
		}
		placements = (int[][])list.toArray(new int[0][]);
		makePlacementsByCell();
	}
	
	private int[] createPlacement(int p) {
		int[] placement = new int[FIGURE.length];
		for (int i = 0; i < FIGURE.length; i++) {
			int q = jumpXY(p, FIGURE[i][0], FIGURE[i][1]);
			if(q < 0 || form[q] == 0) return null;
			placement[i] = q;
		}
		Arrays.sort(placement);
		return placement;
	}
	
	private void makePlacementsByCell() {
		placementsByCell = new int[getSize()][];
		for (int p = 0; p < placementsByCell.length; p++) {
			if(form[p] == 0 || dots[p] == 1) continue;
			int c = 0;
			for (int i = 0; i < placements.length; i++) {
				if(contains(placements[i], p)) c++;
			}
			placementsByCell[p] = new int[c];
			c = 0;
			for (int i = 0; i < placements.length; i++) {
				if(contains(placements[i], p)) {
					placementsByCell[p][c] = i;
					c++;
				}
			}
		}
	}
	
	private boolean contains(int[] placement, int p) {
		for (int i = 0; i < placement.length; i++) {
			if(placement[i] == p) return true;
		}
		return false;
	}
	
}