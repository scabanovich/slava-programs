package ic2006_2;

public class MirrorQuorterField extends SuperDamkaField {
	
	public int[] rotate(int[] state) {
		int[] s = new int[state.length];
		for (int p = 0; p < state.length; p++) {
			int xp = x[p], yp = y[p];
			int xq = width - 1 - yp, yq = xp;
			int q = xy[xq][yq];
			s[q] = state[p];
		}
		return s;
	}

	public int[] reflect(int[] state) {
		int[] s = new int[state.length];
		for (int p = 0; p < state.length; p++) {
			int q = xy[y[p]][x[p]];
			s[q] = state[p];
		}
		return s;
	}

}
