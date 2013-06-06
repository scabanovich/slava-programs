package slava.puzzle.crossnumber.analysis.logic;

public class Region {
	int sum;
	int[] points;
	
	//info
	int place;
	int direction;
	
	public Region(int sum, int[] points) {
		this.sum = sum;
		this.points = points;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(sum).append("=");
		for (int i = 0; i < points.length; i++) sb.append(' ').append(points[i]);
		return sb.toString();
	}
	
	public void setInfo(int place, int direction) {
		this.place = place;
		this.direction = direction;
	}

}
