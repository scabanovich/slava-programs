package ik6_1;

public class Cards_6_1_Field {
	int[][] neighbours = {
		{1,4,7},    //0
		{0,2},      //1
		{1,3},      //2
		{2,5,9},    //3
		{0,6,7},    //4
		{3,9,10},   //5
		{4,11},     //6
		{0,4,8,12}, //7
		{7,9},      //8
		{3,5,8,15}, //9
		{5,16},     //10
		{6,12,17},  //11
		{7,11,13},  //12
		{12,14},    //13
		{13,15},    //14
		{9,14,16},  //15
		{10,15,22}, //16
		{11,18,23}, //17
		{17,19,23,24}, //18
		{18,20,24}, //19
		{19,21,27}, //20
		{20,22,27}, //21
		{16,21,28}, //22
		{17,18,29}, //23
		{18,19,25,29,30}, //24
		{24,26,30}, //25
		{25,27,33}, //26
		{20,21,26,33,34}, //27
		{21,22,34}, //28
		{23,24,30}, //29
		{24,25,29,31}, //30
		{30,32},    //31
		{31,33},    //32
		{26,27,32,34}, //33
		{27,28,33}  //34
	};
	
	Cards_6_1_CellSet[] sets = new Cards_6_1_CellSet[]{
		new Cards_6_1_CellSet(
			Cards_6_1_CellSet.SUIT_KIND,
			new int[]{30,31,32,33}, 
			new int[]{1,1,1,0} 
		),	
		new Cards_6_1_CellSet(
			Cards_6_1_CellSet.SUIT_KIND,
			new int[]{29,30,33,34}, 
			new int[]{1,1,0,0} 
		),	
		new Cards_6_1_CellSet(
			Cards_6_1_CellSet.VALUE_KIND,
			new int[]{23,24,25,26,27,28}, 
			new int[]{0,1,0,1,1,1,0,0,1} 
		),	
		new Cards_6_1_CellSet(
			Cards_6_1_CellSet.SUIT_KIND,
			new int[]{17,18,19,20,21,22}, 
			new int[]{1,1,0,1} 
		),	
		new Cards_6_1_CellSet(
			Cards_6_1_CellSet.SUIT_KIND,
			new int[]{6,12,15,10}, 
			new int[]{1,0,1,1} 
		),	
		new Cards_6_1_CellSet(
			Cards_6_1_CellSet.SUIT_KIND,
			new int[]{4,0,3,5}, 
			new int[]{0,1,1,1} 
		),	
		new Cards_6_1_CellSet(
			Cards_6_1_CellSet.VALUE_KIND,
			new int[]{0,1,2,3}, 
			new int[]{0,0,1,0,1,0,1,1,0} 
		),	
		new Cards_6_1_CellSet(
			Cards_6_1_CellSet.VALUE_KIND,
			new int[]{4,6,11,17,23,29}, 
			new int[]{1,0,0,1,0,0,1,0,1} 
		),	
		new Cards_6_1_CellSet(
			Cards_6_1_CellSet.SUIT_KIND,
			new int[]{1,7,13,19,25,31}, 
			new int[]{1,1,0,1} 
		),	
		new Cards_6_1_CellSet(
			Cards_6_1_CellSet.VALUE_KIND,
			new int[]{2,8,14,20,26,32}, 
			new int[]{0,1,1,0,1,0,0,0,0} 
		),	
		new Cards_6_1_CellSet(
			Cards_6_1_CellSet.SUIT_KIND,
			new int[]{2,9,14,20,26,32}, 
			new int[]{0,1,1,1} 
		),	
		new Cards_6_1_CellSet(
			Cards_6_1_CellSet.VALUE_KIND,
			new int[]{5,16,21,34}, 
			new int[]{1,1,0,1,0,0,0,0,0} 
		),	
		new Cards_6_1_CellSet(
			Cards_6_1_CellSet.SUIT_KIND,
			new int[]{5,10,16,22,28,34}, 
			new int[]{0,1,1,1} 
		),	
	};
	
	int size = 35;
	
	int suitCount;
	int valueCount;
	int cardCount;
	
	int[] cardSuit;
	int[] cardValue;
	
	public Cards_6_1_Field() {}
	
	void setCardPackSize(int suitCount, int valueCount) {
		this.suitCount = suitCount;
		this.valueCount = valueCount;
		cardCount = suitCount * valueCount;
		cardSuit = new int[cardCount];
		cardValue = new int[cardCount];
		for (int i = 0; i < cardCount; i++) {
			cardValue[i] = i % valueCount;
			cardSuit[i] = i / valueCount;
		}
		
	}
	
	boolean areCloseCards(int c1, int c2) {
		return cardSuit[c1] == cardSuit[c2] || Math.abs(cardValue[c1] - cardValue[c2]) < 3;
	}
	
	static String[] CARD_VALUES = {"2","3","4","5","6","7","8","9","10",};
	static String[] CARD_SUITS = {"S","C","D","H"};
	
	public String getCardName(int c) {
		return CARD_VALUES[cardValue[c]] + CARD_SUITS[cardSuit[c]];
	}

}
