package pqrst13;

public class Sentences {
	String[] s0 = {"", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
	String[] s1 = {"", "ten", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};
	String[] sh = {"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
	String h = "hundred";
	
	int[] numberLengths = new int[399];
	
	String[] sentences = {
		"The sentence at bottom right has letters",
		"The sentence at bottom left has letters",
		"The sentence at top left has letters",
		"The sentence at top right has letters",
		"All of these five sentences contain letters in total",
	};








	int[] sentenceLengths = new int[sentences.length];
	
	int[] inserts = new int[sentences.length];
	
	public void solve() {
		init();
		anal();
	}
	
	public void init() {
		makeNumberLength();
		for (int i = 0; i < sentences.length; i++) {
			sentenceLengths[i] = 0;
			for (int j = 0; j < sentences[i].length(); j++) {
				if(Character.isLetter(sentences[i].charAt(j))) ++sentenceLengths[i];
			}
			System.out.println(sentenceLengths[i]);
		}
	}
	
	void makeNumberLength() {
		for (int i = 1; i < numberLengths.length; i++) {
			int n2 = i / 100;
			int n1 = (i / 10) % 10;
			int n0 = i % 10;
			if(n2 > 0) {
				numberLengths[i] += s0[n2].length() + h.length();
				if(n1 > 0 || n0 > 0) numberLengths[i] += 3;
			}
			if(n0 == 0) {
				numberLengths[i] += s1[n1].length();
			} else if(n1 == 1) {
				numberLengths[i] += sh[n0].length();
			} else {
				numberLengths[i] += s1[n1].length() + s0[n0].length();
			}
		}
	}
	
	void anal() {
		for (int q3 = 30; q3 < 60; q3++) {
			inserts[3] = q3;
			inserts[2] = sentenceLengths[3] + numberLengths[inserts[3]];
			inserts[1] = sentenceLengths[2] + numberLengths[inserts[2]];
			inserts[0] = sentenceLengths[1] + numberLengths[inserts[1]];
			int q3a = sentenceLengths[0] + numberLengths[inserts[0]];
			if(q3a != q3) continue;
			int sm = inserts[0] + inserts[1] + inserts[2] + inserts[3] + sentenceLengths[4];
			for (int s = sm; s < numberLengths.length; s++) {
				inserts[4] = sm + numberLengths[s];
				if(s != inserts[4]) continue;
				System.out.println("" + inserts[4] + " " + inserts[0] + " " + inserts[1] + " " + inserts[2] + " " + inserts[3]);
			}
		}
	}

	public static void main(String[] args) {
		Sentences s = new Sentences();
		s.solve();
	}

}

//232 44 38 41 43
//229 43 35 40 44
