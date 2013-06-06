package mozaika.validator;

import mozaika.IMozaikaValidator;

import com.slava.common.RectangularField;

public class TetraminoValidator implements IMozaikaValidator {
	RectangularField field;
	RectangularField octaField;

	public TetraminoValidator() {}

	public void setField(RectangularField f) {
		field = f;
		octaField = new RectangularField();
		octaField.setOrts(RectangularField.DIAGONAL_ORTS);
		octaField.setSize(f.getWidth(), f.getHeight());
	}

	public boolean isValid(int[] state) {
		for (int p = 0; p < state.length; p++) {
			if(!checkCell(p, state)) {
				return false;
			}
		}

		return checkFigures(state);
	}

	boolean checkCell(int p, int[] state) {
		if(state[p] != 1) return true;
		int n = 0;
		boolean bordersUnfilled = false;
		for (int d = 0; d < 4; d++) {
			int q = field.jump(p, d);
			if(q < 0 || state[q] == 0) continue;
			if(state[q] < 0) {
				bordersUnfilled = true;
			} else {
				n++;
			}
		}
		if(n < 1 && !bordersUnfilled) return false;
		if(n > 3) return false;
		for (int d = 1; d < 8; d += 2) {
			int q = octaField.jump(p, d);
			if(q < 0 || state[q] != 1) continue;
			int q1 = octaField.jump(p, d - 1);
			if(q1 >= 0 && state[q1] < 0) continue;
			boolean b1 = (q1 >= 0 && state[q1] == 1);
			q1 = octaField.jump(p, d == 7 ? 0 : d + 1);
			if(q1 >= 0 && state[q1] < 0) continue;
			boolean b2 = (q1 >= 0 && state[q1] == 1);
			if(!b1 && !b2) {
				return false;
			}
		}
		return true;
	}

	boolean checkFigures(int[] state) {
		int[] visited = new int[state.length];
		int[] stack = new int[state.length];
		int regionCount = 0;
		for (int p = 0; p < state.length; p++) {
			if(state[p] != 1) continue;
			if(visited[p] > 0) continue;
			regionCount++;
			boolean bordersUnfilled = false;
			int c = 0;
			int v = 1;
			stack[0] = p;
			visited[p] = 1;
			while(c < v) {
				int q = stack[c];
				for (int d = 0; d < 4; d++) {
					int r = field.jump(q, d);
					if(r < 0) continue;
					if(state[r] < 0) {
						bordersUnfilled = true;
						continue;
					}
					if(state[r] == 1 && visited[r] == 0) {
						stack[v] = r;
						visited[r] = 1;
						v++;
					}
				}
				c++;
			}
			if(v < 4 && !bordersUnfilled) {
				return false;
			}
			if(v > 4) {
				return false;
			}
		}
		
		return true;
	}
}
