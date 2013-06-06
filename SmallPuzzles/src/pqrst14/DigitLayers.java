package pqrst14;

/**
  -------0
  |     |
  |1    |2
  |     |
  -------3
  |     |
  |4    |5
  |     |
  -------6
 */

public class DigitLayers {
	int[][] digits = new int[][]{
		{1,1,1,0,1,1,1},
		{0,0,1,0,0,1,0},
		{1,0,1,1,1,0,1},
		{1,0,1,1,0,1,1},
		{0,1,1,1,0,1,0},
		{1,1,0,1,0,1,1},
		{1,1,0,1,1,1,1},
		{1,0,1,0,0,1,0},
		{1,1,1,1,1,1,1},
		{1,1,1,1,0,1,1}
	};
	
	static int[] FORM = {2, 2, 1, 2, 2, 2, 1};
	
	public void solve(int[] form) {
		System.out.println("start");
		for (int i1 = 0; i1 < digits.length; i1++) {
			for (int i2 = i1 + 1; i2 < digits.length - 1; i2++) {
				for (int i3 = i2 + 1; i3 < digits.length; i3++) {
					if(!isSolution(form, i1, i2, i3)) continue;
					System.out.println("" + i1 + " " + i2 + " " + i3);
				}
			}
		}
	}
	
	boolean isSolution(int[] form, int i1, int i2, int i3) {
		for (int k = 0; k < digits[0].length; k++) {
			if(digits[i1][k] + digits[i2][k] + digits[i3][k] != form[k]) return false;
		}
		return true;
	}
		
}
