package may2011;

public class ThreeButtonState {
	int a;
	int b;
	int c;
	int t;
	
	ThreeButtonState next;

	public ThreeButtonState(int a, int b, int c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	ThreeButtonState back() {
		ThreeButtonState result = new ThreeButtonState(a, b, c);
		result.next = this;
		result.doBack();
		return result;
	}

	void doBack() {
		if(a == b + c) {
			a = Math.abs(b - c);
		} else if(b == a + c) {
			b = Math.abs(a - c);
		} else if(c == a + b) {
			c = Math.abs(a - b);
		} else {
			throw new RuntimeException("Invalid state " + this);
		}			
	}

	public boolean isNext(ThreeButtonState s) {
		if(a == s.a && b == s.b && c == s.a + s.b) return true;
		if(a == s.a && b == s.a + s.c && c == s.c) return true;
		if(a == s.b + s.c && b == s.b && c == s.c) return true;
		return false;
	}

	public boolean canBePredecessor(ThreeButtonState s) {
		return a <= s.a && b <= s.b && c <= s.c;
	}

	public String toString() {
		return "" + a + ":" + b + ":" + c;
	}

}
