package forsmarts;

import forsmarts.distances.DistancesField;

public class EasyScyscripersRunner {
	static int A = 0;
	static int B = 1;
	static int C = 2;
	//x,y,d,v
	static int[][] LETTER_RESTRICTIONS = {
		{0,4,0,A}, {0,0,0,B},
		{1,0,1,A}, {3,0,1,A},
		{5,1,2,B}, {5,3,2,A},
		{4,5,3,A}, {2,5,3,C},
	};
	static int[][] HOUSE_RESTRICTIONS = {
		{0,5,0,3}, {0,3,0,1}, {0,2,0,2},
		{4,0,1,3}, {5,0,1,2}, 
		{5,0,2,1}, {5,4,2,2},
		{1,5,3,2},
	};

	static int[][] LETTER_RESTRICTIONS_T = {
		{0,1,0,C}, {0,2,0,B}, {0,4,0,A},
		{1,0,1,C}, {3,0,1,A}, {4,0,1,B},
		{5,0,2,C}, {5,2,2,A}, {5,3,2,C},
		{0,5,3,A}, {3,5,3,B}, {4,5,3,C}
	};
	static int[][] HOUSE_RESTRICTIONS_T = {
		{0,0,0,3}, {0,3,0,3}, {0,5,0,2},
		{0,0,1,2}, {2,0,1,1}, {5,0,1,1},
		{5,1,2,1}, {5,4,2,2}, {5,5,2,2},
		{1,5,3,1}, {2,5,3,3}, {5,5,3,3}

	};

	public static void main(String[] args) {
		DistancesField f = new DistancesField();
		f.setSize(6, 6);
		EasyScyscripersSolver solver = new EasyScyscripersSolver();
		solver.setField(f);
		solver.setRestrictions(LETTER_RESTRICTIONS, HOUSE_RESTRICTIONS);		
		solver.solve();
		System.out.println("SolutionCount=" + solver.solutionCount);
	}

}

/*
 B 2 1 3 C A
 C A 3 1 B 2
 2 C B A 1 3
 3 B 2 C A 1
 1 3 A B 2 C
 A 1 C 2 3 B

 A32ABA
*/