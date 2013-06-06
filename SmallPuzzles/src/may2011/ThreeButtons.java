package may2011;

public class ThreeButtons {
	
	ThreeButtonState startState;
	
	int finalNumberB;

	public void setStartState(ThreeButtonState startState) {
		this.startState = startState;
	}

	public void setFinalNumberB(int b) {
		finalNumberB = b;
	}

	public void solve() {
		for (int a = startState.a; a <= finalNumberB - startState.c; a++) {
			ThreeButtonState finalState = new ThreeButtonState(a, finalNumberB, finalNumberB - a);
			int n = isSolvable(finalState);
			if(n >= 0) {
				System.out.println(finalState + " in " + n + " moves.");
			}
		}
	}

	public int isSolvable(ThreeButtonState state) {
		if(!startState.canBePredecessor(state)) return -1;
		if(state.isNext(startState)) return 1;
		int n = isSolvable(state.back());
		return n < 0 ? n : n + 1;		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ThreeButtons p = new ThreeButtons();
		p.setFinalNumberB(49);
		p.setStartState(new ThreeButtonState(2, 9, 6));
		p.solve();

	}

}
/**
ƒостаточно в ответе написать последнюю строку, остальное восстанавливаетс€ однозначно.

1) 22:39:17
2) 25:44:19
3) ?
4) 54:34:20
5) 17:30:13 и 19:30:11
6) 20:29:49
7) 26:67:41 и 30:67:37
8) 27:10:17
9)67:30:37 за 5 ходов.
67:52:15 за 5 ходов.
67:42:25 за 4 ходов.
10)60:23:37


—лаварик

*/