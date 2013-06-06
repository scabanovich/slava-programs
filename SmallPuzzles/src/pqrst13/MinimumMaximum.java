package pqrst13;

public class MinimumMaximum {
	int width;
	int height;
	int size;
	int[] x,y;
	int[][] jp;
	
	char[] code = {'M', 'I', 'N', 'U', 'A', 'X'};
	int LETTER_COUNT = 6;
	int[] word1 = {0,1,2,1,0,3,0};
	int[] word2 = {0,4,5,1,0,3,0};

	int[] table;
	int[][] h; // first index - letter number, second index - ways number
	
	double bestScore = 0d;
	
	public MinimumMaximum() {
		setSize(8, 8);
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
			jp[0][i] = (x[i] == width - 1                      ) ? -1 : i + 1; 
			jp[1][i] = (x[i] == width - 1 || y[i] == height - 1) ? -1 : i + 1 + width; 
			jp[2][i] = (                     y[i] == height - 1) ? -1 : i     + width; 
			jp[3][i] = (x[i] == 0         || y[i] == height - 1) ? -1 : i - 1 + width; 
			jp[4][i] = (x[i] == 0                              ) ? -1 : i - 1; 
			jp[5][i] = (x[i] == 0         || y[i] == 0         ) ? -1 : i - 1 - width; 
			jp[6][i] = (                     y[i] == 0         ) ? -1 : i     - width; 
			jp[7][i] = (x[i] == width - 1 || y[i] == 0         ) ? -1 : i + 1 - width; 
		}
	}
	
	public void solve() {
		init();
		double sbs = 0;
		while(true) {
			bestScore = 0d;
			System.out.println("---->");
			run0();
			int b = 0;
			while(b < 20) {
				double d = bestScore;
				run1(5);
				if(d == bestScore) ++b;
			}			
			if(bestScore > 4500) {
				b = 0;
				while(b < 12) {
					double d = bestScore;
					run1(6);
					if(d == bestScore) ++b;
				}
			}
			if(bestScore > sbs) {
				sbs = bestScore;
				int n1 = getOccurances(word1);
				int n2 = getOccurances(word2);
				printTable();
				System.out.println(n1 + " " + n2);
			}
		}
	}
	
	void init() {
		table = new int[size];
		randomizeTable();
		h = new int[word1.length][size];
	}
	
	void randomizeTable() {
		for (int i = 0; i < size; i++) {
			table[i] = (int)(LETTER_COUNT * Math.random());
		}
		
		///
		///for (int i = 0; i < width; i++) table[i] = 0;
	}
	
	void run0() {
		int n1 = 0;
		int n2 = 0;
		while(n1 < 200 || n2 < 200) {
			randomizeTable();
			h = new int[word1.length][size];
			n1 = getOccurances(word1);
			n2 = getOccurances(word2);
		}
//		printTable();
//		System.out.println(n1 + " " + n2);
	}
	
	void run1(int selectionSize) {
		int stateCount = 1;
		for (int i = 0; i < selectionSize; i++) stateCount *= LETTER_COUNT;
		int[] s = generateSelection(selectionSize);
		int[] best = null;
		int[] copy = (int[])table.clone();
		for (int i = 0; i < stateCount; i++) {
			int state = i;
			for (int t = 0; t < selectionSize; t++) {
				table[s[t]] = (state % LETTER_COUNT);
				state = state / LETTER_COUNT;
			}
			int n1 = getOccurances(word1);
			int n2 = getOccurances(word2);
			if(n2 == 0 || n1 == 0) continue;
			double score = (n1 > n2) ? (n1 + n2 - 100.0d * n1 / n2) : (n1 + n2 - 100.0d * n2 / n1);
			if(score > bestScore) {
				bestScore = score;
				best = (int[])table.clone();
				if(score > 4000) System.out.println(score);
						break;
			}
		}
		if(best != null) {
			for (int i = 0; i < size; i++) table[i] = best[i];
		} else {
			for (int i = 0; i < size; i++) table[i] = copy[i];
		}
	}
	
	int[] generateSelection(int v) {
		int[] f = new int[size];
		int t = 0;
		while(t < v) {
			int i = (int)(size * Math.random());
			if(f[i] == 1) continue;
			///
			///if(i < width) continue;
			f[i] = 1;
			++t;
		}
		int[] s = new int[v];
		t = 0;
		for (int i = 0; i < size; i++) {
			if(f[i] == 1) {
				s[t] = i;
				++t;
			}
		}
		return s;
	}
	
	int getOccurances(int[] word) {
		for (int i = 0; i < size; i++) {
			h[0][i] = table[i] == word[0] ? 1 : 0;
		}
		int last = word.length - 1;
		for (int t = 1; t <= last; t++) {
			int t1 = t - 1;
			for (int i = 0; i < size; i++) {
				h[t][i] = 0;
				if(table[i] != word[t]) continue;
				for (int d = 0; d < 8; d++) {
					int j = jp[d][i];
					if(j >= 0 && table[j] == word[t1]) h[t][i] += h[t1][j];
				}
			}
		}
		int result = 0;
		for (int i = 0; i < size; i++) {
			result += h[last][i];
		}		
		return result;
	}
	
	void printTable() {
		for (int i = 0; i < size; i++) {
			if(x[i] == 0) System.out.println("");
			System.out.print(" " + code[table[i]]);
		}
		System.out.println("");
	}


	public static void main(String[] args) {
		MinimumMaximum p = new MinimumMaximum();
		p.solve();
	}

}

/*
6402.992481203008

 M M U M U M
 U M U M U M
 A M I M I M
 M X I N I N
 A M I M I M
 M M U M U M
6408 798

 M A A A U M
 M M X M M M
 U I I I U U
 M M N M M M
 U I I I U U
 M M N M M M
6408 798

7x7
11276.0

 M M M M M M M
 U U A A A U U
 M M M X M M M
 U U I I I U U
 M M M N M M M
 U U I I I U U
 M M M N M M M
8976 2640

8x8
17876.016420361248

 M M M M M N M M
 A A A U I I I U
 M X M M M N M M
 A I I U I I I U
 N N M M M N M M
 I I U U I I I U
 M M M M M N M M
 U U U U I I I U
18148 1218

////////////////////
6203.455598455598
 m m m m m m
 u m u m u m
 a m i m i m
 a x i n i n
 a m i m i m
 m m u m u m
6228 777

 m m m m m m
 m u m u m u
 m i m i m a
 n i n i x m
 m i m i m a
 m u m u m m
6228 777

////////////////////////
5899.172932330827
 m m m m m m
 u u i i i u
 m m m n m m
 u u i i i u
 m m m x m m
 m u a a a m
5832 798

5899.172932330827
 m m m m m m
 u i i i u u
 m m n m m m
 u i i i u u
 m m x m m m
 m a m a u m
5832 798

 */
