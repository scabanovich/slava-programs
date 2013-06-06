package balance;

public class BalanceGenerator {
	int size;
	BalanceSolver solver = new BalanceSolver();
	
	public void setSize(int s) {
		size = s;
	}
	
	public void generate() {
		int attemptCount = 0;
		do {
			attemptCount++;
			Weight root = createBough(null, size);
			solver = new BalanceSolver();
			solver.setRoot(root);
			solver.solve();
			if(solver.solutionCount > 0) {
				System.out.println("-->" + solver.solutionCount);
			}
			if(attemptCount % 100 == 0) {
				System.out.println(attemptCount);
			}
		} while(solver.solutionCount != 1);
	}
	
	public Weight createBough(Weight parent, int size) {
		Weight c = new Weight(parent, 0);
		if(size == 1) return c;
		int[] sz = new int[5];
		int q = 0;
		int ss = size;
		while(ss > 0) {
			int ds = (ss == 1 || q == 2) ? ss : ((int)(Math.random() * (size + 2)/ 2) + 1);
			if(ds > ss) ds = ss;
			sz[q] = ds;
			q++;
			ss -= ds;
		}
		if(q == 1) {
			q = 2;
			sz[1] = 1;
			sz[0]--;
		}
		for (int i = 0; i < q; i++) {
			createBough(c, sz[i]);
		}
		Weight[] ws = c.children;
		int min = -(int)(Math.random() * 3) - ( q / 2);
		ws[0].leg = min;
		int max = ((int)(Math.random() * 3)) + 1;
		if(max - min < q - 1) max = min + q - 1;
		ws[q - 1].leg = max;
		if(q > 2) {
			ws[1].leg = min + 1 + (int)(Math.random() * (max - min - 1));
		}
		if(q == 4) {
			do {
				ws[2].leg = min + 1 + (int)(Math.random() * (max - min - 1));
			} while(ws[2].leg == ws[1].leg);
		}
		return c;
	}
	
	public static Weight generateTest() {
		Weight root = new Weight();

		Weight p1 = new Weight(root, -4);
		Weight p11 = new Weight(p1, -6);
		Weight p12 = new Weight(p1, 3);
		Weight p13 = new Weight(p1, 4);
		Weight p14 = new Weight(p1, 0);

		Weight p2 = new Weight(root, 2);
		Weight p21 = new Weight(p2, -4);
		Weight p22 = new Weight(p2, 1);
		Weight p23 = new Weight(p2, 3);

		Weight p3 = new Weight(root, 3);
		Weight p31 = new Weight(p3, -4);
		Weight p32 = new Weight(p3, -1);
		Weight p33 = new Weight(p3, 3);
		Weight p34 = new Weight(p3, 6);

		Weight p4 = new Weight(root, 4);
		Weight p41 = new Weight(p4, -3);
		Weight p42 = new Weight(p4, -1);
		Weight p43 = new Weight(p4, 4);

		return root;
	}

}
