package ic2006_3;

public class MarathonStateGenerator {
	
	public int[] generateRandomState() {
		int[] numbers = {1,2,3,4,5,6,7,8};
		randomize(numbers);
		int[] signs = {0,0,1,1,2,2,3,3}; //0 +, 1 -, 2 *, 3 /
		randomize(signs);
		int[] state = new int[16];
		int ni = 0;
		int si = 0;
		for (int i = 0; i < 16; i++) {
			int y = i / 4, x = i % 4;
			if((x + y) % 2 == 0) {
				state[i] = numbers[ni];
				ni++;				
			} else {
				state[i] = signs[si];
				si++;
			}
		}
		return state;
	}
	
	void randomize(int[] s) {
		for (int i = s.length - 1; i>= 1; i--) {
			int j = (int)((i + 1) * Math.random());
			int k = s[i];
			s[i] = s[j];
			s[j] = k;
		}
	}

}
