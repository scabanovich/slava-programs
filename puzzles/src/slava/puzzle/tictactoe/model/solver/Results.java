package slava.puzzle.tictactoe.model.solver;

import java.util.HashMap;
import java.util.Map;

public class Results {
	Map<String, Result> results = new HashMap<String, Result>();

	public Result getResult(String code) {
		return results.get(code);
	}

	public void putResult(Result result) {
		results.put(result.getCode(), result);
	}
}
