package slava.math;

public class BinaryExpression extends Expression {
	public static int ADDING = 0;
	public static int SUBTRACTION = 1;
	public static int PRODUCT = 2;
	public static int DIVISION = 3;
	public static char[] OPERATION_CHAR = {'+', '-', '*', '/'};
	
	protected Expression left;
	protected Expression right;
	private int operation = 0;
	
	public BinaryExpression() {
	}
	
	public void setOperation(int i) {
		operation = i;		
	}
	
	public void setLeftExpression(Expression left) {
		this.left = left;
	}

	public void setRightExpression(Expression right) {
		this.right = right;
	}

	public int compute(int[] values) {
		int l = left.compute(values);
		int r = right.compute(values);
		if(operation == ADDING) return l + r;
		if(operation == SUBTRACTION) return l - r;
		if(operation == PRODUCT) return l * r;
		if(operation == DIVISION) return (r == 0) ? Integer.MAX_VALUE : l / r;		
		return 0;
	}
	
	public String toString() {
		return "(" + left.toString() + OPERATION_CHAR[operation] + right.toString() + ")";
	}
	
}
