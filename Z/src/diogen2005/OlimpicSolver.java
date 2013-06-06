package diogen2005;

public class OlimpicSolver {
	OlimpicWords words;
	OlimpicField field;
	int[] form;
	
	int[] values;
	int[] valueDegeneracy;
	int[][] restriction;
	int[] usedWords;
	int[] usedLetters;

	int vacancies;

	int t;
	int[] wayCount;
	int[] way;
	int[][] waysW;
	int[][] waysP;
	int[][] waysD;

	public OlimpicSolver() {}
	
	public void setForm(int[] form) {
		this.form = form;
	}
	
	public void setWords(OlimpicWords words) {
		this.words = words;
	}
	
	public void setField(OlimpicField field) {
		this.field = field;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		vacancies = 0;
		for (int i = 0; i < field.size; i++) {
			if(form[i] == 1) ++vacancies;
		}
		wayCount = new int[vacancies + 1];
		way = new int[vacancies + 1];
		waysD = new int[vacancies + 1][200];
		waysP = new int[vacancies + 1][200];
		waysW = new int[vacancies + 1][200];

		values = new int[field.size];
		for (int i = 0; i < values.length; i++) values[i] = -1;
		valueDegeneracy = new int[field.size];
		
		usedWords = new int[words.digitWords.length];
		usedLetters = new int[words.letters.length()];
		
		restriction = new int[field.size][2];
		
		t = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == words.digitWords.length) return;
//		if(isFinished()) return;
		int letter = getNarrowLetter();
		if(letter < 0) return;
		
		boolean hw = isHalfWord();
		if(hw) {
			for (int i = 0; i < words.digitWords.length; i++) {
				if(usedWords[i] > 0) continue;
				int[] word = words.digitWords[i];
				int p = hwp;
				int d = hwd, d2 = d + 2;
				for (int j = 0; j < word.length && p >= 0; j++) {
					if(canAdd(i, p, d, '\0')) {
						waysD[t][wayCount[t]] = d;
						waysP[t][wayCount[t]] = p;
						waysW[t][wayCount[t]] = i;
						wayCount[t]++;
					}
					p = field.jump(p, d2);
				}
			}
		} else if(vacancies == 0) {
			for (int i = 0; i < words.digitWords.length; i++) {
				if(usedWords[i] > 0) continue;
				for (int p = 0; p < field.size; p++) {
					for (int d = 0; d < 2; d++) {
						if(canAdd(i, p, d, '\0') && crosses(i, p, d)) {
							waysD[t][wayCount[t]] = d;
							waysP[t][wayCount[t]] = p;
							waysW[t][wayCount[t]] = i;
							wayCount[t]++;
						}
					}
				}
			}
		} else {		
			int[] indices = words.getWordsContainingLetter(letter);
			for (int i = 0; i < indices.length; i++) {
				int w = indices[i];
				if(usedWords[w] > 0) continue;
				for (int p = 0; p < field.size; p++) {
					for (int d = 0; d < 2; d++) {
						if(canAdd(w, p, d, letter)) {
							waysD[t][wayCount[t]] = d;
							waysP[t][wayCount[t]] = p;
							waysW[t][wayCount[t]] = w;
							wayCount[t]++;
						}
					}
				}
			}
		}
	}
	
	int hwp;
	int hwd;
	
	boolean isHalfWord() {
		for (int p = 0; p < field.size; p++) {
			if(values[p] < 0) continue;
			for (int d = 0; d < 2; d++) {
				if(restriction[p][d] == 0) {
					int q = field.jump(p, d);
					if(q < 0 || values[q] < 0) continue;
					hwp = p;
					hwd = d;
					return true;
				}				
			}
		}
		return false;
	}
	
	int getNarrowLetter() {
		getDistribution();
		int q = 100;
		int letter = -1;
		for (int i = 0; i < words.letters.length(); i++) {
			if(usedLetters[i] > 0) continue;
			if(dist[i] < q) {
				if(dist[i] == 0) return -1;
				letter = i;
				q = dist[i];
			}
		}
		return letter;
	}
	
	int[] dist;
	
	public void getDistribution() {
		int[][] digitWords = words.digitWords;
		dist = new int[usedLetters.length];
		for (int w = 0; w < digitWords.length; w++) {
			if(usedWords[w] > 0) continue;
			for (int i = 0; i < digitWords[w].length; i++) {
				dist[digitWords[w][i]]++;
			}
		}
//		for (int i = 0; i < words.letters.length(); i++) {
//			System.out.println(words.getLetter(i) + ":" + dist[i]);
//		}
//		throw new RuntimeException();
	}
	
	boolean canAdd(int w, int p, int d, int letter) {
		int[] word = words.digitWords[w];
		if(restriction[p][d] > 0) return false;
		int q = p;
		int q0 = field.jump(q, d + 2);
		if(q0 >= 0) {
			if(form[q0] == 1 || values[q0] >= 0) return false;
		}
		boolean ok = (letter == '\0');
		for (int i = 0; i < word.length; i++) {
			int c = word[i];
			if(q < 0) return false;
			if(restriction[q][d] > 0) return false;
			if(values[q] >= 0 && values[q] != c) return false;
			if(usedLetters[c] == 0 && dist[c] == 1 && form[q] == 0) return false;
			if(word[i] == letter && form[q] == 1) ok = true;
			if(values[q] == 0 && form[q] == 1 && usedLetters[c] > 0) return false;
			q = field.jump(q, d);
		}
		if(q >= 0) {
			if(form[q] == 1 || values[q] >= 0) return false;
		}
		return ok;
	}
	
	boolean crosses(int w, int p, int d) {
		int[] word = words.digitWords[w];
		int q = p;
		boolean result = false;
		for (int i = 0; i < word.length; i++) {
			if(q < 0) return false;
			if(values[q] >= 0) result = true;
			q = field.jump(q, d);
		}
		return result;
	}
	
	void move() {
		int w = waysW[t][way[t]];
		int p = waysP[t][way[t]];
		int d = waysD[t][way[t]];
		add(w, p, d);
		++t;
		srch();
		way[t] = -1;
		
	}
	
	void add(int w, int p, int d) {
		usedWords[w] = 1;
		int[] word = words.digitWords[w];
		int q = p;
		int q0 = field.jump(q, d + 2);
		if(q0 >= 0) {
			restriction[q0][0]++;
			restriction[q0][1]++;
		}
		for (int i = 0; i < word.length; i++) {
			restriction[q][d]++;
			values[q] = word[i];
			valueDegeneracy[q]++;
			if(form[q] == 1) {
				usedLetters[word[i]]++;
				if(usedLetters[word[i]] == 1) vacancies--;
			}
			q = field.jump(q, d);
		}
		if(q >= 0) {
			restriction[q][0]++;
			restriction[q][1]++;
		}
	}
	
	void back() {
		--t;
		int w = waysW[t][way[t]];
		int p = waysP[t][way[t]];
		int d = waysD[t][way[t]];
		remove(w, p, d);
	}
	
	void remove(int w, int p, int d) {
		usedWords[w] = 0;
		int[] word = words.digitWords[w];
		int q = p;
		int q0 = field.jump(q, d + 2);
		if(q0 >= 0) {
			restriction[q0][0]--;
			restriction[q0][1]--;
		}
		for (int i = 0; i < word.length; i++) {
			restriction[q][d]--;
			valueDegeneracy[q]--;
			if(valueDegeneracy[q] == 0) {
				values[q] = -1;
			}
			if(form[q] == 1) {
				usedLetters[word[i]]--;
				if(usedLetters[word[i]] == 0) vacancies++;
			}
			q = field.jump(q, d);
		}
		if(q >= 0) {
			restriction[q][0]--;
			restriction[q][1]--;
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
			if(isFinished()) {
				printSolution();
			}
		}
	}
	
	
	protected boolean isFinished() {
		return vacancies == 0 && !isHalfWord();
	}
	
	void printSolution() {
		System.out.println("vacancies " + vacancies);
		for (int i = 0; i < field.size; i++) {
			int v = values[i];
			char c = (v < 0) ? '+' : words.getLetter(v);
			System.out.print(" " + c);
			if(field.x[i] == field.width - 1) System.out.println("");
		}
		System.out.println("");
	}

}
