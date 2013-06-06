package slava.puzzle.avoidfour.model;

public class AvoidFourPuzzle {
	public static int EMPTY_VALUE = 0;
	public static int CROSS_VALUE = 1;
	public static int OSIGN_VALUE = 2;
	public static int ERROR_VALUE = 3;

	AvoidFourModel model;
	AvoidFourField field;
	int size;
	
	int[] filter; // 0 - cell does not and 1 - does belong to the field
	int[] puzzle; // 0 - cell is not and 1 is a part of the puzzle condition. 
	int[] values; // 0 - value is not set,
                  // 1 - "o" and 2 - "x": puzzle values plus forced values
                  // 3 - both "o" and "x" is forced, puzzle is erroneous;
	int[] history; // for each filled cell value is move number, default: -1;
	
	int[] valueCount = new int[4];
	
	int currentMove;
	
	public void setModel(AvoidFourModel model) {
		this.model = model;
	}
	
	public void init() {
		field = model.getField();
		size = field.getSize();
		if(filter == null || filter.length != size) {
			filter = new int[size];
			puzzle = new int[size];
			values = new int[size];
			history = new int[size];
		}
		for (int i = 0; i < size; i++) {
			filter[i] = 0;
		}
		clearPuzzle();
	}
	
	public void clearPuzzle() {
		for (int i = 0; i < size; i++) {
			puzzle[i] = 0;
			values[i] = 0;
			history[i] = -1;
		}
		currentMove = 0;
		for (int i = 0; i < 4; i++) valueCount[i] = 0;
	}
	
	public boolean isField(int p) {
		return p >= 0 && p < size && filter[p] == 1;
	}
	
	public int[] getFilter() {
		return filter;
	}
	
	public boolean isCondition(int p) {
		return p >= 0 && p < size && puzzle[p] == 1;
	}
	
	public int[] getCondition() {
		return puzzle;
	}
	
	int[] getHistory() {
		return history;
	}
	
	public int getValue(int p) {
		return (p >= 0 && p < size) ? values[p] : 0;
	}
	
	public int[] getValues() {
		return values;
	}
	
	public boolean isErroneous() {
		return valueCount[ERROR_VALUE] > 0;
	}
	
	public void doMove(int p, int value) {
		if(!isField(p) || isErroneous() || values[p] > EMPTY_VALUE 
			|| value <= EMPTY_VALUE || value >= ERROR_VALUE) return;
		puzzle[p] = 1;
		values[p] = value;
		valueCount[value]++;
		valueCount[EMPTY_VALUE]--;
		history[p] = currentMove;
		fillForcedCells();
		currentMove++;
	}
	
	public void undoMove() {
		if(currentMove == 0) return;
		int lastMove = currentMove - 1;
		for (int i = 0; i < size; i++) {
			if(history[i] == lastMove) {
				puzzle[i] = 0;
				history[i] = -1;
				if(values[i] != 0) {
					valueCount[values[i]]--;
					valueCount[EMPTY_VALUE]++;
					values[i] = EMPTY_VALUE;
				}
			}
		}
		currentMove--;
	}
	
	void fillForcedCells() {
		int empty = valueCount[EMPTY_VALUE];
		while(true) {
			for (int p = 0; p < size; p++) {
				if(!isField(p) || values[p] != EMPTY_VALUE) continue;
				int fv = getForcedValue(p);
				if(fv == EMPTY_VALUE) continue;
				puzzle[p] = 0;
				values[p] = fv;
				valueCount[fv]++;
				valueCount[EMPTY_VALUE]--;
				history[p] = currentMove;
			}
			if(empty == valueCount[EMPTY_VALUE]) break;
			empty = valueCount[EMPTY_VALUE];
		}
	}
	
	int getForcedValue(int p) {
		if(values[p] != EMPTY_VALUE) return EMPTY_VALUE;
		int rv = EMPTY_VALUE;
		for (int v = CROSS_VALUE; v < ERROR_VALUE; v++) {
			for (int d = 0; d < 4; d++) {
				int s1 = 0;
				int q = field.jp(d, p);
				while(q >= 0 && values[q] == v) {
					++s1;
					q = field.jp(d, q);
				}
				int d1 = d + 4;
				int s2 = 0;
				q = field.jp(d1, p);
				while(q >= 0 && values[q] == v) {
					++s2;
					q = field.jp(d1, q);
				}
				int s = s1 + s2;
				if(s1 > 3 || s2 > 3) {
					rv = ERROR_VALUE;
				} else if(s >= 3) {
					int v1 = (v == CROSS_VALUE) ? OSIGN_VALUE : (v == OSIGN_VALUE) ? CROSS_VALUE : EMPTY_VALUE;
					rv = rv | v1;
				} 
			}
		}
		return rv;
	}
	
	public int getMoveCount() {
		return currentMove;
	}
	
	public int getValueCount(int v) {
		return (v < EMPTY_VALUE || v > ERROR_VALUE) ? -1 : valueCount[v];
	}
	
	public int[] getValueCounts() {
		return valueCount;
	}
 
}
