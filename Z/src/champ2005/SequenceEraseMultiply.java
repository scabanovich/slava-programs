package champ2005;

/**
 * @author glory
 * 
 * Given a number you can multiply it by 3 or erase the last digit. 
 * How many moves you need to get number n2 if you have started 
 * from number n1?
 *
 */
public class SequenceEraseMultiply {
	static int NUMBER_LIMIT = 2400000;
	byte[] moves = new byte[NUMBER_LIMIT];
	
	int startNumber;
	int endNumber;
	
	public void setStartNumber(int n) {
		startNumber = n;
	}
	
	public void setEndNumber(int n) {
		endNumber = n;
	}
	
	public void solve() {
		for (int i = 0; i < NUMBER_LIMIT; i++) moves[i] = -1;
		moves[startNumber] = 0;
		int t = 0;
		while(true) {
			int dn = iterate(t);
//			System.out.println("t=" + t + " dn=" + dn);
			if(dn == 0) break;
			if(moves[endNumber] >= 0) break;
			++t;
		}
		if(moves[endNumber] > 0) {
			System.out.println("Solved in " + moves[endNumber] + " moves.");
			System.out.println("Solution=" + getBackSolution());
		} else {
			System.out.println("No solution found.");
		}
	}
	
	int iterate(int t) {
		int dn = 0;
		for (int i = 0; i < NUMBER_LIMIT; i++) {
			if(moves[i] != t) continue;
			int a = i * 3;
			if(a < NUMBER_LIMIT && moves[a] == -1) {
				moves[a] = (byte)(t + 1);
				++dn;
			}
			a = i / 10;
			if(a < NUMBER_LIMIT && moves[a] == -1) {
				moves[a] = (byte)(t + 1);
				++dn;
			}
		}
		return dn;		
	}
	
	String getBackSolution() {
		StringBuffer sb = new StringBuffer();
		buildBackSolution(endNumber, sb);
		return sb.toString();
	}
	
	void buildBackSolution(int n, StringBuffer sb) {
		int t = moves[n];
		if(t == 0) return;
		if(n % 3 == 0) {
			int m = n / 3;
			if(moves[m] == t - 1) {
				sb.append(":");
				buildBackSolution(m, sb);
				return;
			}
		}
		for (int i = 0; i < 10; i++) {
			int m = n * 10 + i;
			if(moves[m] == t - 1) {
				sb.append("" + i);
				buildBackSolution(m, sb);
				return;
			}
		}
	}
	
	public static void main(String[] args) {
		SequenceEraseMultiply p = new SequenceEraseMultiply();
		p.setStartNumber(2006);
		p.setEndNumber(2005);
		p.solve();
	}

}
