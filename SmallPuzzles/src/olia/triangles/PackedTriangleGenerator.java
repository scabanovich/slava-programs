package olia.triangles;

import com.slava.common.RectangularField;

public class PackedTriangleGenerator {
	RectangularField field;
	
	PackedTrianglesSolver solver1 = new PackedTrianglesSolver();
	PackedTrianglesSolver solver2 = new PackedTrianglesSolver();
	
	public PackedTriangleGenerator() {
		solver1.setSolutionLimit(1);
		solver1.setRandomized(true);
		solver1.setTreeSizeLimit(1000);
	}

	public void setField(RectangularField field) {
		this.field = field;
		solver1.setField(field);
		int total = field.getSize() / 3;
		solver1.setData(total, null, null);
		solver2.setField(field);
	}

	public void generate() {
		int attempts = 0;
		while(true) {
			attempts++;
			solver1.solve();
			System.out.println(solver1.getTreeSize());
			if(solver1.getSolutionCount() != 1) continue;
			int[] solution = solver1.getSolution();
			int[][] hData = solver1.getCreatedHData();
			int[][] vData = solver1.getCreatedVData();
			solver2.setData(0, hData, vData);
			solver2.check = false;
			solver2.solve();
			int sc = solver2.getSolutionCount();
			int ts = solver2.getTreeSize();
			if(sc == 1) {
				System.out.println("Attempts " + attempts + " tree " + ts);
				printProblemWithSolution(solution, hData, vData);
				return;
			}
		}
	}
	
	void printProblemWithSolution(int[] solution, int[][] hData, int[][] vData) {
		for (int iy = 0; iy < field.getHeight(); iy++) {
			for (int ix = 0; ix < field.getWidth(); ix++) {
				int p = field.getIndex(ix, iy);
				char c = (solution[p] < 0) ? '.' : (char)(97 + (solution[p] % 26));
				System.out.print(" " + c);
				
			}
			if(iy < hData[0].length) {
				System.out.println("  " + hData[0][iy] + " " + hData[1][iy]);
			}
		}
		System.out.println("");
		System.out.print(" ");
		for (int ix = 0; ix < vData[1].length; ix++) {
			System.out.print(" " + vData[0][ix]);
		}
		System.out.println("");
		System.out.print(" ");
		for (int ix = 0; ix < vData[1].length; ix++) {
			System.out.print(" " + vData[1][ix]);
		}
		System.out.println("");
	}

}
