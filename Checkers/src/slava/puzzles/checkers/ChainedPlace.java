package slava.puzzles.checkers;

public class ChainedPlace {
	byte p;
	ChainedPlace prev;
	ChainedPlace next;

	public ChainedPlace(byte p) {
		this.p = p;
	}

	public ChainedPlace addNext(byte q) {
		next = new ChainedPlace(q);
		next.prev = this;
		return next;
	}

	public void dropNext() {
		this.next = null;
	}

	public byte[] toArray() {
		int size = 0;
		ChainedPlace start = this;
		while(start.prev != null) {
			start = start.prev;
		}
		ChainedPlace n = start;
		while(n != null) {
			size++;
			n = n.next;
		}
		byte[] result = new byte[size];
		size = 0;
		n = start;
		while(n != null) {
			result[size] = n.p;
			size++;
			n = n.next;
		}
		return result;
	}

	public boolean contains(int p) {
		ChainedPlace n = this;
		while(n.prev != null) {
			n = n.prev;
		}
		while(n != null) {
			if(n.p == p) {
				return true;
			}
			n = n.next;
		}
		return false;
	}
}
