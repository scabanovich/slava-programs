package ik6_1;

public class Cards_6_1_Runner {

	public static void main(String[] args) {
		Cards_6_1_Field f = new Cards_6_1_Field();
		f.setCardPackSize(4, 9);
		Cards_6_1_Solver solver = new Cards_6_1_Solver();
		solver.setField(f);
		solver.solve();
	}

}

// 6H 9S 4D 8C 2D 5D 5S 9C 6S 2H 8H 8D 4S 7C 4H 9D 5C 2S 10C 3H 6C 3S 9H 5H 7D 10H 3C 10D 6D 10S 4C 8S 3D 7S 2C
//Code=7S,10D,3S,9D,2H,8C
