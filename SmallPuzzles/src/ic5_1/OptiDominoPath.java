package ic5_1;

public class OptiDominoPath {
	int width;
	int height;
	int size;
	int[] x,y;
	int[][] jp;

	int[] form;
	int[] state;
	
	int[] path;
	int[] pathDigits;
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		size = width * height;
		x = new int[size];
		y = new int[size];
		for (int i = 0; i < size; i++) {
			x[i] = (i % width);
			y[i] = (i / width);
		}
		jp = new int[4][size];
		for (int i = 0; i < size; i++) {
			jp[0][i] = (x[i] == width - 1) ? -1 : i + 1;
			jp[1][i] = (y[i] == height - 1) ? -1 : i + width;
			jp[2][i] = (x[i] == 0) ? -1 : i - 1;
			jp[3][i] = (y[i] == 0) ? -1 : i - width;
		}
	}
	
	public void setForm(int[] form) {
		this.form = form;
	}
	
	public void setState(int[] state) {
		this.state = state;
	}
	
	public void computePath(int odd) {
		int length = 0;
		for (int i = 0; i < size; i++) {
			if(state[i] >= 0) ++length;
		}
		path = new int[length];
		pathDigits = new int[length];
		int start = -1;
		for (int p = 0; p < size; p++) {
			if(state[p] >= 0 && ((x[p] + y[p]) % 2 == odd)) {
				start = p;
				break;
			}
		}
		path[0] = start;
		for (int i = 1; i < length; i++) {
			int p = path[i - 1];
			for (int d = 0; d < 4; d++) {
				int q = jp[d][p];
				if(q < 0 || state[q] < 0) continue;
				if(i > 1 && q == path[i - 2]) continue;
				path[i] = q;
				break;
			}
		}
		for (int i = 0; i < length; i++) {
			int p = path[i];
			int s = -1;
			while(p >= 0 && state[p] >= 0) {
				s++;
				p = jp[0][p];
			}
			pathDigits[i] = s;
		}
	}
	
	public int[] getPathDigits() {
		return pathDigits;
	}
	
	public void printValues(int[] values) {
		int[] state2 = (int[])state.clone();
		for (int i = 0; i < values.length; i++) {
			state2[path[i]] = values[i];
		}
		for (int i = 0; i < size; i++) {
			System.out.print(" ");
			if(state2[i] == N) {
				System.out.print(" ");
			} else if(state2[i] == E) {
				System.out.print("-");
			} else {
				System.out.print(state2[i]);
			}
			if(x[i] == width - 1) System.out.println("");			
		}
		for (int i = 0; i < size; i++) {
			if(state2[i] == N) {
			} else if(state2[i] == E) {
				System.out.print("-");
			} else {
				System.out.print(state2[i]);
			}
			if(x[i] == width - 1) System.out.print(",");			
		}
		System.out.println("");
	}
	
	static int N = -2;
	static int E = -1;
	
	static int[] STATE = new int[]{
		N,N,N,N,E,E,E,E,E,N,N,N,N,
		N,N,N,E,E,E,E,E,E,E,N,N,N,
		N,N,E,E,E,E,E,E,E,E,E,N,N,
		N,0,0,0,0,0,0,0,0,0,0,0,N,
		0,0,E,E,E,E,E,E,E,E,E,0,E,
		0,E,E,0,0,0,0,0,0,0,0,0,E,
		0,0,E,0,E,E,E,E,E,E,E,E,E,
		E,0,E,0,0,0,0,0,0,0,0,0,E,
		E,0,E,E,E,E,E,E,E,E,E,0,E,
		N,0,0,0,0,E,0,0,0,0,0,0,N,
		N,N,E,E,0,E,0,E,E,E,E,N,N,
		N,N,N,E,0,E,0,E,E,E,N,N,N,
		N,N,N,N,0,0,0,E,E,N,N,N,N,
	};

	static int[] STATE_2 = new int[]{
		N,N,N,N,E,E,E,E,E,N,N,N,N,
		N,N,N,E,E,E,E,E,E,E,N,N,N,
		N,N,E,E,E,E,E,E,E,E,E,N,N,
		N,0,0,0,0,0,0,0,0,0,0,0,N,
		E,0,E,E,E,E,E,E,E,E,E,0,0,
		E,0,0,0,0,0,0,0,0,0,E,E,0,
		E,E,E,E,E,E,E,E,E,0,E,E,0,
		E,0,0,0,0,0,0,0,0,0,E,E,0,
		E,0,E,E,E,E,E,E,E,E,E,0,0,
		N,0,0,0,0,E,0,0,0,0,0,0,N,
		N,N,E,E,0,E,0,E,E,E,E,N,N,
		N,N,N,E,0,E,0,E,E,E,N,N,N,
		N,N,N,N,0,0,0,E,E,N,N,N,N,
	};
	
	
	public static OptiDominoPath createDefault() {
		OptiDominoPath path = new OptiDominoPath();
		path.setSize(13, 13);
		path.setForm(STATE_2);
		path.setState(STATE_2);
		path.computePath(1);
		return path;
	}

}
