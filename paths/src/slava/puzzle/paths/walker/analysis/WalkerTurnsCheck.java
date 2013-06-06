package slava.puzzle.paths.walker.analysis;

import com.slava.common.RectangularField;

public class WalkerTurnsCheck {
	public static int NO_DATA = 0;
	public static int STRAIGHT = 1;
	public static int TURN = 2;

	public static boolean check(RectangularField field, 
			int[] filter, int[] turns,
			int[][] state, int[] usage) {
		if(turns == null) return true;
		for (int p = 0; p < usage.length; p++) {
			if(turns[p] == NO_DATA) continue;
			int k = getTurnKind(field, filter, state[p], usage, p);
			if(k == NO_DATA) continue;
			if(k != turns[p]) return false;
		}
		return true;
	}
	
	static int getTurnKind(RectangularField field, 
			int[] filter,
			int[] state, int[] usage, int p) {
		if(usage[p] == 2) {
			for (int d = 0; d < 2; d++) {
				if(state[d] == 1 && state[WalkerPathsSolver.REVERSE[d]] == 1) return STRAIGHT;
			}
			for (int d = 0; d < 4; d++) {
				if(state[d] == 1 && state[WalkerPathsSolver.NEXT[d]] == 1) return TURN;
			}
		} else if(usage[p] == 1) {
			for (int d = 0; d < 4; d++) {
				if(state[d] != 1) continue;
				int dr = WalkerPathsSolver.REVERSE[d];
				int q = field.jump(p, dr);
				if(q < 0 || filter[q] == 1 || usage[q] == 2) {
					return TURN;
				}
			}
		}
		return NO_DATA;
	}

}
