package champ2013;

import com.slava.common.RectangularField;

public class ThreeDigitNumbers2 extends AbstractThreeDigitNumbers {
	
	int[] colorTransform = {0,1,3,4,5,2,7,8,9,6};
//	int[] xTransform;
//	int[] yTransform;
	int[] rTransform;

	public ThreeDigitNumbers2() {}
	
	public void setField(RectangularField f) {
		field = f;
		initTransforms();
	}

	void initTransforms() {
//		xTransform = new int[field.getSize()];
//		yTransform = new int[field.getSize()];
		rTransform = new int[field.getSize()];
		for (int p = 0; p < rTransform.length; p++) {
			int x = field.getX(p), y = field.getY(p);
//			xTransform[p] = field.getIndex(field.getWidth() - x - 1, y);
//			yTransform[p] = field.getIndex(x, field.getHeight() - y - 1);
			rTransform[p] = field.getIndex(field.getWidth() - y - 1, x);
		}
	}

	protected void init() {
		super.init();
		place = new int[]{
			field.getIndex(4, 4), field.getIndex(4, 3), field.getIndex(3, 4), field.getIndex(3, 3),
			field.getIndex(4, 2), field.getIndex(2, 4),
			field.getIndex(3, 2), field.getIndex(2, 3), field.getIndex(2, 2),
			field.getIndex(4, 1), field.getIndex(1, 4), field.getIndex(3, 1), field.getIndex(1, 3),
			field.getIndex(2, 1), field.getIndex(1, 2), //field.getIndex(1, 1),
			field.getIndex(4, 0), /*field.getIndex(0, 4),*/ field.getIndex(3, 0),  field.getIndex(2, 0),
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
		int p1 = rTransform[p];
		int v1 = colorTransform[v];
		add(p1, v1);
		int p2 = rTransform[p1];
		int v2 = colorTransform[v1];
		add(p2, v2);
		int p3 = rTransform[p2];
		int v3 = colorTransform[v2];
		add(p3, v3);
	}

	protected void removeWithSimmetric(int p, int v) {
		int p1 = rTransform[p];
		int v1 = colorTransform[v];
		int p2 = rTransform[p1];
		int v2 = colorTransform[v1];
		int p3 = rTransform[p2];
		int v3 = colorTransform[v2];
		remove(p3, v3);
		remove(p2, v2);
		remove(p1, v1);
		remove(p, v);
	}

	protected boolean isSolution() {
		return differentNumbers == 252;
	}

	public static void main(String[] args) {
		RectangularField f = new RectangularField();
		f.setSize(10, 10);
		while(true) {
		ThreeDigitNumbers2 p = new ThreeDigitNumbers2();
		p.setField(f);
		p.solve();
		if(p.bestDiff == 252) break;
		}
	}
}

/**
 * 

 + + + 9 4 + 3 + + +
 + + 2 7 8 1 5 4 + +
 + 3 1 4 3 6 7 1 3 +
 2 4 6 8 2 4 9 5 8 6
 6 1 9 3 6 7 3 4 9 5
 3 7 2 5 9 8 5 7 1 8
 8 6 3 7 2 4 6 8 2 4
 + 5 1 9 8 5 2 1 5 +
 + + 2 3 1 6 9 4 + +
 + + + 5 + 2 7 + + +
2,3,4,5 * 9; 1,6,8 * 8; 7,9 * 7


252
 + + + 6 5 3 + + + +
 + + 3 1 8 2 6 8 + +
 + 7 8 5 9 4 1 9 4 +
 + 9 1 4 6 8 5 2 1 7
 2 5 3 7 5 2 7 6 9 2
 4 7 8 9 4 3 9 5 3 4
 9 1 4 3 6 8 2 1 7 +
 + 2 7 1 2 7 3 6 9 +
 + + 6 8 4 6 1 5 + +
 + + + + 5 3 8 + + +


---------------------------------
238
 + + + + + + + + + +
 + 3 1 2 6 4 5 2 4 +
 + 5 8 3 9 2 6 9 1 +
 + 4 9 1 5 7 1 4 3 +
 + 3 5 6 8 9 2 6 7 +
 + 9 8 4 7 6 8 3 5 +
 + 5 2 1 9 3 1 7 2 +
 + 1 7 8 4 7 5 6 3 +
 + 2 4 3 2 8 4 1 5 +
 + + + + + + + + + +

 237 258 268 329 348 379 436 459 486 526 547 597 618 719

238
 + + + + + + + + + +
 + 6 1 9 4 7 8 9 7 +
 + 8 2 6 3 9 4 3 1 +
 + 7 3 1 8 5 1 7 6 +
 + 6 8 4 2 3 9 4 5 +
 + 3 2 7 5 4 2 6 8 +
 + 8 9 1 3 6 1 5 9 +
 + 1 5 2 7 5 8 4 6 +
 + 9 7 6 9 2 7 1 8 +
 + + + + + + + + + +

*/