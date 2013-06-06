package pqrst12;

import java.util.*;

public class CountingCountries {
	String[] words;
	int[][] digitedWords;
	int[] limits;
	int maxJokerCount = 7;
	
	int[] letterFrequencies;
	boolean[] isFrequent;
	int[][] weights;
	int frequentLetterLimit;
	int[] ranged;
	
	int t;
	int[] way;
	int[] wayCount;
	int[][] ways;
	
	int[] letterUsage;
	int usedWordsCount;
	int usedLettersCount;
	
	int maxUsedLettersCount;
	int jokerCount;	
	
	int goodLetterCount;
	int rareLettersJokerCount;
	
	int desiredMinimum = 85;
	
	public void setWords(String[] words) {
		this.words = words;
		digitedWords = new int[words.length][]; 
		letterFrequencies = new int[limits.length];
		for (int i = 0; i < words.length; i++) {
			digitedWords[i] = new int[words[i].length()];
			for (int k = 0; k < digitedWords[i].length; k++) {
				char c = words[i].substring(k, k + 1).toLowerCase().charAt(0);
				digitedWords[i][k] = ((int)c) - 97;
				letterFrequencies[digitedWords[i][k]]++;
			}
/*
			for (int k = 0; k < digitedWords[i].length; k++) {
				System.out.print(" " + digitedWords[i][k]);
			}
			System.out.println("");
*/
		}
	}
	
	public void setLimits(int[] limits) {
		this.limits = limits;
	}
	
	public void setFrequencies() {
		int FREQUENT_LETTER_THRESHOLD = 28;
		isFrequent = new boolean[letterFrequencies.length];
		for (int i = 0; i < isFrequent.length; i++) {
			isFrequent[i] = letterFrequencies[i] > FREQUENT_LETTER_THRESHOLD;
		}
		weights = new int[words.length][2];
		frequentLetterLimit = maxJokerCount;
		for (int i = 0; i < letterFrequencies.length; i++) {
			if(isFrequent[i]) {
				frequentLetterLimit += limits[i];
			}
		}
		for (int i = 0; i < words.length; i++) {
			for (int k = 0; k < digitedWords[i].length; k++) {
				int letter = digitedWords[i][k];
				if(isFrequent[letter]) weights[i][0]++; else weights[i][1]++;
			}
		}
		
		for (int i = 0; i < letterFrequencies.length; i++) {
			System.out.println(letterFrequencies[i]);
		}
		System.out.println("frequentLetterLimit=" + frequentLetterLimit);
///		for (int i = 0; i < weights.length; i++) {
///			if(weights[i][0] <= weights[i][1])
///			System.out.println(words[i] + " " + weights[i][0] + ":" + weights[i][1]);
///		}
		class C {
			int q;
			C(int q) {
				this.q = q;
			}
		}
		C[] os = new C[words.length];
		for (int i = 0; i < os.length; i++) os[i] = new C(i);
		Arrays.sort(os, new Comparator() {
			public int compare(Object o1, Object o2) {
				C c1 = (C)o1;
				C c2 = (C)o2;
				int i1 = c1.q;
				int i2 = c2.q;
				double d1 = 1d * weights[i1][1] / weights[i1][0];
				double d2 = 1d * weights[i2][1] / weights[i2][0];
				return (d1 > d2) ? -1 : (d1 < d2) ? 1 : 0;  
			}
		});
		ranged = new int[words.length];
		for (int i = 0; i < os.length; i++) ranged[i] = os[i].q;
		
		String[] words_ = new String[words.length];
		int[][] digitedWords_ = new int[digitedWords.length][];
		int[][] weights_ = new int[weights.length][];
		for (int i = 0; i < ranged.length; i++) {
			int k = ranged[i];
			words_[i] = words[k];
			digitedWords_[i] = digitedWords[k];
			weights_[i] = weights[k];
			ranged[i] = i;
		}
		words = words_;
		digitedWords = digitedWords_;
		weights = weights_;
		for (int i = 0; i < ranged.length; i++) {
			int k = ranged[i];
			System.out.println(k + " " + words[k] + " " + weights[k][0] + ":" + weights[k][1]);
		}
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		wayCount = new int[words.length + 1];
		way = new int[words.length + 1];
		ways = new int[words.length + 1][2];
		letterUsage = new int[limits.length];
		usedWordsCount = 0;
		usedLettersCount = 0;
		maxUsedLettersCount = 0;
		jokerCount = 0;
		goodLetterCount = 0;
		rareLettersJokerCount = 0;
		t = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == words.length) return;
		if(!canReachCount(desiredMinimum)) return;
		addWord(t);
		if(jokerCount <= maxJokerCount) {
			wayCount[t] = 2;
			ways[t][0] = 1;
			ways[t][1] = 0;
		} else {
			wayCount[t] = 1;
			ways[t][0] = 0;
		}
		removeWord(t);
	}
	
	boolean canReachCount(int c) {
		int flc = frequentLetterLimit - rareLettersJokerCount;
		int lc = usedLettersCount;
		int glc = goodLetterCount;
		for (int i = 0; i < ranged.length && glc < flc; i++) {
			if(ranged[i] < t) continue;
			glc += weights[ranged[i]][0];
			if(glc > flc) {
				lc += digitedWords[ranged[i]].length * (weights[ranged[i]][0] + flc - glc) / weights[ranged[i]][0];
			} else {
				lc += digitedWords[ranged[i]].length;
			}
		}
		return (lc >= c);
	}
	
	void move() {
		int k = ways[t][way[t]];
		if(k == 1) addWord(t);
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int k = ways[t][way[t]];
		if(k == 1) removeWord(t);
	}
	
	void addWord(int index) {
		usedWordsCount++;
		usedLettersCount += digitedWords[index].length;
		for (int i = 0; i < digitedWords[index].length; i++) {
			int ln = digitedWords[index][i];
			letterUsage[ln]++;
			if(letterUsage[ln] > limits[ln]) {
				++jokerCount;
				if(!isFrequent[ln]) ++rareLettersJokerCount;
			}
		}
		goodLetterCount += weights[index][0];
	}
	
	void removeWord(int index) {
		usedWordsCount--;
		usedLettersCount -= digitedWords[index].length;
		for (int i = 0; i < digitedWords[index].length; i++) {
			int ln = digitedWords[index][i];
			letterUsage[ln]--;
			if(letterUsage[ln] >= limits[ln]) {
				--jokerCount;
				if(!isFrequent[ln]) --rareLettersJokerCount;
			}
		}
		goodLetterCount -= weights[index][0];
	}

	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			++way[t];
			move();
			if(usedLettersCount > maxUsedLettersCount || (usedLettersCount == maxUsedLettersCount && usedLettersCount >= desiredMinimum && ways[t - 1][way[t - 1]] == 1)) {
				maxUsedLettersCount = usedLettersCount;
				printSolution();
			}
		}
	}
	
	void printSolution() {
		System.out.print(maxUsedLettersCount + ":");
		for (int i = 0; i < t; i++) {
			int k = ways[i][way[i]];
			if(k == 1) System.out.print(" " + words[i] + ",");
		}
		System.out.println("");
	}

	public static void main(String[] args) {
		CountingCountries c = new CountingCountries();
		c.setLimits(LIMITS);
		c.setWords(COUNTRIES);
		c.setFrequencies();
		c.solve();
	}
	
	static String[] COUNTRIES = new String[]{
		"Afghanistan", "Algeria", "Angola", "Antarctica", "Argentina",
		"Armenia", "Australia", "Austria", "Azerbaijan", "Belarus",
		"Belgium", "Bolivia", "Brazil", "Bulgaria", "Cambodia",
		"Cameroon", "Canada", "Chile", "China", "Colombia",
		"Cuba", "Denmark", "Ecuador", "Egypt", "Estonia",
		"Ethiopia", "Finland", "France", "Georgia", "Germany",
		"Ghana", "Grenada", "Guadeloupe", "Guatemala", "Haiti",
		"Honduras", "Hungary", "Iceland", "India", "Indonesia",
		"Iran", "Iraq", "Ireland", "Israel", "Italy",
		"Jamaica", "Japan", "Kenya", "Korea", "Kuwait",
		"Laos", "Latvia", "Libya", "Liechtenstein", "Lithuania",
		"Luxembourg", "Malaysia", "Maldives", "Malta", "Mexico",
		"Moldova", "Mongolia", "Morocco", "Mozambique", "Nepal",
		"Netherlands", "Nicaragua", "Nigeria", "Norway", "Pakistan",
		"Panama", "Paraguay", "Peru", "Poland", "Portugal",
		"Qatar", "Romania", "Russia", "Rwanda", "Senegal",
		"Slovakia", "Slovenia", "Somalia", "Spain", "Sudan",
		"Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan",
		"Thailand", "Tunisia", "Turkey", "Uganda", "Ukraine",
		"Uzbekistan", "Venezuela", "Yemen", "Zambia", "Zimbabwe"
	};
	
	static int[] LIMITS = new int[]{
		6,3,3,3,6,3,3,3,6,3,3,3,3,3,6,3,3,3,3,3,6,3,3,3,3,3
	};

}

/*
85: Chile, Cuba, Egypt, Guadeloupe, Honduras, Hungary, Kuwait, Mexico, Moldova, Slovakia, Spain, Turkey, Zimbabwe
85: China, Cuba, Egypt, Guadeloupe, Honduras, Hungary, Kuwait, Mexico, Moldova, Slovakia, Spain, Turkey, Zimbabwe
85: Chile, Ecuador, Egypt, Honduras, Hungary, Kuwait, Mexico, Moldova, Paraguay, Slovakia, Uzbekistan, Zimbabwe
*/
