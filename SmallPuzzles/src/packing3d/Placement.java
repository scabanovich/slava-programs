package packing3d;

import java.util.Arrays;

public class Placement {
	int[] points;
	int index;
	String code;
	
	public Placement(int index, int[] points) {
		setIndex(index);
		setPoints(points);
	}
	
	public void setPoints(int[] points) {
		this.points = (int[])points.clone();
		Arrays.sort(this.points);
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
	
	public int[] getPoints() {
		return points;
	}
	
	public boolean contains(int p) {
		for (int i = 0; i <  points.length; i++) {
			if(points[i] == p) return true;
		}
		return false;
	}

	public boolean equals(Placement other) {
		if(this.points.length != other.points.length) return false;
		for (int i = 0; i < points.length; i++) {
			if(this.points[i] != other.points[i]) return false;
		}
		return true;
	}
	
	public String getCode() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < points.length; i++) {
			sb.append("" + points[i] + ",");
		}		
		return sb.toString();
	}

}
