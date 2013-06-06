package slava.puzzles.hexa.fence.gui.action;

import java.util.ArrayList;

import slava.puzzle.template.action.PuzzleActionSolve;
import slava.puzzles.hexa.fence.analysis.HexaFenceGenerator;
import slava.puzzles.hexa.fence.model.HexaFenceModel;

public class HexaFenceActionGenerate extends PuzzleActionSolve {

	public HexaFenceActionGenerate() {}

	protected void execute() {
		HexaFenceGenerator g = new HexaFenceGenerator();
		HexaFenceModel model = (HexaFenceModel)manager.getModel();
		g.setField(model.getField());
		g.setForm(model.getPuzzleInfo().getForm());
		g.generate();
		int[] mdata = model.getPuzzleInfo().getData();
		int[] gdata = g.getData();
		for (int p = 0; p < mdata.length; p++) mdata[p] = gdata[p];
		int[] solution = g.getSolution();
		ArrayList ss = new ArrayList();
		ss.add(solution);
		model.setSolutions(ss);
	}

}
