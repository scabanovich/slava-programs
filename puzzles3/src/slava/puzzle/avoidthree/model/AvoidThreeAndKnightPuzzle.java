package slava.puzzle.avoidthree.model;

public class AvoidThreeAndKnightPuzzle {
	public static int EMPTY_VALUE = 0;
	public static int ERROR_VALUE = 1;
	public static int A_VALUE = 2;
	public static int B_VALUE = 3;
	public static int C_VALUE = 4;
	public static int D_VALUE = 5;

	int maxValue = D_VALUE;

	AvoidThreeAndKnightModel model;

	int size;
	
	int[] filter; // 0 - cell does not and 1 - does belong to the field
	int[] puzzle; // 0 - cell is not and 1 is a part of the puzzle condition. 
	int[] values; // 0 - value is not set, 1 - all values are forced, puzzle is erroneous,
                  // 2,3, etc - possible values;
	int[] history; // for each filled cell value is move number, default: -1;
	
	int[] valueCount = new int[maxValue + 1];
	
	int currentMove;
	
	public AvoidThreeAndKnightPuzzle() {}
	
	public void setModel(AvoidThreeAndKnightModel model) {
		this.model = model;		
	}
	
	public void init() {
		size = model.getField().getSize();
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
	
	public int getMaxValue() {
		return maxValue;
	}

	public void clearPuzzle() {
		for (int i = 0; i < size; i++) {
			puzzle[i] = 0;
			values[i] = EMPTY_VALUE;
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
			|| value <= EMPTY_VALUE || value == ERROR_VALUE || value > maxValue) return;
		if(!check(p, value)) return;
		puzzle[p] = 1;
		values[p] = value;
		valueCount[value]++;
		valueCount[EMPTY_VALUE]--;
		history[p] = currentMove;
		fillForcedCells();
		currentMove++;
	}
	
	boolean check(int p, int value) {
		int[] os = createOpportunities(p);
		if(os == null || os[value] != 0) return false;
		return true;
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
				int[] os = createOpportunities(p);
				int fv = getForcedValue(os);
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
	
	int[] createOpportunities(int p) {
		int[] os = new int[maxValue + 1];
		if(filter[p] == 0) return null;
		if(values[p] != EMPTY_VALUE) return null;
		for (int d = 0; d < 8; d++) {
			int q = model.getKnightField().jump(p, d);
			if(q < 0 || filter[q] == 0) continue;
			os[values[q]] = 1;
		}
		for (int d = 0; d < 4; d++) {
			int q1 = model.getField().jump(p, d);
			int q2 = model.getField().jump(p, d + 4);
			int v1 = (q1 < 0 || filter[q1] == 0) ? 0 : values[q1];
			int v2 = (q2 < 0 || filter[q2] == 0) ? 0 : values[q2];
			if(v1 == v2 && v1 > ERROR_VALUE) {
				os[v1] = 1;
				continue;
			}
			if(v1 > ERROR_VALUE) {
				int qq = model.getField().jump(q1, d);
				if(qq >= 0 && filter[qq] == 1 && values[qq] == v1) {
					os[v1] = 1;
				}
			}
			if(v2 > ERROR_VALUE) {
				int qq = model.getField().jump(q2, d + 4);
				if(qq >= 0 && filter[qq] == 1 && values[qq] == v2) {
					os[v2] = 1;
				}
			}
		}
		return os;
	}
	
	int getForcedValue(int[] os) {
		int fv = ERROR_VALUE;
		for (int i = A_VALUE; i <= maxValue; i++) {
			if(os[i] == 0) {
				if(fv != ERROR_VALUE) return EMPTY_VALUE;
				fv = i;
			}
		}
		return fv;
	}

	public int getMoveCount() {
		return currentMove;
	}
	
	public int getValueCount(int v) {
		return (v < EMPTY_VALUE || v > maxValue) ? -1 : valueCount[v];
	}
	
	public int[] getValueCounts() {
		return valueCount;
	}
 
}
