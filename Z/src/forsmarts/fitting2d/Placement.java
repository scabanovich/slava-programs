package forsmarts.fitting2d;

import java.util.Arrays;

public class Placement {
	int index;
	int[] points;
	int cross;
	
	public Placement(int index, int[] points) {
		this.index = index;
		this.points = points;
		Arrays.sort(points);
	}

	public int getIndex() {
		return index;
	}
	
	public int[] getPoints() {
		return points;
	}
	
	public void setCross(int c) {
		cross = c;
	}
	
	public int getCross() {
		return cross;
	}
	
	public String getCode() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < points.length; i++) {
			sb.append(points[i]).append(",");
		}
		return sb.toString();
	}
}
