package match2005;

import com.slava.common.RectangularField;

public class PentapipeField extends RectangularField {
	int[] rotation;
	int[] reflection;

	public void setSize(int width) {
		super.setSize(width, width);
		rotation = new int[getSize()];
		reflection = new int[getSize()];
		for (int i = 0; i < getSize(); i++) {
			reflection[i] = getIndex(getY(i), getX(i));
			rotation[i] = getIndex(getHeight() - 1 - getY(i), getX(i));
		}
	}
	
	public int[] rotation() {
		return rotation;
	}
	
	public int[] reflection() {
		return reflection;
	}

}
