package pqrst12;

import java.util.*;

public class Blackjack2 {
	int[] weights = new int[]{1,2,3,4,5,6,7,8,9,10,10,10,10};
	char[] faces = new char[]{'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K'};	
	int[][] lines;
	
	int totalSelectedLinesCount;
	int[] selectedLinesCount;
	int[][] selectedLines;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int maxSelectedLinesCount;
	
	public void initLines() {
		List list = new ArrayList();
		int k = 1; 
		for (int i = 0; i < weights.length; i++) k = 2 * k;
		for (int i = k - 1; i >= 0; i--) {
			int[] line = new int[weights.length];
			int i0 = i;
			int sum = 0;
			for (int j = 0; j < weights.length; j++) {
				line[j] = i0 % 2;
				i0 = i0 / 2;
				if(line[j] == 1) sum += weights[j];
			}
			if(sum == 21 || (line[0] == 1 && sum == 11)) {
				list.add(line);
			}
		}
		lines = (int[][])list.toArray(new int[0][]);
		System.out.println("Total line count" + lines.length);
		for (int i = 0; i < lines.length; i++) {
///			printLine(i);
		}
	}
	
	void printLine(int line) {
		for (int j = 0; j < weights.length; j++) {
			if(lines[line][j] == 1)
			System.out.print(" " + faces[j]);
		}
		System.out.println("");
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		initLines();
		totalSelectedLinesCount = 0;
		selectedLinesCount = new int[4];
		selectedLines = new int[4][6];
		wayCount = new int[lines.length + 1];
		way = new int[lines.length + 1];
		ways = new int[lines.length + 1][5];
		t = 0;
		maxSelectedLinesCount = 0;
	}

	void srch() {
		wayCount[t] = 0;
		if(t >= lines.length) return;
		if(estimateTotalCount() < 12) return;
		int dm = getDMax();
		for (int d = 0; d < dm; d++) {
			if(accept(t, d)) {
				ways[t][wayCount[t]] = d;
				++wayCount[t];
			}
		}
		ways[t][wayCount[t]] = 4;
		++wayCount[t];
	}
	
	int getDMax() {
		if(selectedLinesCount[2] > 0) return 4;
		if(selectedLinesCount[1] > 0) return 3;
		if(selectedLinesCount[0] > 0) return 2;
		return 1;
	}
	
	int estimateTotalCount() {
		int est = totalSelectedLinesCount;
		for (int line = t; line < lines.length; line++) {
			for (int d = 0; d < 4; d++) {
				if(accept(line, d)) {
					++est;
					break;
				}
			}
		}
		return est;
	}
	
	boolean accept(int line, int d) {
		for (int d2 = 0; d2 < 4; d2++) {
			for (int k = 0; k < selectedLinesCount[d2]; k++) {
				int line2 = selectedLines[d2][k];
				int c = getCoincidence(line, line2);
				if((d == d2 && c > 0) || c > 1) return false;
			}
		}
		return true;
	}
	
	int getCoincidence(int line1, int line2) {
		int c = 0;
		for (int i = 0; i < weights.length; i++) {
			if(lines[line1][i] == 1 && lines[line2][i] == 1) ++c;
		}
		return c;
	}
	
	void move() {
		int d = ways[t][way[t]];
		if(d < 4) {
			selectedLines[d][selectedLinesCount[d]] = t;
			selectedLinesCount[d]++;
			totalSelectedLinesCount++;
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int d = ways[t][way[t]];
		if(d < 4) {
			selectedLinesCount[d]--;
			totalSelectedLinesCount--;
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
			if(totalSelectedLinesCount > maxSelectedLinesCount) {
				maxSelectedLinesCount = totalSelectedLinesCount;
				if(maxSelectedLinesCount > 0) {
					printSolution();
				}
			}
		}
	}
	
	void printSolution() {
		System.out.println("Selected lines count = " + maxSelectedLinesCount);
		for (int d = 0; d < 4; d++) {
			for (int k = 0; k < selectedLinesCount[d]; k++) {
				System.out.print("" + d + " : ");
				printLine(selectedLines[d][k]);
			}
		}
	}
	
	public static void main(String[] args) {
		Blackjack2 b = new Blackjack2();
		///b.initLines();
		b.solve();
	}

}
