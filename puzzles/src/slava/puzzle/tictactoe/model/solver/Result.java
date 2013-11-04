package slava.puzzle.tictactoe.model.solver;

import java.util.Map;

public class Result {
	String code;
	int[] forceWin = null;
	Map<Integer,Integer> results = null;
	boolean isForceDefence = false;
	boolean forceAnalysesCompleted = false;

	public Result() {}

	public void setCode(String  code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setForceWin(int[] forceWin) {
		this.forceWin = forceWin;
	}

	public void setResults(Map<Integer, Integer> rs) {
		results = rs;
	}

	public void setForceDefence(boolean b) {
		isForceDefence = b;
	}

	public void setForceAnalysesCompleted(boolean b) {
		forceAnalysesCompleted = b;
	}

	public int[] getForceWin() {
		return forceWin;
	}

	public Map<Integer, Integer> getResults() {
		return results;
	}

	public boolean isForceDefence() {
		return isForceDefence;
	}

	public boolean isForceAnalysesCompleted() {
		return forceAnalysesCompleted;
	}
}
