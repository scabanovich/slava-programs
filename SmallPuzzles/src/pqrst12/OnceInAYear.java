package pqrst12;

public class OnceInAYear {
	int[] usedDigits;
	
	public void solve() {
		int n = 2005;
		usedDigits = new int[10];
		for (int i1 = 1; i1 < 10; i1++) {
			usedDigits[i1]++;
			for (int i2 = 0; i2 < 10; i2++) {
				if(usedDigits[i2] > 0) continue;
				usedDigits[i2]++;
				int a = 10 * i1 + i2;
				int n1 = n - a;
				for (int i3 = 1; i3 < 10; i3++) {
					if(usedDigits[i3] > 0) continue;
					usedDigits[i3]++;
					for (int i4 = 1; i4 < 10; i4++) {
						if(usedDigits[i4] > 0) continue;
						usedDigits[i4]++;
						int b = 10 * i3 + i4;
						int n2 = n1 * b;
						for (int i5 = 0; i5 < 10; i5++) {
							if(usedDigits[i5] > 0) continue;
							usedDigits[i5]++;
							int c = i5;
							int n3 = n2 + i5;
							if(accept(n3)) {
								System.out.println("(" + n3 + " - " + c + ")/" + b + " + " + a);
							}							
							usedDigits[i5]--;
						}
						usedDigits[i4]--;
					}
					usedDigits[i3]--;
				}
				usedDigits[i2]--;
			}			
			usedDigits[i1]--;
		}
		
	}
	
	boolean accept(int n) {
		if(n < 10000 || n > 99999) return false;
		boolean r = true;
		int[] d = new int[5];
		for (int i = 0; i < 5; i++) {
			d[i] = n % 10;
			n = n / 10;
			usedDigits[d[i]]++;
			if(usedDigits[d[i]] > 1) r = false;
		}
		for (int i = 0; i < 5; i++) {
			usedDigits[d[i]]--;
		}		
		return r;
	}

	public static void main(String[] args) {
		OnceInAYear a = new OnceInAYear();
		a.solve();
	}
	
}

/*
 * (25046 - 8)/13 + 79
 * 
 * 250468,13,79
 * 
 */