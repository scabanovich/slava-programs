package slava.puzzle.tictactoe.model;

import com.slava.common.RectangularField;

import slava.puzzle.template.model.PuzzleModel;
import slava.puzzle.tictactoe.model.solver.Result;
import slava.puzzle.tictactoe.model.solver.Results;
import slava.puzzle.tictactoe.model.solver.TicTacToeForceDefenceMove;
import slava.puzzle.tictactoe.model.solver.TicTacToeForceSolver;

public class TicTacToeModel extends PuzzleModel {
	RectangularField field;
	TicTacToeState state = new TicTacToeState();

	Results results = new Results();

	public TicTacToeModel() {
		field = new RectangularField();
		field.setOrts(RectangularField.DIAGONAL_ORTS);
		field.setSize(21, 21);
		state.setField(field);
		setLoader(new TicTacToeLoader());

		startNewGame();
	}

	public RectangularField getField() {
		return field;
	}

	public TicTacToeState getState() {
		return state;
	}

	public int getTurn() {
		return state.getTurn();
	}

	public int getMovesMade() {
		return state.getMovesMade();
	}

	public int getValue(int p) {
		return state.getValue(p);
	}

	public void setValue(int p, int v) {
		state.setValue(p, v);
	}

	public boolean isCompleted() {
		return state.isCompleted();
	}

	public int getFiveStart() {
		return state.getFiveStart();
	}

	public int getFiveEnd() {
		return state.getFiveEnd();
	}

	public void startNewGame() {
		state.clean();
	}

	public void move(int p) {
		state.move(p);
		computeForceWin();
	}

	public void back() {
		state.back();
	}

	void computeForceWin() {
		String code = state.getCode();
		if(results.getResult(code) != null) {
			return;
		}
		Result result = new Result();
		result.setCode(code);
		results.putResult(result);

		TicTacToeForceSolver solver = new TicTacToeForceSolver();
		solver.setState(state.copy());
		int[] forceWin = solver.solve();
		if(forceWin != null) {
			result.setForceWin(forceWin);
			result.setForceAnalysesCompleted(true);
		} else {
			if(solver.isForceAnalysesCompleted()) {
				result.setForceAnalysesCompleted(true);
			}
			TicTacToeForceDefenceMove defencer = new TicTacToeForceDefenceMove();
			defencer.setState(state.copy());
			result.setResults(defencer.find());
			result.setForceDefence(defencer.isForceDefence());
		}
	}

	public Results getResults() {
		return results;
	}

	public Result getResult() {
		return getResults().getResult(state.getCode());
	}

}
