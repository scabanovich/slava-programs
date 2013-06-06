package slava.puzzles.hexion.analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.slava.common.TwoDimField;

public class HexionGenerator {
	TwoDimField field;
	int[] form;
	
	int[] fixedValues;
	int holesRequiredCount;
	
	int cellsToFill;
	int holes;
	int[] state;
	
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	Map uniqueProblems;
	Set degenerateProblems;
	
	public HexionGenerator() {}
	
	public void setField(TwoDimField field) {
		this.field = field;
	}
	
	public void setData(int[] s) {
		form = new int[s.length];
		fixedValues = new int[s.length];
		for (int p = 0; p < s.length; p++) {
			if(s[p] == e) {
				form[p] = 0;
				fixedValues[p] = Z;
			} else {
				form[p] = 1;
				fixedValues[p] = s[p];
			}
		}
	}
	
	public void setForm(int[] s) {
		form = s;
	}
	
	public void setFixedValues(int[] s) {
		fixedValues = s;
	}
	
	public void setHolesRequiredCount(int s) {
		holesRequiredCount = s;
	}
	
	public void solve() {
		init();
		anal();
		onFinish();
	}
	
	void init() {
		cellsToFill = 0;
		for (int i = 0; i < form.length; i++) {
			if(form[i] == 1) cellsToFill++;
		}
		holes = 0;
		state = new int[field.getSize()];
		for (int p = 0; p < state.length; p++) state[p] = -1;

		place = new int[cellsToFill + 1];
		wayCount = new int[cellsToFill + 1];
		way = new int[cellsToFill + 1];
		ways = new int[cellsToFill + 1][2];
		
		t = 0;

		uniqueProblems = new HashMap();
		degenerateProblems = new HashSet();
	}
	
	void srch() {
		wayCount[t] = 0;
		if(cellsToFill == 0) return;
		if(holes + cellsToFill < holesRequiredCount) return;
		if(holes > holesRequiredCount) return;
		int p = getNextPlace();
		place[t] = p;
		if(p >= state.length) return;

		if(holesRequiredCount == holes || fixedValues[p] != Z) {
			ways[t][0] = 1;
			wayCount[t] = 1;
		} else if(holes + cellsToFill == holesRequiredCount) {
			ways[t][0] = 0;
			wayCount[t] = 1;
		} else {
			ways[t][0] = 0;
			ways[t][1] = 1;
			wayCount[t] = 2;
		}
	}
	
	int getNextPlace() {
		int p = (t == 0) ? 0 : place[t - 1] + 1;
		while(form[p] == 0) p++;
		return p;
	}
	
	void move() {
		int p = place[t];
		int v = ways[t][way[t]];
		state[p] = v;
		cellsToFill--;
		if(v == 0) holes++;		
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int p = place[t];
		int v = ways[t][way[t]];
		state[p] = -1;
		cellsToFill++;
		if(v == 0) holes--;		
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
			if(isSolution()) {
				onFillingFound();
			}			
		}
	}
	
	boolean isSolution() {
		return cellsToFill == 0 && holesRequiredCount == holes;
	}
	
	void onFillingFound() {
		String code = getProblemCode();
		if(code == null) return;
		if(degenerateProblems.contains(code)) return;
		if(uniqueProblems.containsKey(code)) {
			uniqueProblems.remove(code);
			degenerateProblems.add(code);
		} else {
			uniqueProblems.put(code, state.clone());
		}
	}
	
	void onFinish() {
		System.out.println("Unique problems: " + uniqueProblems.size());
		System.out.println("Degenerate problems: " + degenerateProblems.size());
		Iterator ps = uniqueProblems.keySet().iterator();
		while(ps.hasNext()) {
			String key = ps.next().toString();
			System.out.println(key);
			int[] s = (int[])uniqueProblems.get(key);
			printProblem(s);			
		}
	}
	
	String getProblemCode() {
		StringBuffer sb = new StringBuffer();
		int[] problem = getProblem();
		if(problem == null) return null;
		for (int i = 0; i < problem.length; i++) {
			if(i > 0) sb.append(' ');
			sb.append(problem[i]);
		}
		return sb.toString();
	}
	
	int[] getProblem() {
		int[] problem = new int[7];
		for (int p = 0; p < state.length; p++) {
			if(form[p] == 0) continue;
			if(state[p] == 0) {
				if(fixedValues[p] != Z) return null;
				continue;
			}
			int c = 0;
			for (int d = 0; d < 6; d++) {
				int q = field.jump(p, d);
				if(q >= 0 && form[q] == 1 && state[q] == 1) {
					c++;
				}
			}
			if(fixedValues[p] != Z && fixedValues[p] != c) return null;
			problem[c]++;
		}
		
		return problem;
	}
	
	void printProblem(int[] state) {
		for (int p = 0; p < state.length; p++) {
			if(field.getX(p) == 0) {
				int k = field.getHeight() - 1 - field.getY(p);
				for (int i = 0; i < k; i++) System.out.print(" ");
			}
			if(form[p] == 0) {
				System.out.print("  ");
			} else if(state[p] == 0) {
				System.out.print("o ");
			} else if(fixedValues[p] >= 0) {
				System.out.print("" + fixedValues[p] + " ");
			} else {
				System.out.print("x ");
			}
			if(field.isRightBorder(p)) System.out.println("");
		}
	}

	// preset running
	
	public static void runData5(int holes) {
		HexionGenerator g = new HexionGenerator();
		TwoDimField f = new TwoDimField();
		f.setOrts(TwoDimField.TRIANGULAR_ORTS);
		f.setSize(5,5);
		g.setField(f);
		g.setData(DATA_5);
		g.setHolesRequiredCount(holes);
		g.solve();
	}

	public static void runData7(int holes) {
		HexionGenerator g = new HexionGenerator();
		TwoDimField f = new TwoDimField();
		f.setOrts(TwoDimField.TRIANGULAR_ORTS);
		f.setSize(7,7);
		g.setField(f);
		g.setData(DATA_7);
		g.setHolesRequiredCount(holes);
		g.solve();
	}

	static int e = -2; //out of form
	static int Z = -1; // no fixed value set

	static int[] DATA_7 = {
		        Z,Z,Z,Z,     e,e,e,
		       Z,Z,Z,Z,Z,     e,e,
		      Z,Z,Z,Z,Z,1,     e,
		     Z,Z,Z,Z,Z,Z,Z,
	   e,     Z,Z,Z,Z,Z,Z,
      e,e,     Z,Z,Z,Z,Z,
	 e,e,e,     Z,Z,Z,Z,
	};

	static int[] DATA_5 = {
		     Z,Z,Z,     e,e,
		    Z,Z,Z,2,     e,
		   Z,Z,Z,Z,Z,     
	  e,    Z,4,Z,Z,
	  e,e,   Z,Z,Z,
	};
	
	public static void main(String[] args) {
		runData5(5);
	}

}
