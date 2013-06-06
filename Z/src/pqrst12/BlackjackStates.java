package pqrst12;

public class BlackjackStates {
	static int[][] STATE_A = new int[][]{ //x,6
			{1,10},
			{3,8,10},
			{4,7,10},
			{5,6,10},
			{2},
			{9}
	};
	static int[][] STATE_A_1 = new int[][]{ //x,5
			{2,9,10},          //(6,5) Score=220
			{4,7,10},
			{3,8,10},
			{5,6,10},
			{1}
	};
	static int[][] STATE_B = new int[][]{ //x,4
			{1,10,10},
			{2,4,5,10},
			{6,7,8},
			{3,9,10}
	};
	static int[][] STATE_B_1 = new int[][]{ //x,6
			{1,10,10},         //(5,6) Score=260
			{2,4,5,10},        //(6,6) Score=243
			{6,7,8},
			{3},
			{9},
			{10},
	};
	static int[][] STATE_B_2 = new int[][]{ //x,4
			{1,10,10},         //(7,4) Score=259
			{3,4,5,10},        //(8,4) Score=256
			{6,7,8},
			{2,9,10}
	};
	static int[][] STATE_B_3 = new int[][]{ //x,7 
			{1,10,10},         //(4,7) Score=194
			{6,7,8},
			{2,9,10},
			{3},
			{4},
			{5},
			{10}
	};
	static int[][] STATE_B_4 = new int[][]{ //x,5
			{1,10},            //(6,5) Score=260
			{2,4,5,10},
			{6,7,8},
			{3,9,10},
			{10}
	};
	static int[][] STATE_B_4_1 = new int[][]{ //x,5
			{1,10},            //(6,5) 
			{5,6,9},
			{3,8,10},
			{4,7,10},
			{10}
	};
	static int[][] STATE_B_4_2 = new int[][]{ //x,6
			{1,10},              //(5,6) Score=255
			{2,4,5,10},          //(6,6) Score=248
			{6,7,8},
			{3},
			{9},
			{10,10}
	};
	static int[][] STATE_C = new int[][]{ //x,6
			{1,10,10},
			{2,3,6,10},            //(5,6) Score=235
			{4,9,8},               //(6,6) Score=248
			{5},
			{7},
			{10},
	};
	static int[][] STATE_C_1 = new int[][]{ //x,5
			{1,10,10},             //(6,5) Score=260
			{4,8,9},            
			{5,6,10},
			{3,7,10},
			{2}
	};
	static int[][] STATE_C_2 = new int[][]{ //x,6
			{1,10},
			{2,3,6,10},            //(5,6) Score=250
			{4,8,9},               //(6,6) Score=
			{5},
			{7},
			{10,10}
	};
	static int[][] STATE_D = new int[][]{ //x,5
			{1,5,6},             
			{2,9,10},
			{3,8,10},            
			{4,7,10},
			{10}
	};
	static int[][] STATE_E = new int[][]{ //x,5
			{1,4,6},         //(6,5) Score=260
			{5,7,9},         //(7,5) Score=255
			{3,8,10},
			{2,10,10},
			{10}
	};
	
	static Task[] TASKS = new Task[]{
		new Task(STATE_E, 6, 5),
		new Task(STATE_E, 7, 5),
		new Task(STATE_C_1, 6, 5),
	};

}

class Task {
	int[][] state;
	int width;
	int height;
	Task(int[][] state, int width, int height) {
		this.width = width;
		this.height = height;
		this.state = state;
	}
}
