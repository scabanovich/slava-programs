package forsmarts;

public class RaceToIncreaseRunner {
	static int E = -1;
	static int[] DATA = {
		0,1,2,3,4,5,6,E,E,E,E,E,E,
		7,8,9,0,1,2,3,4,E,E,E,E,E,
		5,6,7,8,9,0,1,2,3,E,E,E,E,
		4,5,6,7,8,9,0,1,2,3,E,E,E,
		4,5,6,7,8,9,0,1,2,3,4,E,E,
		5,6,7,8,9,0,1,2,3,4,5,6,E,
		7,8,9,0,1,2,3,4,5,6,7,8,9,
		E,0,1,2,3,4,5,6,7,8,9,0,1,
		E,E,2,3,4,5,6,7,8,9,0,1,2,
		E,E,E,3,4,5,6,7,8,9,0,1,2,
		E,E,E,E,3,4,5,6,7,8,9,0,1,
		E,E,E,E,E,2,3,4,5,6,7,8,9,
		E,E,E,E,E,E,0,1,2,3,4,5,6
	};

	public static void main(String[] args) {
		XPlusYField f = new F();
		f.setSize(13, 13);
		RaceToIncreaseSolver solver = new RaceToIncreaseSolver();
		solver.setField(f);
		solver.setData(DATA);
		solver.solve();
	}
	
	static class F extends XPlusYField {
		public void setSize(int width, int height) {
			super.setSize(width, height);
			jp = new int[6][size];
			for (int i = 0; i < size; i++) {
				jp[0][i] = (x[i] == width - 1                      ) ? -1 : i + 1;
				jp[1][i] = (x[i] == width - 1 || y[i] == height - 1) ? -1 : i + 1 + width;
				jp[2][i] = (                     y[i] == height - 1) ? -1 : i + width;
				jp[3][i] = (x[i] == 0                              ) ? -1 : i - 1;
				jp[4][i] = (x[i] == 0         || y[i] == 0) ? -1 : i - 1 - width;
				jp[5][i] = (                     y[i] == 0) ? -1 : i - width;
			}
		}
	}

}
/*
Way length=57
 . . . . . . . . . . . . .
 n n n n n n m . . . . . .
 o . . . . . . m . . . . .
 o . . . . . . . m . . . .
 o . . . . . . . . m . . .
 o . . . . . . . . . m . .
 o p p p p p p p p p . m .
 . g h h h h . . . q . . l
 . . g . . i . . . q . . l
 . . . g . i . . . q . . l
 . . . . f e j . . q . . l
 . . . . . d c j . q . . l
 . . . . . . a b j k k k k
 58;G13;1,4,5,6,7,8,10,14,16,20,21,24,30,32,45,46
*/