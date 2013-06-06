package com.slava.circuit;

public class Equation {
	Variable[] variables = new Variable[0];
	int[] coeffitients = new int[0];
	int constant = 0;
	
	public Equation() {		
	}
	
	public boolean isEmpty() {
		return variables.length == 0 && constant == 0;
	}
	
	public void addMember(Variable variable, int coefficient) {
		variables = append(variables, variable);
		coeffitients = append(coeffitients, coefficient);
	}
	
	public void addToConstant(int c) {
		constant += c;
	}
	
	private int[] append(int[] a, int v) {
		int[] b = new int[a.length + 1];
		System.arraycopy(a, 0, b, 0, a.length);
		b[a.length] = v;
		return b;
	}
	
	private Variable[] append(Variable[] a, Variable v) {
		Variable[] b = new Variable[a.length + 1];
		System.arraycopy(a, 0, b, 0, a.length);
		b[a.length] = v;
		return b;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < variables.length; i++) {
			sb.append(" [" + coeffitients[i] + "*" + variables[i] + "]");
		}
		sb.append(" " + constant);
		return sb.toString();
	}

}
