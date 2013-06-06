package forsmarts;

import java.util.*;

import com.slava.common.TwoDimField;

public class BoomerangsField extends TwoDimField {
	protected int[] form;
	protected int[] dots;
	
	protected int[][] placements;
	
	public BoomerangsField() {
		setOrts(TwoDimField.TRIANGULAR_ORTS);
	}

	public void makePlacements(int[] form, int[] dots) {
		this.form = form;
		this.dots = dots;
		List list = new ArrayList();
		for (int p = 0; p < size; p++) {
			if(form[p] == 0) continue;
			for (int d1 = 0; d1 < 6; d1++) {
				int d2 = (d1 + 2) % 6;
				int dotCount = dots[p];
				int p1 = p;
				int p2 = p;
				int l = 0;
				while(p1 >= 0 && p2 >= 0 && dotCount < 2) {
					++l;
					p1 = jp[d1][p1];
					p2 = jp[d2][p2];
					if(p1 < 0 || p2 < 0) break;
					dotCount += dots[p1] + dots[p2];
					if(dotCount == 1) list.add(createPlacement(p, d1, d2, l));
				}
			}
		}
		placements = (int[][])list.toArray(new int[0][]);
	}
	
	private int[] createPlacement(int p, int d1, int d2, int length) {
		int[] placement = new int[2 * length + 1];
		placement[0] = p;
		int p1 = p;
		int p2 = p;
		for (int i = 0; i < length; i++) {
			p1 = jp[d1][p1];
			placement[2 * i + 1] = p1;
			p2 = jp[d2][p2];
			placement[2 * i + 2] = p2;
		}
		return placement;
	}

}
