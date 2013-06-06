package com.slava.arithmetics;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NumberSet implements Comparable<NumberSet>{
	static int ADDITION = 0;
	static int SUBTRACTION = 1;
	static int MULTIPLICATION = 2;
	static int DIVISION = 3;

	int[] numbers;

	NumberSet parent;

	public NumberSet(int[] numbers) {
		this.numbers = numbers;
		Arrays.sort(numbers);
	}

	public int[] getNumbers() {
		return numbers;
	}

	public boolean equals(Object o) {
		if(o instanceof NumberSet) {
			NumberSet other = (NumberSet)o;
			if(numbers.length != other.numbers.length) {
				return false;
			}
			for (int i = 0; i < numbers.length; i++) {
				if(numbers[i] != other.numbers[i]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public int hashcode() {
		int q = 0;
		for (int i = 0; i < numbers.length; i++) {
			q = q * 37 + numbers[i];
		}
		return q;
	}

	public Set<NumberSet> applyAction() {
		HashSet<NumberSet> result = new HashSet<NumberSet>();
		for (int i1 = 0; i1 < numbers.length; i1++) {
			for (int i2 = i1 + 1; i2 < numbers.length; i2++) {
				int n1 = numbers[i1], n2 = numbers[i2];
				int[] ns = exclude(i1, i2);
				// +
				NumberSet s = new NumberSet(include(ns, n1 + n2));
				result.add(s);
				// -
				s = new NumberSet(include(ns, Math.abs(n1 - n2)));
				result.add(s);
				// *
				s = new NumberSet(include(ns, n1 * n2));
				result.add(s);
				if(n1 != 0 && n2 != 0) {
					if(n1 % n2 == 0) {
						s = new NumberSet(include(ns, n1 / n2));
						result.add(s);
					} else if(n2 % n1 == 0) {
						s = new NumberSet(include(ns, n2 / n1));
						result.add(s);
					}
				}
			}
		}
		for (NumberSet s: result) s.parent = this;
		return result;
	}

	private int[] exclude(int i1, int i2) {
		int[] ns = new int[numbers.length - 1];
		int j = 0;
		for (int i = 0; i < numbers.length; i++) {
			if(i == i1 || i == i2) continue;
			ns[j++] = numbers[i];
		}
		return ns;
	}
	private int[] include(int[] ns, int v) {
		ns[ns.length - 1] = v;
		int[] c = (int[])ns.clone();
		return c;		
	}

	public int compareTo(NumberSet o) {
		if(numbers.length != o.numbers.length) {
			return numbers.length - o.numbers.length;
		}
		for (int i = 0; i < numbers.length; i++) {
			if(numbers[i] != o.numbers[i]) {
				return numbers[i] - o.numbers[i];
			}
		}
		return 0;
	}

	public void printWithHistory() {
		for (int i = 0; i < numbers.length; i++) {
			System.out.print(" " + numbers[i]);
		}
		System.out.println("");
		if(parent != null) {
			parent.printWithHistory();
		}
	}

}
