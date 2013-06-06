package pqrst12;

import java.util.*;

public class Blackjack {
	int width;
	int height;
	int size;
	int[] x,y;
	int[][] xy;
	int[][] lines;
	
	int[] values = {1,2,3,4,5,6,7,8,9,10,10,10,10};
	char[] letters = new char[]{' ', 'A', '2', '3', '4', '5', '6', '7', '8', '9', 'J'};
	
	int[] state;
	int currentScore;
	
	int bestScore = -100;
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		size = width * height;
		x = new int[size];
		y = new int[size];
		xy = new int[width][height];
		for (int i = 0; i < size; i++) {
			x[i] = (i % width);
			y[i] = (i / width);
			xy[x[i]][y[i]] = i;
		}
		List list = new ArrayList();
		//vertical
		for (int ix = 0; ix < width; ix++) {
			int[] line = new int[height];
			for (int iy = 0; iy < height; iy++) line[iy] = xy[ix][iy];
			list.add(line);
		}
		//horizontal
		for (int iy = 0; iy < height; iy++) {
			int[] line = new int[width];
			for (int ix = 0; ix < width; ix++) line[ix] = xy[ix][iy];
			list.add(line);
		}
		// \-
		for (int ix = 0; ix < width - 1; ix++) {
			int length = width - ix;
			if(length > height) length = height;
			int[] line = new int[length];
			for (int it = 0; it < length; it++) line[it] = xy[ix + it][it];
			list.add(line);
		}
		// \|
		for (int iy = 1; iy < height - 1; iy++) {
			int length = height - iy;
			if(length > width) length = width;
			int[] line = new int[length];
			for (int it = 0; it < length; it++) line[it] = xy[it][iy + it];
			list.add(line);
		}
		// /-
		for (int ix = 1; ix < width; ix++) {
			int length = ix + 1;
			if(length > height) length = height;
			int[] line = new int[length];
			for (int it = 0; it < length; it++) line[it] = xy[ix - it][it];
			list.add(line);
		}
		// /|
		for (int iy = 1; iy < height - 1; iy++) {
			int length = height - iy;
			if(length > width) length = width;
			int[] line = new int[length];
			for (int it = 0; it < length; it++) line[it] = xy[width - 1 - it][iy + it];
			list.add(line);
		}
		lines = (int[][])list.toArray(new int[0][]);
		System.out.println("Lines count = " + lines.length);
		for (int i = 0; i < lines.length; i++) {
			for (int j = 0; j < lines[i].length; j++) {
				System.out.print(" " + lines[i][j]);
				
			}
			System.out.println("");
		}
		
		state = new int[size];
		for (int i = 0; i < values.length; i++) state[i] = values[i];
		currentScore = compute();
	}
	
	public void initState(int[][] rows) {
		for (int i = 0; i < values.length; i++) state[i] = 0;
		for (int iy = 0; iy < rows.length; iy++) {
			for (int ix = 0; ix < rows[iy].length; ix++) {
				int k = xy[ix][iy];
				state[k] = rows[iy][ix];
			}
		}
		currentScore = compute();
	}
	
	int compute() {
		int delta = 0;
		int b = 0;
		for (int k = 0; k < lines.length; k++) {
			int q = 0;
			int s = 0;
			boolean ace = false;
			for (int c = 0; c < lines[k].length; c++) {
				int i = lines[k][c];
				int v = state[i];
				if(v == 0) continue;
				if(v == 1) ace = true;
				++q;
				s += v;
			}
			if(q < 2) continue;
			int d = Math.abs(21 - s);
			if(ace) {
				int d1 = Math.abs(11 - s);
///				if(d1 < d) 
				  d = d1;
			}
			if(d == 0) b++; else delta += d;			
		}
		return 30 * b - 2 * size - delta * 5;
	}
	
	boolean flip() {
		int k1 = (int)(Math.random() * size);
		int k2 = (int)(Math.random() * size);
		return flip(k1, k2);
	}
	
	boolean flip(int k1, int k2) {
		if(state[k1] == state[k2]) return false;
		int c = state[k1];
		state[k1] = state[k2];
		state[k2] = c;
		int score = compute();
		if(score >= currentScore || Math.random() > 0.98) {
			currentScore = score;
			return true;
		} 
		c = state[k1];
		state[k1] = state[k2];
		state[k2] = c;
		return true;
	}
	
	public void solve() {
		int count1 = 0;
		int count2 = 0;
		while(true) {
			if(currentScore > bestScore) {
				bestScore = currentScore;
				printSolution();
			}
			while(!flip());
			++count1;
			if(count1 == 100000) {
				count1 = 0;
				count2++;
				if(count2 % 100 == 0) System.out.println("-->" + count2);
			}
		}
	}
	
	void printSolution() {
		System.out.println("Score=" + bestScore);
		for (int i = 0; i < size; i++) {
			System.out.print(" " + letters[state[i]]);
			if(x[i] == width - 1) System.out.println("");
		}
	}
	
	boolean flipRows() {
		int y1 = (int)(height * Math.random());
		int y2 = y1;
		while(y2 == y1) y2 = (int)(height * Math.random());
		for (int ix = 0; ix < width; ix++) {
			int k1 = xy[ix][y1];
			int k2 = xy[ix][y2];
			int c = state[k1];
			state[k1] = state[k2];
			state[k2] = c;
		}
		int score = compute();
		if(score >= currentScore || Math.random() > 0.98) {
			currentScore = score;
			return true;
		} 
		for (int ix = 0; ix < width; ix++) {
			int k1 = xy[ix][y1];
			int k2 = xy[ix][y2];
			int c = state[k1];
			state[k1] = state[k2];
			state[k2] = c;
		}
		return true;
	}
	
	boolean flipInsideRow() {
		int y1 = (int)(height * Math.random());
		int x1 = (int)(width * Math.random());
		int x2 = x1; 
		while(x2 == x1) x2 = (int)(width * Math.random());
		return flip(xy[x1][y1], xy[x2][y1]);		
	}
	
	boolean flip2() {
		return (Math.random() < 0.95) ? flipInsideRow() : flipRows();
	}

	public void solve2() {
		int count1 = 0;
		int count2 = 0;
		while(true) {
			if(currentScore > bestScore) {
				bestScore = currentScore;
				printSolution();
			}
			while(!flip2());
			++count1;
			if(count1 == 100000) {
				count1 = 0;
				count2++;
				if(count2 % 100 == 0) System.out.println("-->" + count2);
			}
		}
	}
	
	public static void main(String[] args) {
		Blackjack b = new Blackjack();
		b.setSize(7, 5);
		b.solve();
///		b.initState(BlackjackStates.STATE_B_4);
///		b.solve2();
	}

}

/*
Score=260 STATE_E
   3 8 J    
   4 6   A  
 9 5 7      
         J  
   J     J 2
Score=260 STATE_C_1
 A J       J
       9 8 4
 J     3 7  
       J 6 5
           2
Score=260
   J        
   4 8   J  
     7 3 A  
   5 6     J
 9 2     J  
Score=260
     6    
 9     J 2
         5
 3 J 8    
   A     J
 J   7   4
Score=259
 J     A     J
     9   J 2  
     8 7 6    
     4 3 5   J
Score=259
 J   2     5 4
     8   6 7  
     A J J    
   J     3 9  
Score=252
     3 9   J
 6 8 7      
 J   A J    
 5 J   2 4  
*/