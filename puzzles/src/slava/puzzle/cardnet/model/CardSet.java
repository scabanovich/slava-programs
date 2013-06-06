package slava.puzzle.cardnet.model;

public class CardSet {
	int numberCount;
	int suitCount;
	int size;
	int[] number;
	int[] suit;
	int[][] card;
	String[] names;
	
	static String[] VALUES = {"6", "7", "8", "9", "10", "J", "Q", "K", "A"};
	static String[] SUITES = {"s", "c", "d", "h"};
	
	public CardSet() {
		init(9, 4);
	}
	
	public void init(int numberCount, int suitCount) {
		this.numberCount = numberCount;
		this.suitCount = suitCount;
		size = numberCount * suitCount;
		number = new int[size];
		suit = new int[size];
		card = new int[numberCount][suitCount];
		names = new String[size];
		for (int i = 0; i < size; i++) {
			number[i] = i % numberCount;
			suit[i] = i / numberCount;
			card[number[i]][suit[i]] = i;
			names[i] = getNumberAsString(getNumber(i)) + getSuitAsString(getSuit(i));
		}
	}
	
	public int getSize() {
		return size;
	}
	
	public int getNumberCount() {
		return numberCount;
	}
	
	public int getSuitCount() {
		return suitCount;
	}
	
	public int getNumber(int card) {
		return number[card];
	}
	
	public int getSuit(int card) {
		return suit[card];
	}
	
	public int getCard(int number, int suit) {
		return card[number][suit];
	}
	
	public String getNumberAsString(int number) {
		return (number < 0 || number >= getNumberCount()) ? "" : VALUES[number];
	}
	
	public String getSuitAsString(int suit) {
		return (suit < 0 || suit >= getSuitCount()) ? "" : SUITES[suit];
	}
	
	public String toString(int card) {
		return (card < 0 || card >= size) ? "" : names[card];
	}
	
	
	public int getNumber(char c) {
		for (int i = 0; i < VALUES.length; i++) {
			if(VALUES[i].toLowerCase().charAt(0) == c) return i;
		}
		return -1;
	}

	public int getSuit(char c) {
		for (int i = 0; i < SUITES.length; i++) {
			if(SUITES[i].toLowerCase().charAt(0) == c) return i;
		}
		return -1;
	}
}
