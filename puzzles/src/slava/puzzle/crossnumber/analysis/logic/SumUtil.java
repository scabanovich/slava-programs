package slava.puzzle.crossnumber.analysis.logic;

import java.util.ArrayList;

public class SumUtil {
	public static SumUtil instance = new SumUtil();
	
	//[digit count][sum][set index][number index]
	public int[][][][] sums = new int[10][46][][];
	
	//[digit count][sum][number index]; 
	// equals 0 - if number cannot be used for the sum, 1 - if it can be used
	public int[][][] filters = new int[10][46][10];
	
	public SumUtil() {
		initSums();
		initFilters();
	}
	
	private void initSums() {
		int[] used = new int[10];
		int[] sum = new int[11];
		int[] values = new int[11];
		int length = 0;
		ArrayList[][] sumArrays = new ArrayList[10][46];

		boolean finished = false;
		while(true) {			
			while(values[length] == 9) {
				if(length == 0) {
					finished = true;
					break;
				}
				length--;
				used[values[length]]--;
			}
			if(finished) break;
			do {
				values[length]++;
				sum[length + 1] = sum[length] + values[length];
			} while(values[length] < 9 && used[values[length]] > 0);
			if(used[values[length]] > 0) continue;
			used[values[length]] = 1;
			length++;

			int[] set = new int[length];
			System.arraycopy(values, 0, set, 0, length);
//			System.out.print(sum[length] + ":");
//			for (int i = 0; i < set.length; i++) {
//				System.out.print(" " + set[i]);
//			}
//			System.out.println("");
			if(sumArrays[length][sum[length]] == null) {
				sumArrays[length][sum[length]] = new ArrayList();
			}
			sumArrays[length][sum[length]].add(set);
			
			values[length] = length > 9 ? 9 : values[length - 1];
		}
		for (int i = 0; i < sums.length; i++) {
			for (int j = 0; j < sums[0].length; j++) {
				if(sumArrays[i][j] == null) continue;
				sums[i][j] = (int[][])sumArrays[i][j].toArray(new int[0][]);
//				System.out.println("Length=" + i + " sum=" + j);
//				for (int index = 0; index < sums[i][j].length; index++) {
//					System.out.print("   ");
//					for (int k = 0; k < sums[i][j][index].length; k++) System.out.print(" " + sums[i][j][index][k]);
//					System.out.println("");
//				}
			}
		}
		
	}
	
	private void initFilters() {
		for (int i = 0; i < sums.length; i++) {
			for (int j = 0; j < sums[0].length; j++) {
				if(sums[i][j] == null) continue;
				for (int index = 0; index < sums[i][j].length; index++) {
					for (int k = 0; k < sums[i][j][index].length; k++) {
						filters[i][j][sums[i][j][index][k]] = 1;
					}
				}
			}
		}
//		for (int i = 0; i < filters.length; i++) {
//			for (int j = 0; j < filters[0].length; j++) {
//				if(sums[i][j] == null) continue;
//				System.out.println("Length=" + i + " sum=" + j);
//				for (int k = 0; k < 10; k++) if(filters[i][j][k] == 1) System.out.print(" " + k);
//				System.out.println("");
//			}
//		}
		
	}
	
	public static void main(String[] args) {
		SumUtil u = SumUtil.instance;
	}

}
