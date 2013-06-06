package pqrst12;

public class CountingCountriesRandom {
	String[] words;
	int[][] digitedWords;
	int[] limits;

	int[] wordUsage;
	int[] letterUsage;
	int usedWordsCount;
	int usedLettersCount;
	
	int maxUsedLettersCount;
	int jokerCount;	

	public void setWords(String[] words) {
		this.words = words;
		digitedWords = new int[words.length][]; 
		for (int i = 0; i < words.length; i++) {
			digitedWords[i] = new int[words[i].length()];
			for (int k = 0; k < digitedWords[i].length; k++) {
				char c = words[i].substring(k, k + 1).toLowerCase().charAt(0);
				digitedWords[i][k] = ((int)c) - 97;
			}
		}
	}

	public void setLimits(int[] limits) {
		this.limits = limits;
	}
	
	public void solve() {
		init();
		anal();
	}

	void init() {
		letterUsage = new int[limits.length];
		wordUsage = new int[words.length];
		usedWordsCount = 0;
		usedLettersCount = 0;
		maxUsedLettersCount = 0;
		jokerCount = 0;
	}
	
	void addWord(int index) {
		usedWordsCount++;
		wordUsage[index]++;
		usedLettersCount += digitedWords[index].length;
		for (int i = 0; i < digitedWords[index].length; i++) {
			int ln = digitedWords[index][i];
			letterUsage[ln]++;
			if(letterUsage[ln] > limits[ln]) ++jokerCount;
		}
	}
	
	void removeWord(int index) {
		usedWordsCount--;
		wordUsage[index]--;
		usedLettersCount -= digitedWords[index].length;
		for (int i = 0; i < digitedWords[index].length; i++) {
			int ln = digitedWords[index][i];
			letterUsage[ln]--;
			if(letterUsage[ln] >= limits[ln]) --jokerCount;
		}
	}
	
	void anal() {
		while(true) flip();
	}
	
	void flip() {
		if(jokerCount <= 7) add(); else remove();
	}
	
	void add() {
		int index = (int)(words.length * Math.random());
		while(wordUsage[index] > 0) index = (int)(words.length * Math.random());
		addWord(index);
		check();
	}
	
	void remove() {
		int index = (int)(words.length * Math.random());
		while(wordUsage[index] == 0) index = (int)(words.length * Math.random());
		removeWord(index);
		check();
	}
	
	void check() {
		if(jokerCount <= 7) {
			if(usedLettersCount > maxUsedLettersCount) {
				maxUsedLettersCount = usedLettersCount;
				printSolution();
			}
		}
	}

	void printSolution() {
		System.out.print(maxUsedLettersCount + ":");
		for (int i = 0; i < wordUsage.length; i++) {
			if(wordUsage[i] > 0) System.out.print(" " + words[i] + ",");
		}
		System.out.println("");
	}

}
