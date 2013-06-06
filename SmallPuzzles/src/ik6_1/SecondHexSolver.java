package ik6_1;

public class SecondHexSolver {
	SecondHexField field;
	int valueCount = 4;
	
	int[][] conditions; //[index]{row, value, index}
	
	int[] state;
	int[][] rowUsage; //[rowIndex][value]
	int[][] restriction; //[place][value]
	
	int valuesToAdd;
	int t;
	int[] wayCount;
	int[] way;
	int[] waysValue;
	int[][] waysPlace;
	int[] temp;
	
	int solutionCount;
	
	public SecondHexSolver() {}
	
	public void setField(SecondHexField f) {
		field = f;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	public void setConditions(int[][] c) {
		conditions = c;
	}

	void init() {
		state = new int[field.size];
		valuesToAdd = field.width * valueCount;
		rowUsage = new int[field.rows.length][valueCount + 1];
		restriction = new int[field.size][valueCount + 1];

		wayCount = new int[valuesToAdd + 1];
		way = new int[valuesToAdd + 1];
		waysValue = new int[valuesToAdd + 1];
		waysPlace = new int[valuesToAdd + 1][field.width];
		temp = new int[field.width];

		t = 0;
		solutionCount = 0;
	}

	void srch() {
		wayCount[t] = 0;
		if(valuesToAdd == 0) return;
		if(!checkConditions()) return;
		int wcb = field.width + 1;
		waysValue[t] = -1;
		for (int row = 0; row < field.rows.length; row++) {
			for (int value = 1; value <= valueCount; value++) {
				if(rowUsage[row][value] > 0) continue;
				int wc = computeWayCount(row, value);
				if(wc < wcb) {
					wcb = wc;
					if(wc == 0) return;
					waysValue[t] = value;
					for (int i = 0; i < wc; i++) waysPlace[t][i] = temp[i];
				}
			}
		}
		if(waysValue[t] >= 0) {
			wayCount[t] = wcb;
		}
	}
	
	boolean checkConditions() {
		for (int i = 0; i < conditions.length; i++) {
			int[] condition = conditions[i];
			if(!checkCondition(condition[0], condition[1], condition[2])) {
				return false;
			}
		}
		return true;
	}
	
	boolean checkCondition(int row, int value, int index) {
		int[] rs = field.rows[row];
		int vv = 0;
		for (int i = 1; i <= valueCount; i++) if(rowUsage[row][i] == 0) ++vv;
		int empty = 0;
		int vs = 0;
		if(rowUsage[row][value] == 1) {
			for (int i = 0; i < rs.length; i++) {
				int p = rs[i];
				if(state[p] == value) {
					if(vv == 0) {
						return vs == index - 1;
					}
					return true;
				} else if(state[p] > 0) {
					++vs;
				} else {
					empty++;
				}
			}
		} else {
		}
		return true;
	}
	
	int computeWayCount(int row, int value) {
		int wc = 0;
		int[] rs = field.rows[row];
		for (int i = 0; i < rs.length; i++) {
			if(restriction[rs[i]][value] == 0) {
				temp[wc] = rs[i];
				++wc;
			}
		}
		return wc;
	}
	
	void move() {
		int value = waysValue[t];
		int place = waysPlace[t][way[t]];
		valuesToAdd--;
		state[place] = value;
		for (int i = 1; i <= valueCount; i++) restriction[place][i]++;
		int[] rowIndices = field.rowsByPlace[place];
		for (int i = 0; i < rowIndices.length; i++) {
			int row = rowIndices[i];
			rowUsage[row][value]++;
			int[] rs = field.rows[row];
			for (int j = 0; j < rs.length; j++) restriction[rs[j]][value]++;
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int value = waysValue[t];
		int place = waysPlace[t][way[t]];
		valuesToAdd++;
		state[place] = 0;
		for (int i = 1; i <= valueCount; i++) restriction[place][i]--;
		int[] rowIndices = field.rowsByPlace[place];
		for (int i = 0; i < rowIndices.length; i++) {
			int row = rowIndices[i];
			rowUsage[row][value]--;
			int[] rs = field.rows[row];
			for (int j = 0; j < rs.length; j++) restriction[rs[j]][value]--;
		}
	}

	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			
			if(valuesToAdd == 0 && checkConditions()) {
				solutionCount++;
				if(solutionCount % 1000 == 1 || solutionCount < 10) {
					System.out.println("Solution " + solutionCount);
					printSolution();
				}
			}			
		}		
	}
	
	void printSolution() {
		for (int i = 0; i < field.size; i++) {
			String c = "";
			if(field.form[i] == 0) {
				c = "+";
			} else {
				c = "" + state[i];
			}
			System.out.print(" " + c);
			if(field.x[i] == field.width - 1) System.out.println("");
		}
	}

}
