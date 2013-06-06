package ic2006_2;

import com.slava.common.TwoDimField;

public class SuperDamkaField extends TwoDimField {
	
	public SuperDamkaField() {
		setOrts(TwoDimField.DIAGONAL_ORTS);
	}
	
	public void setSize(int width) {
		super.setSize(width, width);
	}
	
	public String getDesignation(int p) {
		return "" + (char)(97 + x[p]) + (y[p] + 1);
	}
	
}
