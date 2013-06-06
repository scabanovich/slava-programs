package champ2013;

import com.slava.common.RectangularField;

public class CountRectangles {
	RectangularField f;
	
	int[] form = {
		1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,1, 1,1,1,0,0,
		0,0,0,0,0, 1,1,1,1,1, 1,1,1,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 
		0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 
	};
//	{
//		1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,0, 0,
//		1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,0, 0, 
//		1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,0, 0,
//		1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,0, 0,
//		1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,0, 0,
//
//		1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,0, 0, 
//		1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,0, 0,
//		1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,0, 0,
//		1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,0, 0,
//		1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,0, 0,
//
//		1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,0, 0,
//		1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,0, 0,
//		1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,0, 0,
//		1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,1, 1,1,1,0,0, 0,
//		1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,1, 1,1,1,0,0, 0,
//
//		1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,0, 0,
//		1,1,1,1,1, 1,1,1,1,1, 1,1,1,1,1, 1,1,0,0,0, 0,
//		0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,
//	};

	public CountRectangles() {
		f = new RectangularField();
		f.setSize(65, 3);
//		f.setSize(21, 18);
	}

	public int getSize() {
		int result = 0;
		for (int i = 0; i < f.getSize(); i++) {
			if(form[i] == 1) result++;
		}
		return result;
	}

	public int countRectangles() {
		int result = 0;
		for (int ix = 0; ix < f.getWidth(); ix++) {
			for (int jx = ix; jx < f.getWidth(); jx++) {
				for (int iy = 0; iy < f.getHeight(); iy++) {
					for (int jy = iy; jy < f.getHeight(); jy++) {
						if(jy - iy == jx - ix) {
							continue; //Square
						}
						if(exists(ix, jx, iy, jy)) {
							result++;
						}
						
					}
				}
			}
		}
		return result;
	}

	public int countSquares() {
		int result = 0;
		for (int ix = 0; ix < f.getWidth(); ix++) {
			for (int jx = ix; jx < f.getWidth(); jx++) {
				for (int iy = 0; iy < f.getHeight(); iy++) {
					for (int jy = iy; jy < f.getHeight(); jy++) {
						if(jy - iy != jx - ix) {
							continue; //Rectangle
						}
						if(exists(ix, jx, iy, jy)) {
							result++;
						}
						
					}
				}
			}
		}
		return result;
	}

	boolean exists(int ix, int jx, int iy, int jy) {
		for (int kx = ix; kx <= jx; kx++) {
			for (int ky = iy; ky <= jy; ky++) {
				int p = f.getIndex(kx, ky);
				if(form[p] != 1) {
					return false;
				}
			}
		}
		return true;
	}

	static void solveRatio() {
		int minsum = 1000;
		for (int a = -50; a < 50; a++) {
			for (int b = -100; b < 100; b++) {
				for (int c = -100; c < 100; c++) {
					int q = a * (2013 / 3) + b * (2013 / 11) + c * (2013 / 61) + 0 * (2013 / 1);
					if(q == 1) {
						int sum = Math.abs(a) + Math.abs(b) + Math.abs(c);
						if(sum < minsum) {
							minsum = sum;
							System.out.println("a=" + a + " b=" + b + " c=" + c + " sum=" + sum);
						}
					}
				}
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CountRectangles p = new CountRectangles();
		System.out.println("Size=" + p.getSize());
		System.out.println("rectangles=" + p.countRectangles());
		System.out.println("squares=" + p.countSquares());

//		solveRatio();
	}

}
