package slava.puzzle.circuit;

public class Variable {
	boolean isValueSet = false;
	int value = -1;
	
	public Variable() {}
	
	boolean isValueSet() {
		return isValueSet;
	}
	
	public void setValue(int value) {
		this.value = value;
		isValueSet = true;
	}
	
	public void unset() {
		this.value = -1;
		isValueSet = false;
	}
	
	public int getValue() {
		return value;
	}
	
	public String toString() {
		return isValueSet ? "" + value : "unknown";
	}
}
