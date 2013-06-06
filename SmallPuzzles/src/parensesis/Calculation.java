package parensesis;

public class Calculation {
	int[] operations;
	int[] numbers;
	int[] order;
	
	public void setOperations(int[] operations) {
		this.operations = operations;
	}
	
	public void setOrder(int[] order) {
		this.order = order;
	}
	
	public void setNumbers(int[] numbers) {
		this.numbers = numbers;
	}
	
	public int compute() {
		int[] n = (int[])numbers.clone();
		int[] used = new int[operations.length];
		for (int i = 0; i < order.length; i++) {
			int p = order[i];
			used[p] = 1;
			int s = operations[p];
			int n1 = n[p];
			int n2 = n[p + 1];
			int r = (s == ParensesisIterator.PRODUCT) ? n1 * n2 :
			        (s == ParensesisIterator.DIVISION) ? n1 / n2 :
			        (s == ParensesisIterator.SUM) ? n1 + n2 :
			        (s == ParensesisIterator.SUBTRACTION) ? n1 - n2 :
			        0;
			int p1 = p;
			while(p1 >= 0 && used[p1] == 1) {
				n[p1] = r;
				--p1;
			}
			int p2 = p;
			while(p2 < operations.length && used[p2] == 1) {
				n[p2 + 1] = r;
				++p2;
			}			
		}
		return n[0];
	}

}
