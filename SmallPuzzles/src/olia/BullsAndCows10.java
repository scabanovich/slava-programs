package olia;

import java.util.*;

class XData {
	int offset;
	int[] probe;
	int bulls;
	int cows;
	Set usage = new HashSet();
	
	public XData(String s) {
		StringTokenizer st = new StringTokenizer(s, ";- ");
		String[] parts = new String[st.countTokens()];
		for (int i= 0; i < parts.length; i++) parts[i] = st.nextToken();
		offset = Integer.parseInt(parts[0]);
		bulls = Integer.parseInt(parts[parts.length - 2]);
		cows = Integer.parseInt(parts[parts.length - 1]);
		probe = new int[parts.length - 3];
		for (int i = 0; i < probe.length; i++) {
			probe[i] = Integer.parseInt(parts[i + 1]);
			usage.add(new Integer(probe[i]));
		}
	}
	
	public XData(int offset, int[] probe, int bulls, int cows) {
		this.offset = offset;
		this.probe = probe;
		for (int i = 0; i < probe.length; i++) usage.add(new Integer(probe[i]));
		this.bulls = bulls;
		this.cows = cows;
	}

	public boolean check(int[] state) {
		int bc = 0;
		int cc = 0;
		int empty = 0;
		for (int i = 0; i < probe.length; i++) {
			int n = state[i + offset];
			if(n < 0) {
				empty++;
			} else if(n == probe[i]) {
				bc++;
			} else if(usage.contains(Integer.valueOf(n))) {
				cc++;
			}
		}
		if(bc > bulls || bc + empty < bulls) return false;
		if(cc > cows || cc + empty < cows) return false;
		if(bc + cc + empty < bulls + cows) return false;
		return true;
	}
	
	public int getBullsAndCowsIndex(int[] state) {
		int bc = 0;
		int cc = 0;
		int empty = 0;
		for (int i = 0; i < probe.length; i++) {
			int n = state[i + offset];
			if(n < 0) {
				empty++;
			} else if(n == probe[i]) {
				bc++;
			} else if(usage.contains(Integer.valueOf(n))) {
				cc++;
			}
		}
		if(empty > 0) return -1;
		return BullsAndCows10.OUTCOMES.index[bc][cc];
	}
	
	public static XData createRandom(int offset) {
		int[] probe = new int[4];
		int[] usage = new int[10];
		Random r = new Random();
		for (int i = 0; i  < probe.length; i++) {
			int n = r.nextInt(10);
			while(usage[n] > 0) n = r.nextInt(10);
			usage[n] = 1;
			probe[i] = n;
		}
		return new XData(offset, probe, 0, 0);
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(offset).append("; ");
		for (int i = 0; i < probe.length; i++) {
			if(i > 0) sb.append(' ');
			sb.append(probe[i]);
		}
		sb.append("; ").append(bulls).append('-').append(cows);
		return sb.toString();
	}
	
}

class Outcomes {
	int length;
	int[][] index;
	int[] bulls;
	int[] cows;
	
	public Outcomes(int length) {
		this.length = length;
		index = new int[length + 1][length + 1];
		int d = (length + 1) * (length + 2) / 2;
		bulls = new int[d];
		cows = new int[d];
		int i = 0;
		for (int b = 0; b <= length; b++) {
			for (int c = 0; c <= length - b; c++) {
				index[b][c] = i;
				bulls[i] = b;
				cows[i] = c;
				i++;
			}
		}
	}
}

public class BullsAndCows10 {
	static Outcomes OUTCOMES = new Outcomes(4);

	List data = new ArrayList();
	int solutionLimit = 10000;

	public BullsAndCows10() {}
	
	public void addData(XData d) {
		data.add(d);
	}
	
	public List getSolutions() {
		List solutions = new ArrayList();
		
		int[] state = new int[10];
		int[] usage = new int[10];
		int[] way = new int[11];

		int t = 0;
		way[t] = 0; // not -1 because first digit cannot be 0
		while(true) {
			while(way[t] == 9 || t == 10) {
				if(t == 0) return solutions;
				t--;
				usage[way[t]] = 0;
			}
			way[t]++;
			if(usage[way[t]] == 1) continue;
			state[t] = way[t];
			usage[way[t]] = 1;
			t++;
			way[t] = -1;
			if(t == 10) {
				boolean r = true;
				for (int i = 0; i < data.size() && r; i++) {
					r = ((XData)data.get(i)).check(state);
				}
				if(r) {
					solutions.add((int[])state.clone());
					if(solutions.size() > solutionLimit) return solutions;
				}
			}
		}
	}
	
	public XData narrow(List solutions, int offset) {
		int bestSize = solutions.size();
		int bestIndex = -1;
		int min = 4;
		XData d = null;
		int attempt = 0;
		while(attempt < 5000 && (bestSize > 1 || min == 4)) {
			attempt++;
			int[] distribution = new int[15];
			XData x = XData.createRandom(offset);
			for (int i = 0; i < solutions.size(); i++) {
				int index = x.getBullsAndCowsIndex((int[])solutions.get(i));
				distribution[index]++;
			}
			for (int i = 0; i < distribution.length; i++) {
				if(distribution[i] == 0) continue;
						if(OUTCOMES.bulls[i] > 2 && attempt < 4000) continue;
				if(distribution[i] < bestSize 
					|| (distribution[i] == bestSize && OUTCOMES.bulls[i] + OUTCOMES.cows[i] < min) ) {
					bestSize = distribution[i];
					bestIndex = i;
					d = x;
					min = OUTCOMES.bulls[i] + OUTCOMES.cows[i];
				}
			}
		}
		if(d != null) {
			d.bulls = OUTCOMES.bulls[bestIndex];
			d.cows = OUTCOMES.cows[bestIndex];
		}
		System.out.println("Solutions " + bestSize);
		return d;
	}

	//offset; number; bulls-cows
	static String[] DATA = {
		"0;  1 9 6 8;  1-1",
		"2;  2 4 0 7;  2-1",
		"4;  3 5 6 2;  0-2",

	};

	static int OFFSET = 6;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BullsAndCows10 p = new BullsAndCows10();
		for (int i = 0; i < DATA.length; i++) p.addData(new XData(DATA[i]));
		
		List solutions = p.getSolutions();
		if(solutions.size() == 0) {
			System.out.println("No solution found");
		} else if(solutions.size() == 1) {
			System.out.println("Unique solution");
			int[] s = (int[])solutions.get(0);
			for (int i = 0; i < s.length; i++) {
				System.out.print(" " + s[i]);
			}
			System.out.println("");
		} else if(solutions.size() > p.solutionLimit) {
			System.out.println("Too many solutions");
		} else {
			System.out.println("Solution found: " + solutions.size());
			XData d = p.narrow(solutions, OFFSET);
			System.out.println("Data: " + d);
			if(d != null) {
				p.addData(d);
				solutions = p.getSolutions();
				if(solutions.size() == 1) {
					System.out.println("Unique solution");
					int[] s = (int[])solutions.get(0);
					for (int i = 0; i < s.length; i++) {
						System.out.print(" " + s[i]);
					}
					System.out.println("");
				}
			}
		}
	}

}

/**
  
 Puzzle 1.
		0;  3 9 0 4;  3-0
		2;  4 8 6 2;  1-1
		4;  1 7 2 5;  0-2
		6;  1 0 8 9;  3-0
		
		3 9 7 4 6 5 1 0 8 2

 Puzzle 2.
		0;  7 3 8 2;  0-2
		2;  3 8 5 9;  1-2
		4;  4 3 2 7;  1-2
		6;  1 2 9 8;  2-1
		
		6 7 5 8 0 3 4 2 9 1
		
 Puzzle 3.
		0;  2 5 1 7;  2-0
		2;  6 3 0 8;  1-2
		4;  8 9 2 3;  1-1
		6;  3 5 6 7;  2-1
		
		2 4 1 3 8 0 5 9 6 7
 
*/