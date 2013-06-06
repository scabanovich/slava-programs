package forsmarts;

public class DifferentLetters {
	int[][] field;
	String[] words;
	
	int[] letterUsage;
	int[] wordUsage;
	
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	public DifferentLetters() {}
	
	public void setField(int[][] f) {
		field = f;
	}
	
	public void setWords(String[] ws) {
		words = ws;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		way = new int[words.length + 1];
		wayCount = new int[words.length + 1];
		ways = new int[words.length + 1][words.length];
		letterUsage = new int[256];
		wordUsage = new int[words.length];
		for (int i = 0; i < words.length; i++) wordUsage[i] = -1;
		t = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == words.length) return;
		for (int i = 0; i < words.length; i++) {
			if(wordUsage[i] >= 0) continue;
			boolean b = add(t, i);
			remove(t, i);
			if(b) {
				ways[t][wayCount[t]] = i;
				wayCount[t]++;
			}
		}
	}
	
	boolean add(int line, int w) {
		boolean ok = true;
		wordUsage[w] = line;
		String word = words[w];
		for (int i = 0; i < field[line].length; i++) {
			if(field[line][i] == 0) continue;
			char c = word.charAt(i);
			int ci = (int)c;
			letterUsage[ci]++;
			if(letterUsage[ci] > 1) ok = false;
			
		}
		return ok;
	}
	
	void remove(int line, int w) {
		String word = words[w];
		wordUsage[w] = -1;
		for (int i = 0; i < field[line].length; i++) {
			if(field[line][i] == 0) continue;
			char c = word.charAt(i);
			int ci = (int)c;
			letterUsage[ci]--;
		}
	}
	
	void move() {
		int line = t;
		int w = ways[t][way[t]];
		add(line, w);
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int line = t;
		int w = ways[t][way[t]];
		remove(line, w);
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
			if(t == words.length) {
				printSolution();
			}
		}		
	}
	
	void printSolution() {
		System.out.println("Solution:");
		for (int i = 0; i < t; i++) {
			int k = ways[i][way[i]];
			System.out.println(words[k]);
		}
		for (int i = 0; i < words.length; i++) {
			if(i > 0) System.out.print(",");
			System.out.print("" + (wordUsage[i] + 1));
		}
		System.out.println("");
	}
	
	//////////////////////////////
	
	static int[][] FIELD_0 = {
		{1,0,1,1,1,0,0,1,0},
		{1,0,1,1,0,1,0,1,0},
		{1,0,0,1,0,1,1,0,1},
		{0,0,1,1,0,1,1,1,0}
	};
	
	static String[] WORDS_0 = {
		"construct",
		"dramatize",
		"fragility",
		"jobholder"
	};
	
	static int[][] FIELD_1 = {
		{1,0,1,1,0,0},
		{1,0,1,0,0,1},
		{1,0,0,0,1,1},
		{1,0,0,0,1,0},
		{1,0,0,1,0,1},
		{1,0,0,1,1,0},
		{1,0,0,0,0,1},
		{0,1,1,0,0,0},
		{0,1,0,1,0,0},
		{0,0,1,0,1,1}
	};

	static String[] WORDS_1 = {
		"boxing",
		"fumade",
		"hazard",
		"knight",
		"length",
		"opaque",
		"oxygen",
		"papacy",
		"reject",
		"wolves",
	};

	public static void main(String[] args) {
		DifferentLetters p = new DifferentLetters();
		p.setField(FIELD_1);
		p.setWords(WORDS_1);
		p.solve();
	}
	
}
