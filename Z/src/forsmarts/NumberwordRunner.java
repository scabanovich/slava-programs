package forsmarts;

import com.slava.common.RectangularField;

/**
 * Forsmarts. Season 7, Issue 20 No 25. Numberword.
 * 
 * In a rectangular table of any size, write numbers from 1 to 99 
 * (as English words). Numbers should be written successively, 
 * going from the end of the line to the start of the next line. 
 * Letters in rows and columns must not repeat. 
 * Maximize the number of digits in written numbers.
 */

public class NumberwordRunner {
	static String[] NUMBERS = new String[100];
	static int[] SCORING = new int[100];
	static {
		String[] oneDigit = {"", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
		for (int i = 0; i < oneDigit.length; i++) NUMBERS[i] = oneDigit[i];
		String[] teens = {"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
		for (int i = 0; i < teens.length; i++) NUMBERS[i + 10] = teens[i];
		String[] tens = {"twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};
		for (int i = 0; i < tens.length; i++) {
			for (int j = 0; j < oneDigit.length; j++) {
				NUMBERS[10 * (i + 2) + j] = tens[i] + oneDigit[j];
			}
		}
		for (int i = 0; i < SCORING.length; i++) {
			SCORING[i] = i < 10 ? 1 : 2;
		}
	};

	public static void main(String[] args) {
		RectangularField f = new RectangularField();
		f.setSize(7, 25);
		NumberwordSolver solver = new NumberwordSolver();
		solver.setField(f);
		solver.setWords(NUMBERS, SCORING);
		solver.solve();
	}

}

/*

Width=7 Scoring 18
 1 66 44 6 12 4 9 64 10 60 40
Width=8 Scoring 17
 1 60 4 64 10 84 2 8 6 44 70
Width 9 Scoring 17
 4 60 80 44 84 9 64 10 6 5 2
Width 10 Scoring 14
 4 6 12 40 9 64 60 1 50

*/