package olia;

public class CardPack {
	int valueCount;
	int suitCount;
	int size;
	int[] suits, values;
	int[][] index;
	
	public CardPack() {}
	
	public void init(int valueCount, int suitCount) {
		this.valueCount = valueCount;
		this.suitCount = suitCount;
		size = valueCount * suitCount;
		suits = new int[size];
		values = new int[size];
		index = new int[valueCount][suitCount];
		for (int i = 0; i < size; i++) {
			suits[i] = i / valueCount;
			values[i] = i % valueCount;
			index[values[i]][suits[i]] = i;
		}
	}
	
	public int getIndex(int value, int suit) {
		return value < 0 || suit < 0 || value >= valueCount || suit >= suitCount ? -1 : index[value][suit];
	}
	
	public int getSuit(int card) {
		return card < 0 || card >= size ? -1 : suits[card];
	}
	
	public int getValue(int card) {
		return card < 0 || card >= size ? -1 : values[card];
	}
	
	public boolean areOfEqualValueOrSuit(int card1, int card2) {
		return getSuit(card1) == getSuit(card2) || getValue(card1) == getValue(card2);
	}

}
