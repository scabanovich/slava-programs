package champ2006;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

public class NumberArray {
	int[] state;
	NumberArray parent;
	int sums;
	
	public NumberArray() {}

	public NumberArray(int[] s, int sums) {
		Arrays.sort(s);
		setState(s);
		this.sums = sums;
	}

	public void setState(int[] s) {
		state = s;
	}
	
	public void setParent(NumberArray parent) {
		this.parent = parent;
	}
	
	public boolean equals(Object o) {
		NumberArray other = (NumberArray)o;
		if(state.length != other.state.length) return false;
		for (int i = 0; i < other.state.length; i++) {
			if(state[i] != other.state[i]) return false;
		}
		return true;
	}
	
	public String getCode() {
		ByteArrayOutputStream sb = new ByteArrayOutputStream();
		for (int i = 0; i < state.length; i++) sb.write(state[i]);
		return sb.toString();
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < state.length; i++) {
			if(i > 0) sb.append(",");sb.append(state[i]);
		}
		sb.append(';').append(sums);
		return sb.toString();
	}

}
