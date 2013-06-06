package slava.math;

public class Number extends Expression {
	int[] digits;
	
	public Number(int[] digits) {
		this.digits = digits;
	}
	
	public int compute(int[] values) {
		int result = 0;
		for (int i = 0; i < digits.length; i++) {
			int v = values[digits[i]];
			result = result * 10 + v;
		}
		return result;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < digits.length; i++) {
			sb.append((char)(digits[i] + 97));
		}
		return sb.toString();
	}

}
