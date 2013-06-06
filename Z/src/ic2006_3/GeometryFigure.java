package ic2006_3;

public class GeometryFigure {
	int area;
	int[] points;
	
	public GeometryFigure(int[] points, int area) {
		this.points = points;
		this.area = area;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("points={");
		for (int i = 0; i < points.length; i++) {
			if(i > 0) sb.append(',');
			sb.append(points[i]);
		}
		sb.append("} area=").append(area);
		return sb.toString();
	}
	
	public boolean contains(int p) {
		for (int i = 0; i < points.length; i++) {
			if(points[i] == p) return true;
		}
		return false;
	}

}
