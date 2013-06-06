package ic2005_3;

public class WordsInCombs {
	int[][] places = new int[][]{
		{ 1, 2, 3, 4, 5, 6},
		{ 5, 4, 7, 8, 9,10},
		{11,12,13,14, 3, 2},
		{ 3,14,15,16, 7, 4},
		{ 7,16,17,18,19, 8},
		{20,21,22,23,13,12},
		{13,23,24,25,15,14},
		{15,25,26,27,17,16},
		{28,29,30,31,22,21},
		{22,31,32,33,24,23},
		{24,33,34,35,26,25},
		{36,37,38,39,30,29},
		{30,39,40,41,32,31},
		{32,41,42,43,34,33},
		{38,44,45,46,40,39},
		{40,46,47, 0,42,41}
	};
	
	String[] words = new String[]{
		"APAXUC", "MATYAP", "HUXPOM", "CUHEBA",
		"APOMAT", "MOHAPX", "HOKAYT", "CTATYT",
		"KOMETE", "MOPOKA", "OPATOP", "TAPXAH",
		"MAPTEH", "MPAMOP", "PAKETA", "TETUBA"
	};
	
	char[] state;
	int[] usage;
	int[] wordUsage;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] waysW;
	int[][] waysS;
	
	public WordsInCombs() {
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new char[48];
		usage = new int[48];
		wordUsage = new int[words.length];
		wayCount = new int[places.length + 1];
		way = new int[places.length + 1];
		waysW = new int[places.length + 1][places.length * 6];
		waysS = new int[places.length + 1][places.length * 6];
		t = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		for (int w = 0; w < words.length; w++) {
			if(wordUsage[w] == 1) continue;
			for (int s = 0; s < words[w].length(); s++) {
				if(canAdd(w, s)) {
					waysW[t][wayCount[t]] = w;
					waysS[t][wayCount[t]] = s;
					wayCount[t]++;
				}
			}
		}
	}
	
	String getRotatedWord(int w, int s) {
		return words[w].substring(s) + words[w].substring(0, s);
	}
	
	boolean canAdd(int w, int s) {
		String word = getRotatedWord(w, s);
		for (int i = 0; i < word.length(); i++) {
			int p = places[t][i];
			if(usage[p] > 0) {
				if(state[p] != word.charAt(i)) return false;
			}
		}
		return true;
	}
	
	void move() {
		int w = waysW[t][way[t]];
		int s = waysS[t][way[t]];
		add(w, s);
		++t;
		srch();
		way[t] = -1;
	}
	
	void add(int w, int s) {
		String word = getRotatedWord(w, s);
		wordUsage[w] = 1;
		for (int i = 0; i < word.length(); i++) {
			int p = places[t][i];
			usage[p]++;
			if(usage[p] == 1) {
				state[p] = word.charAt(i);
			}
		}
	}
	
	void back() {
		--t;
		int w = waysW[t][way[t]];
		int s = waysS[t][way[t]];
		remove(w, s);
	}
	
	void remove(int w, int s) {
		String word = getRotatedWord(w, s);
		wordUsage[w] = 0;
		for (int i = 0; i < word.length(); i++) {
			int p = places[t][i];
			usage[p]--;
			if(usage[p] == 0) state[p] = ' ';
		}
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
//				System.out.println(t);
			}
			if(t == places.length) {
				System.out.println("solution");
				printSolution();
			}
		}
	}

	void printSolution() {
		for (int i = 0; i < t; i++) {
			int w = waysW[i][way[i]];
			int s = waysS[i][way[i]];
			System.out.println(getRotatedWord(w, s));
		}
	}
	
	public static void main(String[] args) {
		WordsInCombs p = new WordsInCombs();
		p.solve();
	}

}

//TEHUXAHOKETAP
//OMAPAYTAPOMAP
/*
solution
ACUHEB,EHMAPT,APAXUC,UXPOMH,MOPOKA,XMOHAP,AHTAPX,PATOPO,
ETEKOM,OKAYTH,TYTCTA,UBATET,ETAPAK,APMATY,APOMAT,AMOPMP
     
      A   A   X   E   U
    B   C   P   M   T   B
      *   *   *   *   *
    E   U   A   O   E   A
  T   H   X   H   K   T   P
    *   *   *   *   *   *
  P   M   P   T   A   A   O
    A   O   A   Y   P   M
      *   *   *   *   *
    K   P   T   T   M   O
      O   O   C   A   P
*/