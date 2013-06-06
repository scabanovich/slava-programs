package slava.puzzle.crossnumber;

public class CrossNumberField {
	int width;
	int height;
	int size;
	int[] x;
	int[] y;
	int[][] xy;
	int[][] jp;
	
	int[] mask;
	int[] hsum;
	int[] vsum;
	int[] value;
	int[] status; // 0 - nothing, 1 - hsum, 2 - vsum, 3 - hsum & vsum, 4 - value;

	private boolean throughRow = false;

	public CrossNumberField() {
		setSize(11, 11);
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		size = width * height;
		x = new int[size];
		y = new int[size];
		xy = new int[width][height];
		jp = new int[4][size];
		for (int i = 0; i < size; i++) {
			x[i] = (i % width);
			y[i] = (i / width);
			xy[x[i]][y[i]] = i;
		}
		for (int i = 0; i < size; i++) {
			jp[0][i] = (x[i] == width - 1) ? -1 : i + 1;
			jp[1][i] = (y[i] == height - 1) ? -1 : i + width;
			jp[2][i] = (x[i] == 0) ? -1 : i - 1;
			jp[3][i] = (y[i] == 0) ? -1 : i - width;
		}
		mask = new int[size];
		hsum = new int[size];
		vsum = new int[size];
		for (int i = 0; i < size; i++) {
			mask[i] = (x[i] == 0 || y[i] == 0) ? 0 : 1;
			hsum[i] = -1;
			vsum[i] = -1;
		}
		status = new int[size];
		value = new int[size];
		updateStatus();
	}
	
	public int getIndex(int ix, int iy) {
		return xy[ix][iy];
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int size() {
		return size;
	}
	
	public int x(int place) {
		return x[place];
	}
	
	public int y(int place) {
		return y[place];
	}
	
	public int jump(int place, int d) {
		return jp[d][place];
	}
	
	public void setMask(int place, int q) {
		if(mask[place] == q) return;
		mask[place] = q;
		updateStatus();
	}

	public int getMask(int place) {
		return mask[place]; 
	}
	
	public int getStatus(int place) {
		return status[place];
	}
	
	public void setHSum(int place, String val) throws Exception {
		if((status[place] & 1) == 0) return;
		hsum[place] = getValidateSum(val, place, 0);
	}
	
	public void setVSum(int place, String val) throws Exception {
		if((status[place] & 2) == 0) return;
		vsum[place] = getValidateSum(val, place, 1);
	}
	
	int getValidateSum(String val, int place, int d) throws Exception {
		if(val.length() == 0) return -1;
		int v = 0;
		try {
			v = Integer.parseInt(val);
		} catch (Exception e) {
			throw new Exception("Is not integer: " + val);
		}
		return getValidSum(v, place, d);
	}
	
	public void setValue(int place, int v) {
		value[place] = v;
	}
	
	public int getHSum(int place) {
		return hsum[place];
	}
	
	public int getVSum(int place) {
		return vsum[place];
	}
	
	public int getValue(int place) {
		return value[place];
	}
	
	public void updateStatus() {
		for (int i = 0; i < size; i++) {
			if(mask[i] == 1) status[i] = 4; 
			else {			
				status[i] = 0;
				int q = jp[0][i];
				if(q >= 0 && mask[q] == 1) q = jp[0][q];
				if(q >= 0 && mask[q] == 1) status[i] = 1;
				q = jp[1][i];
				if(q >= 0 && mask[q] == 1) q = jp[1][q];
				if(q >= 0 && mask[q] == 1) status[i] += 2;
			}
		}
		revalidateSums();
	}
	
	void revalidateSums() {
		for (int i = 0; i < size; i++) {
			if((status[i] & 1) != 0) {
				hsum[i] = getValidSum(hsum[i], i, 0);
			}
			if((status[i] & 2) != 0) {
				vsum[i] = getValidSum(vsum[i], i, 1);
			}
		}
	}

	int getValidSum(int v, int place, int d) {
		if(v < 1) return v;
		int min = 0; 
		int max = 0;
		int q = jp[d][place];
		int i = 1;
		while(q >= 0 && mask[q] == 1) {
			min += i;
			max += 10 - i;
			++i;
			q = jp[d][q];
		}
		return (v < min) ? min : (v > max) ? max : v;
	}
	
	// for loader
	
	public void setMask(int[] m) {
		for (int i = 0; i < size; i++) mask[i] = m[i];
		updateStatus();
	}	
	public void setHSum(int[] h) {
		for (int i = 0; i < size; i++) hsum[i] = h[i];
	}	
	public void setVSum(int[] v) {
		for (int i = 0; i < size; i++) vsum[i] = v[i];
	}
	
	public int[] getMask() {
		return mask;
	}
	public int[] getHSum() {
		return hsum;
	}
	public int[] getVSum() {
		return vsum;
	}

	public boolean isThroughRow() {
		return throughRow;
	}
	
	public void setThroughRow(boolean b) {
		throughRow = b;
	}

}
