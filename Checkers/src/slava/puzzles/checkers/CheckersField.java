package slava.puzzles.checkers;

public class CheckersField {
	byte size = 32;
	byte[][] directions = {{-1,1}, {1,1}, {1,-1}, {-1,-1}};
	
	byte y[] = {0,0,0,0, 1,1,1,1, 2,2,2,2, 3,3,3,3, 4,4,4,4, 5,5,5,5, 6,6,6,6, 7,7,7,7};
	byte x[] = {0,2,4,6, 1,3,5,7, 0,2,4,6, 1,3,5,7, 0,2,4,6, 1,3,5,7, 0,2,4,6, 1,3,5,7};
	
	byte[][] index = new byte[32][32];

	public CheckersField() {
		for (int xi = 0; xi < 8; xi++) {
			for (int yi = 0; yi < 8; yi++) {
				index[xi][yi] = -1;
			}
			
		}
		for (byte i = 0; i < size; i++) {
			index[x[i]][y[i]] = i;
		}
	}

	public int getSize() {
		return size;
	}

	public byte jump(int p, int d) {
		int x1 = x[p] + directions[d][0];
		int y1 = y[p] + directions[d][1];
		if(x1 < 0 || x1 >= 8 || y1 < 0 || y1 >= 8) return -1;
		return index[x1][y1];
	}
}
