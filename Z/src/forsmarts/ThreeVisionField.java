package forsmarts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreeVisionField extends BoomerangsField {
	
	public ThreeVisionField() {}

	public void makePlacements(int[] form, int[] dots) {
		this.form = form;
		this.dots = dots;
		List list = new ArrayList();
		for (int p = 0; p < size; p++) {
			if(form[p] == 0) continue;
			for (int d1 = 0; d1 < 6; d1++) {
				int p1 = jp[d1][p];
				if(p1 < 0 || form[p1] == 0) continue;
				for (int d2 = d1 + 1; d2 < 6; d2++) {
					int p2 = jp[d2][p];
					if(p2 < 0 || form[p2] == 0) continue;
					int[] placement = createPlacement(p, p1, p2);
					if(!isPlacementMatchingDots(placement, dots)) continue;
					if(!contains(list, placement)) {
						list.add(placement);
					}
				}
			}
		}
		placements = (int[][])list.toArray(new int[0][]);
//		System.out.println("-->" + placements.length);
	}
	
	private int[] createPlacement(int p, int p1, int p2) {
		int[] placement = new int[]{p,p1,p2};
		Arrays.sort(placement);
		return placement;
	}
	
	protected boolean contains(List list, int[] array) {
		for (int i = 0; i < list.size(); i++) {
			int[] a = (int[])list.get(i);
			if(equalArrays(a, array)) return true;
		}
		return false;
	}
	
	protected boolean equalArrays(int[] a1, int[] a2) {
		if(a1.length != a2.length) return false;
		for (int i = 0; i < a1.length; i++) {
			if(a1[i] != a2[i]) return false;
		}
		return true;
	}
	
	protected boolean isPlacementMatchingDots(int[] placement, int[] dots) {
		int[] state = new int[size];
		int[] border = new int[size];
		for (int i = 0; i < placement.length; i++) {
			int p = placement[i];
			state[p] = 1;
			for (int d = 0; d < 6; d++) {
				int q = jp[d][p];
				if(q < 0 || form[q] == 0) continue;
				border[q]++;
			}
		}
		for (int p = 0; p < size; p++) {
			if(border[p] > 0 && dots[p] >= 0 && state[p] == 0) return false;
		}
		return true;
	}
	
}
