package slava.puzzle.cardnet.model;

public class CardnetGeneratorInfo {
	CardnetPuzzleInfo puzzle;
	int[] numbers;
	int[] suits;
	
	public void init(CardnetPuzzleInfo puzzle) {
		this.puzzle = puzzle;
		numbers = new int[puzzle.getField().getSize()];
		suits = new int[puzzle.getField().getSize()];
		clear();
	}
	
	public void clear() {
		for (int i = 0; i < numbers.length; i++) {
			numbers[i] = -1;
			suits[i] = -1;
		}
	}
	
	public int[] getNumbers() {
		return numbers; 
	}
	
	public int[] getSuits() {
		return suits;
	}
	
	public void setNumber(int p, int v) {
		numbers[p] = v;
	}
	
	public void setSuit(int p, int v) {
		suits[p] = v;
	}
	
}
