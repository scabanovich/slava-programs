package olia;

public class TargetsGenerator {
	int sum = 111;
	int count = 9;
	//including
	int maxTarget = 60;
	int minTarget = 10;

	int[] numbers;
	int[] state;
	int freeNumbers;
	
	public boolean generateAttempt() {
		numbers = new int[count];
		state = new int[sum + 1];
		int initial = maxTarget - minTarget + 1;
		freeNumbers = initial;
		
		boolean haveSum = false;
		
		for (int c = 0; c < count; c++) {
			if(freeNumbers == 0) return false;
			int n = -1;
			if(!haveSum && c >= 3 && freeNumbers < initial - c) {
				do {
					n = (int)(initial * Math.random()) + minTarget;
				} while(state[n] == 0 || state[n] >= 1000);
				haveSum = true;
			} else {
				do {
					n = (int)(initial * Math.random()) + minTarget;
				} while(state[n] > 0);
			}
			numbers[c] = n;
			onNewNumber(c, n);
		}
		return true;
	}
	
	void onNewNumber(int c, int n) {
		if(state[n] == 0) freeNumbers--;
		state[n] += 1000;
		for (int q = 0; q < c; q++) {
			int m = sum - n - numbers[q];
			if(m >= minTarget && m <= maxTarget) {
				state[m]++;
				if(state[m] == 1) freeNumbers--;
			}
		}
	}
	
	public void generate() {
		int attemptCount = 0;
		while(true) {
			attemptCount++;
			if(generateAttempt()) break;
			if(attemptCount % 1000 == 0) {
				System.out.println(attemptCount);
			}
		}
		System.out.println("AttemptCount=" + attemptCount);
		for (int c = 0; c < count; c++) {
			System.out.print(" " + numbers[c]);
		}
		System.out.println("");
	}
	
	public void generateMany() {
		while(true) {
			generate();
			System.out.println("Generate one more?(y/n)");
			int i = 13;
			try {
				while((i != (int)'y') && (i != (int)'n')) i = System.in.read();
				if(i == (int)'n') break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		TargetsGenerator g = new TargetsGenerator();
		g.generateMany();
	}

}
