package slava.puzzle.cardsum.model;

public class CardSumField {
	int width;
	int height;
	int size;
	int[] x;
	int[] y;
	int[][] jp;
	
	int[] cards;
	
	String[] visuals;
	int[] values;
	int[] sums;
	
	public CardSumField() {
		setSize(9, 4);
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		size = width * height;
		x = new int[size];
		y = new int[size];
		for (int i = 0; i < size; i++) {
			x[i] = (i % width);
			y[i] = (i / width);
		}
		jp = new int[8][size];
		for (int i = 0; i < size; i++) {
			jp[0][i] = (x[i] == width - 1) ? -1 : i + 1;
			jp[1][i] = (y[i] == height - 1) ? -1 : i + width;
			jp[2][i] = (x[i] == 0) ? -1 : i - 1;
			jp[3][i] = (y[i] == 0) ? -1 : i - width;
			jp[4][i] = (x[i] == width - 1 || y[i] == height - 1) ? -1 : i + 1 + width;
			jp[5][i] = (x[i] == 0 || y[i] == height - 1) ? -1 : i - 1 + width;
			jp[6][i] = (x[i] == 0 || y[i] == 0) ? -1 : i - 1 - width;
			jp[7][i] = (x[i] == width - 1 || y[i] == 0) ? -1 : i + 1 - width;
		}
		cards = new int[size];
		for (int i = 0; i < size; i++) cards[i] = -1;
		sums = new int[width];
		for (int i = 0; i < width; i++) sums[i] = -1;
		values = new int[width];
		if(width == 9) {
			values = new int[]{2,3,4,6,7,8,9,10,11};
		}
		visuals = new String[width];
		if(width == 9) {
			visuals = new String[]{"J", "Q", "K", "6", "7", "8", "9", "10", "A"};
		}
	}	
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int size() {
		return size;
	}
	
	public int x(int i) {
		return x[i];
	}
	
	public int y(int i) {
		return y[i];
	}
	
	public int jump(int i, int d) {
		return jp[d][i];
	}
	
	public int getCard(int i) {
		return cards[i];
	}
	
	public void setCard(int i, int v) {
		cards[i] = v;
	}
	
	public int getValue(int card) {
		return values[card];
	}
	
	public int getValueAt(int i) {
		int card = getCard(i);
		return (card < 0) ? -1 : values[card];
	}
	
	public String getVisual(int card) {
		return (card < 0 || card >= width) ? "" : visuals[card];
	}
	
	public int getSum(int column) {
		return sums[column];
	}
	
	public void setSum(int column, int v) {
		sums[column] = v;
	}

}
