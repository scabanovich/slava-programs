package ik4;

public class AlphabetAnalyzer {
	AlphabetField field;
	int[][] hRestrictions; //z, y
	int[][] vRestrictions; //z, x
	int size;

	int STOP_LAYER = 3;
	int STOP_AT = 6 * (STOP_LAYER + 1);

	int[] occupation;
	int[] usedLetters;
	int usedLettersCount;
	
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;
	
	public void setField(AlphabetField field) {
		this.field = field;
		size = field.size; 
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		occupation = new int[size];
		for (int i = 0; i < size; i++) {
			occupation[i] = -1;
		}
		usedLetters = new int[AlphabetLetters.LETTERS.length];
		place = new int[size + 1];
		wayCount = new int[size + 1];
		way = new int[size + 1];
		ways = new int[size + 1][AlphabetLetters.LETTERS.length + 1];
		int t = 0;
		usedLettersCount = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(usedLettersCount == STOP_AT) return;
		place[t] = getNextPlace();
		if(place[t] < 0) return;
		for (int i = 0; i < AlphabetLetters.LETTERS.length; i++) {
			if(usedLetters[i] == 1) continue;
			if(!canAdd(AlphabetLetters.LETTERS[i], place[t])) continue;
			ways[t][wayCount[t]] = i;
			wayCount[t]++;
		}
		ways[t][wayCount[t]] = -1;
		wayCount[t]++;
	}
	
	int getNextPlace() {
		if(t == 0) return 0;
		if(usedLettersCount == 24) return -1;
		int p = place[t - 1];
		int z = field.z[p];
		if(usedLettersCount % 6 == 0 && usedLettersCount / 6 == z + 1) {
			p = field.xyz(0, 0, z + 1);
			return (!checkLayer(z, p) || !additionalCheckLayer(z)) ? -1 : p;
		} 
		++p;
		if(field.z[p] > z) return -1;
		while(!field.isMainGridPoint(p)) {
			++p;
			if(field.z[p] > z) return -1;
		}
		if(!checkLayer(z, p)) return -1;
		return p;
	}
	
	boolean additionalCheckLayer(int z) {
		if(z == 0) {
			return occupation[field.xyz(8,13,z)] >= 0;
		} else if(z == 1) {
			return occupation[field.xyz(3,11,z)] >= 0;
		} else if(z == 2) {
			return occupation[field.xyz(2,3,z)] >= 0;
		} else if(z == 3) {
			return occupation[field.xyz(7,2,z)] >= 0;
		}
		return true;
	}
	
	boolean checkLayer(int z, int p) {
		for (int c = 0; c < 7; c++) {
			if(!checkColumn(z, c, p)) return false;
		}
		for (int r = 0; r < 7; r++) {
			if(!checkRow(z, r, p)) return false;
		}
		return true;
	}
	
	boolean checkRow(int z, int r, int pc) {
		int n = 0;
		int u = 0;
		int y = 2 * r + 1;
		for (int x = 0; x < field.width; x++) {
			int p = field.xyz(x, y, z);
			if(occupation[p] >= 0) n++; else if(p >= pc) u++;
		}
		if(n > hRestrictions[z][r]) return false;
		if(n + u < hRestrictions[z][r]) return false;
		return true;
	}
	
	boolean checkColumn(int z, int c, int pc) {
		int n = 0;
		int u = 0;
		int x = 2 * c + 1;
		for (int y = 0; y < field.height; y++) {
			int p = field.xyz(x, y, z);
			if(occupation[p] >= 0) n++; else if(p >= pc) u++;
		}
		if(n > vRestrictions[z][c]) return false;
		if(n + u < vRestrictions[z][c]) return false;
		return true;
	}
	
	boolean canAdd(int[][] letter, int p) {
		for (int dy = 0; dy < letter.length; dy++) {
			for (int dx = 0; dx < letter[0].length; dx++) {
				if(letter[dy][dx] == 0) continue;
				int q = field.jump(p, dx, dy);
				if(q < 0 || occupation[q] >= 0) return false;
			}
		}
		return true;
	}
	
	void move() {
		int f = ways[t][way[t]];
		if(f >= 0) {
			addFigure(f, place[t]);
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void addFigure(int f, int p) {
		usedLetters[f] = 1;
		usedLettersCount++;
		int[][] letter = AlphabetLetters.LETTERS[f];
		for (int dy = 0; dy < letter.length; dy++) {
			for (int dx = 0; dx < letter[0].length; dx++) {
				if(letter[dy][dx] == 0) continue;
				int q = field.jump(p, dx, dy);
				occupation[q] = f;
			}
		}
	}
	
	void back() {
		--t;
		int f = ways[t][way[t]];
		if(f >= 0) {
			removeFigure(f, place[t]);
		} 
	}

	void removeFigure(int f, int p) {
		usedLetters[f] = 0;
		usedLettersCount--;
		int[][] letter = AlphabetLetters.LETTERS[f];
		for (int dy = 0; dy < letter.length; dy++) {
			for (int dx = 0; dx < letter[0].length; dx++) {
				if(letter[dy][dx] == 0) continue;
				int q = field.jump(p, dx, dy);
				occupation[q] = -1;
			}
		}
	}
	

	int um = 0;	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] >= wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(usedLettersCount > um) {
				um = usedLettersCount;
				System.out.println("usedLettersCount=" + usedLettersCount + " t=" + t);
				
			}
			if(usedLettersCount == STOP_AT && checkLayer(STOP_LAYER, size) && additionalCheckLayer(STOP_LAYER)) {
				solutionFound();
			}
		}
	}
	
	void solutionFound() {
		++solutionCount;
		System.out.println("-----------------------------");
		int lc = usedLettersCount / 6;		
		for (int i = 0; i < lc; i++) printLayer(i);
	}
	
	void printLayer(int z) {
		for (int y = 0; y < field.height; y++) {
			for (int x = 0; x < field.width; x++) {
				int p = field.xyz(x, y, z);
				int v = occupation[p];
				char c = (v < 0) ? '+' : (char)(97 + v);
				System.out.print(" " + c);
			}
			System.out.println("");
		}
		System.out.println("");
	}
	
	static int[][] H_RESTRICTIONS = new int[][]{
		{4,5,3,2,2,4,1}, //1
		{2,4,4,4,2,5,3}, //2
		{2,2,2,3,3,3,2}, //3
		{3,3,2,3,3,4,4}, //4
	};

	static int[][] V_RESTRICTIONS = new int[][]{
		{3,3,4,4,2,3,3}, //1
		{4,6,3,0,3,4,1}, //2
		{4,4,2,4,3,3,3}, //3
		{2,3,3,1,1,6,5}, //4
	};

	public static void main(String[] args) {
		AlphabetField field = new AlphabetField();
		field.setSize(15, 15, 4);
		AlphabetAnalyzer analyzer = new AlphabetAnalyzer();
		analyzer.setField(field);
		analyzer.hRestrictions = H_RESTRICTIONS;
		analyzer.vRestrictions = V_RESTRICTIONS;
		analyzer.solve();
		System.out.println("solutionCount=" + analyzer.solutionCount);		
	}

}
