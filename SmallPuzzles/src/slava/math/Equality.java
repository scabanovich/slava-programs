package slava.math;

public class Equality extends BinaryExpression {

	public int compute(int[] values) {
		int l = left.compute(values);
		int r = right.compute(values);
		return (l == r) ? 1 : 0;
	}

	public String toString() {
		return left.toString() + "=" + right.toString();
	}

}
