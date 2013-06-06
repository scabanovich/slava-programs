package olia.triangles;

public class PackedTriangle {
	int x;
	int y;
	int[] points;
	int diagonal;
	
	public PackedTriangle(int x, int y, int[] points, int diagonal) {
		this.x = x;
		this.y = y;
		this.diagonal = diagonal;
		this.points = points;
	}
	
	public boolean contains(int p) {
		for (int i = 0; i < points.length; i++) {
			if(points[i] == p) return true;
		}
		return false;
	}

}
