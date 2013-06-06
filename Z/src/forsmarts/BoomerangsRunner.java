package forsmarts;

public class BoomerangsRunner {
	static int[] FORM = {
		1,1,1,1,1,0,0,0,0,
		1,1,1,1,1,1,0,0,0,
		1,1,1,1,1,1,1,0,0,
		1,1,1,1,1,1,1,1,0,
		1,1,1,1,1,1,1,1,1,
		0,1,1,1,1,1,1,1,1,
		0,0,1,1,1,1,1,1,1,
		0,0,0,1,1,1,1,1,1,
		0,0,0,0,1,1,1,1,1,
	};
	
	static int[] DOTS_1 = {
		0,0,0,0,0,0,0,0,0,
		1,1,0,0,0,0,0,0,0,
		0,1,0,0,0,0,0,0,0,
		0,0,0,0,1,1,1,1,0,
		0,0,1,0,0,1,0,0,0,
		0,0,0,0,0,0,1,0,0,
		0,0,1,0,0,0,0,1,0,
		0,0,0,0,0,0,0,0,1,
		0,0,0,0,0,0,0,0,0,
	};

	static int[] DOTS_2 = {
		0,0,0,0,0,0,0,0,0,
		1,0,0,0,0,0,0,0,0,
		1,1,0,0,0,0,1,0,0,
		0,0,1,0,0,1,1,0,0,
		0,0,0,1,1,1,0,1,0,
		0,0,0,0,0,0,1,0,1,
		0,0,0,0,0,0,0,0,1,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,1,0,0,0,0,
	};

	static int[] FORM_3 = {
		1,1,1,1,0,0,0,
		1,1,1,1,1,0,0,
		1,1,1,1,1,1,0,
		1,1,1,1,1,1,1,
		0,1,1,1,1,1,1,
		0,0,1,1,1,1,1,
		0,0,0,1,1,1,1,
	};
	static int[] DOTS_3 = {
		0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,
		1,0,0,0,1,1,0,
		0,1,1,0,0,1,0,
		0,0,1,1,0,0,1,
		0,0,0,0,0,0,0,
	};
	
	static void test() {
		BoomerangsField f = new BoomerangsField();
		f.setSize(7, 7);
		f.makePlacements(FORM_3, DOTS_3);
		BoomerangsSolver solver = new BoomerangsSolver();
		solver.setField(f);
		solver.solve();
	}
	
	static void run(int[] dots) {
		BoomerangsField f = new BoomerangsField();
		f.setSize(9, 9);
		f.makePlacements(FORM, dots);
		BoomerangsSolver solver = new BoomerangsSolver();
		solver.setField(f);
		solver.solve();
	}
	
	static void stringTest() {
		String s = "Beware of <STRONG>greedy</strong> regular expressions.";
		String[] ss = s.split("<(.*)>");
		System.out.println(ss.length);
		for (int i = 0; i < ss.length; i++) System.out.println(ss[i]);
	}

	public static void main(String[] args) {
//		test();
		run(DOTS_2);
	}

}
