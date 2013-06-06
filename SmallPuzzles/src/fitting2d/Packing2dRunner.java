package fitting2d;

public class Packing2dRunner {

	public void run(Problem2D problem) {
		RectangularField field = new RectangularField();
		field.setSize(problem.xSize, problem.ySize);
		Figures2D fgs = new Figures2D();
		fgs.setReflectionAllowed(problem.isReflectionAllowed);
		fgs.createPlacements(problem.figures, field);
		PackingAnalysis analysis = new PackingAnalysis();
		analysis.setPlacements(fgs.placements);
		analysis.setField(field);
		analysis.setUsageLimits(problem.usageLimit);
		analysis.setDesignations(problem.designations);
		analysis.setShapeDisabled(problem.noShape);
		analysis.setFieldRestriction(problem.fieldRestriction);
		analysis.solve();
	}

	public static void main(String[] args) {
		Problem2D problem = new Problem2D_a();
		new Packing2dRunner().run(problem);
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