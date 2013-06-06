package forsmarts.fitting2d;

import com.slava.common.RectangularField;

public class Packing2dRunner {

	public int run(Problem2D problem) {
		PackingAnalysis analysis = new PackingAnalysis();
		return run(problem, analysis);		
	}

	public int run(Problem2D problem, AbstractPackingAnalysis analysis) {
		RectangularField field = new RectangularField();
		field.setSize(problem.xSize, problem.ySize);
		Figures2D fgs = new Figures2D();
		fgs.setFieldRestriction(problem.fieldRestriction);
		fgs.setReflectionAllowed(problem.isReflectionAllowed);
		fgs.createPlacements(problem.figures, field);
		analysis.setPlacements(fgs.placements);
		analysis.setField(field);
		analysis.setUsageLimits(problem.usageLimit);
		analysis.setDesignations(problem.designations);
		analysis.setShapeMode(problem.shapeMode);
		analysis.setFieldRestriction(problem.fieldRestriction);
		analysis.setShowSolutionLimit(problem.showSolutionLimit);
		analysis.setSolutionLimit(problem.solutionLimit);
		analysis.solve();
		return analysis.getSolutionCount();
	}
	
	static void runC() {
		for (int i1 = 27; i1 < Problem2D_c.SELECTED.length; i1+=11) {
			if(Problem2D_c.SELECTED[i1] != 0) continue;
			for (int i2 = 17; i2 < Problem2D_c.SELECTED.length; i2++) {
				if(Problem2D_c.SELECTED[i2] != 0) continue;
				System.out.println("i1=" + i1 + " i2=" + i2);
				Problem2D problem = new Problem2D_c(i1, i2);
				new Packing2dRunner().run(problem);
			}
		}
	}

	public static void main(String[] args) {
//		Problem2D_TreePointCut.run(); 
//		runC();
//		Problem2D problem = new Problem2D_d();
		
		while(true) {
			Problem2D problem = new Problem2D_e();
			int sc = new Packing2dRunner().run(problem);
			System.out.println("Solution count = " + sc);
			if(sc == 1) break;
		}
	}

}
/*
B---BBBBB---B+B,
BBBB1B555BBB+++,
11111B5BB---B+B,
1BBB1B555BBBBB1,
BBBxBxBB5B11111,
666BxB555B1BBB1,
6BBxBxBBBBBBBBB,
666B6B666B6B666,
6B6B6B6B6B6B6B6,
666B66666B66666,
*/

/*
1BBB1B5B555B666,
11111B5B5B5B6B6,
1BBBBB555B5B666,
B+B---BBBxBxBB6,
+++BBBB1BBxB666,
B+B11111BxBxBBB,
-BB1BBB1BBBBBBB,
-B6B666B-B66666,
-B6B6B6B-B6B6B6,
BB66666B-B666B6,
*/