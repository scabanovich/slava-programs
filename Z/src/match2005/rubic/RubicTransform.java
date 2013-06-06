package match2005.rubic;

import java.util.ArrayList;

public class RubicTransform {
	//rotation
	static int[] Z = {
		 2, 4, 7, 1, 6, 0, 3, 5,
		40,41,42,43,44,45,46,47,
		 8, 9,10,11,12,13,14,15,
		29,27,24,30,25,31,28,26,
		23,22,21,20,19,18,17,16,
		39,38,37,36,35,34,33,32
	};
	//rotation
	static int[] X = {
		18,20,23,17,22,16,19,21,
		10,12,15, 9,14, 8,11,13,
		26,28,31,25,30,24,27,29,
		42,44,47,41,46,40,43,45,
		37,35,32,38,33,39,36,34,
		 2, 4, 7, 1, 6, 0, 3, 5
	};
	//reflection
	static int[] Y = {
		 2, 1, 0, 4, 3, 7, 6, 5,
		10, 9, 8,12,11,15,14,13,
		42,41,40,44,43,47,46,45,
		26,25,24,28,27,31,30,29,
		34,33,32,36,35,39,38,37,
		18,17,16,20,19,23,22,21
	};
	
	public static RubicTransform[] TRANSFORMS = new RubicTransform[]{
		new RubicTransform(Z), 
		new RubicTransform(X), 
		new RubicTransform(Y)
	};
	
	int[] direct;
	int[] back;
	
	public RubicTransform(int[] direct) {
		this.direct = direct;
		back = new int[direct.length];
		for (int i = 0; i < direct.length; i++) {
			back[direct[i]] = i;
		}
	}
	
	public int[] transform(int[] squares) {
		int[] result = new int[squares.length];
		for (int i = 0; i < direct.length; i++) {
			result[i] = back[squares[direct[i]]];
		}
		return result;
	}
	
	public static int addToList(RubicState state, ArrayList list) {
		int[] squares = state.getSquares();
		if(contains(list, squares)) return 0;
		int n = 1;
		int c = list.size();
		list.add(squares);
		while(c < list.size()) {
			squares = (int[])list.get(c);
			for (int i = 0; i < TRANSFORMS.length; i++) {
				int[] sq = TRANSFORMS[i].transform(squares);
				if(contains(list, sq)) continue;
				list.add(sq);
				++n;
			}
			++c;
		}
		return n;
	}
	
	public static boolean contains(ArrayList list, int[] squares) {
		for (int i = 0; i < list.size(); i++) {
			if(equals((int[])list.get(i), squares)) return true;
		}
		return false;
	}
	
	public static boolean equals(int[] s1, int[] s2) {
		for (int i = 0; i < s1.length; i++) if(s1[i] != s2[i]) return false;
		return true;
	}

}
