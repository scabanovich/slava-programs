package slava.puzzle.sudoku.model;

public class SudokuProblemInfo {
	int[] regions;
	int[] numbers;
	int[] solution;
	
	int[][] differenceOneData;
	int[][] lessOrGreaterData; // -1 : less, 0 : no data, 1 : greater
	int[][] xvData;            // 0 : no data, 1 : V, 2 : X

	int[]   partitioning;      // for sums
	int[]   sums;              // for partitioning
	
	public static int NO_DIAGONALS = 0;
	public static int MAIN_DIAGONALS = 1;
	public static int ALL_DIAGONALS = 2;
	int addDiagonals = NO_DIAGONALS;
	boolean generateByTemplate = true;
	boolean notTouchingDiagonally = false;
	int neighboursDifferNotMoreThan = 0;
	boolean usingDifferenceOneRestriction = false;
	int differenceValue = 1;
	boolean usingLessOrGreaterRestriction = false;
	boolean usingXVRestriction = false;
	boolean usingPartitioningSumRestriction = false;
	
	public SudokuProblemInfo() {}
	
	public void setSize(int size) {
		regions = new int[size];
		numbers = new int[size];
		differenceOneData = new int[size][4];
		lessOrGreaterData = new int[size][4];
		xvData = new int[size][4];

		partitioning = new int[size];
		for (int i = 0; i < size; i++) partitioning[i] = -1;
		sums = new int[0];

		if(size == 81) {
			int a = 10, b = 11, c = 12, d = 13, e = 14, f = 15, g = 16, h = 17,
			i = 18, j = 19, k = 20, l = 21, m = 22, n = 23, o = 24, p = 25, q = 26,
			r = 27, s = 28, t = 29, u = 30; 
			partitioning = new int[] {
				0,0,1,4,5,5,7,7,8,
				2,2,1,4,4,6,6,9,8,
				2,3,3,3,d,d,9,9,a,
				b,b,c,c,g,d,e,e,a,
				i,b,b,m,g,h,h,e,f,
				i,l,m,m,n,h,h,r,f,
				i,l,k,o,n,r,r,r,s,
				j,k,k,o,q,q,t,s,s,
				j,j,p,p,q,t,t,u,u,
			};
			sums = new int[31];
		}

		solution = null;
	}
	
	public int[] getRegions() {
		return regions;
	}
	
	public int getRegionAt(int i) {
		return regions[i];
	}
	
	public int[] getNumbers() {
		return numbers;
	}
	
	public int[] getSolution() {
		return solution;
	}
	
	public void setSolution(int[] s) {
		solution = s;
	}

	public int getNextFreeIndex() {
		int[] regions = getRegions();
		int[] is = new int[regions.length];
		for (int i = 0; i < regions.length; i++) {
			is[regions[i]]++;
		}
		for (int i = 0; i < is.length; i++) if(is[i] == 0) return i;
		return 0;
	}
	
	public void setNumberAt(int p, int n) {
		numbers[p] = n;
	}
	
	public int getDiagonalsOption() {
		return addDiagonals;
	}

	public void setDiagonalsOption(int b) {
		addDiagonals = b;
	}
	
	public boolean isGenerateByTemplate() {
		return generateByTemplate;
	}
	
	public void setGenerateByTemplate(boolean b) {
		generateByTemplate = b;
	}
	
	public boolean isNotTouchingDiagonally() {
		return notTouchingDiagonally;
	}
	
	public void setNotTouchingDiagonally(boolean b) {
		notTouchingDiagonally = b;
	}

	/**
	 * If 0 - then discarded.
	 * @return
	 */
	public int getNeighboursDifferNotMoreThan() {
		return neighboursDifferNotMoreThan;
	}
	
	public void setNeighboursDifferNotMoreThan(int k) {
		neighboursDifferNotMoreThan = k;;
	}
	
	public boolean isUsingDifferenceOneRestriction() {
		return usingDifferenceOneRestriction;
	}
	
	public void setUsingDifferenceOneRestriction(boolean b) {
		usingDifferenceOneRestriction = b;
		if(!b && differenceOneData != null) differenceOneData = new int[differenceOneData.length][4];
	}

	public int getDifferenceValue() {
		return differenceValue;
	}
	
	public void setDifferenceValue(int c) {
		differenceValue = c;
	}
	
	public int[][] getDifferenceOneData() {
		return differenceOneData;
	}
	
	public boolean isUsingLessOrGreaterRestriction() {
		return usingLessOrGreaterRestriction;
	}
	
	public void setUsingLessOrGreaterRestriction(boolean b) {
		usingLessOrGreaterRestriction = b;
	}

	public int[][] getLessOrGreaterData() {
		return lessOrGreaterData;
	}
	
	public boolean isUsingXVRestriction() {
		return usingXVRestriction;
	}
	
	public void setUsingXVRestriction(boolean b) {
		usingXVRestriction = b;
	}

	public int[][] getXVData() {
		return xvData;
	}

	public boolean isUsingPartitioningSumRestriction() {
		return usingPartitioningSumRestriction;
	}

	public void setUsingPartitioningSumRestriction(boolean b) {
		usingPartitioningSumRestriction = b;
	}

	public int[] getPartitioning() {
		return partitioning;
	}

	public int[] getSums() {
		return sums;
	}

	public void setSums(int[] s) {
		sums = s;
	}

	public int getNextFreePartitionIndex() {
		int[] ps = getPartitioning();
		int[] is = new int[ps.length];
		for (int i = 0; i < ps.length; i++) {
			is[ps[i]]++;
		}
		for (int i = 0; i < is.length; i++) if(is[i] == 0) return i;
		return 0;
	}

	public void changePartitioning(int p, int c) {
		if(partitioning[p] == c) return;
		partitioning[p] = c;
		requcePartitioning();
	}

	public void requcePartitioning() {
		int max = -1;
		for (int p = 0; p < partitioning.length; p++) {
			if(partitioning[p] > max) max = partitioning[p];
		}
		int[] usage = new int[max + 1];
		int count = 0;
		for (int p = 0; p < partitioning.length; p++) {
			int q = partitioning[p];
			if(q < 0) continue;
			usage[q]++;
			if(usage[q] == 1) count++;
		}
		if(count == max + 1 && sums.length == count) return;
		int[] nsums = new int[count];
		int[] transform = new int[usage.length];
		
		int j = 0;
		for (int i = 0; i < usage.length; i++) {
			transform[i] = -1;
			if(usage[i] > 0) {
				transform[i] = j;
				j++;
			}
		}
		for (int i = 0; i < transform.length && i < sums.length; i++) {
			if(transform[i] < 0) continue;
			nsums[transform[i]] = sums[i];
		}
		for (int p = 0; p < partitioning.length; p++) {
			int q = partitioning[p];
			if(q < 0) continue;
			partitioning[p] = transform[q];
		}
		sums = nsums;
	}

}
