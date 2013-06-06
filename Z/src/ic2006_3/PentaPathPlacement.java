package ic2006_3;

public class PentaPathPlacement {
	int index;
	int[] dots;
	int[] points;
	int cross;
	
	public PentaPathPlacement(int index, int[] dots, int[] points) {
		this.index = index;
		this.points = points;
		this.dots = dots;
		sort();
	}

	public int getIndex() {
		return index;
	}
	
	public int[] getDots() {
		return dots;
	}
	
	public int[] getPoints() {
		return points;
	}
	
	void sort() {
		for (int i = 1; i < points.length; i++) {
			int j = i;
			while(isLessThanPrevious(j)) {
				int s = dots[j]; dots[j] = dots[j - 1]; dots[j - 1] = s;
				s = points[j]; points[j] = points[j - 1]; points[j - 1] = s;
				j--;
			}
		}
	}
	private boolean isLessThanPrevious(int i) {
		if(i == 0) return false;
		if(dots[i] < dots[i - 1]) return true;
		if(dots[i] > dots[i - 1]) return false;
		if(points[i] < points[i - 1]) return true;
		return false;
	}
	
	public String getCode() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < points.length; i++) {
			sb.append(dots[i]).append(':').append(points[i]).append(",");
		}
		return sb.toString();
	}
	
	public boolean hasAsDot(int p) {
		for (int i = 0; i < dots.length; i++) {
			if(dots[i] == 1 && points[i] == p) return true;
		}
		return false;
	}

}
