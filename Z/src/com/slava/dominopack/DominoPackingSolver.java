package com.slava.dominopack;

import com.slava.common.RectangularField;

public class DominoPackingSolver {
	static int[] reverse = {2,3,0,1};
	private DominoSet domino;
	private RectangularField field;
	int[] numbers; // -1 means that cell does not belong to the field.

	int[] state; // -1 - not considered; n - direction of domino;
	int[] dominoUsage;
	int movesToEnd;

	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;

	int solutionCount;
	int treeCount;
	int[] solution;

	public DominoPackingSolver() {}

	public void setDomino(DominoSet domino) {
		this.domino = domino;
	}

	public void setField(RectangularField field) {
		this.field = field;
	}

	public RectangularField getField() {
		return field;
	}

	public void setNumbers(int[] numbers) {
		this.numbers = numbers;
	}

	public void solve() {
		check();
		init();
		anal();		
	}

	void check() {
		
	}

	void init() {
		state = new int[numbers.length];
		movesToEnd = 0;
		for (int p = 0; p < numbers.length; p++) {
			state[p] = -1;
			if(numbers[p] >= 0) movesToEnd++;
		}
		if(movesToEnd % 2 == 1) throw new RuntimeException("Odd size of field:" + movesToEnd);
		movesToEnd = movesToEnd / 2;

		dominoUsage = new int[domino.getSize()];
		
		way = new int[movesToEnd + 1];
		place = new int[movesToEnd + 1];
		wayCount = new int[movesToEnd + 1];
		ways = new int[movesToEnd + 1][4];
		temp = new int[4];
		
		t = 0;
		solutionCount = 0;
		treeCount = 0;		
	}

	void srch() {
		wayCount[t] = 0;
		if(movesToEnd == 0) return;
		int wcm = 9;
		for (int p = 0; p < state.length; p++) {
			if(state[p] >= 0 || numbers[p] < 0) continue;
			int wc = getWayCount(p);
			if(wc == 0) return;
			if(wc < wcm) {
				wcm = wc;
				place[t] = p;
				for (int k = 0; k < wc; k++) ways[t][k] = temp[k];
				if(wc == 1) break;
			}			
		}
		if(wcm < 9) {
			wayCount[t] = wcm;
		}
	}

	int getWayCount(int p) {
		int wc = 0;
		for (int d = 0; d < 4; d++) {
			int q = field.jump(p, d);
			if(q >= 0 && state[q] < 0 && numbers[q] >= 0) {
				int piece = domino.getIndex(numbers[p], numbers[q]);
				if(dominoUsage[piece] == 0) {
					temp[wc] = d;
					wc++;
				}
			}
		}
		return wc;
	}

	int[] temp;

	void move() {
		int p = place[t];
		int d = ways[t][way[t]];
		int q = field.jump(p, d);
		state[p] = d;
		state[q] = reverse[d];
		int piece = domino.getIndex(numbers[p], numbers[q]);
		dominoUsage[piece]++;
		movesToEnd--;
		++t;
		srch();
		way[t] = -1;
	}

	void back() {
		--t;
		int p = place[t];
		int d = ways[t][way[t]];
		int q = field.jump(p, d);
		state[p] = -1;
		state[q] = -1;
		int piece = domino.getIndex(numbers[p], numbers[q]);
		dominoUsage[piece]--;
		movesToEnd++;
	}

	void anal() {
		srch();
		way[t] = -1;
		int tm = 0;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t > tm) {
				tm = t;
//				System.out.println("t=" + tm);
			}
			if(wayCount[t] == 0) {
				treeCount++;
			}
			if(movesToEnd == 0) {
				++solutionCount;
				if(solutionCount == 1) {
					solution  = (int[])state.clone();
					printSolution();
				}
			}
		}
	}

	void printSolution() {
		System.out.println("Solution:");
		for (int p = 0; p < state.length; p++) {
			String s = "" + state[p];
			System.out.print(" " + s);
			if(field.isRightBorder(p)) {
				System.out.println("");
			}
		}
	}

	public int getSolutionCount() {
		return solutionCount;
	}

	public int[] getSolution() {
		return solution;
	}

	public int getTreeCount() {
		return treeCount;
	}

}
