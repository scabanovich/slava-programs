package mozaika;

import com.slava.common.RectangularField;

public interface IMozaikaValidator {
	public void setField(RectangularField field);
	public boolean isValid(int[] state);
}
