package slava.puzzles.hexa.common;

import com.slava.common.RectangularField;

public class HexaField extends RectangularField {
	protected int length;
	protected int[] form;
 
	public HexaField() {
		setOrts(TRIANGULAR_ORTS);
	}
	
	public int getSideLength() {
		return length;
	}
	
	public void setSideLength(int length) {
		this.length = length;
		int w = 2 * length - 1;
		int s = w * w;
		form = new int[s];
		setSize(w, w);
		for (int i = 0; i < size; i++) {
			if(Math.abs(getX(i) - getY(i)) >= length) {
				form[i] = 0;
			} else {
				form[i] = 1;
			}
		}
		//reuse form
		setSize(w, w);
	}

	public int getIndex(int ix, int iy) {
		int p = super.getIndex(ix, iy);
		return isInField(p) ? p : -1;
	}
	
	public boolean isInField(int p) {
		return p >= 0 && p < size && form[p] == 1;
	}

}
