package pqrst11;

import java.util.*;

public class Collision {
	int width;
	int height;
	int size;
	int[] x,y;
	int[][] jp;
	int[][][] variants;
	
	char[][] words;
	
	char[] field;
	int[] degeneracy;
	
	int crossingCount;
	int maxCrossingCount;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	public void setWords(char[][] words) {
		this.words = words;
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		size = width * height;
		x = new int[size];
		y = new int[size];
		for (int i = 0; i < size; i++) {
			x[i] = (i % width);
			y[i] = (i / width);
		}
		jp = new int[8][size];
		for (int i = 0; i < size; i++) {
			jp[0][i] = (x[i] == width - 1)                      ? -1 : i + 1;
			jp[1][i] = (x[i] == width - 1 || y[i] == height - 1)? -1 : i + 1 + width;
			jp[2][i] = (y[i] == height - 1)                     ? -1 : i + width;
			jp[3][i] = (x[i] == 0 || y[i] == height - 1)        ? -1 : i - 1 + width;
			jp[4][i] = (x[i] == 0)                              ? -1 : i - 1;
			jp[5][i] = (x[i] == 0 || y[i] == 0)                 ? -1 : i - 1 - width;
			jp[6][i] = (y[i] == 0)                              ? -1 : i - width;
			jp[7][i] = (x[i] == width - 1 || y[i] == 0)         ? -1 : i + 1 - width;
		}
		variants = new int[8][][];
		for (int s = 4; s < 8; s++) {
			List l = new ArrayList();
			for (int i = 0; i < size; i++) {
				for (int d = 0; d < 8; d++) {
					int[] r = place(i, d, s);
					if(r != null) l.add(r);
				}
			}
			variants[s] = (int[][])l.toArray(new int[0][]);
		}
		field = new char[size];
		for (int i = 0; i < size; i++) field[i] = ' ';
		degeneracy = new int[size];
	}
	
	int[] place(int i, int d, int s) {
		int[] r = new int[s];
		r[0] = i;
		int k = 1;
		while(k < s) {
			i = jp[d][i];
			if(i < 0) return null;
			r[k] = i;
			++k;
		}
		return r;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		t = 0;
		wayCount = new int[words.length + 1];
		way = new int[words.length + 1];
		ways = new int[words.length + 1][200];
		maxCrossingCount = 0;
		crossingCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t >= words.length) return;
		int l = words[t].length;
		int[][] vs = variants[l];
		for (int k = 0; k < vs.length; k++) {
			if(!canPutWord(vs[k], t)) continue;
			if(t == 0) {
				int p = vs[k][0];
				if(x[p] > 3 || y[p] > x[p]) continue;
			}
			ways[t][wayCount[t]] = k;
			wayCount[t]++;
		}
	}
	
	boolean canPutWord(int[] v, int w) {
		int l = words[w].length;
		for (int c = 0; c < l; c++) {
			int p = v[c];
			if(field[p] != ' ' && field[p] != words[w][c]) return false;
		}
		return true;
	}
	
	void move() {
		int l = words[t].length;
		int[][] vs = variants[l];
		int k = ways[t][way[t]];
		addWord(vs[k], t);
		++t;
		srch();
		way[t] = -1;
	}
	
	void addWord(int[] v, int w) {
		int l = words[w].length;
		for (int c = 0; c < l; c++) {
			int p = v[c];
			field[p] = words[w][c];
			degeneracy[p]++;
			if(degeneracy[p] > 1) crossingCount++;
		}
	}
	
	void removeWord(int[] v, int w) {
		int l = words[w].length;
		for (int c = 0; c < l; c++) {
			int p = v[c];
			if(degeneracy[p] > 1) crossingCount--;
			degeneracy[p]--;
			if(degeneracy[p] == 0) field[p] = ' ';
		}
	}
	
	void back() {
		--t;
		int l = words[t].length;
		int[][] vs = variants[l];
		int k = ways[t][way[t]];
		removeWord(vs[k], t);		
	}
	
	public void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(crossingCount + t> maxCrossingCount) {
				maxCrossingCount = crossingCount + t;
				System.out.println("-->" + maxCrossingCount);
				printSolution();
			}
		}
	}
	
	void printSolution() {
		for (int i = 0; i < size; i++) {
			char c = field[i] == ' ' ? 'B' : field[i];
			System.out.print(c);
			if(x[i] == width - 1) System.out.println("");
		}
	}

	static char[][] WORDS = new char[][]{
		{'P', 'L', 'U', 'T', 'O'},
///		{'J', 'U', 'P', 'I', 'T', 'E', 'R'},
		{'N', 'E', 'P', 'T', 'U', 'N', 'E'},
		{'M', 'E', 'R', 'C', 'U', 'R', 'Y'},
		{'S', 'A', 'T', 'U', 'R', 'N'},
		{'U', 'R', 'A', 'N', 'U', 'S'},
		{'V', 'E', 'N', 'U', 'S'},
		{'E', 'A', 'R', 'T', 'H'},
		{'M', 'A', 'R', 'S'}
	};

	public static void main(String[] args) {
		Collision c = new Collision();
		c.setSize(7, 7);
		c.setWords(WORDS);
		c.solve();
	}
}

/*
9. Collision
-->19 + 49 = 68
 N Y   P     E
   R S L S N A
   U U U U   R
   C   T N   T
   R P O A E H
   E M A R S V
 N M     U    

6. Moving Buttleships
x-xx---x
-----x--
xxx--x--
-----x-x
-x-x-x-x
-x------
----xxx-
--x-----
A1, H1, D5, A6        

10. Pentathlon
336:
BBBBBBTBB
BBBBTTTBB
BBBWWITBB
BYWWPIVVV
BYWPPIUUV
YYZPPIBUV
BYZZZIUUL
BBBFZLLLL
BBBFFFBBB
BBBXFBBBB
BBXXXBBNN
BBBXBNNNB
*/