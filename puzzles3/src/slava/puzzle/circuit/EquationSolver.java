package slava.puzzle.circuit;

/**
 * 
 */
public class EquationSolver {
	Variable[] variables;
	Equation[] equations;
	int[] valueSet;

	int[] used;
	int unsetVariablesCount;

	int t;
	int[] currentVariable;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int solutionCount;

	public EquationSolver() {}
	
	public void setVariables(Variable[] variables) {
		this.variables = variables;
	}
	
	public void setEquations(Equation[] equations) {
		this.equations = equations;
	}
	
	public void setValueSet(int[] set) {
		valueSet = set;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		unsetVariablesCount = 0;
		used = new int[valueSet.length];
		for (int i = 0; i < variables.length; i++) {
			int v = variables[i].getValue();
			if(!variables[i].isValueSet) {
				unsetVariablesCount++;
			} else {
				for (int j = 0; j < valueSet.length; j++) {
					if(valueSet[j] == v) {
						used[j]++;
					}
				}
			}
		}
		currentVariable = new int[unsetVariablesCount + 1];
		wayCount = new int[unsetVariablesCount + 1];
		way = new int[unsetVariablesCount + 1];
		ways = new int[unsetVariablesCount + 1][valueSet.length];
		t = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(unsetVariablesCount == 0) return;
		int c = getCurrentVariable();
		currentVariable[t] = c;
		for (int j = 0; j < valueSet.length; j++) {
			if(used[j] > 0) continue;
			variables[c].setValue(valueSet[j]);
			if(checkEquations()) {
				ways[t][wayCount[t]] = j;
				wayCount[t]++;				
			}
			variables[c].unset();
		}
	}
	
	int getCurrentVariable() {
		int mx = 20;
		Variable mvi = null;
		for (int i = 0; i < equations.length && mx > 1; i++) {
			Equation eq = equations[i];
			int x = 0;
			Variable vi = null;
			for (int j = 0; j < eq.variables.length; j++) {
				if(!eq.variables[j].isValueSet) {
					vi = eq.variables[j];
					++x;
				}
			}
			if(x > 0 && x < mx) {
				mx = x;
				mvi = vi;
			}
		}
		for (int i = 0; i < variables.length; i++) {
			if(variables[i] == mvi) return i;
		}
		for (int i = 0; i < variables.length; i++) {
			if(!variables[i].isValueSet) return i;
		}
		return -1;
	}
	
	boolean checkEquations() {
		for (int i = 0; i < equations.length; i++) {
			if(!checkEquation(equations[i])) return false;
		}
		return true;
	}
	
	boolean checkEquation(Equation eq) {
		int sum = eq.constant;
		int x = 0;
		for (int i = 0; i < eq.variables.length; i++) {
			Variable v = eq.variables[i];
			if(v.isValueSet) {
				sum += v.getValue() * eq.coeffitients[i];
			} else {
				++x;
			}
		}
		return x > 0 || sum == 0;
	}
	
	void move() {
		int i = currentVariable[t];
		int j = ways[t][way[t]];
		variables[i].setValue(valueSet[j]);
//		System.out.println(i + "=" + valueSet[j]);
		unsetVariablesCount--;
		used[j]++;		
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int i = currentVariable[t];
		int j = ways[t][way[t]];
		variables[i].unset();
		unsetVariablesCount++;
		used[j]--;		
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(unsetVariablesCount == 0) {
				solutionCount++;
				if(solutionCount < 3) {
					printSolution();
				}
			}
		}
	}
	
	void printSolution() {
		System.out.println("Solution");
		for (int i = 0; i < variables.length; i++) {
			System.out.println(variables[i].toString());
		}
	}
	
	public int getSolutionCount() {
		return solutionCount;
	}

}
