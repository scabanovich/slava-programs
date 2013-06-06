package pqrst15;

public class CrosswordPacker {
	static char EMPTY = '.';
	CrosswordField field;
	String[] words;
	char[] initialFilling;
//	Set allowedPairs = new HashSet();
	int[][] allowedPairs = new int[256][256];

	char[] state;
	int[][] restriction;
	int[] wordUsage;
	
	int t;
	int crossingCount;
	int[] wayCount;
	int[] way;
	int[][] waysW;
	int[][] waysP;
	int[][] waysD;

	int[][][] rejected; //[word][place][dir] - [t]

	int treeCount = 0;
	int solutionCount = 0;
	
	public CrosswordPacker() {}
	
	public void setWords(String[] words) {
		this.words = words;
		for (int i = 0; i < words.length; i++) {
			for (int j = 0; j < words[i].length() - 1; j++) {
				char c1 = words[i].charAt(j);
				char c2 = words[i].charAt(j + 1);
				allowedPairs[(byte)c1][(byte)c2] = 1;
//				allowedPairs.add(words[i].substring(j, j + 2));
			}
		}
//		System.out.println(allowedPairs.size());
	}
	
	public void setInitialFilling(String[] f) {
		initialFilling = new char[field.size];
		for (int i = 0; i < field.size; i++) initialFilling[i] = EMPTY;
		for (int i = 0; i < f.length; i++) {
			for (int j = 0; j < f[i].length(); j++) {
				int p = field.xy[i][j];
				initialFilling[p] = f[i].charAt(j);
			}
		}
	}
	
	public void setField(CrosswordField f) {
		field = f;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new char[field.size];
		for (int i = 0; i < state.length; i++) state[i] = EMPTY;
		restriction = new int[field.size][2];
		wordUsage = new int[words.length];
		wayCount = new int[words.length + 1];
		way = new int[words.length + 1];
		waysW = new int[words.length + 1][300];
		waysP = new int[words.length + 1][300];
		waysD = new int[words.length + 1][300];
		
		rejected = new int[words.length][field.size][2];
		for (int w = 0; w < words.length; w++) {
			for (int p = 0; p < field.size; p++) {
				for (int d = 0; d < 2; d++) {
					rejected[w][p][d] = -1;
				}
			}
		}
		t = 0;
		crossingCount = 0;
		initByInitialFilling();
///		makeDiagonalRestriction();
	}
	
	void makeDiagonalRestriction() {
		for (int i = 0; i < state.length; i++) {
			int q = field.x[i] + field.y[i];
			if(q < field.width - 6 || q > field.width + 5) {
				restriction[i][0]++;
				restriction[i][1]++;
			}
		}
	}
	
	void initByInitialFilling() {
		if(initialFilling == null) return;
		for (int i = 0; i < state.length; i++) state[i] = initialFilling[i];
		for (int i = 0; i < state.length; i++) {
			if(state[i] == EMPTY) {
				for (int d = 0; d < 4; d++) {
					int q = field.jump(i, d, 1);
					if(q < 0 || state[q] == EMPTY) continue;
					int q2 = field.jump(q, d, 1);
					if(q2 < 0 || state[q2] == EMPTY) continue;
					restriction[i][0]++;
					restriction[i][1]++;
					break;
				}
			} else {
				int xw = field.jumpX(i, -1);
				int xe = field.jumpX(i, 1);
				if((xw >= 0 && state[xw] != EMPTY)
					|| (xe >= 0 && state[xe] != EMPTY)) {
					restriction[i][0]++;
				}
				int xn = field.jumpY(i, -1);
				int xs = field.jumpY(i, 1);
				if((xn >= 0 && state[xn] != EMPTY)
					|| (xs >= 0 && state[xs] != EMPTY)) {
					restriction[i][1]++;
				}
			}
		}		
	}
	
	int forceP = -1;
	int forceD = -1;
	
	void srch() {
		wayCount[t] = 0;
		if(t == words.length) return;

/// RUSSIAN_NUMERALS
///			int weight = getWeight();
///			if(weight >= 2006 || !canTakeWeight(weight)) return;
			
/// COUNTRIES
//		if(t == 12) return;
//		if(t == 6 && crossingCount < 7) return;
//		if(crossingCount < t * 1.6 - 3) return;
//		if(t > 11 && crossingCount < t * 1.6 - 2) return;

				//if(t > 5 && wordUsage[words.length - 2] == 0) return;
//		if(t == 7 && crossingCount < 9) return;
//		if(t == 11 && crossingCount < 14) return;
//		if(t == 13 && crossingCount < 17) return;
//		if(t == 16 && crossingCount < 21) return;

		if(t == 0 && initialFilling == null) {
			for (int ix = field.width / 2; ix < field.width; ix++) {
				for (int iy = 0; iy < field.height; iy++) {
					int p = field.xy[ix][iy];
					for (int d = 0; d < 2; d++) {
					for (int wi = 0; wi < 5/*words.length*/; wi++) {
						if(canPut0(wi, p, d)) {
							addVar(wi, field.xy[ix][iy], d);
						}
					}
					}
				}
			}
		} else if(getForce()) {			
			for (int w = 0; w < words.length; w++) {
				if(wordUsage[w] > 0) continue;
				int p = forceP;
				int d = forceD;
				if(state[p] == EMPTY) continue;
				if(restriction[p][d] != 0) continue;
				for (int dl = 0; dl < words[w].length(); dl++) {
					if(state[p] != words[w].charAt(dl)) continue;
					int q = field.jump(p, d, -dl);
					if(q < 0) continue;
					if(canPut(w, q, d)) {
						addVar(w, q, d);
					}
				}
			}
		} else {
			for (int w = 0; w < words.length; w++) {
				if(wordUsage[w] > 0) continue;
				for (int p = 0; p < field.size; p++) {
					if(state[p] == EMPTY) continue;
					for (int d = 0; d < 2; d++) {
						if(restriction[p][d] != 0) continue;
						for (int dl = 0; dl < words[w].length(); dl++) {
							if(state[p] != words[w].charAt(dl)) continue;
							int q = field.jump(p, d, -dl);
							if(q < 0) continue;
							if(dl > 0 && state[q] != EMPTY) continue;
							if(canPut(w, q, d)) {
								addVar(w, q, d);
							}
						}
					}
				}
			}
		}
		if(t < 15) randomize();
	}
	
	void addVar(int w, int p, int d) {
		if(wayCount[t] >= waysW[t].length) return;
		if(rejected[w][p][d] >= 0) {
			return;
		}
		waysW[t][wayCount[t]] = w;
		waysP[t][wayCount[t]] = p;
		waysD[t][wayCount[t]] = d;
		wayCount[t]++;
	}
	
	void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = wayCount[t] - 1; i >= 1; i--) {
			int j = (int)(Math.random() * (i + 1));
			if(j == i) continue;
			flip(waysW[t], i, j);
			flip(waysP[t], i, j);
			flip(waysD[t], i, j);
		}
//		if(t > 0 && wayCount[t] > 3) wayCount[t] = 3;
	}
	void flip(int[] a, int i, int j) {
		int c = a[i];
		a[i] = a[j];
		a[j] = c;
	}	

	boolean canPut0(int wi, int p, int d) {
		if(wordUsage[wi] > 0) return false;
		int q = field.jump(p, d, -1);
		if(q >= 0 && state[q] != EMPTY) return false;
		String word = words[wi];
		for (int i = 0; i < word.length(); i++) {
			q = field.jump(p, d, i);
			if(q < 0) return false;
			if(state[q] != EMPTY && state[q] != word.charAt(i)) return false;
			if(restriction[q][d] != 0) return false;
		}
		q = field.jump(p, d, word.length());
		if(q >= 0 && state[q] != EMPTY) return false;
		return true;
	}

	boolean canPut(int wi, int p, int d) {
		if(wordUsage[wi] > 0) return false;
		int q = field.jump(p, d, -1);
		if(q >= 0 && state[q] != EMPTY) return false;
		String word = words[wi];
		boolean hasCrossings = false;
		int da = (d == 0) ? 1 : 0;
		for (int i = 0; i < word.length(); i++) {
			q = field.jump(p, d, i);
			if(q < 0) return false;
			char c = word.charAt(i);
			if(state[q] != EMPTY && state[q] != c) return false;
			if(restriction[q][d] != 0) return false;
			if(state[q] != EMPTY) hasCrossings = true;
			int q1 = field.jump(q, da, 1);
			if(q1 >= 0 && state[q1] != EMPTY) {
				if(allowedPairs[(byte)c][(byte)state[q1]] != 1) return false;
			}
			q1 = field.jump(q, da, -1);
			if(q1 >= 0 && state[q1] != EMPTY) {
				if(allowedPairs[(byte)state[q1]][(byte)c] != 1) return false;
			}
		}
		q = field.jump(p, d, word.length());
		if(q >= 0 && state[q] != EMPTY) return false;
		return hasCrossings;
	}
	
	boolean getForce() {
		forceP = -1;
		forceD = -1;
		for (int p = 0; p < field.size; p++) {
			if(state[p] == EMPTY) continue;
			for (int d = 0; d < 2; d++) {
				if(restriction[p][d] > 0) continue;
				int q = field.jump(p, d, 1);
				if(q < 0 || state[q] == EMPTY) continue;
				forceD = d;
				forceP = p;
				return true;
			}
		}
		return false;
	}
	
	void move() {
		int wi = waysW[t][way[t]];
		int p = waysP[t][way[t]];
		int d = waysD[t][way[t]];
		rejected[wi][p][d] = t;
		put(wi, p, d);
		++t;
		srch();
		way[t] = -1;
	}
	
	void put(int wi, int p, int d) {
		wordUsage[wi]++;
		int q = field.jump(p, d, -1);
		if(q >= 0) {
			restriction[q][0]++;
			restriction[q][1]++;
		}
		String word = words[wi];
		for (int i = 0; i < word.length(); i++) {
			q = field.jump(p, d, i);
			if(state[q] == EMPTY) {
				state[q] = word.charAt(i);
			} else {
				crossingCount++;
			}
			restriction[q][d]++;
		}
		q = field.jump(p, d, word.length());
		if(q >= 0) {
			restriction[q][0]++;
			restriction[q][1]++;
		}
	}
	
	void back() {
		for (int i = 0; i < wayCount[t]; i++) {
			int w = waysW[t][i];
			int p = waysP[t][i];
			int d = waysD[t][i];
			rejected[w][p][d] = -1;
		}
//		for (int w = 0; w < words.length; w++) {
//			for (int p = 0; p < field.size; p++) {
//				for (int d = 0; d < 2; d++) {
//					if(rejected[w][p][d] >= t)
//						rejected[w][p][d] = -1;
//				}
//			}
//		}
		--t;
		int wi = waysW[t][way[t]];
		int p = waysP[t][way[t]];
		int d = waysD[t][way[t]];
		remove(wi, p, d);
	}

	void remove(int wi, int p, int d) {
		wordUsage[wi]--;
		int q = field.jump(p, d, -1);
		if(q >= 0) {
			restriction[q][0]--;
			restriction[q][1]--;
		}
		String word = words[wi];
		for (int i = 0; i < word.length(); i++) {
			q = field.jump(p, d, i);
			restriction[q][d]--;
			if(restriction[q][0] == 0 && restriction[q][1] == 0) {
				state[q] = EMPTY;
			} else {
				crossingCount--;
			}
		}
		q = field.jump(p, d, word.length());
		if(q >= 0) {
			restriction[q][0]--;
			restriction[q][1]--;
		}
	}

	void anal() {
		srch();
		way[t] = -1;
		int tm = 6;
		int bestScore = 4; 
		            //200; //RUSSIAN_NUMERALS
		int q = 0;
		  boolean a2006 = false;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
					//if(t == 0) tm = 12;
			}
			way[t]++;
			move();
// RUSSIAN NUMERALS
//			if(!a2006 && getWeight() == 2006) {
//				a2006 = true;
//				System.out.println("x");
//			}
//			if(t > tm && !getForce() 
//					&& getWeight() == 2006
//					) {
//				tm = t;
//				System.out.println("tm=" + tm);
//				print();
//					printWeights();
//			}

/// main
//			if(t == words.length && !getForce()) {
//				int score = getScore();
//				if(score > bestScore) {
//					bestScore = score;
//					System.out.println("Solution " + crossingCount + " " + bestScore);
//					print();
//				}
//			}
			
/// COUNTRIES
//			if(!getForce()) {
//				int score = crossingCount * 4 - 2 * t;
//				if(score > bestScore) {
//					bestScore = score;
//					System.out.println("Solution " + t + " " + crossingCount + " " + bestScore);
//					print();
//				}
//			}

			if(wayCount[t] == 0) {
				q++;
				int a = (tm > 16) ? 50000000 : (tm > 15) ? 10000000 : 7000000;
				if(q > a) {
					while(t > 1) back();
					q = 0;
				}
//				if(q % 100000 == 0) System.out.print("" + t + "; ");
			}
			
			//months
			if(t > tm) {
				tm = t;
				System.out.println("-->" + tm);
			}
			if(t == words.length && !getForce()/* && isHandVequal()*/) {
				print();
				solutionCount++;
				return;
			}
			if(wayCount[t] == 0) {
				treeCount++;
				//Tree Limit
				if(tm < 16 && treeCount == 15000) return;
//				if(treeCount == 100000) return;
				if(treeCount % 100000 == 0 || t >= 17) printProgress();
			}
		}
	}
	
	void print() {
		for (int p = 0; p < field.size; p++) {
			System.out.print(" " + state[p]);
			if(field.x[p] == field.width - 1) System.out.println("");
		}
		System.out.println("");
	}

	void printProgress() {
		for (int i = 0; i < t; i++ ) {
			System.out.print(" " + way[i] + "/" + wayCount[i]);
		}
		System.out.println();
	}
	
	int getScore() {
		int res = 30 * crossingCount;
		int xs = field.width;
		int ys = field.height;
		for (int ix = 0; ix < field.width; ix++) {
			boolean used = false;
			for (int iy = 0; iy < field.height && !used; iy++) {
				if(state[field.xy[ix][iy]] != EMPTY) used = true;
			}
			if(!used) --xs;
		}
		for (int iy = 0; iy < field.height; iy++) {
			boolean used = false;
			for (int ix = 0; ix < field.width && !used; ix++) {
				if(state[field.xy[ix][iy]] != EMPTY) used = true;
			}
			if(!used) --ys;
		}
		res -= 2 * xs * ys;
		return res;
	}
	
	private int getWeight() {
		int w = 0;
		for (int i = 0; i < t; i++) {
			int wi = waysW[i][way[i]];
			w += WordSet.WEIGHTS[wi];
		}
		return w;
	}
	void printWeights() {
		for (int i = 0; i < t; i++) {
			int wi = waysW[i][way[i]];
			System.out.print(" " + WordSet.WEIGHTS[wi]);
		}
		System.out.println("");
		
	}
/*	
	int getMinWeight() {
		int k = t;
		int c = 0;
		for (int i = 0; i < words.length; i++) {
			if(wordUsage[i] > 0) continue;
			c += WordSet.WEIGHTS[i];
			++k;
			if(k == 16) return c;
		}
		return c;
	}
*/	
	
	int WORD_COUNT = 17;
	boolean canTakeWeight(int w) {
				if(true) return true;
		int d = WORD_COUNT - t;
		int dw = 2006 - w;
		int dt = 0;
		int s = 0;
		int[] n = new int[d + 1];
		n[0] = -1;
		while(true) {
			if(dt == d && s == dw) return true;
			while(dt == d || s >= dw || n[dt] >= words.length - 1) {
				if(dt == 0) return false;
				dt--;
				s -= WordSet.WEIGHTS[n[dt]];
			}
			n[dt]++;
			if(wordUsage[n[dt]] > 0 || WordSet.WEIGHTS[n[dt]] + s > dw) continue;
			s += WordSet.WEIGHTS[n[dt]];
			dt++;
			n[dt] = n[dt - 1];			
		}
	}
	
	//months
	boolean isHandVequal() {
		int c = 0;
		for (int i = 0; i < t; i++) {
			int w = waysW[i][way[i]];
			int d = waysD[i][way[i]];
			int dc = WordSet.MONTHS_WEIGHTS[w];
			if(d == 0) c += dc; else c -= dc;
		}
		return c == 0;
	}
	
	static String[] INITIAL = new String[]{
		 ".............H.T..",
		 ".............O.R.G",
		 "...........UKRAINA",
		 "..........K..V.N.N",
		 ".........KOSTARIKA",
		 "..........R..T.D..",
		 "....A..ARGENTINA..",
		 "....V..R..&..&.D..",
		 "..~.S..A..........",
		 "..KOTDIVUAR.......",
		 "..V.R..I.N.F......",
		 "BRAZILI&.GERMANI&.",
		 "..D.&.S..L.A......",
		 "..O...P..I.N......",
		 "PORTUGALI&.@......",
		 "...O..N.R..I......",
		 "...G..ITALI&......",
		 ".&PONI&.N.........",
	};

	static String[] INITIAL1 = new String[]{
		"ASMUS...",
		"........",
		"........",
		"........",
		"........",
	};

	public static void main(String[] args) {
		CrosswordField f = new CrosswordField();
		f.setSize(17, 12);
		while(true) {
		CrosswordPacker p = new CrosswordPacker();
		p.setWords(WordSet.getCurrentWords());
		p.setField(f);
//			p.setInitialFilling(INITIAL1);
		p.solve();
		if(p.solutionCount > 0) break;
		}
	}

}

/*
Solution 22 372
 f + s e v e n + + s i x t e e n
 o n e + + + + + + i + + h + l +
 u + v + + f + + + x + f i v e +
 r + e + e i g h t + t + r + v +
 + + n + + f + + w + h + t + e +
 + + t + + t + f o u r t e e n +
 t w e l v e + + + + e + e + + +
 e + e + + e i g h t e e n + + +
 n i n e + n + + + + + + + + + +

Solution 22
 + + f o u r t e e n + + + + + + +
 + + + n + + e + l + + + t + + + +
 + + s e v e n t e e n + h + + + +
 + + i + + + + + v + + e i g h t +
 s i x + f i f t e e n + r + + w +
 e + t w o + i + n + + + t h r e e
 v + e + u + v + + + + + e + + l +
 e + e + r + e i g h t e e n + v +
 n i n e + + + + + + + + n + + e +

Solution 23 402
 + + + + + + + + + s + t + + + o
 f i v e + + + + e i g h t e e n
 o + + l + + + f + x + r + + + e
 u + + e + t h i r t e e n + + +
 r + + v + w + f + e + e i g h t
 + + s e v e n t e e n + n + + e
 + t + n + l + e + n + s e v e n
 + w + + + v + e + + + i + + + +
 f o u r t e e n + + + x + + + +

Solution 23 386
 . . . . . F I F T E E N . . . . . . T
 . . . T W O . . H . L . . E . . . . E
 . . . H . U . F I V E . S I X T E E N
 F O U R . R . . R . V . . G . W . . .
 . . . E . T . . T . E I G H T E E N .
 . . S E V E N T E E N . . T . L . I .
 . . I . . E . . E . . . . . . V . N .
 . . X . O N E . N . . . . . S E V E N

Solution 23 404
 . . . . . . S I X . . . .
 . T W E L V E . . . . . .
 . W . . . . V . T E N . .
 F O U R . . E . H . . E .
 O . . . F . N . R . . I .
 U . . S I X T E E N . G .
 R . F . F . E . E I G H T
 T H I R T E E N . N . T .
 E . V . E . N . S E V E N
 E L E V E N . . . . . E .
 N . . . N . . . . . O N E
Solution 24 380
 . . . . . S . . . . . . . . . . . .
 . . . . . I . . . . . . . T . . S .
 . . . S I X T E E N . . T H R E E .
 . . . . . . . L . I . E . I . . V .
 . . . F I F T E E N . I . R . . E .
 . T W O . I . V . E I G H T E E N .
 . . . U . V . E . . . H . E . . T .
 F O U R T E E N . . . T W E L V E .
 . N . . E . . . . . . . . N . . E .
 S E V E N . . . . . . . . . . . N .

tm=16
 D V E S T I . P . T . S . . . S . S . .
 E . . . Y . . & . R . O . . # E S T ' .
 S T O . S T O T R I . R . . . M . O . .
 & . D . & . . ' . S T O S E M ' . D . .
 T R I . 4 . . . . T . K . . . . . V . .
 ' . N . A . . D V A . . D V A D = A T '

 1000 300 200 107 103 102 100 40 20 10 7 6 5 3 2 1 
tm=16
 D . S T O . S T O O D I N . . . S . . T
 V . O . . . E . . . E . . . P & T ' . Y
 E . R . . . M . . . V . O . . . O . . S
 S T O P & T ' . D . & . D E S & T ' . &
 T . K . . . . . V . T R I . . . R . . 4
 I . . D V A D = A T ' . N . T R I S T A

 1000 300 200 105 103 101 100 40 20 10 9 7 5 3 2 1 

tm=16
 . 4 . S . T R I D = A T ' . S T O D V A
 . E . T . R . . V . . Y . S . . D . O .
 S T O O D I N . E . . S T O T R I . S .
 . Y . . V . . . S . . & . R . . N . E .
 . R . . A . . . T . . 4 . O . . . . M .
 S E M ' . . T R I S T A . K . P & T ' .

 1000 300 200 103 102 101 100 40 30 8 7 5 4 3 2 1 
tm=15
 . D . . . T Y S & 4 A S T O . D . S . .
 . E . . . . . . . E . T . D E V & T ' .
 . S . P & T ' S O T . O . I . A . O . S
 . & . & . R . . . Y . . . N . . . D . E
 S T O T R I . S O R O K . . . . . V . M
 . ' . ' . . . . . E . . D V A D = A T '

 1100 4 500 100 3 5 1 9 103 2 10 40 102 20 7

tm=15
 . . . . . P . O . 4 . . D V A D = A T '
 . S . . . & . D . E . . E . . . . . Y .
 . T . S . T R I S T A . S O R O K . S .
 V O S E M ' . N . Y . . & . . . . . & .
 . . . M . . . . . R . . T R I . . . 4 .
 P & T ' S O T . # E S T ' . . . D V A .

 500 7 8 100 5 300 1 4 6 10 3 20 40 1000 2
tm=15
 P & T ' S O T . . S . 4 . O . D . S . .
 & . R . E . Y . . T . E . D V E S T I .
 T . I . M . S O R O K T R I . S . O . .
 ' . . . ' . & . . . . Y . N . & . D V A
 . . . . . . 4 . . . . R . . . T . V . .
 . D V A D = A T ' . D E V & T ' . A . .

 500 7 3 1000 43 1 100 4 5 9 10 20 200 102 2
tm=15
 S . . . . . . S T O O D I N . S . V . P
 T Y S & 4 A . E . D . E . . . O . O . &
 O . . . . . . M . I . S . . . R . S . T
 D V A D = A T ' . N . & . S T O S E M '
 V . . V . . R . . . . T . T . K . M . .
 A . . A . . I . P & T ' S O T . . ' . .

 1000 102 20 2 3 7 101 1 10 500 100 107 5 8 40

tm=15
 D . S T O D V A . D E S & T ' . . . S .
 V . O . . . O . . V . E . R . # E S T '
 E . R . T Y S & 4 A . M . I . . . . O .
 S T O . R . E . . . . ' . S T O P & T '
 T . K . I . M . . . . . . T . . . . R .
 I . . . . . ' . D V A D = A T ' . . I .

 1000 3 2 8 10 7 102 40 100 200 300 20 105 103 6

*/