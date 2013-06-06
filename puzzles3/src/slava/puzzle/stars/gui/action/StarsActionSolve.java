package slava.puzzle.stars.gui.action;

import java.util.ArrayList;

import slava.puzzle.stars.analysis.StarsRegionField;
import slava.puzzle.stars.analysis.StarsSolver;
import slava.puzzle.stars.model.StarsModel;
import slava.puzzle.stars.model.StarsSetsField;
import slava.puzzle.template.action.PuzzleActionSolve;

public class StarsActionSolve extends PuzzleActionSolve {
	StarsSolver solver = new StarsSolver();
	
	public StarsActionSolve() {}

	protected void execute() {
		StarsModel model = (StarsModel)manager.getModel();
		StarsSetsField sf = model.getField();
		if(!check(sf)) return;
		StarsRegionField f = new StarsRegionField();
		f.setSize(sf.getWidth());
		int[] colors = new int[sf.getSize()];
		for (int i = 0; i < colors.length; i++) {
			colors[i] = sf.getSetAt(i);
		}
		f.setRegions(colors);
		solver.setField(f);
		try {
			solver.solve();
		} catch (Exception e) {
			model.setSolutionInfo(e.getMessage());
			model.setSolutions(new ArrayList());
			return;
		}
		model.setSolutionInfo("Solutions = " + solver.getSolutionCount() + " Tree Size = " + solver.getTreeSize());
		ArrayList l = new ArrayList();
		int[][] sl = solver.getSolution();
		if(sl != null) for (int i = 0; i < sl.length; i++) l.add(sl[i]);
		model.setSolutions(l);
	}
	
	boolean check(StarsSetsField sf) {
		for (int c = 0; c < sf.getWidth(); c++) {
			if(sf.getSetVolume(c) == 0) {
				StarsModel model = (StarsModel)manager.getModel();
				model.setSolutionInfo("You have to draw more regions.");
				model.setSolutions(new ArrayList());
				return false;
			}
		}
		return true;
	}

}
