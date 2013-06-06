package smallpuzzles.xor;

import java.util.Random;

/**
 * For a set of non-negative integer numbers finds
 * pair of numbers which gives maximum value of XOR product.
 * 
 * @author Slava Kabanovich
 *
 */
public class XORMaximumFinder {
	private int[] numbers;
	private SolutionInfo solutionInfo;
	
	private class SolutionInfo {
		int first;
		int second;
		int xor;
		
		SolutionInfo(int first, int second) {
			this.first = first;
			this.second = second;
			xor = (first | second) - (first & second);
		}
	}

	/**
	 * 
	 * @param numbers Array of non-negative integer numbers.
	 * @param solveTrivially If true, trivial algorithm enumerating
	 * 						all pairs is used. If false, sophisticated 
	 * 						algorithm is used.
	 */
	public XORMaximumFinder(int[] numbers, boolean solveTrivially) {
		if(numbers == null || numbers.length < 2) {
			throw new IllegalArgumentException("Array 'numbers' must be not null and have at list 2 elements.");
		}
		for (int i = 0; i < numbers.length; i++) {
			if(numbers[i] < 0) {
				throw new IllegalArgumentException("Array 'numbers' must have only non-negative integer elements.");
			}
		}
		this.numbers = numbers;
		solutionInfo = (solveTrivially) ? solveTrivially() : solve();
	}
	
	public int getFirstNumber() {
		return solutionInfo.first;
	}
	
	public int getSecondNumber() {
		return solutionInfo.second;
	}
	
	public int getMuximumXORValue() {
		return solutionInfo.xor;
	}
	
	private SolutionInfo solveTrivially() {
		int xorMax = 0;
		int first = -1;
		int second = -1;
		for (int i = 0; i < numbers.length; i++) {
			for (int j = i + 1; j < numbers.length; j++) {
				int a = numbers[i];
				int b = numbers[j];
				int xor = (a | b) - (a & b);
				if(xor > xorMax) {
					xorMax = xor;
					first = a;
					second = b;
				}
			}
		}
		return new SolutionInfo(first, second);
	}
	
	/**
	 * Solves the problem in a sophisticated way.
	 * @return
	 */
	private SolutionInfo solve() {
		int maxNumber = 0;
		for (int i = 0; i < numbers.length; i++) {
			if(numbers[i] > maxNumber) maxNumber = numbers[i];
		}
		int power = 1;
		while(power <= maxNumber) {
			power *= 2;
		}
		power /= 2;
		return solve(numbers, power);
	}

	/**
	 * Utility method
	 * @param ns
	 * @param power
	 * @return
	 */
	private SolutionInfo solve(int[] ns, int power) {
		if(power == 0) {
			// that can happen only if all numbers are equal;
			return new SolutionInfo(ns[0], ns[1]);
		}
		SetSeparator separated = new SetSeparator(ns, power);
		if(separated.ns1 == EMPTY || separated.ns2 == EMPTY) {
			return solve(ns, power / 2);
		}
		return solve(separated.ns1, separated.ns2, power / 2);
	}

	/**
	 * Utility method
	 * @param ns1
	 * @param ns2
	 * @param power
	 * @return
	 */
	private SolutionInfo solve(int[] ns1, int[] ns2, int power) {
		if(ns1.length == 0 || ns2.length == 0) return null;
		if(power == 0) {
			return new SolutionInfo(ns1[0], ns2[0]);
		}
		SetSeparator separated1 = new SetSeparator(ns1, power);
		SetSeparator separated2 = new SetSeparator(ns2, power);
		SolutionInfo s1 = solve(separated1.ns1, separated2.ns2, power / 2);
		SolutionInfo s2 = solve(separated1.ns2, separated2.ns1, power / 2);
		if(s1 == null && s2 == null) {
			int[] ns = new int[ns1.length + ns2.length];
			System.arraycopy(ns1, 0, ns, 0, ns1.length);
			System.arraycopy(ns2, 0, ns, ns1.length, ns2.length);
			return solve(ns, power / 2);
		} else if(s1 == null) {
			return s2;
		} else if(s2 == null) {
			return s1;
		}
		return (s1.xor > s2.xor) ? s1 : s2;
	}
	
	static int[] EMPTY = new int[0];

	/**
	 * Utility class Separates array ns into 2 arrays,
	 * first contains elements for which (ns[i] & power) == power,
	 * second contains all other elements.
	 * Parameter power must be an exact power of 2.
	 */
	private static class SetSeparator {
		int[] ns1;
		int[] ns2;
		SetSeparator(int[] ns, int power) {
			int count1 = 0;
			for (int i = 0; i < ns.length; i++) {
				if((ns[i] & power) == power) count1++;
			}
			if(count1 == 0) {
				ns1 = EMPTY;
				ns2 = ns;
				return;
			} else if(count1 == ns.length) {
				ns1 = ns;
				ns2 = EMPTY;
				return;
			}
			ns1 = new int[count1];
			ns2 = new int[ns.length - count1];
			int c1 = 0;
			int c2 = 0;
			for (int i = 0; i < ns.length; i++) {
				if((ns[i] & power) == power) {
					ns1[c1] = ns[i];
					c1++;
				} else {
					ns2[c2] = ns[i];
					c2++;
				}
			}
		}
	}
	
	/**
	 * Returns solution info.
	 */
	public String toString() {
		return "First: " + getFirstNumber() + "\n"
			+ "Second: " + getSecondNumber() + "\n"
			+ "XOR: " + getMuximumXORValue();
	}
	
	
	//Tests follow:

	public static void test(int size) {
		int[] randomNumbers = generateRandomNumbers(size);
		Random v = new Random();
		for (int i = 0; i < size; i++) {
			randomNumbers[i] = v.nextInt(1000000000);
		}
		XORMaximumFinder finder = new XORMaximumFinder(randomNumbers, false);
		System.out.println("Solution");
		System.out.println(finder);
		
		XORMaximumFinder trivialFinder = new XORMaximumFinder(randomNumbers, true);
		System.out.println("Trivial Solution");
		System.out.println(trivialFinder);
		
		if(finder.getMuximumXORValue() == trivialFinder.getMuximumXORValue()) {
			System.out.println("Success");
		} else {
			System.out.println("Failure");
		}
	}
	
	public static void testTimeDependency() {
		int size = 1000;
		for (int i = 0; i < 7; i++) {
			int[] randomNumbers = generateRandomNumbers(size);
			long t = System.currentTimeMillis();
			XORMaximumFinder finder = new XORMaximumFinder(randomNumbers, false);
			long dt = System.currentTimeMillis() - t;
			System.out.println("Array size=" + size + " Solved sophisticatedly in " + dt + " ms");
			
			t = System.currentTimeMillis();
			XORMaximumFinder trivialFinder = new XORMaximumFinder(randomNumbers, true);
			dt = System.currentTimeMillis() - t;
			System.out.println("Array size=" + size + " Solved trivially in " + dt + " ms");

			if(finder.getMuximumXORValue() == trivialFinder.getMuximumXORValue()) {
				System.out.println("Success");
			} else {
				System.out.println("Failure");
			}

			size *= 2;
		}
	}
	
	private static int[] generateRandomNumbers(int size) {
		int[] randomNumbers = new int[size];
		Random v = new Random();
		for (int i = 0; i < size; i++) {
			randomNumbers[i] = v.nextInt(1000000000);
		}
		return randomNumbers;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		test(100);
		testTimeDependency();

	}

}
