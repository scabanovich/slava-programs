package olia;

public class Patience1 {
	int width;
	int length;
	int size;
	int[] x, y;
	
	public Patience1() {
		setSize(4, 9);
	}
	
	public void setSize(int width, int length) {
		this.width = width;
		this.length = length;
		size = width * length;
		x = new int[size];
		y = new int[size];
		for (int i = 0; i < size; i++) {
			x[i] = (i % width);
			y[i] = (i / width);
		}
	}
	
	public int[] shuffle() {
		int[] s = new int[size];
		for (int i = 0; i < size; i++) s[i] = i;
		for (int i = size - 1; i > 0; i--) {
			int j = (int)((i + 1) * Math.random());
			if(j == i) continue;
			int q = s[i];
			s[i] = s[j];
			s[j] = q;
		}
		return s;
	}
	
	public int[] round(int[] s) {
		int[] k = new int[size];
		int l = 0;
		int lm = s.length / 3; 
		for (int i = 0; i < lm; i++) {
			int i0 = i * 3, i1 = i0 + 1, i2 = i0 + 2;
			if(isSuccessful(s[i0], s[i1], s[i2])) continue;
			k[l] = s[i0];
			++l;
			k[l] = s[i1];
			++l;
			k[l] = s[i2];
			++l;
		}
		int[] r = new int[l];
		System.arraycopy(k, 0, r, 0, l);
		return r;
	}
	
	public int[] regroup(int[] s) {
		int lm = s.length / 3;
		int[] r = new int[s.length];
		for (int i = 0; i < lm; i++) {
			r[3 * i] = s[i];
			r[3 * i + 1] = s[i + lm];
			r[3 * i + 2] = s[i + lm + lm];
		}
		return r;
	}
	
	public boolean isSuccessful(int a, int b, int c) {
		return x[a] == x[b] || x[a] == x[c] || x[b] == x[c] ||
		       y[a] == y[b] || y[a] == y[c] || y[b] == y[c];
	}

	public int test(int rounds) {
		int[] s = shuffle();
		s = round(s);
		while(rounds > 1) {
			rounds--;
			if(s.length == 0) return 0;
			if(s.length == 3) return 1;
			s = regroup(s);
			s = round(s);
		}
		return (s.length / 3);
	}
	
	public int[] yes_no_test(int number, int rounds) {
		int[] r = new int[2];
		for (int i = 0; i < number; i++) {
			if(test(rounds) == 0) r[0]++; else r[1]++;
		}
		return r;
	}

	public void yes_no_testAndPrint(int number, int rounds) {
		int[] yes_no = yes_no_test(number, rounds);
		System.out.println(yes_no[0] + ":" + yes_no[1]);
	}

	public int[] leftOverTest(int number, int rounds) {
		int[] r = new int[size / 3 + 1]; 
		for (int i = 0; i < number; i++) {
			r[test(rounds)]++;
		}
		return r;
	}
	
	public void leftOverTestAndPrint(int number, int rounds) {
		int[] r = leftOverTest(number, rounds);
		for (int i = 0; i < r.length; i++) {
			if(i > 0) System.out.print(":");
			System.out.print(r[i]);
		}
		System.out.println("");
	}

	public static void main(String[] args) {
		Patience1 p = new Patience1();
		p.leftOverTestAndPrint(24000000, 2);
	}
}
