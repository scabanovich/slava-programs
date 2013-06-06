package mozaika.validator;

import mozaika.IMozaikaValidator;

import com.slava.common.RectangularField;

public class ColoredPillsValidator implements IMozaikaValidator {
	int[][] pairs = {
		{11,12}, {14,15},
		{19, 28}, {22,31}, {25,34},
		{38,39}, {41,42},
		{46,55}, {49,58},{52,61},
		{65,66}, {68,69}
	};

	public ColoredPillsValidator() {}

	public void setField(RectangularField f) {
		//init pairs here
	}

	public boolean isValid(int[] state) {
		for (int i = 0; i < pairs.length; i++) {
			int v1 = state[pairs[i][0]];
			int v2 = state[pairs[i][1]];
			if(v1 > 0 && v2 > 0 && v1 != v2) {
				return false;
			}
		}
		return true;
	}

}
