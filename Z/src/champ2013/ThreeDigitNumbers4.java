package champ2013;

import com.slava.common.RectangularField;

public class ThreeDigitNumbers4 extends AbstractThreeDigitNumbers {
	
	int[] xcolorTransform = {0,1,3,2,5,4,7,6,9,8};
	int[] ycolorTransform = {0,1,4,5,2,3,8,9,6,7};
	int[] xTransform;
	int[] yTransform;

	public ThreeDigitNumbers4() {}
	
	public void setField(RectangularField f) {
		field = f;
		initTransforms();
	}

	void initTransforms() {
		xTransform = new int[field.getSize()];
		yTransform = new int[field.getSize()];
		for (int p = 0; p < xTransform.length; p++) {
			int x = field.getX(p), y = field.getY(p);
			xTransform[p] = field.getIndex(field.getWidth() - x - 1, y);
			yTransform[p] = field.getIndex(x, field.getHeight() - y - 1);
		}
	}

	protected void init() {
		super.init();
		place = new int[]{
			field.getIndex(4, 4), field.getIndex(4, 3), field.getIndex(3, 4), field.getIndex(3, 3),
			field.getIndex(4, 2), field.getIndex(2, 4),
			field.getIndex(3, 2), field.getIndex(2, 3), field.getIndex(2, 2),
			field.getIndex(4, 1), field.getIndex(1, 4), field.getIndex(3, 1), field.getIndex(1, 3),
			field.getIndex(2, 1), field.getIndex(1, 2), field.getIndex(1, 1),
			field.getIndex(4, 0), /*field.getIndex(0, 4),*/ field.getIndex(3, 0),  //field.getIndex(2, 0),
		};
	}

	protected void srch() {
		wayCount[t] = 0;
		if(t == place.length) return;
		int p = place[t];
		int c = 0;
		for (int v = 1; v <= 9; v++) {
			int d = differentNumbers;
			addWithSimmetric(p, v);
			int delta = differentNumbers - d;
			removeWithSimmetric(p, v);
			if(delta > 0) {
				cs[c] = delta;
				ws[c] = v;
				c++;
			}
		}

		randomize0(c);
		sort(c);
		if(t < 7 && c > 3) c = 3;
		if(t < 19 && c > 4) c = 4;
		
		for (int i = 0; i < c; i++) {
			ways[t][i] = ws[i];
		}
		wayCount[t] = c;
		randomize();
	}

	protected void addWithSimmetric(int p, int v) {
		add(p, v);
		int p1 = xTransform[p];
		int v1 = xcolorTransform[v];
		add(p1, v1);
		int p2 = yTransform[p1];
		int v2 = ycolorTransform[v1];
		add(p2, v2);
		int p3 = xTransform[p2];
		int v3 = xcolorTransform[v2];
		add(p3, v3);
	}

	protected void removeWithSimmetric(int p, int v) {
		int p1 = xTransform[p];
		int v1 = xcolorTransform[v];
		int p2 = yTransform[p1];
		int v2 = ycolorTransform[v1];
		int p3 = xTransform[p2];
		int v3 = xcolorTransform[v2];
		remove(p3, v3);
		remove(p2, v2);
		remove(p1, v1);
		remove(p, v);
	}

	protected boolean shouldBreak() {
		if(treeCount > 200000 && bestDiff < 238) {
			System.out.println("---");
			return true;
		}
		if(treeCount > 2000000) {
			System.out.println("---");
			return true;
		}
		return false;
	}

	protected boolean isSolution() {
		return differentNumbers == 252;
	}

	public static void main(String[] args) {
		RectangularField f = new RectangularField();
		f.setSize(10, 10);
		while(true) {
		ThreeDigitNumbers4 p = new ThreeDigitNumbers4();
		p.bestDiff = 236;
		p.setField(f);
		p.solve();
		if(p.bestDiff == 252) break;
		}
	}
}

/**
 * 


*/