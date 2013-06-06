package year2013;

/**
  For a given string S finds substring B with maximal length such that S = A + B + B + C.
  Usage:
      String text = ...;
      SubsequentEqualSubstringsFinder f = new SubsequentEqualSubstringsFinder();
      f.find(text);
      int length = f.getSequenceLength();
      int position = f.getFirstSequencePosition();
      f.printResults();
*/
public class SubsequentEqualSubstringsFinder {
	String text;
	int pos1 = -1;
	int pos2 = -1;

	public SubsequentEqualSubstringsFinder() {
	}

	public void find(String text) {
		find(text, 0, text.length());
	}

	public void find(String text, int from, int to) {
		this.text = text;
		pos1 = -1;
		pos2 = -1;
		doFind(from, to, 0);
	}

	public int getFirstSequencePosition() {
		return pos1;
	}

	public int getSequenceLength() {
		return pos2 - pos1;
	}

	private int doFind(int from, int to, int minimum) {
		if(to == from || to - from < 2 * minimum) {
			return minimum;
		}
		int p = (to + from) / 2;
		for (int q = from; q + minimum <= p; q++) {
			if(find(from, to, q, p)) {
				minimum = p - q + 1;
			}			
		}
		for (int q = to - 1; q >= p + minimum; q--) {
			if(find(from, to, p, q)) {
				minimum = q - p + 1;
			}
		}
		minimum = doFind(from, p, minimum);
		minimum = doFind(p + 1, to, minimum);
		return minimum;
	}

	private boolean find(int from, int to, int left, int right) {
		int c = right - left;
		int p = left;
		int q = right;
		while(q < text.length() && p < right && text.charAt(p) == text.charAt(q)) {
			c--;
			if(c == 0) {
				pos1 = left;
				pos2 = right;
				return true;
			}
			p++;
			q++;
		}
		p = left - 1;
		q = right - 1;
		while(p >= 0 && q > left && text.charAt(p) == text.charAt(q)) {
			c--;
			if(c == 0) {
				pos1 = p;
				pos2 = q;
				return true;
			}
			p--;
			q--;
		}
		return false;
	}

	public void printResults() {
		if(pos1 < 0) {
			System.out.println("Nothing found");
		} else {
			System.out.println("Length = " + getSequenceLength());
			System.out.println("First position = " + getFirstSequencePosition());
		}
	}

}
