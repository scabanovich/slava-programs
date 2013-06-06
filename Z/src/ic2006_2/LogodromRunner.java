package ic2006_2;

public class LogodromRunner {
	static int[] FILTER = {
		1,1,1,0,0,0,0,1,1,1,
		1,1,0,0,0,0,0,0,1,1,
		1,0,0,0,0,0,0,0,0,1,
		0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,
		1,0,0,0,0,0,0,0,0,1,
		1,1,0,0,0,0,0,0,1,1,
		1,1,1,0,0,0,0,1,1,1,
	};
	
	static int E = -1;
	static int[] REQ_A = {
		E,E,E,E,E,E,E,E,E,E,
		E,E,5,5,5,5,5,5,E,E,
		E,0,2,E,E,E,E,0,E,E,
		E,E,0,2,E,E,0,E,E,E,
		E,E,E,0,2,0,E,E,E,E,
		E,E,E,E,0,2,E,E,E,E,
		E,E,E,0,E,0,2,E,E,E,
		E,E,0,E,E,E,0,2,E,E,
		E,E,0,0,0,0,0,0,E,E,
		E,E,E,E,E,E,E,E,E,E,
	};
	static int[] REQ = {
		E,E,E,E,E,E,E,E,E,E,
		E,E,E,E,E,E,E,2,E,E,
		E,0,E,E,E,E,2,0,0,E,
		E,E,0,E,E,2,0,E,0,E,
		E,E,E,0,2,0,E,E,0,E,
		E,E,E,2,0,E,E,E,0,E,
		E,E,2,0,E,0,E,E,0,E,
		E,2,0,E,E,E,0,E,0,E,
		E,E,E,E,E,E,E,0,E,E,
		E,E,E,E,E,E,E,E,E,E,
	};
	static int[] REQ_B = {
		E,E,E,E,E,E,0,E,E,E,
		E,E,E,E,E,E,0,0,E,E,
		E,E,E,E,E,E,0,E,0,E,
		E,E,E,E,E,E,0,0,E,0,
		E,E,E,E,E,E,0,E,E,E,
		E,E,E,E,E,0,0,E,E,E,
		E,E,E,E,0,E,0,E,E,E,
		E,E,E,0,E,E,0,E,E,E,
		E,E,0,E,E,E,0,E,E,E,
		E,E,E,E,E,E,0,E,E,E,
	};
	static int[] REQ_C = {
		E,E,E,E,E,E,E,E,E,E,
		E,E,0,E,E,E,E,1,E,E,
		E,E,E,0,E,E,1,0,E,E,
		E,E,E,E,0,1,0,E,E,2,
		E,E,E,E,1,0,E,E,2,E,
		E,E,E,1,0,E,0,2,E,1,
		E,E,1,0,E,E,2,0,1,E,
		E,1,0,E,E,2,E,1,0,E,
		E,E,E,E,2,E,1,E,E,E,
		E,E,E,2,E,1,E,E,E,E,
	};

	public static void main(String[] args) {
		LogodromField f = new LogodromField();
		f.setSize(10);
		f.setFilter(FILTER);
		LogodromWords words = new LogodromWords();
		LogodromSolver solver = new LogodromSolver();
		solver.setField(f);
		solver.setWords(words);
//		solver.setReqState(REQ_A);
		solver.setReqState(REQ_C);
		solver.solve();
	}

}

/*
Solution 25
 - - - N A I R - - -
 - - E C D A E T - -
 - A D S C C A S O -
 O R A M O R T A A R
 T K R A D D T A A B
 I T T R A T A R D A
 S T S O R A R T A C
 - O S T A R A B R -
 - - S A T O R A - -
 - - - N D O R - - -
Solution 21
 - - - A T T A - - -
 - - T R R I A A - -
 - O S T E D A R A -
 T S R S M A A B R A
 O S O D A C A R K T
 R S T A B R A E T I
 O T A O R R A C O T
 - A N D S D A N R -
 - - D R T R A D - -
 - - - O C C A - - -
*/