package mozaika.validator;

import mozaika.IMozaikaValidator;

import com.slava.common.RectangularField;

/**
 * Check that grid is divided into 3 continuous colored regions.
 * If filling is not complete, state has values -1, in this case,
 * check that region of each given color can be made continuous
 * by replacing them with that color. 
 *  
 * @author slava
 *
 */
public class RegionsValidator implements IMozaikaValidator {
	RectangularField field;

	public RegionsValidator() {
	}

	public void setField(RectangularField field) {
		this.field = field;
	}

	public boolean isValid(int[] state) {
		for (int value = 0; value < 3; value++) {
			if(!isContinuous(state, value)) {
				return false;
			}
		}
		return true;
	}

	boolean isContinuous(int[] state, int value) {
		int[] visited = new int[state.length];
		int[] stack = new int[state.length];
		int regionCount = 0;
		for (int p = 0; p < state.length; p++) {
			if(state[p] != value) continue;
			if(visited[p] > 0) continue;
			regionCount++;
			int c = 0;
			int v = 1;
			stack[0] = p;
			visited[p] = 1;
			while(c < v) {
				int q = stack[c];
				for (int d = 0; d < 4; d++) {
					int r = field.jump(q, d);
					if(r >= 0 && (state[r] == value || state[r] == -1) 
							&& visited[r] == 0) {
						stack[v] = r;
						visited[r] = 1;
						v++;
					}
				}
				c++;
			}
		}
		
		return regionCount < 2;
	}

}
