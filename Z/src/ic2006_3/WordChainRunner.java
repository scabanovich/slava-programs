package ic2006_3;

import com.slava.common.TwoDimField;

public class WordChainRunner {
	static int[] FORM = {
		1,1,1,1,0,0,0,0,
		1,1,1,1,1,0,0,0,
		1,1,1,1,1,1,0,0,
		1,1,1,1,1,1,1,0,
		1,1,1,1,1,1,1,1,
		0,1,1,1,1,0,1,1,
		0,0,1,1,1,1,1,1,
		0,0,0,1,1,1,1,1,
		0,0,0,0,1,1,1,1
	};
	
	static char o = '\0';
	static char p = ' ';
	static char B = 'B';
	static char E = 'E';
	static char R = 'R';
	static char U = 'U';
	
	static char[] initial = {
		p,p,p,p,o,o,o,o,
		p,p,p,U,p,o,o,o,
		p,p,p,p,p,p,o,o,
		p,p,p,p,p,p,p,o,
		p,E,p,R,p,p,p,p,
		o,p,p,p,p,o,p,p,
		o,o,p,p,p,p,p,p,
		o,o,o,p,p,'R',p,'F',
		o,o,o,o,p,p,B,'E',
	};
	
	static String[] WORDS = {
		"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
		"JULY", "AUGUST", 
		//"SEPTEMBER", 
		"OCTOBER", "NOVEMBER", 
		//"DECEMBER" 
	};

	public static void main(String[] args) {
		TwoDimField f = new TwoDimField();
		f.setOrts(TwoDimField.TRIANGULAR_ORTS);
		f.setSize(8, 9);
		WordChainSolver solver = new WordChainSolver();
		solver.setField(f);
		solver.setForm(FORM);
		solver.setInitialData(initial);
		solver.setWords(WORDS);
		solver.solve();
		System.out.println("solutionCount=" + solver.solutionCount);
	}

}

/**
     U J R - 
    N B E U G 
   E O M S T U 
  O T C E V N A 
 B E A R - O Y -
  R U Y J - M A
   J N U A R Y
    A L R U F
     Y - B E

*/