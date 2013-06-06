package slava.puzzles.connectfour.model;

import slava.puzzle.template.model.PuzzleModel;
import slava.puzzles.connectfour.solve.ConnectFourState;

import com.slava.common.RectangularField;

public class ConnectFourModel extends PuzzleModel {
	RectangularField field;
	ConnectFourState state;
	int initialVolume = 0;
	boolean isCompleted = false;
	byte[] analysisResults = null;

	public ConnectFourModel() {
		field = new RectangularField();
		field.setOrts(RectangularField.DIAGONAL_ORTS);
		field.setSize(7, 6);
		state = new ConnectFourState(field);
	}

	public RectangularField getField() {
		return field;
	}

	public ConnectFourState getState() {
		return state;
	}

	public boolean isCompleted() {
		return isCompleted || state.getFilled() == state.getField().getSize();
	}

	public boolean canMove(int x) {
		return !isCompleted && state.canMove(x);
	}

	public boolean canBack() {
		return state.getFilled() > initialVolume;
	}

	public void move(int x) {
		if(state.canMove(x)) {
			setAnalysisResults(null);
			if(state.getSmartState().getResult(x) == state.getTurn()) { //TODO check that it is last!!!
				isCompleted = true;
			}
			state.move(x);
		}
	}

	public void back() {
		if(state.getFilled() > initialVolume) {
			setAnalysisResults(null);
			state.back();
			isCompleted = false;
		}
	}

	public void setAnalysisResults(byte[] r) {
		analysisResults = r;
	}

	public byte[] getAnalysisResults() {
		return analysisResults;
	}

	public boolean isShowingAnalysis() {
		return analysisResults != null;
	}
	
}
