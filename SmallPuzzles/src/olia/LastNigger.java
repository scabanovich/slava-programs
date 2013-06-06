package olia;

public class LastNigger {
	int[][] table;

	public void computeTable(int maxN, int maxM) {
		table = new int[maxN + 1][maxM + 1];
		for (int m = 1; m <= maxM; m++) {
			table[1][m] = 1;
			for (int n = 2; n <= maxN; n++) {
				int f = (m <= n || (m % n) == 0) ? m : m % n;
				int l = f + table[n - 1][m];
				while(l > n) l -= n;
				table[n][m] = l;
			}
		}
	}

	public void print() {
		for (int m = 1; m < table[0].length; m++) {
			String ms = "" + m;
			if(ms.length() < 2) ms = " " + ms;
			System.out.print("m=" + ms + " : ");
			for (int n = 1; n < table.length; n++) {
				String s = "" + table[n][m];
				while(s.length() < 3) s = " " + s;
				System.out.print(" " + s);
			}
			System.out.println("");
		}
		System.out.println("");
		for (int m = 1; m < table.length; m++) {
			String s = "" + table[m][m];
			while(s.length() < 3) s = " " + s;
			System.out.print(" " + s);
		}
	}

	public static void main(String[] args) {
		LastNigger p = new LastNigger();
		p.computeTable(20, 20);
		p.print();
	}

}
