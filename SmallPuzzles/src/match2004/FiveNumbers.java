package match2004;

public class FiveNumbers {
	int number = 2004;
	
	double[] numbers;
	
	// used indices from array 'numbers'
	int[] usedNumbers;
	int[] usedOperands; // + * - /
	int t;
	int[] wayCount;
	int[] way;
	// indices from array 'numbers'
	int[][] firstNumber;
	int[][] secondNumber;
	int[][] operand;
	int solutionCount;
	String solutionInfo;
	boolean printSolution = true;
	
	public void setNumbers(int[] ns) {
		numbers = new double[9];
		for (int i = 0; i < 5; i++) numbers[i] = ns[i];
	}
	
	public void setPrintSolution(boolean b) {
		printSolution = b;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	public void init() {
		wayCount = new int[5];
		way = new int[5];
		firstNumber = new int[5][100];
		secondNumber = new int[5][100];
		operand = new int[5][100];
		usedNumbers = new int[10];
		usedOperands = new int[4];
		t = 0;
		solutionCount = 0;
		solutionInfo = "";
	}
	
	public void srch() {
		wayCount[t] = 0;
		if(t == 4) return;
		for (int n1 = 0; n1 < 5 + t; n1++) {
			if(usedNumbers[n1] != 0) continue;
			for (int op = 0; op < 4; op++) {
				if(usedOperands[op] != 0) continue;
				int m2 = 0;
				if(op < 2) m2 = n1 + 1;
				for (int n2 = m2; n2 < 5 + t; n2++) {
					if(n2 == n1) continue;
					if(usedNumbers[n2] != 0) continue;
					firstNumber[t][wayCount[t]] = n1;
					secondNumber[t][wayCount[t]] = n2;
					operand[t][wayCount[t]] = op;
					wayCount[t]++;
				}
			}
		}
	}
	
	void move() {
		int n1 = firstNumber[t][way[t]];
		int n2 = secondNumber[t][way[t]];
		int op = operand[t][way[t]];
		double d1 = numbers[n1];
		double d2 = numbers[n2];
		double d = (op == 0) ? d1 + d2 :
		           (op == 1) ? d1 * d2 :
		           (op == 2) ? d1 - d2 :
		           (d2 == 0) ? 1.0d+6 : d1 / d2; 
		numbers[5 + t] = d;
		usedNumbers[n1] = 1;
		usedNumbers[n2] = 1;
		usedOperands[op] = 1;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int n1 = firstNumber[t][way[t]];
		int n2 = secondNumber[t][way[t]];
		int op = operand[t][way[t]];
		usedNumbers[n1] = 0;
		usedNumbers[n2] = 0;
		usedOperands[op] = 0;		
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			++way[t];
			move();
			if(t == 4) {
				double d = numbers[4 + t];
				long q = Math.round(d), qs = q;
				if(Math.abs(d - q) >= 0.0000001) continue;
				q = Math.abs(q);
				if(qs == number) {
					solutionCount++;
					if(solutionCount == 1) printSolution();
				} else if(q < number) {
					printSolution();
					solutionCount = -1;
					return;
 				}
			}
		}
	}
	
	public String getSolutionInfo() {
		return solutionInfo;
	}
	
	void printSolution() {
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		for (int tt = 0; tt < 4; tt++) {
			int n1 = firstNumber[tt][way[tt]];
			int n2 = secondNumber[tt][way[tt]];
			int op = operand[tt][way[tt]];
			double d1 = numbers[n1];
			double d2 = numbers[n2];
			char s = (op == 0) ? '+' : (op == 1) ? '*' : (op == 2) ? '-' : '/';
			sb.append(d1).append(s).append(d2).append('=').append(numbers[5 + tt]).append('\n');
		}
		solutionInfo = sb.toString();
		if(printSolution) {
			System.out.println(solutionInfo);
		}
	}
	
	public static void findFifthNumber(int[] fourNumbers) {
		for (int i = 2; i < 2000; i++) {
			boolean h = false;
			for (int k = 0; k < 4 && !h; k++) {
				if(i == fourNumbers[k]) h = true; 
			}
			if(h) continue;
			int[] fiveNumbers = new int[5];
			for (int k = 0; k < 4; k++) {
				fiveNumbers[k] = fourNumbers[k];
			}
			fiveNumbers[4] = i;
			int sc = check(fiveNumbers);
			if(sc > 0) break;
			if(sc == 0) {
				System.out.println("i=" + i);
			}
		}
	}
	
	public static int check(int[] fiveNumbers) {
		FiveNumbers n = new FiveNumbers();
		n.setNumbers(fiveNumbers);
		n.solve();
		return n.solutionCount;
	}
	
	public static void parametrization() {
		int de = 29;
		int abc = 2004 * de;
		for (int a = 20; a < 200; a++) {
			int b = abc / a - 0;
			int c = abc - a * b;
			for (int e = 2; e < 200; e++) {
				int d = e + de;
				int sum = a + b + c + d + e;
				if(sum > 845) continue;
				int[] fiveNumbers = new int[]{a,b,c,d,e};
				if(check(fiveNumbers) > 0) {
					System.out.println("sum=" + sum);
					return;
				} 
			}
		}
	}

	public static void totalEnumeration() {
		//351 //457
		for (int s = 502; s < 503; s++) {
			System.out.println("s=" + s);
			int q = 0;
			int m1 = s / 5;
			for (int i1 = s - 10; i1 >= m1; i1--) {
				int s1 = s - i1;
				int m2 = s1 / 4;
				int mx2 = s1 - 6;
				if(mx2 >= i1) mx2 = i1 - 1;
				for (int i2 = mx2; i2 >= m2; i2--) {
					int s2 = s1 - i2;
					int m3 = s2 / 3;
					int mx3 = s2 - 3;
					if(mx3 >= i2) mx3 = i2 - 1;
					for (int i3 = mx3; i3 >= m3; i3--) {
						int s3 = s2 - i3;
						int m4 = s3 / 2;
						int mx4 = s3 - 1;
						if(mx4 >= i3) mx4 = i3 - 1;
						for (int i4 = mx4; i4 >= m4; i4--) {
							int s4 = s3 - i4;
							int i5 = s4;
							if(i5 >= i4 || i5 < 1) continue;
							++q;
							int[] fiveNumbers = new int[]{i1,i2,i3,i4,i5};

							FiveNumbers n = new FiveNumbers();
							n.setPrintSolution(false);
							n.setNumbers(fiveNumbers);
							n.solve();
							int r = n.solutionCount;
							if(r > 0) {
								System.out.println(n.getSolutionInfo());
								return;
							} 
							///
							///System.out.println(i1 + " " + i2 + " " + i3 + " " + i4 + " " + i5);
						}
					}
				}
			}
			System.out.println("  q=" + q);
		}
	}

	public static void totalEnumeration2() {
		//351 //457
		for (int s = 500; s > 457; s--) {
			System.out.println("s=" + s);
			int q = 0;
			//int m1 = s / 5;
			int i1 = 167;
			int s1 = s - i1;
			int m2 = s1 / 4;
			int mx2 = s1 - 6;
///			if(mx2 >= i1) mx2 = i1 - 1;
			for (int i2 = mx2; i2 >= m2; i2--) {
				if(i2 == i1) continue;
				int s2 = s1 - i2;
				int m3 = s2 / 3;
				int mx3 = s2 - 3;
				if(mx3 >= i2) mx3 = i2 - 1;
				for (int i3 = mx3; i3 >= m3; i3--) {
					int s3 = s2 - i3;
					int m4 = s3 / 2;
					int mx4 = s3 - 1;
					if(mx4 >= i3) mx4 = i3 - 1;
					for (int i4 = mx4; i4 >= m4; i4--) {
						int s4 = s3 - i4;
						int i5 = s4;
						if(i5 >= i4 || i5 < 1) continue;
						++q;
						int[] fiveNumbers = new int[]{i1,i2,i3,i4,i5};

						FiveNumbers n = new FiveNumbers();
						n.setPrintSolution(false);
						n.setNumbers(fiveNumbers);
						n.solve();
						int r = n.solutionCount;
						if(r > 0) {
							System.out.println(n.getSolutionInfo());
							return;
						} 
						///
						///System.out.println(i1 + " " + i2 + " " + i3 + " " + i4 + " " + i5);
					}
				}
			}
			System.out.println("  q=" + q);
		}
	}

	public static void main(String[] args) {
///		FiveNumbers.parametrization();
		FiveNumbers.totalEnumeration2();
///		int[] ns = new int[]{83, 119, 143, 103, 98};
///		FiveNumbers.check(ns);
///		FiveNumbers.findFifthNumber(ns);
	}

}

//(83 * 119) + 143 / (103 - 98)