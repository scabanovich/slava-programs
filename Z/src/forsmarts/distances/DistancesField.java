package forsmarts.distances;

import com.slava.common.RectangularField;

public class DistancesField extends RectangularField {
	
	public DistancesField() {}
	
	public void prohibitJump(int p, int d) {
		jp[d][p] = -1;
	}
	
	public int getDistance(int p1, boolean isDouble1, int p2, boolean isDouble2) {
		int dy = 2 * Math.abs(y[p1] - y[p2]);
		int x1 = 2 * x[p1];
		int x2 = 2 * x[p2];
		if(isDouble1) x1--; 
		if(isDouble2) x2--;
		int dx = Math.abs(x1 - x2);
		return dx * dx + dy * dy;
	}


}
