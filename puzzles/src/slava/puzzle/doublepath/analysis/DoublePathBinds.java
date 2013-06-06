package slava.puzzle.doublepath.analysis;

import java.util.*;
import slava.puzzle.doublepath.model.DoublePathField;

public class DoublePathBinds {
	DoublePathField field;
	int whiteCount;
	int blackCount;
	int[] points;

	int transitionCount;
	int[][] transitions;  // transitions[point_s][j] = point_e
	int[][] transitionIndex;
	int[] transitionPoint;
	int[] transitionDirection;
	int[][] intersections; // [point_1][j_1][point_2][j_2]
	
	public void setField(DoublePathField field) {
		this.field = field;
		makePoints();
		makeTransitions();
		makeIntersections();
		///printIntersections(9);
	}
	
	private void makePoints() {
		whiteCount = 0;
		blackCount = 0;
		for (int i = 0; i < field.getSize(); i++) {
			int c = field.getState(i);
			switch(c) {
				case 1: case 3: blackCount++; break;
				case 2: case 4: whiteCount++;
			}
		}
		points = new int[whiteCount + blackCount];
		int h = 0;
		for (int i = 0; i < field.getSize(); i++) {
			if(field.getState(i) == 0) continue;
			points[h] = i;
			++h;				
		}
	}

	private void makeTransitions() {
		transitions = new int[points.length][];
		transitionCount = 0;
		for (int i = 0; i < points.length; i++) {
			ArrayList list = new ArrayList();
			for (int j = 0; j < points.length; j++) {
				if(i == j) continue;
				int pi = field.getState(points[i]);
				int pj = field.getState(points[j]);
				if(pi == 0 || pj == 0 || ((pi % 2) != (pj % 2))) continue;
				if(field.areSeparatedByPoint(points[i], points[j])) {
					continue;
				} 
				list.add(new Integer(j));
			}			
			Integer[] is = (Integer[])list.toArray(new Integer[0]);
			transitionCount += is.length;
			transitions[i] = new int[is.length];
			for (int j = 0; j < is.length; j++) transitions[i][j] = is[j].intValue();
		}
		transitionIndex = new int[points.length][];
		transitionPoint = new int[transitionCount];
		transitionDirection = new int[transitionCount];
		int c = 0;
		for (int i = 0; i < points.length; i++) {
			transitionIndex[i] = new int[transitions[i].length];
			for (int j = 0; j < transitions[i].length; j++) {
				transitionIndex[i][j] = c;
				transitionPoint[c] = i;
				transitionDirection[c] = j;
				++c;
			}
		}
	}
	
	private void makeIntersections() {
		intersections = new int[transitionCount][transitionCount];
		for (int i = 0; i < transitionCount; i++) {
			int i_s = getTransitionStart(i);
			int i_e = getTransitionEnd(i);
			for (int j = 0; j < transitionCount; j++) {
				if(i == j) {
					intersections[i][j] = 1;
					continue;
				}
				int j_s = getTransitionStart(j);
				int j_e = getTransitionEnd(j);
				if(i_s == j_e && i_e == j_s) {
					intersections[i][j] = 1;
					continue;
				}
				if((isEndPoint(i_s) && (i_s == j_s || i_s == j_e))
				 ||(isEndPoint(i_e) && (i_e == j_s || i_e == j_e))
				) {
					intersections[i][j] = 1;
					continue;
				}
				if(intersect(i_s, i_e, j_s, j_e)) {
///					System.out.println("-->" + pointToString(i_s) + "-" + pointToString(i_e) + " & " + pointToString(j_s) + "-" + pointToString(j_e));
					intersections[i][j] = 1;
				} 
			}
		}
	}
	
	public boolean isEndPoint(int point) {
		return field.getState(points[point]) > 2;
	}
	
	public int getTransitionCount() {
		return transitionCount;
	}
	
	public int getTransitionStart(int transitionIndex) {
		return transitionPoint[transitionIndex];
	}
	
	public int getTransitionEnd(int transitionIndex) {
		int p = transitionPoint[transitionIndex];
		int d = transitionDirection[transitionIndex];
		return transitions[p][d];
	}
	
	public int[] getTransitionsForStart(int startPoint) {
		return transitions[startPoint];
	}
	
	private boolean intersect(int a, int b, int c, int d) {
		a = points[a];
		b = points[b];
		c = points[c];
		d = points[d];
		int c_d_a = (field.x(d) - field.x(c)) * (field.y(a) - field.y(c)) 
		          - (field.y(d) - field.y(c)) * (field.x(a) - field.x(c));
		int c_d_b = (field.x(d) - field.x(c)) * (field.y(b) - field.y(c)) 
				  - (field.y(d) - field.y(c)) * (field.x(b) - field.x(c));
		if(c_d_a * c_d_b >= 0) return false;
		int a_b_c = (field.x(b) - field.x(a)) * (field.y(c) - field.y(a)) 
				  - (field.y(b) - field.y(a)) * (field.x(c) - field.x(a));
		int a_b_d = (field.x(b) - field.x(a)) * (field.y(d) - field.y(a)) 
				  - (field.y(b) - field.y(a)) * (field.x(d) - field.x(a));
		if(a_b_c * a_b_d >= 0) return false;
		return true;
	}

	private void printTransitions() {
		for (int i = 0; i < transitionCount; i++) {
			System.out.println("  " + transitionToString(i));
		}
	}
	
	String transitionToString(int i) {
		int ps = transitionPoint[i];
		int d = transitionDirection[i];
		int pe = transitions[ps][d];
		return pointToString(ps) + "-" + pointToString(pe);
	}
	
	String pointToString(int p) {
		int p_i = points[p];
		int x = field.x(p_i), y = field.y(p_i);
		return ("" + x + ":" + y);
	}
	
	private void printIntersections(int i) {
		System.out.println("  " + transitionToString(i));
		for (int j = 0; j < transitionCount; j++) {
			if(intersections[i][j] == 1) {
				System.out.println("    " + transitionToString(j));
			}
		}
	}
}
