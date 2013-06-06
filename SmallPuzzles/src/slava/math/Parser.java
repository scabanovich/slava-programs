package slava.math;

import java.util.*;

public class Parser {
	Map letterIndices = new HashMap();
	char[] characters = new char[20];
	
	public Expression parse(String expression) {
		return parse(expression, 0);
	}
	
	protected Expression parse(String expression, int off) {
		int eq = expression.indexOf('=');
		if(eq >= 0) {
			Expression left = parse(expression.substring(0, eq), off);
			Expression right = parse(expression.substring(eq + 1), off + eq + 1);
			Equality result = new Equality();
			result.setLeftExpression(left);
			result.setRightExpression(right);
			return result;
		}
		if(expression.startsWith(" ")) return parse(expression.substring(1, expression.length()), off + 1);
		if(expression.endsWith(" ")) return parse(expression.substring(0, expression.length() - 1), off);
		int position = getLastOperandPosition(expression, off);
		if(position >= 0) {
			BinaryExpression b = new BinaryExpression();
			Expression left = parse(expression.substring(0, position), off);
			Expression right = parse(expression.substring(position + 1), off + position + 1);
			b.setLeftExpression(left);
			b.setRightExpression(right);
			char c = expression.charAt(position);
			int operation = (c == '+') ? BinaryExpression.ADDING :
				(c == '-') ? BinaryExpression.SUBTRACTION :
				(c == '*') ? BinaryExpression.PRODUCT :
				(c == '/') ? BinaryExpression.DIVISION :
				BinaryExpression.ADDING;
				b.setOperation(operation);
			return b;
		}
		if(expression.startsWith("(") && expression.endsWith(")")) {
			return parse(expression.substring(1, expression.length() - 1), off + 1);
		}
		if(isNumber(expression)) {
			return new Number(getDigits(expression));
		}
		System.out.println("!" + expression + "!");
		throw new RuntimeException("Cannot parse at " + off);
	}
	
	int getLastOperandPosition(String expression, int off) {
		int result = -1;
		int position = -1;
		int pc = 0;
		for (int i = expression.length() - 1; i >= 0; i--) {
			char c = expression.charAt(i);
			if(c == ')') {
				++pc;
			} else if(c == '(') {
				--pc;
				if(pc < 0) {
					System.out.println("!" + expression + "!");
					throw new RuntimeException("Unexpected ) at " + (off + i));
				} 
			}
			if(pc > 0) continue;
			if(c == '+') {
				if(result < 0 || result >= BinaryExpression.PRODUCT) {
					result = BinaryExpression.ADDING;
					position = i;
				} 
			} else if(c == '-') {
				if(result < 0 || result >= BinaryExpression.PRODUCT) {
					result = BinaryExpression.SUBTRACTION;
					position = i;
				} 
			} else if(c == '*') {
				if(result < 0) {
					result = BinaryExpression.PRODUCT;
					position = i;
				} 
			} else if(c == '/') {
				if(result < 0) { 
					result = BinaryExpression.DIVISION;
					position = i;
				}
			}
		}
		return position;  
	}
	
	boolean isNumber(String expression) {
		for (int i = 0; i < expression.length(); i++) {
			char c = expression.charAt(i);
			if(!Character.isLetter(c)) return false;
		}
		return true;
	}
	
	int[] getDigits(String expression) {
		int[] ds = new int[expression.length()];
		for (int i = 0; i < expression.length(); i++) {
			char c = expression.substring(i, i + 1).toLowerCase().charAt(0);
			Character ch = new Character(c);
			Integer vi = (Integer)letterIndices.get(ch);
			if(vi == null) {
				int k = letterIndices.size();
				vi = new Integer(k);
				letterIndices.put(ch, vi);
				characters[k] = ch.charValue();
			}
			ds[i] = vi.intValue();
		}
		return ds;
	}

}
