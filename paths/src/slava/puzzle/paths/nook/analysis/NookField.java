package slava.puzzle.paths.nook.analysis;

import com.slava.common.*;

public class NookField extends TwoDimField {
	int[][] distances;
	int[] filter;
	int filteredSize = 0;

	public static int[][] NOOK_ORTS = {
		{2,1}, {1,2}, {-1,2}, {-2,1}, {-2,-1}, {-1,-2}, {1,-2}, {2,-1}
	};
	
	public NookField() {
		setOrts(NOOK_ORTS);
	}
	
	public void setFilter(int[] f) {
		filter = f;
		filteredSize = getSize();
		for (int p = 0; p < size; p++) {
			if(filter[p] == 1) filteredSize--;
			for (int d = 0; d < 8; d++) {
				int q = jump(p, d);
				if(q >= 0 && (filter[p] == 1 || filter[q] == 1)) {
					jp[d][p] = -1;
				}
			}
		}
	}
	
	public int getFilteredSize() {
		return filteredSize;
	}
	
	public void setSize(int width, int height) {
		super.setSize(width, height);
		filteredSize = getSize();
		makeDistances();
	}
	
	public int getDistance(int p, int q) {
		return distances[p][q];
	}
	
	void makeDistances() {
		distances = new int[size][size];
		int delta = 0;
		for (int p = 0; p < size; p++) {
			for (int q = 0; q < size; q++) {
				if(q == p) {
					distances[p][q] = 0;
					delta++;
				} else {
					distances[p][q] = 100;
				}
			}
		}
		int it = 0;
		while (delta > 0) {
			delta = 0;
			it++;
			for (int p = 0; p < size; p++) {
				for (int d = 0; d < 8; d++) {
					int pa = jump(p, d);
					if(pa < 0) continue;
					for (int q = 0; q < size; q++) {
						if(distances[q][p] == 100) continue;
						if(distances[q][p] + 1 < distances[q][pa]) {
							delta++;
							distances[q][pa] = distances[q][p] + 1;
						}
					}
				}
			}
		}
	}
	
}
