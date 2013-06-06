package slava.puzzle.football.analysis;

import slava.puzzle.football.model.FootballField;

public class FootballProblemChecker {
	FootballField field;
	int[] pathValues;
	int[] beginValues;
	int[] endValues;
	int pathValuesCount;
	int beginValuesCount;
	int endValuesCount;

	public void setField(FootballField field) {
		this.field = field;
	}
	
	public void setPath(int[] values) {
		pathValues = new int[values.length];
		for (int i = 0; i < pathValues.length; i++) {
			pathValues[i] = (values[i] <= 0) ? 0 : 1;
		}
		pathValues[0] = 1;
		pathValues[pathValues.length - 1] = 1;
		pathValuesCount = 0;
		for (int i = 0; i < pathValues.length; i++) {
			pathValuesCount += pathValues[i];
		}
	}
	
	public boolean check() {
		stack = new int[field.getSize()];
		beginValues = new int[field.getSize()];
		endValues = new int[field.getSize()];
		for (int i = 0; i < pathValues.length; i++) {
			beginValues[i] = 0;
			endValues[i] = 0;
		}
		beginValues[0] = 1;
		beginValues[pathValues.length - 1] = 1;
		endValues[0] = 1;
		endValues[pathValues.length - 1] = 1;
		fillFromBegin();
		fillFromEnd();
		return compare();
	}
	
	int[] stack;
	
	void fillFromBegin() {
		int v = 1;
		int c = 0;
		stack[0] = 0;
		while(c < v) {
			int p = stack[c];
			int k = field.getValueAt(p);
			if(k > 0) {
				for (int d = 0; d < 4; d++) {
					int pc = p;
					int kc = k;
					while(kc > 0 && pc >= 0) {
						--kc;
						pc = field.jump(pc, d);
					}
					if(pc >= 0 && beginValues[pc] == 0) {
						beginValues[pc] = 1;
						stack[v] = pc;
						++v;
					}
				}
			}
			++c;
		}
		beginValuesCount = v;
	}

	void fillFromEnd() {
		int v = 1;
		int c = 0;
		stack[0] = field.getSize() - 1;
		while(c < v) {
			int p = stack[c];
			for (int d = 0; d < 4; d++) {
				int pc = field.jump(p, d);
				int k = 1;
				while(pc >= 0) {
					if(endValues[pc] == 0 && field.getValueAt(pc) == k) {
						endValues[pc] = 1;
						stack[v] = pc;
						++v;
					}
					++k;
					pc = field.jump(pc, d);
				}
			}
			++c;
		}
		endValuesCount = v;
	}
	
	boolean compare() {
		for (int i = 0; i < field.getSize(); i++) {
			if(beginValues[i] == 1 && endValues[i] == 1) {
				if(pathValues[i] != 1) {
					return false;
				} 
			}
		}
		return true;
	}
	
	public int[] computeState() {
		check();
		for (int i = 0; i < field.getSize(); i++) {
			pathValues[i] = (beginValues[i] == 1 && endValues[i] == 1) ? 1 : 0; 
		}
		int[] state = new int[field.getSize()];
		for (int i = 0; i < field.getSize(); i++) {
			if(beginValues[i] == 1) state[i] += 1;
			if(endValues[i] == 1) state[i] += 2;
		}
		checkPath(state);
		return state;
	}
	
	void checkPath(int[] state) {
		int[] s = new int[state.length];
		int v = 1;
		int c = 0;
		stack[0] = field.getSize() - 1;
		s[stack[0]] = 1;
		while(c < v) {
			int p = stack[c];
			int dv = 0;
			s[p] = 2;
			for (int d = 0; d < 4; d++) {
				int pc = field.jump(p, d);
				int k = 1;
				while(pc >= 0) {
					if(state[pc] >= 3 && field.getValueAt(pc) == k && s[pc] < 2) {
						if(s[pc] == 0) {
							stack[v] = pc;
							++v;
						}
						s[pc] = 1;
						++dv;
					}
					++k;
					pc = field.jump(pc, d);
				}
			}
			if(dv > 1) state[p] = 4;
			++c;
		}
	}
	
}
