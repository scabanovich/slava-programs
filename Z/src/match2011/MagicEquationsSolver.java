package match2011;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MagicEquationsSolver {
	Equation[] equations;
	Equation[][] equationsByPosition;
	int[] valueLimits;
	Map<Integer, Integer> initialValues = new HashMap<Integer, Integer>();

	int[] state;
	int[] valueUsage;
	int movesToEnd;

	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	int[] temp;

	int treeCount;
	int solutionCount;

	public MagicEquationsSolver() {}

	public void setEquations(Equation[] equations) {
		this.equations = equations;
	}

	void resetEquations() {
		movesToEnd = 0;
		state = new int[getMaxPosition() + 1];
		for (int p = 0; p < state.length; p++) {
			state[p] = 1000;
		}
		equationsByPosition = new Equation[state.length][0];
		Map<Integer, List<Equation>> mapping = new HashMap<Integer, List<Equation>>();
		for (int i = 0; i < equations.length; i++) {
			for (int j = 0; j < equations[i].getLength(); j++) {
				int p = equations[i].getPositionAt(j);
				List<Equation> l = mapping.get(p);
				if(l == null) {
					l = new ArrayList<Equation>();
					mapping.put(p, l);
				}
				l.add(equations[i]);
				if(state[p] == 1000) {
					state[p] = -1;
					movesToEnd++;
				}
			}
		}
		for (Integer p: mapping.keySet()) {
			equationsByPosition[p] = mapping.get(p).toArray(new Equation[0]);
		}
	}

	public void setValueLimits(int[] valueLimits) {
		this.valueLimits = valueLimits;
	}

	public void addInitialValue(int position, int value) {
		initialValues.put(position, value);
	}

	int getMaxPosition() {
		int m = -1;
		for (int i = 0; i < equations.length; i++) {
			for (int j = 0; j < equations[i].getLength(); j++) {
				if(equations[i].getPositionAt(j) > m) {
					m = equations[i].getPositionAt(j);
				}
			}
		}
		return m;
	}

	void setValue(int position, int value) {
		if(state[position] >= 0) {
			return;
		}
		Equation[] eqs = equationsByPosition[position];
		for (int i = 0; i < eqs.length; i++) {
			eqs[i].exclude(position, value);
		}
		state[position] = value;
		valueUsage[value]++;
		movesToEnd--;
	}

	public void clearValue(int position) {
		if(state[position] < 0) {
			return;
		}
		Equation[] eqs = equationsByPosition[position];
		for (int i = 0; i < eqs.length; i++) {
			eqs[i].restore(position);
		}
		int value = state[position];
		state[position] = -1;
		valueUsage[value]--;
		movesToEnd++;
	}

	public void solve() {
		init();
		anal();
	}

	void init() {
		resetEquations();
		valueUsage = new int[valueLimits.length];
		for (Integer p: initialValues.keySet()) {
			Integer v = initialValues.get(p);
			setValue(p, v);
		}

		place = new int[movesToEnd + 1];
		way = new int[movesToEnd + 1];
		wayCount = new int[movesToEnd + 1];
		ways = new int[movesToEnd + 1][valueLimits.length];

		t = 0;
		solutionCount = 0;
		treeCount = 0;
	}

	void srch() {
		wayCount[t] = 0;
		if(movesToEnd == 0) return;
		place[t] = getNarrowPlace();
		if(place[t] < 0) return;
		for (int v = 0; v < valueLimits.length; v++) {
			if(canSet(v)) {
				ways[t][wayCount[t]] = v;
				wayCount[t]++;
			}
		}
		
	}

	int[] importance = {0, 10, 5, 2, 1, 1, 1, 1, 1, 1, 1, 1};

	int getNarrowPlace() {
		int result = -1;
		int max = 0;
		for (int p = 0; p < state.length; p++) {
			if(state[p] >= 0) continue;
			Equation[] eq = equationsByPosition[p];
			int imp = 0;
			for (int i = 0; i < eq.length; i++) imp += importance[eq[i].getLength()];
			if(imp > max) {
				max = imp;
				result = p;
			}
		}
		return result;
	}

	boolean canSet(int value) {
		if(valueUsage[value] >= valueLimits[value]) {
			return false;
		}
		Equation[] eqs = equationsByPosition[place[t]];
		for (int i = 0; i < eqs.length; i++) {
			Equation eq = eqs[i];
			if(eq.getLength() == 1) {
				if(eq.getSum() != value) {
					return false;
				}
			} else if(eq.getLength() == 2) {
				int v1 = eq.getSum() - value;
				if(v1 < 0 || v1 >= valueLimits.length || valueUsage[v1] >= valueLimits[v1]) {
					return false;
				}
				if(v1 == value && valueUsage[v1] + 1 >= valueLimits[v1]) {
					return false;
				}
			} else {
				int s = eq.getSum() - value;
				int n = eq.getLength() - 1;
				if(getMinSum(n) > s || getMaxSum(n) < s) {
					return false;
				}
			}
		}
		return true;
	}

	int getMinSum(int n) {
		int s = 0;
		for (int i = 0; i < valueLimits.length && n > 0; i++) {
			int dn = valueLimits[i] - valueUsage[i];
			if(dn > n) dn = n;
			n -= dn;
			s += i * dn;
		}
		return s;
	}
	int getMaxSum(int n) {
		int s = 0;
		for (int i = valueLimits.length - 1; i >= 0 && n > 0; i--) {
			int dn = valueLimits[i] - valueUsage[i];
			if(dn > n) dn = n;
			n -= dn;
			s += i * dn;
		}
		return s;
	}
	

	void move() {
		int p = place[t];
		int v = ways[t][way[t]];
		setValue(p, v);
		++t;
		srch();
		way[t] = -1;
	}

	void back() {
		--t;
		int p = place[t];
		clearValue(p);
	}

	void anal() {
		srch();
		way[t] = -1;
		int tm = 0;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t > tm) {
				tm = t;
//				System.out.println("t=" + tm);
			}
			if(wayCount[t] == 0) {
				treeCount++;
			}
			if(movesToEnd == 0) {
				++solutionCount;
				if(solutionCount == 1 || solutionCount % 1000 == 1) 
					printSolution();
				onSolutionFound(state);
			}
		}
	}

	public void onSolutionFound(int[] state) {
		
	}

	void printSolution() {
		for (int p = 0; p < state.length; p++) {
			System.out.print(" " + state[p]);
		}
		System.out.println("");
	}

	public int getSolutionCount() {
		return solutionCount;
	}
}
