package com.slava.sudoku;

public class TriangularSudokuRunner {
	
	static int[] PROBLEM = {
		      0,      0,
		  0,0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,  0,0,0,0,0,
		0,0,0,0,0,  0,0,0,0,0,
		  0,0,0,0,0,0,0,0,0,
		  0,0,0,0,0,0,0,0,0,
	          0,      0,
	};

	public static void solve() {
		SudokuRunner.solveVisual(new TriangularSudokuField(), PROBLEM);
	}

	public static void generate() {
		TriangularSudokuField field = new TriangularSudokuField();
		SudokuGenerator g = new SudokuGenerator();
//		g.setExpectedResult(extectedResult);
		g.setForceRemoveNumbersFrom(field.fakeIndices);
		g.setField(field);
		int[] problem = g.generateLogical();
		String problemString = field.printSolution(problem);
		System.out.println("Problem");
		System.out.println(problemString);
		SudokuRunner.solve(field, problem);
	}
	
	public static void generateMany() {
		while(true) {
			generate();
			System.out.println("Generate one more (y/n)? ");
			boolean exit = false;
			boolean more = false;
			while(!exit && !more) {
				try {
					int c = System.in.read();
					if(c == (int)'y') more = true;
					if(c == (int)'n') exit = true;
				} catch (Exception e) {
				}
			}
			if(exit) break;
		}
	}

	public static void main(String[] args) {
		generateMany();
		//solve();
	}


}
