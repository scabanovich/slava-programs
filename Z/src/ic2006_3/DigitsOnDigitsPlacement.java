package ic2006_3;

public class DigitsOnDigitsPlacement {
	int index;
	int[] points;
	int[] values;
	int cross;
	
	public DigitsOnDigitsPlacement(int index, int[] values, int[] points) {
		this.index = index;
		this.points = points;
		this.values = values;
		sort();
	}

	public int getIndex() {
		return index;
	}
	
	public int[] getValues() {
		return values;
	}
	
	public int[] getPoints() {
		return points;
	}
	
	void sort() {
		for (int i = 1; i < points.length; i++) {
			int j = i;
			while(isLessThanPrevious(j)) {
				int s = values[j]; values[j] = values[j - 1]; values[j - 1] = s;
				s = points[j]; points[j] = points[j - 1]; points[j - 1] = s;
				j--;
			}
		}
	}
	private boolean isLessThanPrevious(int i) {
		if(i == 0) return false;
		if(values[i] < values[i - 1]) return true;
		if(values[i] > values[i - 1]) return false;
		if(points[i] < points[i - 1]) return true;
		return false;
	}
	
	public String getCode() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < points.length; i++) {
			sb.append(values[i]).append(':').append(points[i]).append(",");
		}
		return sb.toString();
	}
	
	public int getValue(int p) {
		for (int i = 0; i < values.length; i++) {
			if(points[i] == p) return values[i];
		}
		return -1;
	}

}
