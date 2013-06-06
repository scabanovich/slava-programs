package ic2006_2;

public class SuperDamkaRunner {
	static int[] INITIAL = {
		0,0,0,0,0,1,0,0,
		0,0,0,1,0,0,0,0,//
		0,0,1,0,0,0,0,0,
		0,1,0,0,0,0,1,0,//
		0,0,0,0,1,0,0,0,
		0,0,0,0,0,1,0,0,
		0,0,1,0,0,0,0,0,
		0,0,0,0,0,0,0,0,
	};

	public static void main(String[] args) {
		for (int p = 0; p < INITIAL.length; p++) {
			if(INITIAL[p] != 0) continue;
			SuperDamkaField f = new SuperDamkaField();
			f.setSize(8);
			System.out.println("p=" + f.getDesignation(p));
			SuperDamkaSolver solver = new SuperDamkaSolver();
			solver.setField(f);
			solver.setInitialState(INITIAL);
			solver.setInitialPlace(p);
			solver.solve();
		}
	}

}

//10: e3-c1-c7-g7-f8-f2-h4-d4-b2-b6-e3
