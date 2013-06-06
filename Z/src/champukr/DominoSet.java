package champukr;

public class DominoSet {
	int dimension;
	int size;
	int[][] dominoIndex;
	int[] lessNumber;
	int[] largerNumber;
	
	
	public void setSize(int dimension) {
		this.dimension = dimension;
		size = (dimension * (dimension + 1)) / 2;
		dominoIndex = new int[dimension][dimension];
		lessNumber = new int[size];
		largerNumber = new int[size];
		int c = 0;
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j <= i; j++) {
				dominoIndex[i][j] = c;
				dominoIndex[j][i] = c;
				largerNumber[c] = i;
				lessNumber[c] = j;
				++c;
			}
		}
	}
	
	


}
