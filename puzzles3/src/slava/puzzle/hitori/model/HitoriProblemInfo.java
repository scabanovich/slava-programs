package slava.puzzle.hitori.model;

public class HitoriProblemInfo {
	/**
	 * Numbers in cells.
	 */
	int[] numbers;
	
	/**
	 * -1 - not assigned, 0 - white, 1 - black
	 */
	int[] solution = null;
	
	public HitoriProblemInfo() {}

	public void setSolution(int[] solution) {
		this.solution = solution;
	}
	
	public int[] getNumbers() {
		return numbers;
	}
	
	public void setNumbers(int[] ns) {
		if(numbers == null || numbers.length != ns.length) {
			numbers = ns;
		} else {
			for (int i = 0; i < ns.length; i++) numbers[i] = ns[i];
		}
	}

	public int[] getSolution() {
		return solution;
	}
	
}
