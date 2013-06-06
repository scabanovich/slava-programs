package match2005;

public class Cycle {
	
	public void run() {
		System.out.println(Math.log(2d));
		int size = 100;
		int limit = size / 2;
		
		double d = 0;
		for (int i = limit + 1; i <= size; i++) {
			d += 1d / i;
		}
		System.out.println(d);
		
		int attempts = 0;
		int longCycleCount = 0;
		while(attempts < 100000) {
			++attempts;
			int[] order = getRandomOrder(size);
			if(hasCycleLongerThan(limit, order)) ++longCycleCount;
		}
		System.out.println(longCycleCount + " " + attempts);
	}
	
	int[] getRandomOrder(int size) {
		int[] check = new int[size];
		for (int i = 0; i < check.length; i++) check[i] = i;
		for (int i = check.length - 1; i >= 0; i--) {
			int j = (int) ((i + 1) * Math.random());
			if(i != j) {
				int k = check[i];
				check[i] = check[j];
				check[j] = k;
			}
		}
		return check;
	}
	
	boolean hasCycleLongerThan(int q, int[] order) {
		for (int i = 0; i < order.length; i++) {
			int b = i;
			int e = order[b];
			int c = 1;
			while(e > b) {
				e = order[e];
				++c;
			}
			if(c > q) return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		Cycle c = new Cycle();
		c.run();
	}	

}

//687947 688137 687744 687051 686881 687245 688858 688353
