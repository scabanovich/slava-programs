package slava.puzzle.paths.nook.model;

import java.util.ArrayList;
import com.slava.common.RectangularField;
import slava.puzzle.template.model.PuzzleModel;

public class NookPathsModel extends PuzzleModel {
	NookPathsPuzzle puzzle;
	NookPathsPreferences preferences = new NookPathsPreferences();
	
	protected RectangularField field;
	protected ArrayList solutions = new ArrayList();
	protected int currentSolution = -1;

	public NookPathsModel() {
		field = new RectangularField();
		field.setSize(8, 8);
		puzzle = new NookPathsPuzzle();
		puzzle.setModel(this);
		puzzle.init();
		setLoader(new NookPathsLoader());
	}

	public RectangularField getField() {
		return field;
	}
	
	public NookPathsPuzzle getPuzzleInfo() {
		return puzzle;
	}
	
	public NookPathsPreferences getPreferences() {
		return preferences;
	}
	
	public boolean isShowingSolution() {
		return currentSolution >= 0;
	}
	
	public void setSolutions(ArrayList list) {
		solutions.clear();
		solutions.addAll(list);
		if(solutions.size() == 0) {
			currentSolution = -1;
		} else {
			currentSolution = 0;
		}		
	}
	
	public int[] getSelectedSolution() {
		return currentSolution < 0 ? null : (int[])solutions.get((currentSolution));
	}
	
	public void nextSolution() {
		if(currentSolution < 0) return;
		currentSolution++;
		if(currentSolution == solutions.size()) currentSolution = 0;
	}
	
	public void prevSolution() {
		if(currentSolution < 0) return;
		currentSolution--;
		if(currentSolution < 0) currentSolution = solutions.size() - 1;
	}
	
	public void clearSolutions() {
		setSolutionInfo(null);
		solutions.clear();
		currentSolution = -1;
	}
	
	public void changeFieldSize(int width, int height) {
		clearSolutions();
		field.setSize(width, height);
		puzzle.init();
	}

}
