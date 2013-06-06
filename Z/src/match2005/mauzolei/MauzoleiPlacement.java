package match2005.mauzolei;

import java.util.Arrays;

public class MauzoleiPlacement {
	int index;
	int[] points;
	
	public MauzoleiPlacement(int index, int[] points) {
		this.index = index;
		this.points = points;
		Arrays.sort(points);
	}
	
	public boolean equals(MauzoleiPlacement p) {
		if(points.length != p.points.length) return false;
		for (int i = 0; i < points.length; i++) {
			if(points[i] != p.points[i]) return false;
		}
		return true;
	}
	
	public int getIndex() {
		return index;
	}
	
	public int[] getPoints() {
		return points;
	}
	
	public boolean contains(int p) {
		for (int i = 0; i < points.length; i++) {
			if(p == points[i]) return true;
		}
		return false;
	}

}
