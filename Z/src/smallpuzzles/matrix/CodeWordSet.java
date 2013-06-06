package smallpuzzles.matrix;

public class CodeWordSet {
	int size;
	int wSize;
	
	int[] state;
	int unresolved;

	public CodeWordSet(int size) {
		this.size = size;
		wSize = 1;
		for (int i = 0; i < size; i++) wSize *= 2;
		state = new int[wSize];
		for (int i = 0; i < wSize; i++) state[i] = -1;
		unresolved = wSize;
	}
	
	public void solve() {
		int iteration = 0;
		while(unresolved > 0) {
			iteration++;
			int w = getBestChoice();
			register(w);
			System.out.println(iteration + " " + w + "   " + unresolved);
		}
	}
	
	int getRandowWord() {
		return (int)(wSize * Math.random());
	}
	
	int getBestChoice() {
		int bss = -1;
		int bw = -1;
		int m = 0;
		while(m < 1000 || bw < 1) {
			m++;
			int w = getRandowWord();
//			while(state[w] >= 0) w = getRandowWord();
			int ss = getSetSize(w);
			if(ss > bss) {
				m = 0;
				bss = ss;
				bw = w;
			}
		}
		return bw;
	}
	
	public int getSetSize(int w) {
		int q = 0;
		if(state[w] < 0) q++;
		int[] a = toArray(w);
		for (int i1 = 0; i1 < size; i1++) {
			a[i1] = 1 - a[i1];
			int w1 = toWord(a);
			if(state[w1] < 0) q++;
			for (int i2 = i1 + 1; i2 < size; i2++) {
				a[i2] = 1 - a[i2];
				int w2 = toWord(a);
				if(state[w2] < 0) q++;
				for (int i3 = i2 + 1; i3 < size; i3++) {
					a[i3] = 1 - a[i3];
					int w3 = toWord(a);
					if(state[w3] < 0) q++;

					a[i3] = 1 - a[i3];
				}
				a[i2] = 1 - a[i2];
			}
			a[i1] = 1 - a[i1];
		}
		return q;
	}
	
	public void register(int w) {
		register(w, 0);
		int[] a = toArray(w);
		for (int i1 = 0; i1 < size; i1++) {
			a[i1] = 1 - a[i1];
			int w1 = toWord(a);
			register(w1, 1);
			for (int i2 = i1 + 1; i2 < size; i2++) {
				a[i2] = 1 - a[i2];
				int w2 = toWord(a);
				register(w2, 2);
				for (int i3 = i2 + 1; i3 < size; i3++) {
					a[i3] = 1 - a[i3];
					int w3 = toWord(a);
					register(w3, 3);
					a[i3] = 1 - a[i3];
				}
				a[i2] = 1 - a[i2];
			}
			a[i1] = 1 - a[i1];
		}
	}
	
	void register(int w, int d) {
		if(state[w] == -1) {
			state[w] = d;
			unresolved--;
		} else if(state[w] > d) {
			state[w] = d;
		}
	}
	
	public int getDistance(int w1, int w2) {
		int[] a1 = toArray(w1), a2 = toArray(w2);
		int c = 0;
		for (int i = 0; i < size; i++) if(a1[i] != a2[i]) c++;
		return c;
	}
	
	public int toWord(int[] a) {
		int c = 0;
		for (int i = a.length - 1; i >= 0; i--) c = c * 2 + a[i];
		return c;
	}
	
	public int[] toArray(int w) {
		int[] a = new int[size];
		int i = 0;
		while(w > 0) {
			a[i] = w % 2;
			w = w / 2;
			i++;
		}
		return a;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CodeWordSet p = new CodeWordSet(20);
		p.solve();
	}

}
