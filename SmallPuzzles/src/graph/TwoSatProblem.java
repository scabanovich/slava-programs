package graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

public class TwoSatProblem {
	int[][] conditions;
	int size;

	public TwoSatProblem(int[][] conditions) {
		this.conditions = conditions;
		size = getMax(conditions) + 1;
	}

	public void solve() {
		while(cleanConditions()) {
		}
		reculculate();
		System.out.println("-->" + conditions.length);
		print();
		System.out.println("----");
		int[][] os = getOccurances();
		for (int i = 1; i < os.length; i++) {
			System.out.print(i + " " + os[i][0] + ":" + os[i][1] + "; ");
		}
		System.out.println("");
		while(cleanConditions2()) {
			System.out.println("Clean 2");
			print();
		}
		System.out.println("Finally");
		reculculate();
		Arrays.sort(conditions, comparator);
		print();
		while(applyEqualities()) {
			System.out.println("Equalities applied");
			print();
		}
		while(excludeNode()) {
			System.out.println("Node excluded");
			print();
		}
	}

	void print() {
		for (int i = 0; i < conditions.length; i++) {
			System.out.println(conditions[i][0] + " " + conditions[i][1]); 
		}
	}

	void reculculate() {
		int n = 1;
		int[] mapping = new int[size];
		for (int i = 0; i < conditions.length; i++) {
			for (int j = 0; j < conditions[i].length; j++) {
				int k = conditions[i][j];
				boolean negative = (k < 0);
				if(negative) k = -k;
				if(mapping[k] == 0) {
					mapping[k] = n;
					n++;
				}
				conditions[i][j] = negative ? -mapping[k] : mapping[k];
			}
		}
		normalize();
		size = n;
	}

	/**
	 * Remove conditions with a variable that is used only without NOT or only with NOT.
	 * @return
	 */
	boolean cleanConditions() {
		List<int[]> list = new ArrayList<int[]>();
		int[][] os = getOccurances();
		for (int i = 0; i < conditions.length; i++) {
			int k1 = Math.abs(conditions[i][0]);
			int k2 = Math.abs(conditions[i][1]);
			if(conditions[i][0] == -conditions[i][1]) continue;
			if(os[k1][0] == 0 || os[k1][1] == 0 || os[k2][0] == 0 || os[k2][1] == 0) continue;
			list.add(conditions[i]);
		}
		if(conditions.length == list.size()) {
			return false;
		}
		conditions = list.toArray(new int[list.size()][]);
		return true;
	}

	void normalize() {
		for (int i = 0; i < conditions.length; i++) {
			int k1 = Math.abs(conditions[i][0]);
			int k2 = Math.abs(conditions[i][1]);
			if(k1 > k2) {
				int c = conditions[i][0];
				conditions[i][0] = conditions[i][1];
				conditions[i][1] = c;
			}
		}
	}
	
	static ConditionsComparator comparator = new ConditionsComparator();
	
	static class ConditionsComparator implements Comparator<int[]> {

		public int compare(int[] o1, int[] o2) {
			if(o1.length != 2 || o2.length != 2) throw new RuntimeException("" + o1.length + " " + o2.length);
			int k10 = Math.abs(o1[0]), k11 = Math.abs(o1[1]); 
			int k20 = Math.abs(o2[0]), k21 = Math.abs(o2[1]);
			int k1min = k10 < k11 ? k10 : k11;
			int k2min = k20 < k21 ? k20 : k21;
			int k1max = k10 < k11 ? k11 : k10;
			int k2max = k20 < k21 ? k21 : k20;
			if(k1min != k2min) {
				return (k1min - k2min);
			}
			if(k1max != k2max) {
				return k1max - k2max;
			}
			// TODO Auto-generated method stub
			return 0;
		}	
	}

	/**
	 * Remove a variable that is used exactly once with NOT and exactly once without NOT.
	 * @return
	 */
	boolean cleanConditions2() {
		int i0 = -1;
		int[][] os = getOccurances();
		for (int i = 0; i < os.length; i++) {
			if(os[i][0] == 1 && os[i][1] == 1) {
				i0 = i;
				break;
			}
		}
		if(i0 > 0) {
			int ps = 0;
			int[] pairs = new int[2];
			List<int[]> list = new ArrayList<int[]>();
			for (int i = 0; i < conditions.length; i++) {
				int k1 = conditions[i][0];
				int k2 = conditions[i][1];
				if(Math.abs(k1) == i0 && Math.abs(k2) == i0) {
					//continue;
				} else if(Math.abs(k1) == i0) {
					pairs[ps] = k2;
					ps++;
				} else if(Math.abs(k2) == i0) {
					pairs[ps] = k1;
					ps++;
				} else {
					list.add(conditions[i]);
				}
			}
			if(ps > 0) list.add(pairs);
			conditions = list.toArray(new int[list.size()][]);
//			while(cleanConditions());
			return true;
		}
		return false;
	}

	boolean applyEqualities() {
		int[][] os = getOccurances();
		int value = 0;
		for (int i = 0; i < conditions.length; i++) {
			if(conditions[i][0] == conditions[i][1]) {
				int v = conditions[i][0];
				int va = Math.abs(v);
				if(getEquality(-v)) {
					if(conditions.length == 2) {
						return false;
					}
					conditions = new int[][]{{v,v},{-v,-v}};
					return true;
				}
				if(os[va][0] + os[va][1] > 2) {
					value = v;
					break;
				}
			}
		}
		if(value != 0) {
			for (int i = 0; i < conditions.length; i++) {
				if(conditions[i][0] == conditions[i][1]) {
					continue;
				} else if(conditions[i][0] == value || conditions[i][1] == value) {
					removeCondition(i);
					i--;
				} else if(conditions[i][0] == -value) {
					conditions[i][0] = conditions[i][1];
				} else if(conditions[i][1] == -value) {
					conditions[i][1] = conditions[i][0];
				}
			}
			normalize();
			Arrays.sort(conditions, comparator);
			removeRepetitions();
			while(cleanConditions());
			return true;
		}
		return false;
	}

	boolean getEquality(int v) {
		for (int i = 0; i < conditions.length; i++) {
			if(conditions[i][1] == v && conditions[i][0] == v) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Excludes variable i0 if it is used exactly once with NOT, or exactly once without NOT 
	 * @return
	 */
	boolean excludeNode() {
		int i0 = -1;
		int[][] os = getOccurances();
		int singular = 0, multiple = 0;
		for (int i = 0; i < os.length; i++) {
			if((os[i][0] == 1 || os[i][1] == 1) && (os[i][0] > 1 || os[i][1] > 1)) {
				i0 = i;
				if(os[i][0] == 1) {
					singular = i;
					multiple = -i;
				} else {
					singular = -i;
					multiple = i;
				}
				break;
			}
		}
		if(i0 > 0) {
			int singularPair = 0;
			for (int i = 0; i < conditions.length; i++) {
				if(conditions[i][0] == singular) {
					singularPair = conditions[i][1];
					removeCondition(i);
					break;
				} else if(conditions[i][1] == singular) {
					singularPair = conditions[i][0];
					removeCondition(i);
					break;
				}
			}
			for (int i = 0; i < conditions.length; i++) {
				if(conditions[i][0] == multiple) {
					conditions[i][0] = singularPair;
				} else if(conditions[i][1] == multiple) {
					conditions[i][1] = singularPair;
				}
			}
			normalize();
			Arrays.sort(conditions, comparator);
			removeRepetitions();
			while(cleanConditions());
			while(applyEqualities());
			return true;
		}
		return false;
	}

	void removeCondition(int i) {
		int[][] cs = new int[conditions.length - 1][2];
		if(i > 0) System.arraycopy(conditions, 0, cs, 0, i);
		System.arraycopy(conditions, i + 1, cs, i, conditions.length - i - 1);
		conditions = cs;
	}

	/**
	 * Sort and normilize before using this method.
	 */
	void removeRepetitions() {
		for (int i = 1; i < conditions.length; i++) {
			while(conditions[i][0] == conditions[i - 1][0] && conditions[i][1] == conditions[i - 1][1]) {
				removeCondition(i);
				if(i == conditions.length) break;
			}
		}
	}

	// [0] - positive; [1] - negative
	public int[][] getOccurances() {
		int[][] result = new int[size][2];
		for (int i = 0; i < conditions.length; i++) {
			for (int j = 0; j < conditions[i].length; j++) {
				int k = conditions[i][j];
				if(k > 0) {
					result[k][0]++;
				} else {
					result[-k][1]++;
				}
			}
		}
		return result;
	}

	
	static int[][] readLines(String uri) {
		StringTokenizer st = Util.read(uri);
		String s = st.nextToken().trim();
		int lines = Integer.parseInt(s);
		System.out.println(st.countTokens() + " " + lines);
		int[][] result = new int[lines][2];
		for (int i = 0; i < lines; i++) {
			s = st.nextToken();
			int[] vs = Util.parseInt(s);
			if(vs.length != 2) throw new RuntimeException("Two integers expected: " + s);
			result[i] = vs;
		}
		
		return result;
	}

	static int getMax(int[][] conditions) {
		int max = -1;
		for (int i = 0; i < conditions.length; i++) {
			for (int j = 0; j < conditions[i].length; j++) {
				int k = Math.abs(conditions[i][j]);
				if(k > max) max = k;
			}
		}
		return max;
	}


	static int[][] generateConditions(int nodes, int conditions, int randomConditions) {
		int[][] result = new int[conditions][2];
		int[] values = new int[nodes + 1];
		Random seed = new Random();
		for (int i = 1; i < values.length; i++) {
			values[i] = (seed.nextInt(2) == 1) ? -1 : 1;
		}
		for (int i = 0; i < conditions; i++) {
			int n1 = seed.nextInt(nodes) + 1;
			result[i][0] = (seed.nextInt(2) == 1) ? -n1 : n1;
			int n2 = seed.nextInt(nodes) + 1;
			while(n2 == n1) n2 = seed.nextInt(nodes) + 1; // case n1=n2 is too trivial
			result[i][1] = (seed.nextInt(2) == 1) ? -n2 : n2;
			if(i < conditions - randomConditions) { 
				if(result[i][0] * values[n1] < 0 && result[i][1] * values[n2] < 0) {
					result[i][1] = -result[i][1];
					if(seed.nextInt(3) > 0) result[i][0] = -result[i][0];
				}
			}
		}
		return result;		
	}

	static int[][] generateCircularConditions(int conditions) {
		int[][] result = new int[conditions][2];
		for (int i = 0; i < conditions; i++) {
			result[i][0] = -(i + 1);
			result[i][1] = (i + 2 > conditions) ? i + 2 - conditions : i + 2;
		}
		return result;
	}

	
	public static void main(String[] args) {
		int[][] conditions = generateCircularConditions(300);
//			generateConditions(1000000, 2241150, 0);
//			readLines("https://spark-public.s3.amazonaws.com/algo2/datasets/2sat6.txt");
		TwoSatProblem2 p = new TwoSatProblem2(conditions);
		p.solve();
	}
}

/**
--------------
1: 1
2: 0
3: 1
4: 1
5: 0
6: 0
*/
