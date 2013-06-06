package champ2013;

import com.slava.common.RectangularField;

public class ThreeDigitNumbers3 extends AbstractThreeDigitNumbers {
	
	int[] colorTransform = {0,1,3,2,5,4,7,6,9,8};
	int[] xTransform;

	public ThreeDigitNumbers3() {}
	
	public void setField(RectangularField f) {
		field = f;
		initTransforms();
	}

	void initTransforms() {
		xTransform = new int[field.getSize()];
		for (int p = 0; p < xTransform.length; p++) {
			int x = field.getX(p), y = field.getY(p);
			xTransform[p] = field.getIndex(field.getWidth() - x - 1, y);
		}
	}

	protected void init() {
		super.init();
		place = new int[state.length / 2];
		int k = 0;
		for (int ix = 3; ix >= 0; ix--) {
			for (int iy = 0; iy < field.getHeight(); iy++) {
				place[k] = field.getIndex(ix, iy);
				k++;
			}
		}
	}

	protected void srch() {
		wayCount[t] = 0;
		if(t == place.length) return;
		if(t > 0 && hasNeighbour(place[t - 1], state[place[t - 1]])) return;
		
		if(t == 16 && differentNumbers < 118) {
			return;
		}
		if(t == 24 && differentNumbers < 184) {
			return;
		}

		int p = place[t];
		int c = 0;
		for (int v = 1; v <= 9; v++) {
			int d = differentNumbers;
			addWithSimmetric(p, v);
			int delta = differentNumbers - d;
			removeWithSimmetric(p, v);
			if(delta > 0 || t < 1) {
				cs[c] = delta;
				ws[c] = v;
				c++;
			}
		}

		randomize0(c);
		sort(c);
		if(t < 16 && c > 3) c = 3;
		if(t < 32 && c > 4) c = 4;
		
		for (int i = 0; i < c; i++) {
			ways[t][i] = ws[i];
		}
		wayCount[t] = c;
		randomize();
	}

	boolean hasNeighbour(int p, int v) {
		for (int d = 0; d < 4; d++) {
			int q = field.jump(p, d);
			if(q >= 0 && state[q] == v) {
				return true;
			}
			for (int d1 = 0; d1 < 4; d1++) {
				int r = field.jump(q, d1);
				if(r != p && r > 0 && state[r] == v) return true;
			}
		}
		return false;
	}

	protected void addWithSimmetric(int p, int v) {
		add(p, v);
		int p1 = xTransform[p];
		int v1 = colorTransform[v];
		add(p1, v1);
	}

	protected void removeWithSimmetric(int p, int v) {
		int p1 = xTransform[p];
		int v1 = colorTransform[v];
		remove(p1, v1);
		remove(p, v);
	}

	protected boolean shouldBreak() {
		if(treeCount > 100000 && bestDiff < 238) {
			System.out.println("---" + bestDiff);
			return true;
		}
		if(treeCount > 10000000) {
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
		f.setSize(8, 9);
		while(true) {
		ThreeDigitNumbers3 p = new ThreeDigitNumbers3();
			p.bestDiff = 234;
			p.setField(f);
			p.solve();
			if(p.bestDiff == 252) break;
		}
	}
}

/**



*/