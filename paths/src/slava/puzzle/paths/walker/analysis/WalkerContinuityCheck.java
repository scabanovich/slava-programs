package slava.puzzle.paths.walker.analysis;

import com.slava.common.RectangularField;

public class WalkerContinuityCheck {

	public static boolean check(RectangularField field, int[] filter,
			int[][] state, int[] usage) {
		int[] visited = new int[state.length];
		int[] stack = new int[state.length];
		int g = 0;
		int gu = 0;
		for (int p = 0; p < state.length; p++) {
			if(usage[p] > 0 || visited[p] > 0 || filter[p] == 1) continue;
			boolean access = false;
			int vc = 0;
			int vv = 1;
			stack[0] = p;
			visited[p] = 1;
			while(vc < vv) {
				int q = stack[vc];
				for (int d = 0; d < 4; d++) {
					if(state[q][d] == 0) continue;
					int s = field.jump(q, d);
					if(s < 0 || visited[s] > 0) continue;
					if(usage[s] > 0) {
						access = true;
					} else {
						visited[s] = 1;
						stack[vv] = s;
						vv++;
					}
				}
				vc++;
			}
			g++;
			if(!access) gu++;
		}
		return gu == 0 || g < 2;
	}
	
	public static int getCircleLength(RectangularField field, int[] filter,
			int[][] state, int[] usage, int start) {
		if(usage[start] < 2) return -1;
		int d0 = -1;
		for (int d = 0; d < 4; d++) {
			if(state[start][d] == 1) {
				d0 = d;
				break;
			}
		}
		if(d0 < 0) return -1;
		int current = field.jump(start, d0);
		int length = 1;
		while(current != start) {
			if(usage[current] != 2) return -1;
			int d1 = WalkerPathsSolver.REVERSE[d0];
			d0 = -1;
			for (int d = 0; d < 4; d++) {
				if(state[current][d] == 1 && d != d1) {
					d0 = d;
					break;
				}
			}
			if(d0 < 0) return -1;
			current = field.jump(current, d0);
			length++;			
		}		
		return length;
	}

}
