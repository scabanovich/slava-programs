package forsmarts.fitting2d.tight;

import com.slava.common.RectangularField;

import forsmarts.fitting2d.Figures2D;
import forsmarts.fitting2d.Problem2D;

public class TightPackingRunner {

	public int run(Problem2D problem) {
		TightPackingSolver analysis = new TightPackingSolver();
		return run(problem, analysis);		
	}

	public int run(Problem2D problem, TightPackingSolver analysis) {
		RectangularField field = new RectangularField();
		field.setSize(problem.xSize, problem.ySize);
		Figures2D fgs = new Figures2D();
		int[] restriction = problem.fieldRestriction == null ? null : new int[problem.fieldRestriction.length];
		if(restriction != null) {
			for (int i = 0; i < restriction.length; i++) {
				restriction[i] = problem.fieldRestriction[i] <= 0 ? 1 : 0;
			}
		}
		fgs.setFieldRestriction(restriction);
		fgs.setReflectionAllowed(problem.isReflectionAllowed);
		fgs.createPlacements(problem.figures, field);
		analysis.setPlacements(fgs.getPlacements());
		analysis.setField(field);
		analysis.setUsageLimits(problem.usageLimit);
		//adapt fieldRestrictions to fill limits
		analysis.setFillLimits(problem.fieldRestriction);
		analysis.setShowSolutionLimit(problem.showSolutionLimit);
		analysis.setSolutionLimit(problem.solutionLimit);
		analysis.solve();
		return analysis.getSolutionCount();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Problem2D problem = new ProblemPentaTight();
		int sc = new TightPackingRunner().run(problem);

	}

}
