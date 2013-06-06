package match2011;

import java.util.Random;

/**
 * First stage
 * a a - - - - a a
 * - a a - - a a -
 * - - a a a a - -
 * - - - a a - - -
 * - - a a a a - -
 * - a a - - a a -
 * a a - - - - a a
 * 
 * @author slava
 *
 */
public class MagicDiagonalsGenerator {
	int[] form = {
		1,1,0,0,0,0,1,1,
		0,1,1,0,0,1,1,0,
		0,0,1,1,1,1,0,0,
		0,0,0,1,1,0,0,0,
		0,0,1,1,1,1,0,0,
		0,1,1,0,0,1,1,0,
		1,1,0,0,0,0,1,1,
	};
	int[][] equations = {
		{0, 9,18,27,36,45,54},
		{1,10,19,28,37,46,55},
		{6,13,20,27,34,41,48},
		{7,14,21,28,35,42,49}
	};
	int[] valueLimits = {8,8,6,4};
	
	int[] result;

	MagicDiagonalsGenerator() {}
	
	Random seed = new Random();

	public int[] generate() {
		int attempts = 0;
		while(true) {
			attempts++;
			int[] usage = new int[valueLimits.length];
			result = new int[form.length];
			for (int i = 0; i < form.length; i++) {
				result[i] = -1;
				if(form[i] == 1) {
					int v = seed.nextInt(valueLimits.length);
					if(i == 27 || i == 28) {
						v = 0;
					}
					while(usage[v] >= valueLimits[v]) {
						v = seed.nextInt(valueLimits.length);
					}
					result[i] = v;
					usage[v]++;
				}
			}
			if(checkResult()) {
				break;
			}
		}
//		System.out.println("Attempts=" + attempts);
//		printResult();
		return result;
	}

	boolean checkResult() {
		for (int k = 0; k < equations.length;k++) {
			int s = 0;
			for (int c = 0; c < equations[k].length; c++) {
				s += result[equations[k][c]];
			}
			if(s != 8) {
				return false;
			}
		}
		return true;
	}

	void printResult() {
		for (int i = 0; i < result.length; i++) {
			String s = (result[i] < 0) ? "-" : "" + result[i];
			System.out.print(" " + s);
			if(i % 8 == 7) {
				System.out.println("");
			}
		}
	}

}

