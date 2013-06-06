package com.slava.domino;

public class DamkaState {
	static int CODE_MODULE = 100000001;
	static byte EMPTY = (byte)255;
	byte[] state; // -1 empty, 0 white, 1 black, 2 white Damka, 3 - black Damka
	byte turn;    // 0 white, 1 black
	int code;
	byte damkaCount;	
	
	DamkaMove move;
	
	public DamkaState() {
		state = new byte[32];
		for (int i = 0; i < state.length; i++) state[i] = EMPTY;
		for (int i = 0; i < 12; i++) {
			state[i] = 0;
			state[state.length - 1 - i] = 1;
		}
		turn = 0;
	}
	
	public DamkaState(DamkaState parent, byte begin, byte end, byte value) {
///		this.parent = parent;
		if(parent != null) {
			turn = (byte)(1 - parent.turn);
			state = new byte[parent.state.length];
			for (int i = 0; i < state.length; i++) {
				state[i] = parent.state[i];
			}
			state[end] = value;
			state[begin] = -1;
		}
		move = new DamkaMove(parent.move, begin, end);
		generateCode();
	}
	
	public int getCode() {
		return code;
	}
	
	public int hashcode() {
		return getCode();
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof DamkaState)) return false;
		DamkaState other = (DamkaState)o;
		return getCode() == other.getCode();
	}
	
	private void generateCode() {
		long c = 0;
		for (int i = 0; i < state.length; i++) {
			c = c * 5 + state[i];
		}
		c = c * 2 + turn;
		code = (int)(c % CODE_MODULE);
		if(code < 0) code += CODE_MODULE;
		int d = 0;
		for (int i = 0; i < state.length; i++) {
			if(state[i] >= 2) d++;
		}
		damkaCount = (byte)d;
	}
	
	byte getDamkaCount() {
		return damkaCount;
	}

}
