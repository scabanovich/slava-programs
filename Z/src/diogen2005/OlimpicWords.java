package diogen2005;

public class OlimpicWords {
	String letters;
	String[] words = {
		"ANTVERPEN", "AMSTERDAM", "ATLANTA", "AFINy",
		"BARSELONA", "BERLIN", "LONDON", "LOSANDzELES",
		"MEL'BURN", "MONREAL'", "MOSKVA", "MEHIKO", "MuNHEN",
		"PARIz", "RIM", "SENTLUIS", "SEUL", "SIDNEY",
		"STOKGOL'M", "TOKIO", "HEL'SINKI",
	};
	int[][] digitWords;
	int[][] wordsForLetter;
	
	public OlimpicWords() {
		makeDigitWords();
		makeWordsForLetter();
	}
	
	void makeDigitWords() {
		letters = "";
		digitWords = new int[words.length][];
		for (int i = 0; i < words.length; i++) {
			digitWords[i] = new int[words[i].length()];
			for (int j = 0; j < words[i].length(); j++) {
				char c = words[i].charAt(j);
				int q = letters.indexOf(c);
				if(q < 0) {
					q = letters.length();
					letters += c;
				}
				digitWords[i][j] = q;
			}
		}
	}
	
	public char getLetter(int q) {
		return (q < 0 || q >= letters.length()) ? '\0' : letters.charAt(q);
	}
	
	void makeWordsForLetter() {
		wordsForLetter = new int[letters.length()][];
		for (int letter = 0; letter < letters.length(); letter++) {
			int c = 0;
			for (int i = 0; i < digitWords.length; i++) {
				if(contains(digitWords[i], letter)) ++c;
			}
			wordsForLetter[letter] = new int[c];			
			c = 0;
			for (int i = 0; i < digitWords.length; i++) {
				if(contains(digitWords[i], letter)) {
					wordsForLetter[letter][c] = i;
					++c;
				}
			}
			
		}
	}
	
	public boolean contains(int[] word, int letter) {
		for (int i = 0; i < word.length; i++) {
			if(word[i] == letter) return true;
		}
		return false;
	}
	
	public int[] getWordsContainingLetter(int letter) {
		return wordsForLetter[letter];
	}

}
