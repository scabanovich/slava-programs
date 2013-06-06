package smallpuzzles.jumpsbyarrows;

import com.slava.common.RectangularField;

public class JumpsByArrowsGenerator {
	RectangularField field;
	JumpsByArrowsSolver solver = new JumpsByArrowsSolver();
	
	public JumpsByArrowsGenerator() {}
	
	public void setField(RectangularField field) {
		this.field = field;
		solver.setField(field);
	}
	
	public void generate() {
		generateByRandomArrows();
	}
	
	public void generateByRandomArrows() {
		int atemptCount = 0;
		while(true) {
			atemptCount++;
			int[] arrows = getRandomArrows();
			solver.setArrows(arrows);
			solver.setTimesheet(null);
			solver.solve();
			if(solver.getSolutionCount() == 1) break;
			if(solver.getSolutionCount() > 0) {
				System.out.println(solver.getSolutionCount());
				int[] best = solver.getNarrowestPointData();
				if(best != null) {
					System.out.println("p=" + best[0] + " t=" + best[1] + " s=" + best[2]);
					if(best[2] == 1) {
						solver.setTimesheet(createTimeSheet(best[0], best[1]));
						solver.solve();
						if(solver.getSolutionCount() != 1) {
							System.out.println("error");
						} else {
							break;
						}
					}
				}
			}
			if(atemptCount % 100 == 0) {
				System.out.println("attempts=" + atemptCount);
			}
		}
		System.out.println("attempts=" + atemptCount);
		solver.printSolution(solver.getSolution());
	}
	
	int[] getRandomArrows() {
		int[] arrows = new int[field.getSize()];
		for (int p = 0; p < arrows.length; p++) {
			do {
				arrows[p] = (int)(Math.random() * 4);
			} while(!isArrowOk(p, arrows[p]));
		}
		return arrows;
	}
	
	boolean isArrowOk(int p, int d) {
		return field.jump(p, d) >= 0;		
	}
	
	int[] createTimeSheet(int p, int t) {
		int[] ts = new int[field.getSize()];
		for (int q = 0; q < ts.length; q++) ts[q] = -1;
		ts[p] = t;
		return ts;
	}

}
