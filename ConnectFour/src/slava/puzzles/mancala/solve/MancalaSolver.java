package slava.puzzles.mancala.solve;

public class MancalaSolver {

	public int solve(MancalaState state) {
		
		return 0;
	}

	public static void main(String[] args) {
		int[] state = new int[]{4,4,3,3,3,3, 5,0,0,3,3,3};
		MancalaState s = new MancalaState(MancalaState.FIRST, state);
		boolean result = s.solve((short)0);
		System.out.println("Result=" + result);
	}
}
